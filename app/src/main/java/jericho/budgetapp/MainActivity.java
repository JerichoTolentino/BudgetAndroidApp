package jericho.budgetapp;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.budget_app.expenses.Expense;
import com.budget_app.expenses.ExpenseGroup;
import com.budget_app.expenses.Purchase;
import com.budget_app.jt_interfaces.Priceable;
import com.budget_app.plans.Plan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import databases.DBHandler;
import utils.Utils;

public class MainActivity extends AppCompatActivity {

    public static DBHandler g_dbHandler;

    //region Members

    private Toolbar toolbar;
    private ArrayList<Purchase> m_allPurchases;
    private ListView lvPurchases;

    private Plan m_activePlan;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, toolbar.getMenu());
        Utils.showMenuItems(toolbar.getMenu(), new int[]{R.id.view_history});
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
            System.out.println(m_activePlan.toString());
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Create A Plan")
                    .setMessage("Welcome to BudgetBuddy!\nLet's create your first plan.")
                    .setNeutralButton("Ok", dialogClickListener)
                    .setCancelable(false)
                    .show();
        }
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

    public void makePurchases()
    {

        for (int i = 0; i < lvPurchases.getCount(); i++)
        {
            Purchase purchase = (Purchase) lvPurchases.getItemAtPosition(i);

            if (purchase.getQuantity() > 0)
                m_allPurchases.add(purchase);
        }

        String message = "";
        for (Purchase p : m_allPurchases) {
            message += p.toString() + "\n";
            g_dbHandler.addPurchase(p);
        }

        if (message.length() > 0)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        initPurchases();
        m_allPurchases.clear();
    }

    //endregion

    //region Helper Methods

    private void initPurchases()
    {
        ArrayList<Expense> expenses = g_dbHandler.queryExpenses(null);
        ArrayList<ExpenseGroup> expenseGroups = g_dbHandler.queryExpenseGroups(null);
        Purchase[] purchases = GeneratePurchasesArray(expenses, expenseGroups);

        ListAdapter listAdapter = new PurchaseRowAdapter(this, purchases);
        lvPurchases = findViewById(R.id.lvPurchases);
        lvPurchases.setAdapter(listAdapter);
    }

    private Purchase[] GeneratePurchasesArray(ArrayList<Expense> expenses, ArrayList<ExpenseGroup> expenseGroups)
    {
        ArrayList<Priceable> priceables = new ArrayList<>(expenses.size() + expenseGroups.size());

        for (Expense e : expenses)
            priceables.add(e);
        for (ExpenseGroup e : expenseGroups)
            priceables.add(e);

        Collections.sort(priceables, Utils.getPrioeableNameComparator());
        Purchase[] purchases = new Purchase[priceables.size()];

        for (int i = 0; i < priceables.size(); i++)
            purchases[i] = new Purchase(priceables.get(i), 0);

        return purchases;
    }

    private void goToMenuActivity()
    {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    private void goToViewPurchaseHistoryActivity()
    {
        Intent intent = new Intent(MainActivity.this, ViewPurchaseHistory.class);
        startActivity(intent);
    }

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

    public void addToPurchases(Purchase purchase)
    {
        if(m_allPurchases.contains(purchase))
        {
            Purchase realPurchase = m_allPurchases.get(m_allPurchases.indexOf(purchase));
            realPurchase.setQuantity(purchase.getQuantity());
        }
        else
            m_allPurchases.add(purchase);
    }

    public void removeFromPurchases(Purchase purchase)
    {
        if(purchase.getQuantity() < 1)
            m_allPurchases.remove(purchase);

        if(m_allPurchases.contains(purchase))
        {
            Purchase realPurchase = m_allPurchases.get(m_allPurchases.indexOf(purchase));
            realPurchase.setQuantity(purchase.getQuantity() - 1);
        }
    }

    public void updateTotalPrice(String newPrice)
    {
        final TextView tvCurrentTotal = findViewById(R.id.tvCurrentTotal);

        tvCurrentTotal.setText(newPrice);
    }

    public long getTotalPrice()
    {
        final TextView tvCurrentTotal = findViewById(R.id.tvCurrentTotal);
        return Long.parseLong(tvCurrentTotal.getText().toString().replace("$","").replace(",","").replace(".",""));
    }

    //endregion

    //region Alert Dialog

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
