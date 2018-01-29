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
import com.budget_app.expenses.Purchase;
import com.budget_app.jt_interfaces.Priceable;
import com.budget_app.jt_linked_list.Node;
import com.budget_app.jt_linked_list.SortedList;
import com.budget_app.master.BudgetAppManager;
import com.budget_app.utilities.MoneyFormatter;

import databases.DBHandler;
import utils.Utils;

public class MainActivity extends AppCompatActivity {

    public static DBHandler g_dbHandler;

    private Toolbar toolbar;
    private SortedList m_allPurchases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BudgetAppManager.init();
        g_dbHandler = new DBHandler(getApplicationContext(), null, null, 0);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_view_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        setSupportActionBar(toolbar);

        m_allPurchases = new SortedList();

        SortedList expenses = new SortedList(g_dbHandler.queryExpenses(null));
        Priceable items[] = new Priceable[expenses.getSize()];

        Node curr = expenses.getHead();
        int index = 0;
        while (curr != null)
        {
            items[index] = (Priceable) curr.getItem();
            index++;
            curr = curr.getNext();
        }

        ListAdapter listAdapter = new PriceableRowAdapter(this, items);
        ListView lvExpenses = findViewById(R.id.lvPurchases);
        lvExpenses.setAdapter(listAdapter);
    }

    // region Toolbar Events

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, toolbar.getMenu());
        Utils.showMenuItems(toolbar.getMenu(), new int[]{R.id.view_history});
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId())
        {
            // this is navigation icon listener
            case android.R.id.home:
                intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region Event Handlers

    public void btnPurchase_onClick(View v)
    {
        Node curr = m_allPurchases.getHead();
        String message = "";

        while(curr != null)
        {
            if(curr.getItem() != null && curr.getItem() instanceof Purchase)
            {
                Purchase currPurchase = (Purchase)curr.getItem();

                if(message.equals(""))
                    message += MoneyFormatter.formatLongToMoney(currPurchase.getItem().getPrice() * currPurchase.getQuantity()) + " purchase successful!";
                else
                    message += "\n" + MoneyFormatter.formatLongToMoney(currPurchase.getItem().getPrice()) + " purchase successful!";
            }
            curr = curr.getNext();
        }

        if(!message.equals(""))
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //endregion

    //region Helper Methods

    public void addToPurchases(Purchase purchase)
    {
        Node purchaseNode = m_allPurchases.findNode(purchase);

        if(purchaseNode != null && purchaseNode.getItem() instanceof Purchase)
        {
            Purchase realPurchase = (Purchase) purchaseNode.getItem();
            realPurchase.setQuantity(purchase.getQuantity());
        }
        else
            m_allPurchases.insertSorted(purchase);
    }

    public void removeFromPurchases(Purchase purchase)
    {
        if(purchase.getQuantity() < 1)
            m_allPurchases.removeNode(purchase);

        Node purchaseNode = m_allPurchases.findNode(purchase);

        if(purchaseNode != null && purchaseNode.getItem() instanceof Purchase)
        {
            Purchase realPurchase = (Purchase) purchaseNode.getItem();
            realPurchase.setQuantity(purchase.getQuantity());
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
