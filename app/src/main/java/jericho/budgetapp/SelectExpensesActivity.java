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

import com.budget_app.expenses.Expense;
import com.budget_app.expenses.ExpenseGroup;
import com.budget_app.expenses.ExpenseInGroup;
import com.budget_app.expenses.Purchase;

import java.util.ArrayList;

import utils.Utils;

public class SelectExpensesActivity extends AppCompatActivity {

    //region Members

    private Toolbar toolbar;
    private ListView lvExpenses;
    private ExpenseGroup m_expenseGroup;

    //endregion

    //region onCreate()

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, toolbar.getMenu());
        Utils.showMenuItems(toolbar.getMenu(), null);
        return super.onCreateOptionsMenu(menu);
    }

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

    public void btnConfirm_OnClick(View v)
    {
        try
        {
            ArrayList<ExpenseInGroup> expenses = new ArrayList<>();

            // build list of expenses user wants to add
            for (int i = 0; i < lvExpenses.getAdapter().getCount(); i++) {
                Purchase purchase = (Purchase) lvExpenses.getAdapter().getItem(i);
                if (purchase.getQuantity() > 0)
                    expenses.add(new ExpenseInGroup((Expense) purchase.getItem(), purchase.getQuantity()));
            }

            // add the expenses to the group
            for (ExpenseInGroup addExpense : expenses) {
                boolean found = false;
                for (ExpenseInGroup currExpense : m_expenseGroup.getExpenses()) {
                    if (currExpense.getId() == addExpense.getId()) {
                        found = true;
                        currExpense.setQuantity(currExpense.getQuantity() + addExpense.getQuantity());
                        m_expenseGroup.updatePrice();
                        break;
                    }
                }
                if (!found)
                    m_expenseGroup.addExpense(addExpense);
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

    private void returnToEditExpenseGroupActivity()
    {
        Intent intent = new Intent(SelectExpensesActivity.this, EditExpenseGroupActivity.class);
        intent.putExtra("expenseGroup", m_expenseGroup);
        intent.putExtra("createNew", getIntent().getExtras().getBoolean("createNew"));
        startActivity(intent);
    }

    //endregion

}
