package jericho.budgetapp;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.budget_app.plans.PeriodicBudget;
import com.budget_app.plans.Plan;
import com.budget_app.utilities.MoneyFormatter;

import java.text.ParseException;

public class EditBudgetPlanActivity extends AppCompatActivity {

    //region Constants

    private static final String CREATE_NEW_TAG = "CreateNew";
    private static final String MODIFY_EXISTING_TAG = "ModifyExisting";

    //endregion

    //region Members

    private Toolbar toolbar;
    private ViewGroup layoutParent;

    //name
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget_plan);

        layoutParent = findViewById(R.id.RootParent);

        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initWidgetReferences();

        if (getIntent().getExtras() != null) {
            m_plan = (Plan) getIntent().getExtras().getSerializable("plan");
            m_createNew = getIntent().getExtras().getBoolean("createNew");
            syncWidgetsWithPlan();
        }
        else
            m_createNew = true;

        toggleInputMode(m_createNew);
    }

    //endregion

    //region Event Handlers

    public void btnGeneratePlan_OnClick(View v)
    {
        try {
            generatePlan();
            toggleInputMode(false);
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }

    public void btnWeekday_OnClick(View v)
    {
        try {
            if (v instanceof ToggleButton) {
                ToggleButton button = (ToggleButton) v;
                clearOtherButtons(button);
                syncDailyLimit();
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }

    public void btnConfirm_OnClick(View v)
    {
        try
        {
            if (checkAmountRemaining()) {
                String message;
                updatePlan();

                if (m_createNew) {
                    MainActivity.g_dbHandler.addPlan(m_plan);
                    message = "Plan added!";
                } else {
                    MainActivity.g_dbHandler.updatePlan(m_plan);
                    message = "Plan updated!";
                }

                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }
        catch (Exception ex)
        {
            System.err.println(ex.toString());
        }
    }

    //endregion

    //region Functionality Methods

    public void generatePlan() throws ParseException
    {
        String planName = etName.getText().toString();

        long annualIncome = MoneyFormatter.formatMoneyToLong(etAnnualIncome.getText().toString());
        long annualExpenses = MoneyFormatter.formatMoneyToLong(etAnnualExpenses.getText().toString());
        long annualSavings = MoneyFormatter.formatMoneyToLong(etAnnualSavings.getText().toString());

        m_plan = new Plan(planName, annualIncome, annualExpenses, annualSavings);

        syncWidgetsWithPlan();
    }

    //endregion

    //region Helper Methods

    private void clearOtherButtons(ToggleButton button)
    {
        for (ToggleButton b : m_toggleButtons) {
            if (b != button) {
                b.setChecked(false);
                b.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            } else {
                b.setChecked(true);
                b.setBackgroundColor(getResources().getColor(R.color.colorSelectedItem));
            }
        }
    }

    private ToggleButton getSelectedDayOfTheWeek()
    {
        for (ToggleButton b : m_toggleButtons)
            if (b.isChecked())
                return b;

        return null;
    }

    private void toggleInputMode(boolean createNew)
    {
        toggleChildViews(layoutParent, createNew);
        toggleWeekDayButtons(!createNew);
    }

    private void toggleChildViews(ViewGroup viewGroup, boolean createNew)
    {
        for (int i = 0; i < viewGroup.getChildCount(); i++)
        {
            View view = viewGroup.getChildAt(i);

            if (view instanceof ViewGroup) {
                ViewGroup layout = (ViewGroup) view;
                toggleChildViews(layout, createNew);
            }

            if (view.getTag() != null) {
                if (view.getTag().toString().equals(CREATE_NEW_TAG))
                    view.setEnabled(createNew);
                else if (view.getTag().toString().equals(MODIFY_EXISTING_TAG))
                    view.setEnabled(!createNew);
            }
        }
    }

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

    private void syncDailyLimit()
    {
        updateCurrentDailyBudget();

        ToggleButton dayButton = getSelectedDayOfTheWeek();
        int day = Integer.parseInt(dayButton.getTag().toString());
        m_currentDailyBudget = m_plan.getDailyBudgetOn(day);

        String dailyLimit = MoneyFormatter.formatLongToMoney(m_currentDailyBudget.getTotalBudget(), false);
        etDailyLimit.setText(dailyLimit);
    }

    private void toggleWeekDayButtons(boolean enabled)
    {
        for (ToggleButton b : m_toggleButtons)
            b.setEnabled(enabled);
    }

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

    private void updatePlan()
    {
        try
        {
            String name = etName.getText().toString();
            long annualIncome = MoneyFormatter.formatMoneyToLong(etAnnualIncome.getText().toString());
            long annualExpenses = MoneyFormatter.formatMoneyToLong(etAnnualExpenses.getText().toString());
            long annualSavings = MoneyFormatter.formatMoneyToLong(etAnnualSavings.getText().toString());
            long totalWeeklyBudget = MoneyFormatter.formatMoneyToLong(etTotalWeeklyBudget.getText().toString());
            long amountAllocated = MoneyFormatter.formatMoneyToLong(etAmountAllocated.getText().toString());  // TODO: inform user that they have extra money to spend
            long amountRemaining = MoneyFormatter.formatMoneyToLong(etAmountRemaining.getText().toString());  // TODO: maybe ask them to add to savings?

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
    //TODO: Figure out why dialog doesn't wait for user input to continue
    private boolean checkAmountRemaining()
    {
        updateCurrentDailyBudget();

        if (m_amountRemaining > 0)
        {
            //DialogFragment dialog = new CustomAlertDialog().newInstance("Amount Remaining", "You still have some money left over! Add the rest to savings?");
            //dialog.show(getFragmentManager(), "dialog");
            AlertDialog.Builder builder = new AlertDialog.Builder(EditBudgetPlanActivity.this);
            builder.setTitle("Hey there ;)")
                   .setMessage("You still have some money left over! Add the rest to savings?")
                   .setPositiveButton("Yes", dialogClickListener)
                   .setNegativeButton("No", dialogClickListener)
                   .show();
        }
        else if (m_amountRemaining < 0)
        {
            //DialogFragment dialog = new CustomAlertDialog().newInstance("Amount Remaining", "You are spending too much! Try lowering some of your daily limits.");
            //dialog.show(getFragmentManager(), "dialog");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Get out of my face you peasant")
                   .setMessage("You are spending too much! Try lowering some of your daily limits.")
                   .setNeutralButton("Ok", dialogClickListener)
                   .show();
            return false;
        }

        return true;    // TODO: returning true causes rest of code in btnConfirm_Click to execute, hence calling onBackPressed(). Move the actual DB writing code to a method and confirm ONLY calls this one
                        // Call the update/create method within the dialogOnClickListener!
    }

    //endregion

    //region Alert Dialog

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    Toast.makeText(EditBudgetPlanActivity.this, "YAY", Toast.LENGTH_SHORT).show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    Toast.makeText(EditBudgetPlanActivity.this, "NOO", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //endregion

}
