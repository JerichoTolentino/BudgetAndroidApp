package jericho.budgetapp.Presentation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import jericho.budgetapp.Model.Expense;
import jericho.budgetapp.Model.ExpenseGroup;
import jericho.budgetapp.Model.Purchase;
import jericho.budgetapp.Model.Priceable;
import jericho.budgetapp.R;
import jericho.budgetapp.Model.PeriodicBudget;
import jericho.budgetapp.Model.Plan;
import utilities.MoneyFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import utilities.Utility;
import jericho.budgetapp.Persistence.DBHandler;

//TODO: REFACTOR EVERYTHING

public class MainActivity extends AppCompatActivity {

    public static DBHandler g_dbHandler;

    //region Members

    private Toolbar toolbar;
    private ArrayList<Purchase> m_allPurchases;
    private ListView lvPurchases;
    private Spinner spPeriodicBudget;

    private Plan m_activePlan;
    private String m_periodicBudgetSelected = "Daily Budget";

    //endregion

    //region onCreate()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        g_dbHandler = new DBHandler(getApplicationContext(), null, null, 0);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_view_menu);
        toolbar.setTitle(R.string.budget_buddy);
        setSupportActionBar(toolbar);

        m_allPurchases = new ArrayList<>();
        initPurchases();

        spPeriodicBudget = findViewById(R.id.spPeriodicBudget);

        spPeriodicBudget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                try
                {
                    m_periodicBudgetSelected = spPeriodicBudget.getSelectedItem().toString();
                    updateRemainingBudget();
                }
                catch (Exception ex)
                {
                    System.err.println(ex.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, toolbar.getMenu());
        Utility.showMenuItems(toolbar.getMenu(), new int[]{R.id.view_history, R.id.quick_add_expense});
        return super.onCreateOptionsMenu(menu);
    }

    //endregion

    // region Toolbar Events

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                goToMenuActivity();
                break;

            case R.id.view_history:
                goToViewPurchaseHistoryActivity();
                break;

            case R.id.quick_add_expense:
                goToQuickAddExpenseActivity();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region Event Handlers

    @Override
    protected void onResume() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.active_plan_prefs), Context.MODE_PRIVATE);
        long activePlanID = sharedPreferences.getLong(getString(R.string.active_plan_id), -1);

        if (activePlanID > 0) {
            setActivePlan(activePlanID);
            syncWidgetsWithActivePlan();
            checkForQuickAddExpense();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Create A Plan")
                    .setMessage("Welcome to BudgetBuddy!\nLet's create your first plan.")
                    .setNeutralButton("Ok", dialogClickListener)
                    .setCancelable(false)
                    .show();
        }

        if (m_activePlan != null)
            m_activePlan.updateBudgets(new Date());

        super.onResume();
    }

    public void btnPurchase_onClick(View v)
    {
        try {
            makePurchases();
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }

    //endregion

    //region Functionality Methods

    public void makeQuickPurchase(Purchase purchase)
    {
        g_dbHandler.addPurchase(purchase);
        PeriodicBudget dailyBudget = m_activePlan.getDailyBudgetOn(Utility.getDayOfTheWeek());
        dailyBudget.spend(purchase.getItem().getPrice() * purchase.getQuantity());
        syncWidgetsWithActivePlan();
        g_dbHandler.updatePeriodicBudget(dailyBudget);

        Toast.makeText(this, "Purchase successful!", Toast.LENGTH_SHORT).show();
    }

    public void makePurchases()
    {
        for (int i = 0; i < lvPurchases.getCount(); i++)
        {
            Purchase purchase = (Purchase) lvPurchases.getItemAtPosition(i);

            if (purchase.getQuantity() > 0)
                m_allPurchases.add(purchase);
        }

        long amount = 0;
        for (Purchase p : m_allPurchases) {
            amount += p.getItem().getPrice();
            g_dbHandler.addPurchase(p);
        }

        //PeriodicBudget dailyBudget = m_activePlan.getDailyBudgetOn(Utils.getDayOfTheWeek());
        //dailyBudget.spend(amount);
        m_activePlan.spend(amount, Utility.getDayOfTheWeek());
        syncWidgetsWithActivePlan();
        g_dbHandler.updatePlan(m_activePlan);

        if (amount > 0)
            Toast.makeText(this, "Purchase successful!", Toast.LENGTH_SHORT).show();

        updateTotalPrice(MoneyFormatter.formatLongToMoney(0, true));
        initPurchases();
        m_allPurchases.clear();
    }

    //endregion

    //region Helper Methods

    private void updateRemainingBudget()
    {
        TextView etRemainingBudget = findViewById(R.id.tvRemainingBudget);
        PeriodicBudget periodicBudget = null;

        switch (m_periodicBudgetSelected)
        {
            case "Daily Budget":
                periodicBudget = m_activePlan.getDailyBudgetOn(Utility.getDayOfTheWeek());
                break;

            case "Weekly Budget":
                periodicBudget = m_activePlan.getWeeklyBudget();
                break;
        }

        if (periodicBudget != null)
            etRemainingBudget.setText(MoneyFormatter.formatLongToMoney(periodicBudget.getCurrentBudget(), true));

    }

    private void syncWidgetsWithActivePlan()
    {
        if (m_activePlan != null)
        {
            updateRemainingBudget();
        }
    }

    /**
     * Initializes the ListView with Purchases generated from all existing Expenses and ExpenseGroups.
     */
    private void initPurchases()
    {
        ArrayList<Expense> expenses = g_dbHandler.queryExpenses(null);
        ArrayList<ExpenseGroup> expenseGroups = g_dbHandler.queryExpenseGroups(null);
        Purchase[] purchases = GeneratePurchasesArray(expenses, expenseGroups);

        ListAdapter listAdapter = new PurchaseRowAdapter(this, purchases);
        lvPurchases = findViewById(R.id.lvPurchases);
        lvPurchases.setAdapter(listAdapter);
    }

    /**
     * Generates an array of Purchases from a collection of Expenses and ExpenseGroups.
     * @param expenses The Expenses to generate Purchases from.
     * @param expenseGroups The ExpenseGroups to generate Purchases from.
     * @return An array of Purchases generated from the specified Expenses and ExpenseGroups.
     */
    private Purchase[] GeneratePurchasesArray(ArrayList<Expense> expenses, ArrayList<ExpenseGroup> expenseGroups)
    {
        ArrayList<Priceable> priceables = new ArrayList<>(expenses.size() + expenseGroups.size());

        for (Expense e : expenses)
            priceables.add(e);
        for (ExpenseGroup e : expenseGroups)
            priceables.add(e);

        Collections.sort(priceables, Utility.getPrioeableNameComparator());
        Purchase[] purchases = new Purchase[priceables.size()];

        for (int i = 0; i < priceables.size(); i++)
            purchases[i] = new Purchase(priceables.get(i), 0);

        return purchases;
    }

    /**
     * Navigates to the MenuActivity.
     */
    private void goToMenuActivity()
    {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    /**
     * Navigates to the ViewPurchaseHistory activity.
     */
    private void goToViewPurchaseHistoryActivity()
    {
        Intent intent = new Intent(MainActivity.this, ViewPurchaseHistory.class);
        startActivity(intent);
    }

    /**
     * Navigates to the QuickAddExpenseActivity.
     */
    private void goToQuickAddExpenseActivity()
    {
        Intent intent = new Intent(MainActivity.this, QuickAddExpenseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    /**
     * Checks for a QuickAddExpense in the Intent and makes a purchase if it exists.
     */
    private void checkForQuickAddExpense()
    {
        Map<String, Object> extras = Utility.getExtrasFromIntent(getIntent());

        if (extras.values().size() > 0)
        {
            Object quickAddPurchase = extras.get("quickAddExpense");
            if (quickAddPurchase != null && quickAddPurchase instanceof Purchase)
                makeQuickPurchase((Purchase)quickAddPurchase);
        }
    }

    /**
     * Sets the active Plan to the Plan with the specified ID.
     * @param ID The ID of the desired Plan.
     */
    private void setActivePlan(long ID)
    {
        try
        {
            ArrayList<Plan> plans = g_dbHandler.queryPlans(DBHandler.PLANS_COL_ID + "=" + ID);
            m_activePlan = plans.get(0);
        }
        catch (Exception ex)
        {
            System.err.println(ex.toString());
            assert false;
        }
    }

    /**
     * Adds the price of the item to the current total amount to be purchased.
     * @param item The item to add to the purchase.
     */
    public void addToCurrentTotal(Priceable item)
    {
        try {
            TextView tvCurrentTotal = findViewById(R.id.tvCurrentTotal);
            long currTotal = MoneyFormatter.formatMoneyToLong(tvCurrentTotal.getText().toString());
            currTotal += item.getPrice();

            tvCurrentTotal.setText(MoneyFormatter.formatLongToMoney(currTotal, true));
        } catch (Exception ex) {
            System.err.println(ex.toString());
            assert false;
        }
    }

    /**
     * Removes the price of the item from the current total amount to be purchased.
     * @param item The item to remove from the purchase.
     */
    public void removeFromCurrentTotal(Priceable item)
    {
        try {
            TextView tvCurrentTotal = findViewById(R.id.tvCurrentTotal);
            long currTotal = MoneyFormatter.formatMoneyToLong(tvCurrentTotal.getText().toString());
            currTotal -= item.getPrice();

            tvCurrentTotal.setText(MoneyFormatter.formatLongToMoney(currTotal, true));
        } catch (Exception ex) {
            System.err.println(ex.toString());
            assert false;
        }
    }

    /**
     * Updates the total price of the current purchase.
     * @param newPrice The new price to be displayed.
     */
    public void updateTotalPrice(String newPrice)
    {
        final TextView tvCurrentTotal = findViewById(R.id.tvCurrentTotal);
        tvCurrentTotal.setText(newPrice);
    }

    //endregion

    //region Alert Dialog

    /**
     * Navigate to the EditBudgetPlanActivity to create a Plan. (Only occurs when no Plans exist)
     */
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_NEUTRAL:
                    Intent intent = new Intent(MainActivity.this, EditBudgetPlanActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }
    };

    //endregion

}
