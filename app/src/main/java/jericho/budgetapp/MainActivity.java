package jericho.budgetapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
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
import com.budget_app.jt_linked_list.Node;
import com.budget_app.jt_linked_list.SortedList;
import com.budget_app.master.BudgetAppManager;
import com.budget_app.utilities.MoneyFormatter;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private SortedList m_allPurchases;
    private ActionMenuView amvMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BudgetAppManager.init();

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        amvMenu = toolbar.findViewById(R.id.amvMenu);
        amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        setSupportActionBar(toolbar);

        m_allPurchases = new SortedList();

        Purchase purchases[] = new Purchase[10];

        Toast.makeText(this, "Welcome to BudgetApp", Toast.LENGTH_SHORT).show();

        Expense mcdonalds = new Expense("Mcdonalds", 1000, "Food", "Yummy Mcdonalds");
        Expense burgerKing = new Expense("Burger King", 1100, "Food", "Delicious Burger King");
        Expense wendys = new Expense("Wendys", 1200, "Food", "Absolutely delightful Wendys");
        Expense thanhVi = new Expense("Thanh Vi", 854, "Food", "Yum");
        Expense gangnamSushi = new Expense("Gangnam Sushi", 1699, "Food", "Yummy");
        Expense alcohol = new Expense("Alfred Lamb's", 2079, "Drinks", "Slurp");
        Expense water = new Expense("Water", 199, "Drinks", "Plain");
        Expense montreal = new Expense("Montreal Trip", 104523, "Travel", "Fun!");
        Expense tuition = new Expense("Tuition", 2353003, "Education", "Expensive");
        Expense fruit = new Expense("Apple", 54, "Food", "Nice");

        BudgetAppManager.addNewExpense(mcdonalds.getName(), mcdonalds.getPrice(), mcdonalds.getCategory(), mcdonalds.getDescription());
        BudgetAppManager.addNewExpense(burgerKing.getName(), burgerKing.getPrice(), burgerKing.getCategory(), burgerKing.getDescription());
        BudgetAppManager.addNewExpense(wendys.getName(), wendys.getPrice(), wendys.getCategory(), wendys.getDescription());
        BudgetAppManager.addNewExpense(thanhVi.getName(), thanhVi.getPrice(), thanhVi.getCategory(), thanhVi.getDescription());
        BudgetAppManager.addNewExpense(gangnamSushi.getName(), gangnamSushi.getPrice(), gangnamSushi.getCategory(), gangnamSushi.getDescription());
        BudgetAppManager.addNewExpense(alcohol.getName(), alcohol.getPrice(), alcohol.getCategory(), alcohol.getDescription());
        BudgetAppManager.addNewExpense(water.getName(), water.getPrice(), water.getCategory(), water.getDescription());
        BudgetAppManager.addNewExpense(montreal.getName(), montreal.getPrice(), montreal.getCategory(), montreal.getDescription());
        BudgetAppManager.addNewExpense(tuition.getName(), tuition.getPrice(), tuition.getCategory(), tuition.getDescription());
        BudgetAppManager.addNewExpense(fruit.getName(), fruit.getPrice(), fruit.getCategory(), fruit.getDescription());

        purchases[0] = new Purchase(mcdonalds, 0, new Date());
        purchases[1] = new Purchase(burgerKing, 0, new Date());
        purchases[2] = new Purchase(wendys, 0, new Date());
        purchases[3] = new Purchase(thanhVi, 0, new Date());
        purchases[4] = new Purchase(gangnamSushi, 0, new Date());
        purchases[5] = new Purchase(alcohol, 0, new Date());
        purchases[6] = new Purchase(water, 0, new Date());
        purchases[7] = new Purchase(montreal, 0, new Date());
        purchases[8] = new Purchase(tuition, 0, new Date());
        purchases[9] = new Purchase(fruit, 0, new Date());

        ListAdapter listAdapter = new PurchaseRowAdapter(this, purchases);
        ListView lvExpenses = (ListView) findViewById(R.id.lvPurchases);
        lvExpenses.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, amvMenu.getMenu());
        amvMenu.getMenu().findItem(R.id.add_new).setVisible(false);
        amvMenu.getMenu().findItem(R.id.manage_expenses_title).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.open_side_menu:
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.push_left_in,R.anim.push_up_out); // eventually create animation files to change the activity transition
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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
                BudgetAppManager.makePurchase(currPurchase.getItem(), currPurchase.getQuantity());

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
        final TextView tvCurrentTotal = (TextView) findViewById(R.id.tvCurrentTotal);

        tvCurrentTotal.setText(newPrice);
    }

    public long getTotalPrice()
    {
        final TextView tvCurrentTotal = (TextView) findViewById(R.id.tvCurrentTotal);
        return Long.parseLong(tvCurrentTotal.getText().toString().replace("$","").replace(",","").replace(".",""));
    }

    //endregion

}
