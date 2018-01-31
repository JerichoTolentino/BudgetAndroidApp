package jericho.budgetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.budget_app.expenses.Expense;

import java.util.HashSet;

public class MenuActivity extends AppCompatActivity {

    //region onCreate()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        String[] menuItems = new String[] {"Main Page", "Manage Expenses", "Manage Budget/Savings", "TBA", "View Statistics", "Settings"};
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
                    default:
                        Toast.makeText(MenuActivity.this, value, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    //endregion

    //region Helper Methods

    private void goToMainActivity()
    {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToManageExpensesActivity()
    {
        Intent intent = new Intent(MenuActivity.this, ManageExpensesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //endregion

}
