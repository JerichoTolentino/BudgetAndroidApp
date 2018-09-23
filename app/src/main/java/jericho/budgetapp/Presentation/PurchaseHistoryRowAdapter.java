package jericho.budgetapp.Presentation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import jericho.budgetapp.Model.Purchase;
import jericho.budgetapp.Model.Priceable;
import jericho.budgetapp.R;
import utilities.MoneyFormatter;

import java.text.DateFormat;

/**
 * A custom adapter that displays a history of Purchases in a ListView.
 */
class PurchaseHistoryRowAdapter extends ArrayAdapter<Purchase>
{
    private Context m_context;

    /**
     * Initializes a new instance of a PurchaseHistoryRowAdapter.
     * @param context
     * @param purchases The Purchases to display.
     * @see ArrayAdapter#ArrayAdapter(Context, int, Object[])
     */
    public PurchaseHistoryRowAdapter(@NonNull Context context, Purchase purchases[])
    {
        super(context, R.layout.purchase_history_row, purchases);
        this.m_context = context;
    }

    /**
     * Returns a custom view that displays the history of a Purchase as a ListView row.
     * @param position
     * @param convertView
     * @param parent
     * @return A custom view that displays the history of a Purchase as a ListView row.
     * @see ArrayAdapter#getView(int, View, ViewGroup)
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(R.layout.purchase_history_row, parent, false);

        //Get references to row data object
        final Purchase purchase = getItem(position);
        final Priceable item = purchase.getItem();

        //Get references to row elements
        final TextView tvName = customView.findViewById(R.id.tvName);
        final TextView tvPrice = customView.findViewById(R.id.tvPrice);
        final TextView tvQuantity = customView.findViewById(R.id.tvQuantity);
        final TextView tvDate = customView.findViewById(R.id.tvDate);

        assert item != null;

        //Set row elements based on purchase fields
        tvName.setText(item.getName());
        tvPrice.setText(MoneyFormatter.formatLongToMoney(item.getPrice(), true));
        tvQuantity.setText(String.valueOf(purchase.getQuantity()));
        tvDate.setText(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(purchase.getDate()));

        return customView;
    }
}
