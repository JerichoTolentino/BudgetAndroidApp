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

public class MenuActivity extends AppCompatActivity {

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
                Intent intent = null;


                switch (value)
                {
                    case "Main Page":
                        intent = new Intent(MenuActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    case "Manage Expenses":
                        intent = new Intent(MenuActivity.this, ManageExpensesActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(MenuActivity.this, value, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }
}
