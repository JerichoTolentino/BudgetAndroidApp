package jericho.budgetapp.Presentation;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import jericho.budgetapp.Model.Purchase;
import jericho.budgetapp.Model.Priceable;
import jericho.budgetapp.R;
import utilities.MoneyFormatter;

/**
 * A custom ListView row adapter that displays Purchases.
 */
class PurchaseRowAdapter extends ArrayAdapter<Purchase>
{
    private static final int NOT_SELECTED_COLOR = Color.WHITE;
    private Context m_context;

    /**
     * Initializes a new instance of a PurchaseRowAdapter with the specified Purchases.
     * @param context
     * @param purchases The Purchases to display.
     * @see ArrayAdapter#ArrayAdapter(Context, int, Object[])
     */
    public PurchaseRowAdapter(@NonNull Context context, Purchase purchases[])
    {
        super(context, R.layout.purchase_row, purchases);
        this.m_context = context;
    }

    /**
     * Returns a custom view that displays the contents of a Purchase in a ListView row.
     * @param position
     * @param convertView
     * @param parent
     * @return A custom view that displays the contents of a Purchase in a ListView row.
     * @see ArrayAdapter#getView(int, View, ViewGroup)
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(R.layout.purchase_row, parent, false);

        //Get references to row data object
        final Purchase purchase = getItem(position);
        final Priceable item = purchase.getItem();

        //Get references to row elements
        final TextView tvName = customView.findViewById(R.id.tvName);
        final TextView tvPrice = customView.findViewById(R.id.tvPrice);
        final TextView tvQuantity = customView.findViewById(R.id.tvQuantity);
        final Button btnIncrease = customView.findViewById(R.id.btnIncrease);
        final Button btnDecrease = customView.findViewById(R.id.btnDecrease);

        assert item != null;

        //Set row elements based on purchase fields
        tvName.setText(item.getName());
        tvPrice.setText(MoneyFormatter.formatLongToMoney(item.getPrice(), true));
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

                        if (getContext() instanceof MainActivity)
                            ((MainActivity)getContext()).addToCurrentTotal(purchase.getItem());
                    }
                }
        );

        //Decrease quantity event handler
        btnDecrease.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        if(purchase.getQuantity() > 0) {
                            purchase.setQuantity(purchase.getQuantity() - 1);

                            if (getContext() instanceof MainActivity)
                                ((MainActivity)getContext()).removeFromCurrentTotal(purchase.getItem());
                        }
                        tvQuantity.setText(String.valueOf(purchase.getQuantity()));
                        checkQuantity(purchase.getQuantity(), customView);
                    }
                }
        );

        return customView;
    }

    /**
     * Changes color of row based on quantity value.
     * @param quantity The quantity of the item being purchased.
     * @param customView The view hosting the purchase.
     */
    private void checkQuantity(int quantity, View customView)
    {
        if(quantity > 0)
            customView.setBackgroundColor(ContextCompat.getColor(m_context, R.color.colorSelectedItem));
        else
            customView.setBackgroundColor(NOT_SELECTED_COLOR);
    }
}
