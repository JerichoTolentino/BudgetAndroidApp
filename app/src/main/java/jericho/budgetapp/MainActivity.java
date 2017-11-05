package jericho.budgetapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.budget_app.expenses.*;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Purchase purchases[] = new Purchase[10];

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

        ListAdapter listAdapter = new ExpenseRowAdapter(this, purchases);
        ListView lvExpenses = (ListView) findViewById(R.id.lvExpenses);
        lvExpenses.setAdapter(listAdapter);


    }
}
