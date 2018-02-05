package jericho.budgetapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private boolean m_createNew;

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

    //endregion

    //region Functionality Methods

    public void generatePlan() throws ParseException
    {
        String planName = etName.getText().toString();

        long annualIncome = MoneyFormatter.formatMoneyToLong(etAnnualIncome.getText().toString());
        long annualExpenses = MoneyFormatter.formatMoneyToLong(etAnnualExpenses.getText().toString());
        long annualSavings = MoneyFormatter.formatMoneyToLong(etAnnualSavings.getText().toString());

        m_plan = new Plan(planName, annualIncome, annualExpenses, annualSavings);

        syncPlanWithWidgets();
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

    private void syncPlanWithWidgets()
    {
        String name = m_plan.getName();
        String annualIncome = MoneyFormatter.formatLongToMoney(m_plan.getAnnualIncome(), false);
        String annualExpenses = MoneyFormatter.formatLongToMoney(m_plan.getAnnualExpenses(), false);
        String annualSavings = MoneyFormatter.formatLongToMoney(m_plan.getAnnualSavings(), false);

        PeriodicBudget weeklyBudget = m_plan.getWeeklyBudget();
        String totalWeeklyBudget = MoneyFormatter.formatLongToMoney(weeklyBudget.getTotalBudget(), false);
        String amountAllocated = MoneyFormatter.formatLongToMoney(weeklyBudget.getAmountSpent(), false);
        String amountRemaining = MoneyFormatter.formatLongToMoney(weeklyBudget.getRemainingAmount(), false);

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
        ToggleButton dayButton = getSelectedDayOfTheWeek();
        int day = Integer.parseInt(dayButton.getTag().toString());
        PeriodicBudget dailyBudget = m_plan.getDailyBudgetOn(day);

        String dailyLimit = MoneyFormatter.formatLongToMoney(dailyBudget.getTotalBudget(), false);
        etDailyLimit.setText(dailyLimit);
    }

    private void toggleWeekDayButtons(boolean enabled)
    {
        for (ToggleButton b : m_toggleButtons)
            b.setEnabled(enabled);
    }

    //endregion

}
