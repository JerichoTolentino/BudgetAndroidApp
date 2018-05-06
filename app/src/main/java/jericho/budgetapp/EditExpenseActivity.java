package jericho.budgetapp;

import android.content.DialogInterface;
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
import android.widget.Toast;

import expenses.Expense;
import utilities.MoneyFormatter;

import java.util.Map;

import utilities.Utility;
import databases.DBHandler;

/**
 * An activity where Expenses can be created/edited.
 */
public class EditExpenseActivity extends AppCompatActivity {

    //region Members

    private Toolbar toolbar;
    private EditText etName;
    private EditText etPrice;
    private EditText etCategory;
    private EditText etDescription;
    private Expense m_expense;
    private boolean m_createNew;

    //endregion

    //region onCreate()

    /**
     * Initializes the activity with the Expense passed in the Intent.
     * @param savedInstanceState
     * @see AppCompatActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etCategory = findViewById(R.id.etCategory);
        etDescription = findViewById(R.id.etDescription);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setTitle(R.string.edit_expense);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Map<String, Object> extras = Utility.getExtrasFromIntent(getIntent());

        if (extras.values().size() > 0)
        {
            m_expense = (Expense) extras.get("expense");
            m_createNew = (boolean) extras.get("createNew");
        }

        InputMultilineEditText.setContext(this);

        if (!m_createNew)
        {
            etName.setText(m_expense.getName());
            etPrice.setText(MoneyFormatter.formatLongToMoney(m_expense.getPrice(), false));
            etCategory.setText(m_expense.getCategory());
            etDescription.setText(m_expense.getDescription());
        }
    }

    //endregion

    //region Toolbar Events

    /**
     * Displays the remove icon on the action bar.
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
     * Handles the action bar buttons being pressed.
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
                builder.setMessage("Are you sure you want to delete this expense?")
                       .setPositiveButton("Yes", dialogClickListener)
                       .setNegativeButton("No", dialogClickListener).show();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region Event Handlers

    /**
     * Saves the changes made to the Expense.
     * @param v
     */
    public void btnConfirm_OnClick(View v)
    {
        try {
            String message = "Failed to save changes.";
            m_expense.setName(etName.getText().toString());
            m_expense.setPrice(MoneyFormatter.formatMoneyToLong(etPrice.getText().toString()));
            m_expense.setCategory(etCategory.getText().toString());
            m_expense.setDescription(etDescription.getText().toString());

            if (m_createNew) {
                MainActivity.g_dbHandler.addExpense(m_expense);
                message = "Expense added!";
            } else {
                if (MainActivity.g_dbHandler.queryExpenses(DBHandler.EXPENSE_COL_ID + "=" + m_expense.getId()).size() == 1) {
                    MainActivity.g_dbHandler.updateExpense(m_expense);
                    message = "Expense updated!";
                }
            }

            Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            onBackPressed();    // leave activity
        }
        catch (Exception ex)
        {
            System.err.println(ex.toString());
        }
    }

    //endregion

    //region Alert Dialog

    /**
     * Deletes the Expense from the database if the user confirms.
     */
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    MainActivity.g_dbHandler.removeExpense(m_expense.getId());
                    Toast.makeText(EditExpenseActivity.this, "Expense deleted!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    //endregion

}
