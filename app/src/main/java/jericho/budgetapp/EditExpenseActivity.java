package jericho.budgetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.budget_app.expenses.Expense;

import databases.DBHandler;
import utils.Utils;

public class EditExpenseActivity extends AppCompatActivity {

    private ActionMenuView amvMenu;
    private EditText etName;
    private EditText etPrice;
    private EditText etCategory;
    private EditText etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etCategory = findViewById(R.id.etCategory);
        etDescription = findViewById(R.id.etDescription);

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        amvMenu = toolbar.findViewById(R.id.amvMenu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String expenseID = getIntent().getExtras().getString("ExpenseID");
        String expenseName = getIntent().getExtras().getString("ExpenseName");
        String expensePrice = getIntent().getExtras().getString("ExpensePrice");
        String expenseCategory = getIntent().getExtras().getString("ExpenseCategory");
        String expenseDescription = getIntent().getExtras().getString("ExpenseDescription");

        etName.setText(expenseName);
        etPrice.setText(expensePrice);
        etCategory.setText(expenseCategory);
        etDescription.setText(expenseDescription);
    }

    //region Toolbar Events

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, amvMenu.getMenu());

        Utils.showMenuItems(amvMenu.getMenu(), null);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //endregion

}
