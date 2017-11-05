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

class ExpenseRowAdapter extends ArrayAdapter<Purchase>
{
    private static final int SELECTED_COLOR = Color.GREEN;
    private static final int NOT_SELECTED_COLOR = Color.WHITE;

    public ExpenseRowAdapter(@NonNull Context context, Purchase purchases[])
    {
        super(context, R.layout.expense_row, purchases);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(R.layout.expense_row, parent, false);

        //Get references to row data object
        final Purchase purchase = getItem(position);
        final Expense expense = (Expense)purchase.getItem();

        //Get references to row elements
        final TextView tvName = customView.findViewById(R.id.tvName);
        final TextView tvPrice = customView.findViewById(R.id.tvPrice);
        final TextView tvQuantity = customView.findViewById(R.id.tvQuantity);
        final Button btnIncrease = customView.findViewById(R.id.btnIncrease);
        final Button btnDecrease = customView.findViewById(R.id.btnDecrease);

        assert expense != null;

        //Set row elements based on purchase fields
        tvName.setText(expense.getName());
        tvPrice.setText(MoneyFormatter.formatLongToMoney(expense.getPrice()));
        tvQuantity.setText(String.valueOf(purchase.getQuantity()));
        checkQuantity(purchase.getQuantity(), customView);

        //Increase quantity event handler
        btnIncrease.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        purchase.setQuantity(purchase.getQuantity() + 1);

                        tvQuantity.setText(String.valueOf(purchase.getQuantity()));

                        checkQuantity(purchase.getQuantity(), customView);
                    }
                }
        );

        //Decrease quantity event handler
        btnDecrease.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        if(purchase.getQuantity() > 0)
                            purchase.setQuantity(purchase.getQuantity() - 1);

                        tvQuantity.setText(String.valueOf(purchase.getQuantity()));

                        checkQuantity(purchase.getQuantity(), customView);
                    }
                }
        );


        return customView;
    }

    //Change color of row based on quantity value
    private void checkQuantity(int quantity, View customView)
    {
        if(quantity > 0)
            customView.setBackgroundColor(SELECTED_COLOR);
        else
            customView.setBackgroundColor(NOT_SELECTED_COLOR);
    }
}
