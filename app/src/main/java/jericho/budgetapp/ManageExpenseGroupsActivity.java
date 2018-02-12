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
import android.widget.Toast;

import com.budget_app.expenses.ExpenseGroup;

import java.util.ArrayList;

import utils.Utils;

public class ManageExpenseGroupsActivity extends AppCompatActivity {

    //region Members

    private Toolbar toolbar;

    //endregion

    //region onCreate()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_expensegroups);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_view_menu);
        toolbar.setTitle(R.string.expense_groups);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, toolbar.getMenu());
        Utils.showMenuItems(toolbar.getMenu(), new int[] {R.id.add_new});
        return super.onCreateOptionsMenu(menu);
    }

    //endregion

    //region Event Handlers

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

    @Override
    protected void onResume() {
        updateExpenseGroupListView();
        super.onResume();
    }

    public void btnSwitchView_OnClick(View v)
    {
        Toast.makeText(this, "Do something else.", Toast.LENGTH_SHORT).show();
        //goToManageExpensesActivity();
    }

    //endregion

    // region Helper Methods

    private void goToEditExpenseGroupActivity()
    {
        Intent intent = new Intent(ManageExpenseGroupsActivity.this, EditExpenseGroupActivity.class);
        intent.putExtra("expenseGroup", new ExpenseGroup());
        intent.putExtra("createNew", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void goToMenuActivity()
    {
        Intent intent = new Intent(ManageExpenseGroupsActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    private void goToManageExpensesActivity()
    {
        Intent intent = new Intent(ManageExpenseGroupsActivity.this, ManageExpensesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

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
