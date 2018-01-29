package jericho.budgetapp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.budget_app.expenses.Expense;
import com.budget_app.expenses.Purchase;
import com.budget_app.jt_interfaces.Priceable;
import com.budget_app.utilities.MoneyFormatter;

/**
 * Created by Jericho on 11/4/2017.
 */

class PriceableRowAdapter extends ArrayAdapter<Priceable>
{
    private static final int NOT_SELECTED_COLOR = Color.WHITE;
    private Context m_context;

    public PriceableRowAdapter(@NonNull Context context, Priceable items[])
    {
        super(context, R.layout.priceable_row, items);
        this.m_context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(R.layout.priceable_row, parent, false);

        //Get references to row data object
        final Priceable item = getItem(position);

        //Get references to row elements
        final TextView tvName = customView.findViewById(R.id.tvName);
        final TextView tvPrice = customView.findViewById(R.id.tvPrice);
        final TextView tvQuantity = customView.findViewById(R.id.tvQuantity);
        final Button btnIncrease = customView.findViewById(R.id.btnIncrease);
        final Button btnDecrease = customView.findViewById(R.id.btnDecrease);

        //Get reference to total
        final TextView tvTotal = parent.findViewById(R.id.tvCurrentTotal);

        assert item != null;

        //Set row elements based on purchase fields
        tvName.setText(item.getName());
        tvPrice.setText(MoneyFormatter.formatLongToMoney(item.getPrice()));
        checkQuantity(Integer.parseInt(tvQuantity.getText().toString()), customView);

        //Increase quantity event handler
        btnIncrease.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        int quantity = Integer.parseInt(tvQuantity.getText().toString());
                        tvQuantity.setText(String.valueOf(quantity + 1));

                        quantity = Integer.parseInt(tvQuantity.getText().toString());
                        checkQuantity(quantity, customView);
                    }
                }
        );

        //Decrease quantity event handler
        btnDecrease.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        int quantity = Integer.parseInt(tvQuantity.getText().toString());

                        if (quantity > 0)
                        {
                            tvQuantity.setText(String.valueOf(Integer.parseInt(tvQuantity.getText().toString()) - 1));

                            quantity = Integer.parseInt(tvQuantity.getText().toString());
                            checkQuantity(quantity, customView);
                        }

                    }
                }
        );

        return customView;
    }

    //Change color of row based on quantity value
    private void checkQuantity(int quantity, View customView)
    {
        if(quantity > 0)
            customView.setBackgroundColor(m_context.getResources().getColor(R.color.colorSelectedItem));
        else
            customView.setBackgroundColor(NOT_SELECTED_COLOR);
    }
}
