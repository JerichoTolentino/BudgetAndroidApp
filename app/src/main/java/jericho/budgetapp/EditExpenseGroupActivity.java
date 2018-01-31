package jericho.budgetapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.budget_app.expenses.ExpenseGroup;
import com.budget_app.expenses.ExpenseInGroup;
import com.budget_app.utilities.MoneyFormatter;

import java.util.ArrayList;

import databases.DBHandler;
import utils.Utils;

public class EditExpenseGroupActivity extends AppCompatActivity {

    //region Members

    private Toolbar toolbar;
    private EditText etName;
    private EditText etPrice;
    private EditText etCategory;
    private ListView lvExpenses;
    private ExpenseGroup m_expenseGroup;
    private boolean m_createNew;

    //endregion

    //region onCreate()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense_group);

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etCategory = findViewById(R.id.etCategory);
        lvExpenses = findViewById(R.id.lvExpenses);

        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent() != null && getIntent().getExtras() != null) {
            m_expenseGroup = (ExpenseGroup) getIntent().getSerializableExtra("expenseGroup");
            m_createNew = getIntent().getExtras().getBoolean("createNew");

            etName.setText(m_expenseGroup.getName());
            etPrice.setText(MoneyFormatter.formatLongToMoney(m_expenseGroup.getPrice()).replace("$", ""));
            etCategory.setText(m_expenseGroup.getCategory());

            updateExpensesListView();
        }
    }

    //endregion

    //region Toolbar Events

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, toolbar.getMenu());

        Utils.showMenuItems(toolbar.getMenu(), new int[] {R.id.remove});

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.remove:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete this expense group?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //endregion

    //region Event Handlers

    public void btnConfirm_OnClick(View v)
    {
        try {
            String message = "Failed to save changes.";

            m_expenseGroup.setName(etName.getText().toString());
            m_expenseGroup.setPrice(Long.parseLong(etPrice.getText().toString().replace(",", "").replace(".","")));
            m_expenseGroup.setCategory(etCategory.getText().toString());

            if (m_createNew) {
                MainActivity.g_dbHandler.addExpenseGroup(m_expenseGroup);
                message = "Expense group added!";
            } else {
                if (MainActivity.g_dbHandler.queryExpenseGroups(DBHandler.EXPENSEGROUP_COL_ID + "=" + m_expenseGroup.getId()).size() == 1) {
                    MainActivity.g_dbHandler.updateExpenseGroup(m_expenseGroup);
                    message = "Expense group updated!";
                }
            }

            Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            onBackPressed();
            //Intent intent = new Intent(EditExpenseGroupActivity.this, ManageExpenseGroupsActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //startActivity(intent);
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage() + ex.getStackTrace());
        }
    }

    public void btnAddExpenses_OnClick(View v)
    {
        try
        {
            goToSelectExpensesActivity();
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage() + ex.getStackTrace());
        }
    }

    //endregion

    //region Alert Dialog

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    MainActivity.g_dbHandler.removeExpenseGroup(m_expenseGroup.getId());
                    Toast.makeText(EditExpenseGroupActivity.this, "Expense group deleted!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    //endregion

    //region Helper Methods

    private ArrayList<ExpenseInGroup> getExpensesFromListView()
    {
        ArrayList<ExpenseInGroup> expenses = new ArrayList<>();

        for (int i = 0; i < lvExpenses.getAdapter().getCount(); i++)
        {
            ExpenseInGroup e = (ExpenseInGroup) lvExpenses.getAdapter().getItem(i);
            expenses.add(e);
        }

        return expenses;
    }

    private void goToSelectExpensesActivity()
    {
        Intent intent = new Intent(EditExpenseGroupActivity.this, SelectExpensesActivity.class);
        m_expenseGroup.setName(etName.getText().toString());
        m_expenseGroup.updatePrice();
        m_expenseGroup.setCategory(etCategory.getText().toString());

        intent.putExtra("expenseGroup", m_expenseGroup);
        intent.putExtra("createNew", m_createNew);
        startActivity(intent);
    }

    public void updateExpensesListView()
    {
        ArrayList<ExpenseInGroup> expenseList = m_expenseGroup.getExpenses();

        // build array of ExpenseInGroup objects
        ExpenseInGroup[] expenses = new ExpenseInGroup[expenseList.size()];
        int i = 0;
        for (ExpenseInGroup e : expenseList)
        {
            expenses[i] = e;
            i++;
        }

        ListAdapter listAdapter = new ExpenseWithQuantityRowAdapter(this, expenses);
        ListView lvExpenses = findViewById(R.id.lvExpenses);
        lvExpenses.setAdapter(listAdapter);

        etPrice.setEnabled(true);
        etPrice.setText(MoneyFormatter.formatLongToMoney(m_expenseGroup.getPrice()).replace("$", ""));
        etPrice.setEnabled(false);
    }

    //endregion

    //region Public Methods

    public void removeExpenseFromGroup(ExpenseInGroup expense)
    {
        m_expenseGroup.removeExpense(expense);
    }

    //endregion

}
