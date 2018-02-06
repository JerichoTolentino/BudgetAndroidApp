package jericho.budgetapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.budget_app.expenses.Expense;
import com.budget_app.expenses.ExpenseGroup;
import com.budget_app.expenses.Purchase;
import com.budget_app.plans.Plan;

import java.util.ArrayList;

public class ManageBudgetPlansActivity extends AppCompatActivity {

    private ListView lvPlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_budget_plans);

        lvPlans = findViewById(R.id.lvPlans);

        initPlans();
    }

    //region Helper Methods

    private void initPlans()
    {
        ArrayList<Plan> plans = MainActivity.g_dbHandler.queryPlans(null);
        Plan[] plansArray = (Plan[]) plans.toArray();

        ListAdapter listAdapter = new BudgetPlanRowAdapter(this, plansArray);
        lvPlans = findViewById(R.id.lvPurchases);
        lvPlans.setAdapter(listAdapter);
    }

    //endregion

}
