package jericho.budgetapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.budget_app.expenses.Expense;
import com.budget_app.expenses.ExpenseGroup;
import com.budget_app.expenses.ExpenseInGroup;
import com.budget_app.jt_linked_list.LinkedList;
import com.budget_app.jt_linked_list.Node;
import com.budget_app.jt_linked_list.SortedList;
import com.budget_app.utilities.MoneyFormatter;

import java.util.ArrayList;

import databases.DBHandler;
import utils.Utils;

public class EditExpenseGroupActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etName;
    private EditText etPrice;
    private EditText etCategory;
    private ExpenseGroup m_expenseGroup;
    private ListView lvExpenses;

    private boolean m_createNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense_group);

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etCategory = findViewById(R.id.etCategory);
        lvExpenses = findViewById(R.id.lvExpenses);

        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        m_expenseGroup = (ExpenseGroup) getIntent().getSerializableExtra("expenseGroup");
        m_createNew = getIntent().getExtras().getBoolean("createNew");

        if (!m_createNew)
        {
            etName.setText(m_expenseGroup.getName());
            etPrice.setText(MoneyFormatter.formatLongToMoney(m_expenseGroup.getPrice()).replace("$", ""));
            etCategory.setText(m_expenseGroup.getCategory());
        }

        LinkedList expensesToAdd;
        if ((expensesToAdd = (LinkedList) getIntent().getSerializableExtra("expensesToAdd")) != null)
        {
            LinkedList currExpenses = m_expenseGroup.getExpenses();
            Node curr = currExpenses.getHead();
            Node add = expensesToAdd.getHead();

            while (add != null)
            {
                Expense expenseToAdd = (Expense) add.getItem();
                boolean okayToAdd = true;

                while (curr != null)
                {
                    Expense existingExpense = (Expense) curr.getItem();
                    if (expenseToAdd.equals(existingExpense))
                        okayToAdd = false;

                    curr = curr.getNext();
                }

                if (okayToAdd)
                    m_expenseGroup.addExpense(expenseToAdd);

                add = add.getNext();
            }
        }

        updateExpensesListView();
    }

    //region Toolbar Events

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, toolbar.getMenu());

        Utils.showMenuItems(toolbar.getMenu(), new int[] {R.id.remove});

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.remove:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete this expense group?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //endregion

    //region Event Handlers

    public void btnConfirm_OnClick(View v)
    {
        try {
            String message = "Failed to save changes.";

            m_expenseGroup.setName(etName.getText().toString());
            m_expenseGroup.setPrice(Long.parseLong(etPrice.getText().toString().replace(",", "").replace(".","")));
            m_expenseGroup.setCategory(etCategory.getText().toString());

            if (m_createNew) {
                MainActivity.g_dbHandler.addExpenseGroup(m_expenseGroup);
                message = "Expense group added!";
            } else {
                if (MainActivity.g_dbHandler.queryExpenseGroups(DBHandler.EXPENSEGROUP_COL_ID + "=" + m_expenseGroup.getId()).getSize() == 1) {
                    MainActivity.g_dbHandler.updateExpenseGroup(m_expenseGroup);
                    message = "Expense group updated!";
                }
            }

            Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditExpenseGroupActivity.this, ManageExpenseGroupsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage() + ex.getStackTrace());
        }
    }

    public void btnAddExpenses_OnClick(View v)
    {
        try
        {
            Intent intent = new Intent(EditExpenseGroupActivity.this, SelectExpensesActivity.class);
            intent.putExtra("expenseGroup", m_expenseGroup);
            intent.putExtra("createNew", m_createNew);
            startActivity(intent);
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage() + ex.getStackTrace());
        }
    }

    //endregion

    //region Alert Dialog

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    MainActivity.g_dbHandler.removeExpenseGroup(m_expenseGroup.getId());
                    Toast.makeText(EditExpenseGroupActivity.this, "Expense group deleted!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    //endregion

    //region Helper Methods

    private SortedList getExpensesFromListView()
    {
        SortedList expenses = new SortedList();
        for (int i = 0; i < lvExpenses.getAdapter().getCount(); i++)
        {
            ExpenseInGroup e = (ExpenseInGroup) lvExpenses.getAdapter().getItem(i);
            for (int j = 0; j < e.getQuantity(); j++)
                expenses.insertSorted(e.getExpense());
        }
        return expenses;
    }

    public void updateExpensesListView()
    {
        LinkedList expenseList = m_expenseGroup.getExpenses();
        ArrayList<ExpenseInGroup> expenseWithQuantities = new ArrayList<>();
        Node curr = expenseList.getHead();

        while (curr != null)
        {
            Expense expense = (Expense) curr.getItem();

            if (ArrayListContainsExpense(expenseWithQuantities, expense))
            {
                ExpenseInGroup expenseInGroup = expenseWithQuantities.get(IndexOfExpenseInArrayList(expenseWithQuantities, expense));
                expenseInGroup.setQuantity(expenseInGroup.getQuantity() + 1);
            }
            else
                expenseWithQuantities.add(new ExpenseInGroup(expense, 1));

            curr = curr.getNext();
        }


        // build array of ExpenseInGroup objects
        ExpenseInGroup[] expenses = new ExpenseInGroup[expenseWithQuantities.size()];
        int i = 0;
        for (ExpenseInGroup e : expenseWithQuantities)
        {
            expenses[i] = e;
            i++;
        }

        ListAdapter listAdapter = new ExpenseWithQuantityRowAdapter(this, expenses);
        ListView lvExpenses = findViewById(R.id.lvExpenses);
        lvExpenses.setAdapter(listAdapter);

        etPrice.setEnabled(true);
        etPrice.setText(MoneyFormatter.formatLongToMoney(m_expenseGroup.getPrice()).replace("$", ""));
        etPrice.setEnabled(false);
    }

    private boolean ArrayListContainsExpense(ArrayList<ExpenseInGroup> list, Expense expense)
    {
        for (Expense e : list)
        {
            if (e.equals(expense)) return true;
        }
        return false;
    }

    private int IndexOfExpenseInArrayList(ArrayList<ExpenseInGroup> list, Expense expense)
    {
        int index = 0;

        for (Expense e : list)
        {
            if (e.equals(expense)) return index;
            index++;
        }

        return -1;
    }

    //endregion

    public void removeExpenseFromGroup(Expense expense)
    {
        m_expenseGroup.removeExpense(expense);
    }


}
