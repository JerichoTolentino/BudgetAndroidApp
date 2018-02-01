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

import com.budget_app.expenses.Expense;

import java.util.ArrayList;

import utils.Utils;

public class ManageExpensesActivity extends AppCompatActivity {

    //region Members

    private Toolbar toolbar;

    //endregion

    //region onCreate()

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
                goToEditExpenseActivity();
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

    public void btnSwitchView_OnClick(View v)
    {
        Toast.makeText(this, "Do something else.", Toast.LENGTH_SHORT).show();
        //goToManageExpenseGroupsActivity();
    }

    //endregion

    // region Helper Methods

    private void goToManageExpenseGroupsActivity()
    {
        Intent intent = new Intent(ManageExpensesActivity.this, ManageExpenseGroupsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToEditExpenseActivity()
    {
        Intent intent = new Intent(ManageExpensesActivity.this, EditExpenseActivity.class);
        intent.putExtra("expense", new Expense());
        intent.putExtra("createNew", true);
        startActivity(intent);
    }

    private void goToMenuActivity()
    {
        Intent intent = new Intent(ManageExpensesActivity.this, MenuActivity.class);
        startActivity(intent);
    }

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
