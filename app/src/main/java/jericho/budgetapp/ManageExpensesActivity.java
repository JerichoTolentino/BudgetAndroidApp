package jericho.budgetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import expenses.Expense;
import utilities.Utility;

import java.util.ArrayList;

/**
 * An activity where all Expenses can be managed.
 */
public class ManageExpensesActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_manage_expenses);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_view_menu);
        toolbar.setTitle(R.string.expenses);
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
     * Handles the menu button presses.
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
                goToEditExpenseActivity();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Updates the ListView of Expenses.
     */
    @Override
    protected void onResume() {
        updateExpenseListView();
        super.onResume();
    }

    //TODO: figure out what to do with this button.
    /**
     * UNUSED
     * @param v
     */
    public void btnSwitchView_OnClick(View v)
    {
        Toast.makeText(this, "Do something else.", Toast.LENGTH_SHORT).show();
        //goToManageExpenseGroupsActivity();
    }

    //endregion

    // region Helper Methods

    /**
     * Navigates to the ManageExpenseGroupsActivity.
     */
    private void goToManageExpenseGroupsActivity()
    {
        Intent intent = new Intent(ManageExpensesActivity.this, ManageExpenseGroupsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Navigates to the EditExpenseActivity to create a new Expense.
     */
    private void goToEditExpenseActivity()
    {
        Intent intent = new Intent(ManageExpensesActivity.this, EditExpenseActivity.class);
        intent.putExtra("expense", new Expense());
        intent.putExtra("createNew", true);
        startActivity(intent);
    }

    /**
     * Navigates to the MenuActivity.
     */
    private void goToMenuActivity()
    {
        Intent intent = new Intent(ManageExpensesActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    /**
     * Updates the ListView of Expenses with all Expenses.
     */
    public void updateExpenseListView()
    {
        ArrayList<Expense> expenses = MainActivity.g_dbHandler.queryExpenses(null);
        Expense[] expenseArray = new Expense[expenses.size()];

        int index = 0;
        for (Expense e : expenses)
        {
            expenseArray[index] = e;
            index++;
        }

        ExpenseRowAdapter adapter = new ExpenseRowAdapter(ManageExpensesActivity.this, expenseArray);
        ListView lvExpenses = findViewById(R.id.lvExpenses);
        lvExpenses.setAdapter(adapter);
    }

    //endregion
}
