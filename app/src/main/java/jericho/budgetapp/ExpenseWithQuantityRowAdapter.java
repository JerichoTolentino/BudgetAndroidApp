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

import com.budget_app.expenses.ExpenseInGroup;
import com.budget_app.utilities.MoneyFormatter;

/**
 * Created by Jericho on 11/4/2017.
 */

class ExpenseWithQuantityRowAdapter extends ArrayAdapter<ExpenseInGroup>
{
    private Context m_context;

    public ExpenseWithQuantityRowAdapter(@NonNull Context context, ExpenseInGroup expenses[])
    {
        super(context, R.layout.expense_row_for_expense_group, expenses);
        this.m_context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(R.layout.expense_row_for_expense_group, parent, false);

        //Get references to row data object
        final ExpenseInGroup expense = getItem(position);

        //Get references to row elements
        final TextView tvName = customView.findViewById(R.id.tvName);
        final TextView tvPrice = customView.findViewById(R.id.tvPrice);
        final ImageButton btnClear = customView.findViewById(R.id.btnEdit);
        final TextView tvQuantity = customView.findViewById(R.id.tvQuantity);


        if (expense != null)
        {
            //Set row elements based on expense fields
            tvName.setText(expense.getName());
            tvPrice.setText(MoneyFormatter.formatLongToMoney(expense.getPrice(), true));
            tvQuantity.setText("x" + String.valueOf(expense.getQuantity()));

            btnClear.setOnClickListener(
                    new View.OnClickListener()
                    {
                        public void onClick(View v)
                        {
                            ((EditExpenseGroupActivity)getContext()).removeExpenseFromGroup(expense);
                            ((EditExpenseGroupActivity)getContext()).updateExpensesListView();
                        }
                    }
            );
        }

        return customView;
    }

}
