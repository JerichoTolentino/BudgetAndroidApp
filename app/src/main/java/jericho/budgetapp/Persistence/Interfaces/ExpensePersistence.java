package jericho.budgetapp.Persistence.Interfaces;

import jericho.budgetapp.Model.Expense;

public interface ExpensePersistence
{

    /**
     * Gets the {@link Expense} with the specified ID.
     * @param id The ID of the {@link Expense} to get.
     * @return The {@link Expense} with the specified ID.
     */
    Expense getExpense(long id);

    /**
     * Gets the {@link Expense} with the specified name.
     * @param name The name of the {@link Expense} to get.
     * @return The {@link Expense} with the specified name.
     */
    Expense getExpense(String name);

    /**
     * Adds the specified {@link Expense} to persistence.
     * @param expense The {@link Expense} to add.
     * @return The new ID if addition was successful, -1 otherwise.
     */
    long addExpense(Expense expense);

    /**
     * Updates the specified {@link Expense}.
     * @param expense The {@link Expense} to update.
     * @return True if the update was successful, false otherwise.
     */
    boolean updateExpense(Expense expense);

    /**
     * Deletes the {@link Expense} with the specified ID.
     * @param id The ID of the {@link Expense} to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    boolean deleteExpense(long id);

    /**
     * Deletes the {@link Expense} with the specified name.
     * @param name The name of the {@link Expense} to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    boolean deleteExpense(String name);

}
