package jericho.budgetapp.Presentation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import jericho.budgetapp.Model.Expense;
import jericho.budgetapp.R;
import utilities.MoneyFormatter;

/**
 * A custom ListView row adapter to display the contents of Expenses.
 */
class ExpenseRowAdapter extends ArrayAdapter<Expense>
{
    private Context m_context;

    /**
     * Initializes a new instance of an ExpenseRowAdapter with the specified Expenses.
     * @param context
     * @param expenses The Expenses to display in the ListView.
     * @see ArrayAdapter#ArrayAdapter(Context, int, Object[])
     */
    public ExpenseRowAdapter(@NonNull Context context, Expense expenses[])
    {
        super(context, R.layout.budget_plan_row, expenses);
        this.m_context = context;
    }

    /**
     * Returns a custom view that displays the contents of an Expense in a ListView row.
     * @param position
     * @param convertView
     * @param parent
     * @return A custom view that displays the contents of an Expense in a ListView row.
     * @see ArrayAdapter#getView(int, View, ViewGroup)
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(R.layout.budget_plan_row, parent, false);

        //Get references to row data object
        final Expense expense = getItem(position);

        //Get references to row elements
        final TextView tvName = customView.findViewById(R.id.tvName);
        final TextView tvPrice = customView.findViewById(R.id.tvPrice);
        final ImageButton btnEdit = customView.findViewById(R.id.btnEdit);

        if (expense != null)
        {
            //Set row elements based on expense fields
            tvName.setText(expense.getName());
            tvPrice.setText(MoneyFormatter.formatLongToMoney(expense.getPrice(), true));

            btnEdit.setOnClickListener(
                    new View.OnClickListener()
                    {
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(getContext(), EditExpenseActivity.class);
                            intent.putExtra("expense", expense);
                            intent.putExtra("createNew", false);
                            getContext().startActivity(intent);
                        }
                    }
            );
        }

        return customView;
    }

}
