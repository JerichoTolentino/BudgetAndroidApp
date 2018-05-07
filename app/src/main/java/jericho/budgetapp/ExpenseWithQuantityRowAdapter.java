package jericho.budgetapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import expenses.Expense;
import utilities.KeyValuePair;
import utilities.MoneyFormatter;

/**
 * A custom ListView row adapter to display the contents of ExpenseInGroup objects.
 */
class ExpenseWithQuantityRowAdapter extends ArrayAdapter<KeyValuePair<Expense, Integer>>
{
    private Context m_context;

    /**
     * Initializes a new instance of an ExpenseWithQuantityRowAdapter with the specified ExpenseInGroup objects.
     * @param context
     * @param expenses The Expenses to display in the ListView.
     * @see ArrayAdapter#ArrayAdapter(Context, int, Object[])
     */
    public ExpenseWithQuantityRowAdapter(@NonNull Context context, List<KeyValuePair<Expense, Integer>> expenses)
    {
        super(context, R.layout.expense_row_for_expense_group, expenses);
        this.m_context = context;
    }

    /**
     * Returns a custom view that displays the contents of an ExpenseInGroup object in a ListView row.
     * @param position
     * @param convertView
     * @param parent
     * @return A custom view that displays the contents of an ExpenseInGroup object in a ListView row.
     * @see ArrayAdapter#getView(int, View, ViewGroup)
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(R.layout.expense_row_for_expense_group, parent, false);

        //Get references to row data object
        final KeyValuePair<Expense, Integer> kvp = getItem(position);

        //Get references to row elements
        final TextView tvName = customView.findViewById(R.id.tvName);
        final TextView tvPrice = customView.findViewById(R.id.tvPrice);
        final ImageButton btnClear = customView.findViewById(R.id.btnEdit);
        final TextView tvQuantity = customView.findViewById(R.id.tvQuantity);


        if (kvp != null)
        {
            //Set row elements based on expense fields
            tvName.setText(kvp.getKey().getName());
            tvPrice.setText(MoneyFormatter.formatLongToMoney(kvp.getKey().getPrice(), true));
            tvQuantity.setText(String.format(Locale.CANADA, "x%d", kvp.getValue()));

            btnClear.setOnClickListener(
                    new View.OnClickListener()
                    {
                        public void onClick(View v)
                        {
                            ((EditExpenseGroupActivity)getContext()).removeExpenseFromGroup(kvp.getKey());
                            ((EditExpenseGroupActivity)getContext()).updateExpensesListView();
                        }
                    }
            );
        }

        return customView;
    }

}
