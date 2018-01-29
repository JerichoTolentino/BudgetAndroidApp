package jericho.budgetapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.budget_app.expenses.ExpenseGroup;
import com.budget_app.jt_linked_list.Node;
import com.budget_app.jt_linked_list.SortedList;

import utils.Utils;

public class ManageExpenseGroupsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_expensegroups);

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
                intent = new Intent(ManageExpenseGroupsActivity.this, MenuActivity.class);
                startActivity(intent);
                break;

            case R.id.add_new:
                intent = new Intent(ManageExpenseGroupsActivity.this, EditExpenseGroupActivity.class);
                intent.putExtra("expenseGroup", new ExpenseGroup());
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
        updateExpenseGroupListView();
        super.onResume();
    }

    //region Event Handlers

    public void btnSwitchView_OnClick(View v)
    {
        Intent intent = new Intent(ManageExpenseGroupsActivity.this, ManageExpensesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //endregion

    // region Helper Methods

    public void updateExpenseGroupListView()
    {
        SortedList expenseGroups = new SortedList(MainActivity.g_dbHandler.queryExpenseGroups(null));
        ExpenseGroup[] expenseGroupArray = new ExpenseGroup[expenseGroups.getSize()];

        Node curr = expenseGroups.getHead();
        int index = 0;
        while(curr != null)
        {
            expenseGroupArray[index] = (ExpenseGroup) curr.getItem();
            index++;
            curr = curr.getNext();
        }

        ExpenseGroupRowAdapter adapter = new ExpenseGroupRowAdapter(ManageExpenseGroupsActivity.this, expenseGroupArray);
        ListView lvExpenses = findViewById(R.id.lvExpenses);
        lvExpenses.setAdapter(adapter);
    }

    //endregion
}
