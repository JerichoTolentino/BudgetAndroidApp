package jericho.budgetapp.Presentation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import jericho.budgetapp.R;
import jericho.budgetapp.Model.PeriodicBudget;
import jericho.budgetapp.Model.Plan;
import utilities.MoneyFormatter;
import utilities.Utility;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

//TODO: refactor helper methods
/**
 * An activity where Plans can be created/edited.
 */
public class EditBudgetPlanActivity extends AppCompatActivity {

    //region Constants

    private static final String CREATE_NEW_TAG = "CreateNew";
    private static final String MODIFY_EXISTING_TAG = "ModifyExisting";

    //endregion

    //region Members

    private Toolbar oToolbar;
    private ViewGroup oLayoutParent;

    // name
    private TextView tvName;
    private EditText etName;

    // create new plan section
    private TextView tvAnnualIncome;
    private TextView tvAnnualExpenses;
    private TextView tvAnnualSavings;
    private EditText etAnnualIncome;
    private EditText etAnnualExpenses;
    private EditText etAnnualSavings;
    private Button btnGeneratePlan;

    // edit existing plan section
    private TextView tvTotalWeeklyBudget;
    private TextView tvAmountAllocated;
    private TextView tvAmountRemaining;
    private EditText etTotalWeeklyBudget;
    private EditText etAmountAllocated;
    private EditText etAmountRemaining;
    private EditText etDailyLimit;
    private ToggleButton[] m_toggleButtons;

    private Plan m_plan;
    private PeriodicBudget m_currentDailyBudget;
    private boolean m_createNew;

    // Variables used to display the amount allocated vs amount remaining when the user is editing their daily budgets
    private long m_amountAllocated;
    private long m_amountRemaining;

    //endregion

    //region onCreate()

    /**
     * Initializes the GUI with the Plan's data.
     * @param savedInstanceState
     * @see AppCompatActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget_plan);

        oLayoutParent = findViewById(R.id.RootParent);

        // Initialize action bar
        oToolbar = findViewById(R.id.custom_toolbar);
        oToolbar.setTitle(R.string.edit_plan);
        setSupportActionBar(oToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initializes references to views
        initWidgetReferences();

        // Retrieve Plan data and display on activity.
        Map<String, Object> extras = Utility.getExtrasFromIntent(getIntent());
        if (extras.values().size() > 0)
        {
            m_plan = (Plan) extras.get("plan");
            m_createNew = (boolean) extras.get("createNew");
            syncWidgetsWithPlan();
        }
        else
            m_createNew = true;

        toggleInputMode(m_createNew);
    }

    //endregion

    //region Toolbar Events

    /**
     * Decides which menu items to display based on whether the Plan is the active.
     * @param menu
     * @return
     * @see AppCompatActivity#onCreateOptionsMenu(Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, oToolbar.getMenu());

        if (checkIsActivePlan())
            Utility.showMenuItems(oToolbar.getMenu(), new int[] {R.id.remove, R.id.filled_star});
        else
            Utility.showMenuItems(oToolbar.getMenu(), new int[] {R.id.remove, R.id.empty_star});

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Stores the handlers to execute when a menu item is pressed.
     * @param item
     * @return
     * @see AppCompatActivity#onOptionsItemSelected(MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try {
            long ID;

            switch (item.getItemId()) {

                // Go back
                case android.R.id.home:
                    onBackPressed();
                    break;

                // Delete the Plan
                case R.id.remove:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Are you sure you want to delete this plan?")
                            .setPositiveButton("Yes", deletePlanClickListener)
                            .setNegativeButton("No", deletePlanClickListener).show();
                    break;

                // Set the Plan as the active Plan
                case R.id.empty_star:
                    ID = m_plan != null ? m_plan.getId() : -1;
                    setActivePlan(ID);
                    break;

                // Make this (active) Plan not the active Plan
                case R.id.filled_star:
                    ArrayList<Plan> plans = MainActivity.g_dbHandler.queryPlans(null);
                    if (plans.size() > 0) {
                        if (plans.get(0).getId() != m_plan.getId())
                            ID = plans.get(0).getId();
                        else {
                            if (plans.size() > 1)
                                ID = plans.get(1).getId();
                            else {
                                ID = m_plan.getId();
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                                builder1.setMessage("You must have an active plan!\nCreate another plan if you don't want to use this one anymore.")
                                        .setNeutralButton("Ok", neutralClickListener)
                                        .show();
                            }
                        }
                    } else
                        ID = -1;
                    setActivePlan(ID);
                    break;

                default:
                    break;
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
            assert false;
        }

        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region Event Handlers

    /**
     * Generates a Plan and toggles the input mode.
     */
    public void btnGeneratePlan_OnClick(View v)
    {
        try {
            generatePlan();
            toggleInputMode(false);
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }

    /**
     * Selects the day of the week corresponding to the button clicked.
     * <p>
     *     Note: This event handler handles all 7 weekday button click events.
     *     The tags of the buttons are used to discern which date was selected.
     * </p>
     * @param v
     */
    public void btnWeekday_OnClick(View v)
    {
        try {
            if (v instanceof ToggleButton) {
                ToggleButton button = (ToggleButton) v;
                selectDailyBudgetButton(button);
                syncDailyLimit();
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }

    /**
     * Checks if the changes are valid and then saves the changes to the Plan.
     * @param v
     */
    public void btnConfirm_OnClick(View v)
    {
        try
        {
           checkAmountRemaining();
        }
        catch (Exception ex)
        {
            System.err.println(ex.toString());
        }
    }

    //endregion

    //region Functionality Methods

    /**
     * Generates a new Plan with the specified required values.
     * @throws ParseException Thrown if any of the currency values fail to parse.
     */
    public void generatePlan() throws ParseException
    {
        String planName = etName.getText().toString();

        long annualIncome = MoneyFormatter.formatMoneyToLong(etAnnualIncome.getText().toString());
        long annualExpenses = MoneyFormatter.formatMoneyToLong(etAnnualExpenses.getText().toString());
        long annualSavings = MoneyFormatter.formatMoneyToLong(etAnnualSavings.getText().toString());

        m_plan = new Plan(planName, annualIncome, annualExpenses, annualSavings);

        syncWidgetsWithPlan();
    }

    /**
     * Saves the changes made to the Plan.
     */
    public void confirmChanges()
    {
        String message;
        updatePlan();

        if (m_createNew) {
            MainActivity.g_dbHandler.addPlan(m_plan);
            message = "Plan added!";
        } else {
            MainActivity.g_dbHandler.updatePlan(m_plan);
            message = "Plan updated!";
        }

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.active_plan_prefs), Context.MODE_PRIVATE);
        long activePlanID = sharedPreferences.getLong(getString(R.string.active_plan_id), -1);
        if (activePlanID < 0)
        {
            long planID = 0;
            try {
                planID = MainActivity.g_dbHandler.queryPlans(null).get(0).getId();
            } catch (Exception ex) {
                System.err.println(ex.toString());
                assert false;
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(getString(R.string.active_plan_id), planID);
            editor.apply();
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    //endregion

    //region Helper Methods

    /**
     * Resets all daily budget buttons to their initial state and sets the specified button.
     * @param button The button to set as the selected button.
     */
    private void selectDailyBudgetButton(ToggleButton button)
    {
        for (ToggleButton b : m_toggleButtons) {
            if (b != button) {
                b.setChecked(false);
                b.setBackgroundColor(ContextCompat.getColor(EditBudgetPlanActivity.this, android.R.color.darker_gray));
            } else {
                b.setChecked(true);
            }
            b.setBackgroundColor(ContextCompat.getColor(EditBudgetPlanActivity.this, R.color.colorSelectedItem));
        }
    }

    /**
     * Finds the day associated with the currently checked daily budget button.
     * @return The daily budget button that is currently checked.
     */
    private ToggleButton getSelectedDayOfTheWeek()
    {
        for (ToggleButton b : m_toggleButtons)
            if (b.isChecked())
                return b;

        return null;
    }

    /**
     * Toggles the input mode to create a new Plan or edit an existing Plan.
     * @param createNew Indicates whether a new Plan is being created or not.
     */
    private void toggleInputMode(boolean createNew)
    {
        // Recursively toggle views based on whether a Plan is being created or edited
        toggleChildViews(oLayoutParent, createNew);

        // Toggle weekday buttons
        for (ToggleButton b : m_toggleButtons)
            b.setEnabled(!createNew);
    }

    /**
     * Toggles all views within a view group based on whether a new Plan is being created or not.
     * <p>
     *     Views that are only available when creating a new Plan have a tag associated with them to indicate it,
     *     and views that are only available when modifying an existing plan also have an associated tag to indicate it.
     * </p>
     * @param viewGroup The view group containing the views to be toggled.
     * @param createNew A value indicating whether a new Plan is being created or not.
     */
    private void toggleChildViews(ViewGroup viewGroup, boolean createNew)
    {
        for (int i = 0; i < viewGroup.getChildCount(); i++)
        {
            View view = viewGroup.getChildAt(i);

            // Recurse into child views
            if (view instanceof ViewGroup) {
                ViewGroup layout = (ViewGroup) view;
                toggleChildViews(layout, createNew);
            }

            // Toggle view
            if (view.getTag() != null) {
                if (view.getTag().toString().equals(CREATE_NEW_TAG))
                    view.setEnabled(createNew);
                else if (view.getTag().toString().equals(MODIFY_EXISTING_TAG))
                    view.setEnabled(!createNew);
            }
        }
    }

    /**
     * Initializes the references to the activity widgets.
     */
    private void initWidgetReferences()
    {
        tvName = findViewById(R.id.tvName);
        etName = findViewById(R.id.etName);

        tvAnnualIncome = findViewById(R.id.tvAnnualIncome);
        tvAnnualExpenses = findViewById(R.id.tvAnnualExpenses);
        tvAnnualSavings = findViewById(R.id.tvAnnualSavings);
        etAnnualIncome = findViewById(R.id.etAnnualIncome);
        etAnnualExpenses = findViewById(R.id.etAnnualExpenses);
        etAnnualSavings = findViewById(R.id.etAnnualSavings);
        btnGeneratePlan = findViewById(R.id.btnGeneratePlan);

        tvTotalWeeklyBudget = findViewById(R.id.tvTotalWeeklyBudget);
        tvAmountAllocated = findViewById(R.id.tvAmountAllocated);
        tvAmountRemaining = findViewById(R.id.tvAmountRemaining);
        etTotalWeeklyBudget = findViewById(R.id.etWeeklyBudget);
        etAmountAllocated = findViewById(R.id.etAmountAllocated);
        etAmountRemaining = findViewById(R.id.etAmountRemaining);
        etDailyLimit = findViewById(R.id.etDailyLimit);

        m_toggleButtons = new ToggleButton[7];
        m_toggleButtons[0] = findViewById(R.id.tbtnSunday);
        m_toggleButtons[1] = findViewById(R.id.tbtnMonday);
        m_toggleButtons[2] = findViewById(R.id.tbtnTuesday);
        m_toggleButtons[3] = findViewById(R.id.tbtnWednesday);
        m_toggleButtons[4] = findViewById(R.id.tbtnThursday);
        m_toggleButtons[5] = findViewById(R.id.tbtnFriday);
        m_toggleButtons[6] = findViewById(R.id.tbtnSaturday);
    }

    /**
     * Updates the widgets to display the values of the Plan being edited.
     */
    private void syncWidgetsWithPlan()
    {
        String name = m_plan.getName();
        String annualIncome = MoneyFormatter.formatLongToMoney(m_plan.getAnnualIncome(), false);
        String annualExpenses = MoneyFormatter.formatLongToMoney(m_plan.getAnnualExpenses(), false);
        String annualSavings = MoneyFormatter.formatLongToMoney(m_plan.getAnnualSavings(), false);

        PeriodicBudget weeklyBudget = m_plan.getWeeklyBudget();
        String totalWeeklyBudget = MoneyFormatter.formatLongToMoney(weeklyBudget.getTotalBudget(), false);

        m_amountAllocated = weeklyBudget.getTotalBudget();
        m_amountRemaining = 0;
        String amountAllocated = MoneyFormatter.formatLongToMoney(m_amountAllocated, false);
        String amountRemaining = MoneyFormatter.formatLongToMoney(m_amountRemaining, false);

        etName.setText(name);
        etAnnualIncome.setText(annualIncome);
        etAnnualExpenses.setText(annualExpenses);
        etAnnualSavings.setText(annualSavings);
        etTotalWeeklyBudget.setText(totalWeeklyBudget);
        etAmountAllocated.setText(amountAllocated);
        etAmountRemaining.setText(amountRemaining);

        syncDailyLimit();
    }

    /**
     * Updates the current daily budget, the sets the current daily budget according to
     * the currently checked daily budget button.
     */
    private void syncDailyLimit()
    {
        updateCurrentDailyBudget();

        ToggleButton dayButton = getSelectedDayOfTheWeek();
        int day = Integer.parseInt(dayButton.getTag().toString());
        m_currentDailyBudget = m_plan.getDailyBudgetOn(day);

        String dailyLimit = MoneyFormatter.formatLongToMoney(m_currentDailyBudget.getTotalBudget(), false);
        etDailyLimit.setText(dailyLimit);
    }

    /**
     * Updates the daily budget currently being edited and the remaining & allocated amounts.
     */
    private void updateCurrentDailyBudget()
    {
        try {
            if (m_currentDailyBudget != null) {
                long oldTotalBudget = m_currentDailyBudget.getTotalBudget();
                long totalBudget = MoneyFormatter.formatMoneyToLong(etDailyLimit.getText().toString());
                m_currentDailyBudget.setTotalBudget(totalBudget);

                // update amount remaining/amount allocated
                long difference = oldTotalBudget - totalBudget;
                m_amountRemaining += difference;
                m_amountAllocated -= difference;

                etAmountRemaining.setText(MoneyFormatter.formatLongToMoney(m_amountRemaining, false));
                etAmountAllocated.setText(MoneyFormatter.formatLongToMoney(m_amountAllocated, false));
            }
        }
        catch (ParseException ex)
        {
            System.err.println(ex.toString());
            assert false;
        }
    }

    /**
     * Reads the values from the input fields and assigns them to the Plan.
     */
    private void updatePlan()
    {
        try
        {
            String name = etName.getText().toString();
            long annualIncome = MoneyFormatter.formatMoneyToLong(etAnnualIncome.getText().toString());
            long annualExpenses = MoneyFormatter.formatMoneyToLong(etAnnualExpenses.getText().toString());
            long annualSavings = MoneyFormatter.formatMoneyToLong(etAnnualSavings.getText().toString());
            long totalWeeklyBudget = MoneyFormatter.formatMoneyToLong(etTotalWeeklyBudget.getText().toString());
            long amountAllocated = MoneyFormatter.formatMoneyToLong(etAmountAllocated.getText().toString());
            long amountRemaining = MoneyFormatter.formatMoneyToLong(etAmountRemaining.getText().toString());

            m_plan.setName(name);
            m_plan.setAnnualIncome(annualIncome);
            m_plan.setAnnualExpenses(annualExpenses);
            m_plan.setAnnualSavings(annualSavings);
            m_plan.getWeeklyBudget().setTotalBudget(totalWeeklyBudget);

            updateCurrentDailyBudget();
        }
        catch (ParseException ex)
        {
            System.err.println(ex.toString());
            assert false;
        }

    }

    //TODO: Actually implement alert dialog properly... (use CustomAlertDialog class)
    /**
     * Checks if the amount budgeted is sufficient and saves the changes to the Plan.
     * <p>
     *     If there are remaining funds, they can be added to the savings.
     *     If there aren't enough funds, the difference can be removed from the savings.
     * </p>
     */
    private void checkAmountRemaining()
    {
        updateCurrentDailyBudget();

        if (m_amountRemaining > 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditBudgetPlanActivity.this);
            builder.setTitle("Funds still available")
                   .setMessage("You still have some money left over! Add the rest to savings?")
                   .setPositiveButton("Yes", underBudgetClickListener)
                   .setNegativeButton("No", underBudgetClickListener)
                   .show();
        }
        else if (m_amountRemaining < 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Insufficient funds")
                   .setMessage("You are spending too much! Would you like to lower your annual savings goal by the difference?")
                   .setPositiveButton("Yes", overBudgetClickListener)
                   .setNegativeButton("No", overBudgetClickListener)
                   .show();
        }
        else
        {
            confirmChanges();
        }
    }

    /**
     * Adds the remaining funds to the annual savings goal.
     */
    private void addAmountRemainingToSavings()
    {
        try
        {
            long annualSavings = MoneyFormatter.formatMoneyToLong(etAnnualSavings.getText().toString());
            long totalWeeklyBudget = MoneyFormatter.formatMoneyToLong(etTotalWeeklyBudget.getText().toString());

            //TODO: If the savings drops below 0, increase the Annual Expenses instead
            annualSavings += m_amountRemaining * 52;
            totalWeeklyBudget -= m_amountRemaining;
            m_amountRemaining = 0;

            etAnnualSavings.setText(MoneyFormatter.formatLongToMoney(annualSavings, false));
            etTotalWeeklyBudget.setText(MoneyFormatter.formatLongToMoney(totalWeeklyBudget, false));
        }
        catch (ParseException ex)
        {
            System.err.println(ex.toString());
            assert false;
        }
    }

    /**
     * Checks if the Plan being edited is the currently active Plan.
     * @return
     */
    private boolean checkIsActivePlan()
    {
        if (m_plan == null)
            return false;

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.active_plan_prefs), Context.MODE_PRIVATE);
        long activePlanID = sharedPreferences.getLong(getString(R.string.active_plan_id), -1);

        return activePlanID == m_plan.getId();
    }

    /**
     * Navigates to the previous activity.
     */
    @Override
    public void onBackPressed() {
        if (this.isTaskRoot()) {
            Intent intent = new Intent(EditBudgetPlanActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else
            super.onBackPressed();
    }

    /**
     * Sets the currently active Plan to the Plan with the specified ID.
     * @param ID The ID of the desired Plan.
     */
    private void setActivePlan(long ID)
    {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.active_plan_prefs), Context.MODE_PRIVATE).edit();

        editor.putLong(getString(R.string.active_plan_id), ID);
        editor.apply();

        if (checkIsActivePlan())
            Utility.showMenuItems(oToolbar.getMenu(), new int[] {R.id.remove, R.id.filled_star});
        else
            Utility.showMenuItems(oToolbar.getMenu(), new int[] {R.id.remove, R.id.empty_star});

        this.invalidateOptionsMenu();
    }

    //endregion

    //region Alert Dialog

    /**
     * Adds the remaining amount to the annual savings if the user confirms.
     */
    DialogInterface.OnClickListener underBudgetClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    addAmountRemainingToSavings();
                    confirmChanges();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // Do nothing
                    break;
            }
        }
    };

    /**
     * Lowers the annual savings by the difference if the user confirms.
     */
    DialogInterface.OnClickListener overBudgetClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditBudgetPlanActivity.this);
                    builder.setTitle("Are you sure?")
                           .setMessage("This will lower your annual savings amount.")
                           .setPositiveButton("Yes", underBudgetClickListener)
                           .setNegativeButton("No", underBudgetClickListener)
                           .show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // Do nothing
                    break;
            }
        }
    };

    //TODO: Remove?
    /**
     * Does nothing.
     */
    DialogInterface.OnClickListener neutralClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i)
            {
                default:
                    break;
            }
        }
    };

    /**
     * Deletes the Plan being edited.
     */
    DialogInterface.OnClickListener deletePlanClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            try {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        MainActivity.g_dbHandler.removePlan(m_plan.getId());

                        if (checkIsActivePlan())
                        {
                            ArrayList<Plan> plans = MainActivity.g_dbHandler.queryPlans(null);
                            long ID = plans.size() > 0 ? plans.get(0).getId() : -1;
                            setActivePlan(ID);
                        }

                        Toast.makeText(EditBudgetPlanActivity.this, "Plan deleted!", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // Do nothing
                        break;
                }
            }
            catch (Exception ex)
            {
                System.err.println(ex.toString());
                assert false;
            }
        }
    };

    //endregion

}
