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

import com.budget_app.expenses.Purchase;
import com.budget_app.jt_interfaces.Priceable;
import com.budget_app.utilities.MoneyFormatter;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Jericho on 11/4/2017.
 */

class PurchaseHistoryRowAdapter extends ArrayAdapter<Purchase>
{
    private Context m_context;

    public PurchaseHistoryRowAdapter(@NonNull Context context, Purchase purchases[])
    {
        super(context, R.layout.purchase_history_row, purchases);
        this.m_context = context;
    }

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
        tvPrice.setText(MoneyFormatter.formatLongToMoney(item.getPrice()));
        tvQuantity.setText(String.valueOf(purchase.getQuantity()));
        tvDate.setText(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(purchase.getDate()));

        return customView;
    }
}
