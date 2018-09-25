package jericho.budgetapp.Logic;

import jericho.budgetapp.Model.Expense;
import jericho.budgetapp.Persistence.Interfaces.ExpensePersistence;

public class ExpenseService
{

    private ExpensePersistence m_expensePersistence;

    /**
     * Initializes a new instance of an {@link ExpenseService}.
     * @param expensePersistence The {@link ExpensePersistence} to use for making calls to persistence.
     */
    public ExpenseService(ExpensePersistence expensePersistence)
    {
        m_expensePersistence = expensePersistence;
    }

    /**
     * Gets the {@link Expense} with the specified ID.
     * @param id The ID of the {@link Expense} to get.
     * @return The {@link Expense} with the specified ID.
     */
    public Expense getExpense(long id)
    {
        return m_expensePersistence.getExpense(id);
    }

    /**
     * Gets the {@link Expense} with the specified name.
     * @param name The name of the {@link Expense} to get.
     * @return The {@link Expense} with the specified name.
     */
    public Expense getExpense(String name)
    {
        return m_expensePersistence.getExpense(name);
    }

    /**
     * Adds the specified {@link Expense} to persistence.
     * @param expense The {@link Expense} to add.
     * @return The new ID if addition was successful, -1 otherwise.
     */
    public long addExpense(Expense expense)
    {
        return m_expensePersistence.addExpense(expense);
    }

    /**
     * Updates the specified {@link Expense}.
     * @param expense The {@link Expense} to update.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateExpense(Expense expense)
    {
        return m_expensePersistence.updateExpense(expense);
    }

    /**
     * Deletes the {@link Expense} with the specified ID.
     * @param id The ID of the {@link Expense} to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deleteExpense(long id)
    {
        return m_expensePersistence.deleteExpense(id);
    }

    /**
     * Deletes the {@link Expense} with the specified name.
     * @param name The name of the {@link Expense} to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deleteExpense(String name)
    {
        return m_expensePersistence.deleteExpense(name);
    }

}
