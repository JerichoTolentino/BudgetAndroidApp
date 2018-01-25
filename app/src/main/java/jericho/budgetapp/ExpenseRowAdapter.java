package jericho.budgetapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.budget_app.expenses.Expense;
import com.budget_app.expenses.Purchase;
import com.budget_app.utilities.MoneyFormatter;

/**
 * Created by Jericho on 11/4/2017.
 */

class ExpenseRowAdapter extends ArrayAdapter<Expense>
{
    private static final int NOT_SELECTED_COLOR = Color.WHITE;
    private Context m_context;

    public ExpenseRowAdapter(@NonNull Context context, Expense expenses[])
    {
        super(context, R.layout.expense_row, expenses);
        this.m_context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(R.layout.expense_row, parent, false);

        //Get references to row data object
        final Expense expense = getItem(position);

        //Get references to row elements
        final TextView tvName = customView.findViewById(R.id.tvName);
        final TextView tvPrice = customView.findViewById(R.id.tvPrice);
        final TextView tvDescription = customView.findViewById(R.id.tvDescription);
        final ImageButton btnEdit = customView.findViewById(R.id.btnEdit);

        assert expense != null;

        //Set row elements based on expense fields
        tvName.setText(expense.getName());
        tvPrice.setText(MoneyFormatter.formatLongToMoney(expense.getPrice()));
        tvDescription.setText(expense.getDescription());

        btnEdit.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getContext(), EditExpenseActivity.class);
                        intent.putExtra("ExpenseID", expense.getId());
                        intent.putExtra("ExpenseName", expense.getName());
                        intent.putExtra("ExpensePrice", String.valueOf(expense.getPrice()));
                        intent.putExtra("ExpenseCategory", expense.getCategory());
                        intent.putExtra("ExpenseDescription", expense.getDescription());
                        getContext().startActivity(intent);
                    }
                }
        );

        return customView;
    }

}
