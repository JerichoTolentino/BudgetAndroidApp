package jericho.budgetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.budget_app.expenses.Expense;
import com.budget_app.jt_linked_list.Node;
import com.budget_app.jt_linked_list.SortedList;
import com.budget_app.master.BudgetAppManager;

import utils.Utils;

public class ManageExpensesActivity extends AppCompatActivity {

    private ActionMenuView amvMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_expenses);

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        amvMenu = toolbar.findViewById(R.id.amvMenu);
        amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        setSupportActionBar(toolbar);

        SortedList expenses = BudgetAppManager.getExpenses();
        Expense[] expenseArray = new Expense[expenses.getSize()];
        Node curr = expenses.getHead();

        int index = 0;
        while(curr != null)
        {
            expenseArray[index] = (Expense) curr.getItem();
            index++;
            curr = curr.getNext();
        }

        ExpenseRowAdapter adapter = new ExpenseRowAdapter(ManageExpensesActivity.this, expenseArray);
        ListView lvExpenses = findViewById(R.id.lvExpenses);
        lvExpenses.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, amvMenu.getMenu());
        Utils.showMenuItems(amvMenu.getMenu(), new int[] {R.id.add_new, R.id.open_side_menu, R.id.manage_expenses_title});
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.open_side_menu:
                Intent intent = new Intent(ManageExpensesActivity.this, MenuActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.push_left_in,R.anim.push_up_out); // eventually create animation files to change the activity transition
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
