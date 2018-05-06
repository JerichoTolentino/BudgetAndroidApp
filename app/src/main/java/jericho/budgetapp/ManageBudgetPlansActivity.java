package jericho.budgetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import plans.Plan;
import utilities.Utility;

import java.util.ArrayList;

/**
 * An activity where all budget Plans can be managed.
 */
public class ManageBudgetPlansActivity extends AppCompatActivity {

    private ListView lvPlans;
    private Toolbar toolbar;

    /**
     * Initializes widget references and the action bar.
     * @param savedInstanceState
     * @see AppCompatActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_budget_plans);

        lvPlans = findViewById(R.id.lvPlans);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_view_menu);
        toolbar.setTitle(R.string.plans);
        setSupportActionBar(toolbar);
    }

    /**
     * Displays the add new button on the menu.
     * @param menu
     * @return
     * @see AppCompatActivity#onCreateOptionsMenu(Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, toolbar.getMenu());
        Utility.showMenuItems(toolbar.getMenu(), new int[] {R.id.add_new});
        return super.onCreateOptionsMenu(menu);
    }

    //region Event Handlers

    /**
     * Handles button presses on the menu bar.
     * @param item
     * @return
     * @see AppCompatActivity#onOptionsItemSelected(MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                goToMenuActivity();
                break;

            case R.id.add_new:
                goToEditBudgetPlanActivity();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Updates the ListView of Plans.
     */
    @Override
    protected void onResume() {
        updatePlanListView();
        super.onResume();
    }

    //endregion

    //region Helper Methods

    /**
     * Navigates to the MenuActivity.
     */
    private void goToMenuActivity()
    {
        Intent intent = new Intent(ManageBudgetPlansActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    /**
     * Navigates to the EditBudgetPlanActivity to create a new Plan.
     */
    private void goToEditBudgetPlanActivity()
    {
        Intent intent = new Intent(ManageBudgetPlansActivity.this, EditBudgetPlanActivity.class);
        intent.putExtra("createNew", true);
        intent.putExtra("plan", new Plan());
        startActivity(intent);
    }

    /**
     * Updates the ListView to display all Plans.
     */
    private void updatePlanListView()
    {
        try
        {
            ArrayList<Plan> plans = MainActivity.g_dbHandler.queryPlans(null);
            Plan[] plansArray = plans.toArray(new Plan[0]);

            ListAdapter listAdapter = new BudgetPlanRowAdapter(this, plansArray);
            lvPlans = findViewById(R.id.lvPlans);
            lvPlans.setAdapter(listAdapter);
        }
        catch (Exception ex)
        {
            assert false;
        }
    }

    //endregion

}
