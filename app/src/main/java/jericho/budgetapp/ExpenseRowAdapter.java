package jericho.budgetapp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.budget_app.expenses.*;
import com.budget_app.utilities.*;

/**
 * Created by Jericho on 11/4/2017.
 */

class ExpenseRowAdapter extends ArrayAdapter<Expense>
{
    private static final int SELECTED_COLOR = Color.GREEN;
    private static final int NOT_SELECTED_COLOR = Color.WHITE;

    public ExpenseRowAdapter(@NonNull Context context, Expense expenses[])
    {
        super(context, R.layout.expense_row, expenses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(R.layout.expense_row, parent, false);

        Expense expense = getItem(position);
        final TextView tvName = customView.findViewById(R.id.tvName);
        final TextView tvPrice = customView.findViewById(R.id.tvPrice);
        final TextView tvQuantity = customView.findViewById(R.id.tvQuantity);
        final Button btnIncrease = customView.findViewById(R.id.btnIncrease);
        final Button btnDecrease = customView.findViewById(R.id.btnDecrease);

        assert expense != null;

        tvName.setText(expense.getName());
        tvPrice.setText(MoneyFormatter.formatLongToMoney(expense.getPrice()));
        tvQuantity.setText("0");

        btnIncrease.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        int quantity = Integer.parseInt(tvQuantity.getText().toString());

                        quantity += 1;

                        tvQuantity.setText(String.valueOf(quantity));

                        if(quantity > 0)
                            customView.setBackgroundColor(SELECTED_COLOR);
                    }
                }
        );

        btnDecrease.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        int quantity = Integer.parseInt(tvQuantity.getText().toString());

                        if(quantity > 0)
                            quantity -= 1;

                        tvQuantity.setText(String.valueOf(quantity));

                        if(quantity == 0)
                            customView.setBackgroundColor(NOT_SELECTED_COLOR);
                    }
                }
        );


        return customView;
    }
}
