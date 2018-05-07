package jericho.budgetapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import expenses.Expense;
import expenses.ExpenseGroup;
import expenses.Purchase;
import utilities.Utility;

import java.util.ArrayList;

/**
 * An activity where multiple Expenses can be selected.
 */
public class SelectExpensesActivity extends AppCompatActivity {

    //region Members

    private Toolbar toolbar;
    private ListView lvExpenses;
    private ExpenseGroup m_expenseGroup;

    //endregion

    //region onCreate()

    /**
     * Initializes the activity with all Expenses.
     * @param savedInstanceState
     * @see AppCompatActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_expenses);

        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        m_expenseGroup = (ExpenseGroup) getIntent().getSerializableExtra("expenseGroup");

        ArrayList<Expense> expenses = MainActivity.g_dbHandler.queryExpenses(null);
        Purchase items[] = new Purchase[expenses.size()];

        int index = 0;
        for (Expense e : expenses)
        {
            items[index] = new Purchase(e, 0);
            index++;
        }

        ListAdapter listAdapter = new PurchaseRowAdapter(this, items);
        lvExpenses = findViewById(R.id.lvExpenses);
        lvExpenses.setAdapter(listAdapter);
    }

    //endregion

    // region Toolbar Events

    /**
     * Displays the menu.
     * @param menu
     * @return
     * @see AppCompatActivity#onCreateOptionsMenu(Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, toolbar.getMenu());
        Utility.showMenuItems(toolbar.getMenu(), null);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handles menu button presses
     * @param item
     * @return
     * @see AppCompatActivity#onOptionsItemSelected(MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            // this is navigation icon listener
            case android.R.id.home:
                returnToEditExpenseGroupActivity();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region Event Handlers

    /**
     * Confirms the selection and returns to the EditExpenseGroupActivity.
     * @param v
     */
    public void btnConfirm_OnClick(View v)
    {
        try
        {
            // Add the desired Expenses to the ExpenseGroup
            for (int i = 0; i < lvExpenses.getAdapter().getCount(); i++)
            {
                Purchase purchase = (Purchase) lvExpenses.getAdapter().getItem(i);

                // We know each Purchase item will only contain an Expense, so this is a safe cast.
                m_expenseGroup.addExpense((Expense) purchase.getItem(), purchase.getQuantity());
            }

            returnToEditExpenseGroupActivity();
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage() + ex.getStackTrace());
        }
    }

    //endregion

    //region Helper Methods

    /**
     * Navigates to the EditExpenseGroupActivity with the Expense(s) selected passed in the Intent.
     */
    private void returnToEditExpenseGroupActivity()
    {
        Intent intent = new Intent(SelectExpensesActivity.this, EditExpenseGroupActivity.class);
        intent.putExtra("expenseGroup", m_expenseGroup);
        intent.putExtra("createNew", getIntent().getExtras().getBoolean("createNew"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    //endregion

}
