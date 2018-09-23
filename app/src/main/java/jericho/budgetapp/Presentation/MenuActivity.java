package jericho.budgetapp.Presentation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import jericho.budgetapp.R;

/**
 * An activity that acts as a navigation menu.
 */
public class MenuActivity extends AppCompatActivity {

    //region onCreate()

    /**
     * Initializes widget references and links buttons up to their associated activities.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        String[] menuItems = new String[] {"Main Page", "Manage Expenses", "Manage Expense Groups", "Manage Plans", "View Statistics", "Settings"};
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuItems);
        ListView lvMenuItems = findViewById(R.id.lvMenuItems);
        lvMenuItems.setAdapter(listAdapter);

        lvMenuItems.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                String value = String.valueOf(parent.getItemAtPosition(position));
                switch (value)
                {
                    case "Main Page":
                        goToMainActivity();
                        break;

                    case "Manage Expenses":
                        goToManageExpensesActivity();
                        break;

                    case "Manage Expense Groups":
                        goToManageExpenseGroupsActivity();
                        break;

                    case "Manage Plans":
                        goToManagePlansActivity();
                        break;

                    default:
                        Toast.makeText(MenuActivity.this, value, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    //endregion

    //region Helper Methods

    /**
     * Navigates to the MainActivity.
     */
    private void goToMainActivity()
    {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Navigates to the ManageExpensesActivity.
     */
    private void goToManageExpensesActivity()
    {
        Intent intent = new Intent(MenuActivity.this, ManageExpensesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Navigates to the ManageExpenseGroupsActivity.
     */
    private void goToManageExpenseGroupsActivity()
    {
        Intent intent = new Intent(MenuActivity.this, ManageExpenseGroupsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Navigate to the ManagePlansActivity.
     */
    private void goToManagePlansActivity()
    {
        Intent intent = new Intent(MenuActivity.this, ManageBudgetPlansActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //endregion

}
