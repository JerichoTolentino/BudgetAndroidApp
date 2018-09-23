package jericho.budgetapp.Presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import jericho.budgetapp.Model.ExpenseGroup;
import jericho.budgetapp.R;
import utilities.Utility;

import java.util.ArrayList;

/**
 * An activity where all ExpenseGroups can be managed.
 */
public class ManageExpenseGroupsActivity extends AppCompatActivity {

    //region Members

    private Toolbar toolbar;

    //endregion

    //region onCreate()

    /**
     * Initializes the action bar.
     * @param savedInstanceState
     * @see AppCompatActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_expensegroups);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_view_menu);
        toolbar.setTitle(R.string.expense_groups);
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

    //endregion

    //region Event Handlers

    /**
     * Handles menu button presses.
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
                goToEditExpenseGroupActivity();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Updates the ListView of ExpenseGroups.
     */
    @Override
    protected void onResume() {
        updateExpenseGroupListView();
        super.onResume();
    }

    //TODO: Figure out what to do with this button.
    /**
     * UNUSED
     * @param v
     */
    public void btnSwitchView_OnClick(View v)
    {
        Toast.makeText(this, "Do something else.", Toast.LENGTH_SHORT).show();
        //goToManageExpensesActivity();
    }

    //endregion

    // region Helper Methods

    /**
     * Navigates to the EditExpenseGroupActivity to create a new ExpenseGroup.
     */
    private void goToEditExpenseGroupActivity()
    {
        Intent intent = new Intent(ManageExpenseGroupsActivity.this, EditExpenseGroupActivity.class);
        intent.putExtra("expenseGroup", new ExpenseGroup());
        intent.putExtra("createNew", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    /**
     * Navigates to the MenuActivity.
     */
    private void goToMenuActivity()
    {
        Intent intent = new Intent(ManageExpenseGroupsActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    /**
     * Navigate to the ManageExpensesActivity.
     */
    private void goToManageExpensesActivity()
    {
        Intent intent = new Intent(ManageExpenseGroupsActivity.this, ManageExpensesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Updates the ListView of ExpenseGroups to display all ExpenseGroups.
     */
    public void updateExpenseGroupListView()
    {
        ArrayList<ExpenseGroup> expenseGroups = MainActivity.g_dbHandler.queryExpenseGroups(null);
        ExpenseGroup[] expenseGroupArray = new ExpenseGroup[expenseGroups.size()];

        int index = 0;
        for (ExpenseGroup e : expenseGroups)
        {
            expenseGroupArray[index] = e;
            index++;
        }

        ExpenseGroupRowAdapter adapter = new ExpenseGroupRowAdapter(ManageExpenseGroupsActivity.this, expenseGroupArray);
        ListView lvExpenses = findViewById(R.id.lvExpenses);
        lvExpenses.setAdapter(adapter);
    }

    //endregion

}
