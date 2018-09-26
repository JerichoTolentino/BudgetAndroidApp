package jericho.budgetapp;

import android.content.Context;

import jericho.budgetapp.Logic.ExpenseGroupService;
import jericho.budgetapp.Logic.ExpenseService;
import jericho.budgetapp.Logic.PurchaseService;
import jericho.budgetapp.Persistence.SQLiteExpense;
import jericho.budgetapp.Persistence.SQLiteExpenseEG;
import jericho.budgetapp.Persistence.SQLiteExpenseGroup;
import jericho.budgetapp.Persistence.SQLitePurchase;
import jericho.budgetapp.Presentation.MainActivity;

public final class Services
{

    private static ExpenseService m_expenseService;
    private static PurchaseService m_purchaseService;
    private static ExpenseGroupService m_expenseGroupService;

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

    /**
     * Gets an instance of an {@link ExpenseGroupService}.
     * @return An instance of an {@link ExpenseGroupService}.
     */
    public static ExpenseGroupService getExpenseGroupService()
    {
        if (m_expenseGroupService == null)
        {
            SQLiteExpenseGroup sqlExpenseGroup = new SQLiteExpenseGroup(
                    BudgetApp.getContext(),
                    BudgetApp.DB_NAME,
                    null,
                    0
            );

            SQLiteExpenseEG sqlExpenseEG = new SQLiteExpenseEG(
                    BudgetApp.getContext(),
                    BudgetApp.DB_NAME,
                    null,
                    0
            );

            SQLiteExpense sqlExpense = new SQLiteExpense(
                    BudgetApp.getContext(),
                    BudgetApp.DB_NAME,
                    null,
                    0
            );

            m_expenseGroupService = new ExpenseGroupService(
                    sqlExpenseGroup,
                    sqlExpenseEG,
                    sqlExpense
            );
        }

        return m_expenseGroupService;
    }

}
