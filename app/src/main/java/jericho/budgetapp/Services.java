package jericho.budgetapp;

import android.content.Context;

import jericho.budgetapp.Logic.ExpenseService;
import jericho.budgetapp.Logic.PurchaseService;
import jericho.budgetapp.Persistence.SQLiteExpense;
import jericho.budgetapp.Persistence.SQLitePurchase;
import jericho.budgetapp.Presentation.MainActivity;

public final class Services
{

    private static ExpenseService m_expenseService;
    private static PurchaseService m_purchaseService;

    /**
     * Gets an instance of an {@link ExpenseService}.
     * @return An instance of an {@link ExpenseService}.
     */
    public static ExpenseService getExpenseService()
    {
        if (m_expenseService == null)
        {
            m_expenseService = new ExpenseService(
                    new SQLiteExpense(
                            BudgetApp.getContext(),
                            BudgetApp.DB_NAME,
                            null,
                            0));
        }

        return m_expenseService;
    }

    /**
     * Gets an instance of a {@link PurchaseService}.
     * @return An instance of a {@link PurchaseService}.
     */
    public static PurchaseService getPurchaseService()
    {
        if (m_purchaseService == null)
        {
            m_purchaseService = new PurchaseService(
                    new SQLitePurchase(
                            BudgetApp.getContext(),
                            BudgetApp.DB_NAME,
                            null,
                            0));
        }

        return m_purchaseService;
    }

}
