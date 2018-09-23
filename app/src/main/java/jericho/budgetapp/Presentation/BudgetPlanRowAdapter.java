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

import jericho.budgetapp.R;
import jericho.budgetapp.Model.Plan;
import utilities.MoneyFormatter;

/**
 * A custom ListView row adapter to display the contents of Plans.
 */
class BudgetPlanRowAdapter extends ArrayAdapter<Plan>
{
    // region Members

    private Context m_context;

    //endregion

    //region Constructor

    /**
     * Initializes a new instance of a BudgetPlanRowAdapter.
     * @param context The context.
     * @param plans The array of plans to be displayed in ListView rows.
     * @see ArrayAdapter#ArrayAdapter(Context, int, Object[])
     */
    public BudgetPlanRowAdapter(@NonNull Context context, Plan plans[])
    {
        super(context, R.layout.budget_plan_row, plans);
        this.m_context = context;
    }

    //endregion

    //region Public Methods

    /**
     * Returns a custom view that displays the contents of a Plan in a ListView row.
     * @param position
     * @param convertView
     * @param parent
     * @return A custom view that displays the contents of a Plan in a ListView row.
     * @see ArrayAdapter#getView(int, View, ViewGroup)
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(R.layout.budget_plan_row, parent, false);

        //Get references to row data object
        final Plan plan = getItem(position);

        //Get references to row elements
        final TextView tvName = customView.findViewById(R.id.tvName);
        final TextView tvPrice = customView.findViewById(R.id.tvPrice);
        final ImageButton btnEdit = customView.findViewById(R.id.btnEdit);

        if (plan != null)
        {
            //Set row elements based on expense fields
            tvName.setText(plan.getName());
            tvPrice.setText(MoneyFormatter.formatLongToMoney(plan.getWeeklyBudget().getTotalBudget(), true) + "/week");

            btnEdit.setOnClickListener(
                    new View.OnClickListener()
                    {
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(getContext(), EditBudgetPlanActivity.class);
                            intent.putExtra("plan", plan);
                            intent.putExtra("createNew", false);
                            getContext().startActivity(intent);
                        }
                    }
            );
        }

        return customView;
    }

    //endregion

}
