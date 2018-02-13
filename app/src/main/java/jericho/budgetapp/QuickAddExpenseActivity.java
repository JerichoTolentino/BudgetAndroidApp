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
import android.widget.Toast;

import com.budget_app.expenses.Expense;
import com.budget_app.expenses.Purchase;
import com.budget_app.utilities.MoneyFormatter;

import java.util.Date;

import databases.DBHandler;
import utils.Utils;

public class QuickAddExpenseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etName;
    private EditText etPrice;
    private EditText etQuantity;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, toolbar.getMenu());

        return super.onCreateOptionsMenu(menu);
    }

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
