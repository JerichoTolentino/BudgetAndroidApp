package jericho.budgetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import databases.DBHandler;

public class EditExpenseActivity extends AppCompatActivity {

    private ActionMenuView amvMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        amvMenu = toolbar.findViewById(R.id.amvMenu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);

        String expenseID = getIntent().getExtras().getString("ExpenseID");
        
    }

    //region Toolbar Events

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, amvMenu.getMenu());

        amvMenu.getMenu().findItem(R.id.add_new).setVisible(false);
        amvMenu.getMenu().findItem(R.id.manage_expenses_title).setVisible(false);
        amvMenu.getMenu().findItem(R.id.view_history).setVisible(false);
        amvMenu.getMenu().findItem(R.id.open_side_menu).setVisible(false);
        amvMenu.getMenu().findItem(R.id.toolbar_title).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    //endregion

}
