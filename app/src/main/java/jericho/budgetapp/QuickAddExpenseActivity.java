package jericho.budgetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import expenses.Expense;
import expenses.Purchase;
import utilities.MoneyFormatter;

/**
 * An activity to quickly make a temporary Expense for a quick purchase.
 */
public class QuickAddExpenseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etName;
    private EditText etPrice;
    private EditText etQuantity;


    /**
     * Initializes widget references and the action bar.
     * @param savedInstanceState
     * @see AppCompatActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add_expense);

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etQuantity = findViewById(R.id.etQuantity);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setTitle(R.string.quick_add_expense);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //endregion

    //region Toolbar Events

    /**
     * Displays the menu bar.
     * @param menu
     * @return
     * @see AppCompatActivity#onCreateOptionsMenu(Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, toolbar.getMenu());

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handles menu button presses.
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

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region Event Handlers

    /**
     * Creates a temporary Purchase with the specified data and navigates to the MainActivity to make the quick purchase.
     * @param v
     */
    public void btnConfirm_OnClick(View v)
    {
        try {
            String name = etName.getText().toString();
            long price = MoneyFormatter.formatMoneyToLong(etPrice.getText().toString());
            int quantity = Integer.parseInt(etQuantity.getText().toString());

            Expense dummyExpense = new Expense(name, price, "Dummy", "Dummy expense for quick add");
            Purchase purchase = new Purchase(dummyExpense, quantity);

            Intent intent = new Intent(QuickAddExpenseActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("quickAddExpense", purchase);
            startActivity(intent);
        }
        catch (Exception ex)
        {
            System.err.println(ex.toString());
        }
    }

    //endregion


}
