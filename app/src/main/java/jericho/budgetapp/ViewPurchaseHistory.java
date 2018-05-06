package jericho.budgetapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import expenses.Purchase;

import java.util.ArrayList;

/**
 * An activity that displays the history of all previous Purchases.
 */
public class ViewPurchaseHistory extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView lvPurchases;

    /**
     * Initializes the activity with the history of previous Purchases.
     * @param savedInstanceState
     * @see AppCompatActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_purchase_history);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setTitle(R.string.purchase_history);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Purchase[] purchaseHistory = getPurchaseHistory();

        ListAdapter listAdapter = new PurchaseHistoryRowAdapter(this, purchaseHistory);
        lvPurchases = findViewById(R.id.lvPurchases);
        lvPurchases.setAdapter(listAdapter);
    }

    //region Helper Methods

    /**
     * Gets the history of all Purchases.
     * @return
     */
    private Purchase[] getPurchaseHistory()
    {
        Purchase[] purchases = null;
        try {
            ArrayList<Purchase> purchaseHistory = MainActivity.g_dbHandler.queryPurchases(null);
            purchaseHistory.sort(Purchase.getDescendendingDateComparator());
            purchases = new Purchase[purchaseHistory.size()];

            for (int i = 0; i < purchaseHistory.size(); i++)
                purchases[i] = purchaseHistory.get(i);

        } catch (Exception ex) {
            System.err.println(ex.toString());
        }

        return purchases;
    }

    //endregion

    //region Toolbar Events

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

}
