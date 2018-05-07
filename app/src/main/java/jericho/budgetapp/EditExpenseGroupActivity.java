package jericho.budgetapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
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

import expenses.Expense;
import expenses.ExpenseGroup;
import utilities.KeyValuePair;
import utilities.MoneyFormatter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utilities.Utility;
import databases.DBHandler;

import static utilities.Utility.count;

/**
 * An activity where ExpenseGroups can be created/edited.
 */
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

    /**
     * Initializes the activity with the ExpenseGroup passed in the Intent.
     * @param savedInstanceState
     * @see AppCompatActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense_group);

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etCategory = findViewById(R.id.etCategory);
        lvExpenses = findViewById(R.id.lvExpenses);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setTitle(R.string.edit_expense_group);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Map<String, Object> extras = Utility.getExtrasFromIntent(getIntent());

        if (extras.values().size() > 0)
        {
            m_expenseGroup = (ExpenseGroup) extras.get("expenseGroup");
            m_createNew = (boolean) extras.get("createNew");

            etName.setText(m_expenseGroup.getName());
            etPrice.setText(MoneyFormatter.formatLongToMoney(m_expenseGroup.getPrice(), false));
            etCategory.setText(m_expenseGroup.getCategory());

            updateExpensesListView();
        }
    }

    //endregion

    //region Toolbar Events

    /**
     * Displays the remove button in the action bar.
     * @param menu
     * @return
     * @see AppCompatActivity#onCreateOptionsMenu(Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, toolbar.getMenu());

        Utility.showMenuItems(toolbar.getMenu(), new int[] {R.id.remove});

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handles the menu buttons being pressed.
     * @param item
     * @return
     * @see AppCompatActivity#onOptionsItemSelected(MenuItem)
     */
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

    /**
     * Navigates back.
     * @return
     * @see AppCompatActivity#onSupportNavigateUp()
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //endregion

    //region Event Handlers

    /**
     * Saves the changes made to the ExpenseGroup.
     * @param v
     */
    public void btnConfirm_OnClick(View v)
    {
        try {
            String message = "Failed to save changes.";

            m_expenseGroup.setName(etName.getText().toString());
            m_expenseGroup.setPrice(MoneyFormatter.formatMoneyToLong(etPrice.getText().toString()));
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
        }
        catch (Exception ex)
        {
            System.err.println(ex.toString());
        }
    }

    /**
     * Opens the SelectExpensesActivity to select Expenses to add to the current ExpenseGroup.
     * @param v
     */
    public void btnAddExpenses_OnClick(View v)
    {
        try
        {
            goToSelectExpensesActivity();
        }
        catch (Exception ex)
        {
            System.err.println(ex.toString());
        }
    }

    //endregion

    //region Alert Dialog

    /**
     * Deletes the current ExpenseGroup if the user confirms.
     */
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

    //region Public Methods

    /**
     * Removes the specified Expense from the current ExpenseGroup.
     * @param expense The Expense to remove.
     */
    public void removeExpenseFromGroup(Expense expense)
    {
        m_expenseGroup.removeExpense(expense);
    }

    //endregion

    //region Helper Methods

    /**
     * Navigates to the SelectExpensesActivity.
     */
    private void goToSelectExpensesActivity()
    {
        Intent intent = new Intent(EditExpenseGroupActivity.this, SelectExpensesActivity.class);
        m_expenseGroup.setName(etName.getText().toString());
        m_expenseGroup.updatePrice();
        m_expenseGroup.setCategory(etCategory.getText().toString());

        intent.putExtra("expenseGroup", m_expenseGroup);
        intent.putExtra("createNew", m_createNew);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    /**
     * Updates the expenses list view to display the Expenses in the current ExpenseGroup.
     */
    public void updateExpensesListView()
    {
        Iterable<KeyValuePair<Expense, Integer>> expenseList = m_expenseGroup.getExpenses();

        // build list of Expense-quantity KeyValuePairs
        List<KeyValuePair<Expense, Integer>> expenses = new ArrayList<>(Utility.count(expenseList));
        for (KeyValuePair<Expense, Integer> kvp : expenseList)
            expenses.add(kvp);

        ListAdapter listAdapter = new ExpenseWithQuantityRowAdapter(this, expenses);
        ListView lvExpenses = findViewById(R.id.lvExpenses);
        lvExpenses.setAdapter(listAdapter);

        etPrice.setEnabled(true);
        etPrice.setText(MoneyFormatter.formatLongToMoney(m_expenseGroup.getPrice(), false));
        etPrice.setEnabled(false);
    }

    //endregion

}
