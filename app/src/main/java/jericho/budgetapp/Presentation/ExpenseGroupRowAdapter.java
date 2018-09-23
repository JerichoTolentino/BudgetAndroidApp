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

import jericho.budgetapp.Model.ExpenseGroup;
import jericho.budgetapp.R;
import utilities.MoneyFormatter;
import utilities.Utility;

/**
 * A custom ListView row adapter to display the contents of ExpenseGroups.
 */
class ExpenseGroupRowAdapter extends ArrayAdapter<ExpenseGroup>
{
    private Context m_context;

    /**
     * Initializes a new instance of an ExpenseGroupRowAdapter with the specified ExpenseGroups
     * @param context
     * @param expenseGroups The ExpenseGroups to display in the ListView.
     * @see ArrayAdapter#ArrayAdapter(Context, int, Object[])
     */
    public ExpenseGroupRowAdapter(@NonNull Context context, ExpenseGroup expenseGroups[])
    {
        super(context, R.layout.expense_group_row, expenseGroups);
        this.m_context = context;
    }

    /**
     * Returns a custom view that displays the contents of an ExpenseGroup.
     * @param position
     * @param convertView
     * @param parent
     * @return A custom view that displays the contents of an ExpenseGroup.
     * @see ArrayAdapter#getView(int, View, ViewGroup)
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            final View customView = layoutInflater.inflate(R.layout.expense_group_row, parent, false);

        //Get references to row data object
        final ExpenseGroup expenseGroup = getItem(position);

        //Get references to row elements
        final TextView tvName = customView.findViewById(R.id.tvName);
        final TextView tvPrice = customView.findViewById(R.id.tvPrice);
        final ImageButton btnEdit = customView.findViewById(R.id.btnEdit);
        final TextView tvNumItems = customView.findViewById(R.id.tvQuantity);

        if (expenseGroup != null)
        {
            //Set row elements based on expense fields
            tvName.setText(expenseGroup.getName());
            tvPrice.setText(MoneyFormatter.formatLongToMoney(expenseGroup.getPrice(), true));
            tvNumItems.setText(String.valueOf(Utility.count(expenseGroup.getExpenses())));

            btnEdit.setOnClickListener(
                    new View.OnClickListener()
                    {
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(getContext(), EditExpenseGroupActivity.class);
                            intent.putExtra("expenseGroup", expenseGroup);
                            intent.putExtra("createNew", false);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            getContext().startActivity(intent);
                        }
                    }
            );
        }

        return customView;
    }

}
