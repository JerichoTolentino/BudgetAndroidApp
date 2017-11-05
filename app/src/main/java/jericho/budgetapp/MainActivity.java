package jericho.budgetapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.budget_app.expenses.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Expense expenses[] = new Expense[10];

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

        expenses[0] = mcdonalds;
        expenses[1] = burgerKing;
        expenses[2] = wendys;
        expenses[3] = thanhVi;
        expenses[4] = gangnamSushi;
        expenses[5] = alcohol;
        expenses[6] = water;
        expenses[7] = montreal;
        expenses[8] = tuition;
        expenses[9] = fruit;

        ListAdapter listAdapter = new ExpenseRowAdapter(this, expenses);
        ListView lvExpenses = (ListView) findViewById(R.id.lvExpenses);
        lvExpenses.setAdapter(listAdapter);


    }
}
