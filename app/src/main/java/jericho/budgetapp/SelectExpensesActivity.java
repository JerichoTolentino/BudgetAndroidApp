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
import com.budget_app.expenses.Purchase;
import com.budget_app.jt_linked_list.LinkedList;
import com.budget_app.jt_linked_list.Node;
import com.budget_app.jt_linked_list.SortedList;
import com.budget_app.master.BudgetAppManager;

import databases.DBHandler;
import utils.Utils;

public class SelectExpensesActivity extends AppCompatActivity {

    public static DBHandler g_dbHandler;

    private Toolbar toolbar;
    private ListView lvExpenses;
    private ExpenseGroup m_expenseGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_expenses);
        BudgetAppManager.init();
        g_dbHandler = new DBHandler(getApplicationContext(), null, null, 0);

        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        m_expenseGroup = (ExpenseGroup) getIntent().getSerializableExtra("expenseGroup");

        SortedList expenses = new SortedList(g_dbHandler.queryExpenses(null));
        Purchase items[] = new Purchase[expenses.getSize()];

        Node curr = expenses.getHead();
        int index = 0;
        while (curr != null)
        {
            items[index] = new Purchase((Expense) curr.getItem(), 0);
            index++;
            curr = curr.getNext();
        }

        ListAdapter listAdapter = new PurchaseRowAdapter(this, items);
        lvExpenses = findViewById(R.id.lvExpenses);
        lvExpenses.setAdapter(listAdapter);
    }

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
        Intent intent;

        switch (item.getItemId())
        {
            // this is navigation icon listener
            case android.R.id.home:
                intent = new Intent(SelectExpensesActivity.this, EditExpenseGroupActivity.class);
                startActivity(intent);

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
            LinkedList purchases = new LinkedList();

            for (int i = 0; i < lvExpenses.getAdapter().getCount(); i++)
            {
                Purchase purchase = (Purchase) lvExpenses.getAdapter().getItem(i);
                if (purchase.getQuantity() > 0)
                {
                    for (int j = 0; j < purchase.getQuantity(); j++)
                        purchases.insertFront(new Expense((Expense) purchase.getItem()));
                }
            }

            Intent intent = new Intent(SelectExpensesActivity.this, EditExpenseGroupActivity.class);
            intent.putExtra("expensesToAdd", purchases);
            intent.putExtra("expenseGroup", m_expenseGroup);
            intent.putExtra("createNew", getIntent().getExtras().getBoolean("createNew"));
            startActivity(intent);

        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage() + ex.getStackTrace());
        }
    }

    //endregion

    //region Helper Methods



    //endregion

}
