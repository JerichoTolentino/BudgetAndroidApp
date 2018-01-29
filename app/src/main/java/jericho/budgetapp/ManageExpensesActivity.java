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

import com.budget_app.expenses.Expense;
import com.budget_app.jt_linked_list.Node;
import com.budget_app.jt_linked_list.SortedList;

import utils.Utils;

public class ManageExpensesActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_expenses);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_view_menu);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, toolbar.getMenu());
        Utils.showMenuItems(toolbar.getMenu(), new int[] {R.id.add_new});
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId())
        {
            case android.R.id.home:
                intent = new Intent(ManageExpensesActivity.this, MenuActivity.class);
                startActivity(intent);
                break;

            case R.id.add_new:
                intent = new Intent(ManageExpensesActivity.this, EditExpenseActivity.class);
                intent.putExtra("expense", new Expense());
                intent.putExtra("createNew", true);
                startActivity(intent);

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        updateExpenseListView();
        super.onResume();
    }

    //region Event Handlers

    public void btnSwitchView_OnClick(View v)
    {
        Intent intent = new Intent(ManageExpensesActivity.this, ManageExpenseGroupsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //endregion

    // region Helper Methods

    public void updateExpenseListView()
    {
        SortedList expenses = new SortedList(MainActivity.g_dbHandler.queryExpenses(null));
        Expense[] expenseArray = new Expense[expenses.getSize()];

        Node curr = expenses.getHead();
        int index = 0;
        while(curr != null)
        {
            expenseArray[index] = (Expense) curr.getItem();
            index++;
            curr = curr.getNext();
        }

        ExpenseRowAdapter adapter = new ExpenseRowAdapter(ManageExpensesActivity.this, expenseArray);
        ListView lvExpenses = findViewById(R.id.lvExpenses);
        lvExpenses.setAdapter(adapter);
    }

    //endregion
}
