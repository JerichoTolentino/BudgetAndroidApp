package jericho.budgetapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.budget_app.expenses.Expense;
import com.budget_app.expenses.ExpenseGroup;
import com.budget_app.expenses.Purchase;
import com.budget_app.jt_interfaces.Priceable;
import com.budget_app.utilities.MoneyFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import databases.DBHandler;
import utils.Utils;

public class MainActivity extends AppCompatActivity {

    public static DBHandler g_dbHandler;

    //region Members

    private Toolbar toolbar;
    private ArrayList<Purchase> m_allPurchases;

    //endregion

    //region onCreate()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        g_dbHandler = new DBHandler(getApplicationContext(), null, null, 0);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_view_menu);
        setSupportActionBar(toolbar);

        m_allPurchases = new ArrayList<>();

        ArrayList<Expense> expenses = g_dbHandler.queryExpenses(null);
        ArrayList<ExpenseGroup> expenseGroups = g_dbHandler.queryExpenseGroups(null);
        Priceable[] priceables = GeneratePriceablesArray(expenses, expenseGroups);

        ListAdapter listAdapter = new PriceableRowAdapter(this, priceables);
        ListView lvExpenses = findViewById(R.id.lvPurchases);
        lvExpenses.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, toolbar.getMenu());
        Utils.showMenuItems(toolbar.getMenu(), new int[]{R.id.view_history});
        return super.onCreateOptionsMenu(menu);
    }

    //endregion

    // region Toolbar Events

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                goToMenuActivity();

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region Event Handlers

    public void btnPurchase_onClick(View v)
    {
        String message = "";

        for (Purchase p : m_allPurchases)
        {
            if(message.equals(""))
                message += MoneyFormatter.formatLongToMoney(p.getItem().getPrice() * p.getQuantity()) + " purchase successful!";
            else
                message += "\n" + MoneyFormatter.formatLongToMoney(p.getItem().getPrice()) + " purchase successful!";
        }

        if(!message.equals(""))
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //endregion

    //region Helper Methods

    private Priceable[] GeneratePriceablesArray(ArrayList<Expense> expenses, ArrayList<ExpenseGroup> expenseGroups)
    {
        ArrayList<Priceable> priceables = new ArrayList<>(expenses.size() + expenseGroups.size());

        for (Expense e : expenses)
            priceables.add(e);
        for (ExpenseGroup e : expenseGroups)
            priceables.add(e);

        priceables.sort(new Comparator<Priceable>() {
            @Override
            public int compare(Priceable priceable, Priceable t1) {
                return priceable.getName().compareTo(t1.getName());
            }
        });

        Priceable[] priceablesArray = new Priceable[priceables.size()];

        int index = 0;
        for (Priceable p : priceables) {
            priceablesArray[index] = p;
            index++;
        }

        return priceablesArray;
    }

    private void goToMenuActivity()
    {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void addToPurchases(Purchase purchase)
    {
        if(m_allPurchases.contains(purchase))
        {
            Purchase realPurchase = m_allPurchases.get(m_allPurchases.indexOf(purchase));
            realPurchase.setQuantity(purchase.getQuantity());
        }
        else
            m_allPurchases.add(purchase);
    }

    public void removeFromPurchases(Purchase purchase)
    {
        if(purchase.getQuantity() < 1)
            m_allPurchases.remove(purchase);

        if(m_allPurchases.contains(purchase))
        {
            Purchase realPurchase = m_allPurchases.get(m_allPurchases.indexOf(purchase));
            realPurchase.setQuantity(purchase.getQuantity() - 1);
        }
    }

    public void updateTotalPrice(String newPrice)
    {
        final TextView tvCurrentTotal = findViewById(R.id.tvCurrentTotal);

        tvCurrentTotal.setText(newPrice);
    }

    public long getTotalPrice()
    {
        final TextView tvCurrentTotal = findViewById(R.id.tvCurrentTotal);
        return Long.parseLong(tvCurrentTotal.getText().toString().replace("$","").replace(",","").replace(".",""));
    }

    //endregion

}
