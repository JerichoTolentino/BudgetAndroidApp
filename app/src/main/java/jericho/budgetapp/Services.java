package jericho.budgetapp;

import android.content.Context;

import jericho.budgetapp.Logic.ExpenseService;
import jericho.budgetapp.Persistence.SQLiteExpense;
import jericho.budgetapp.Presentation.MainActivity;

public final class Services
{

    private static ExpenseService m_expenseService;

    public static ExpenseService getExpenseService()
    {
        if (m_expenseService == null)
        {
            m_expenseService = new ExpenseService(
                    new SQLiteExpense(
                            BudgetApp.getContext(),
                            "BudgetApp.db",
                            null,
                            0));
        }

        return m_expenseService;
    }

}
