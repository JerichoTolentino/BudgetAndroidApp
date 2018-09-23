package jericho.budgetapp.Persistence.Interfaces;

import jericho.budgetapp.Model.ExpenseGroup;

public interface ExpenseGroupPersistence
{

    /**
     * Gets the {@link ExpenseGroup} with the specified ID.
     * @param id The ID of the {@link ExpenseGroup}.
     * @return The {@link ExpenseGroup} with the specified ID.
     */
    ExpenseGroup getExpenseGroup(long id);

    /**
     * Gets the {@link ExpenseGroup} with the specified name.
     * @param name The name of the {@link ExpenseGroup}.
     * @return The {@link ExpenseGroup} with the specified name.
     */
    ExpenseGroup getExpenseGroup(String name);

    /**
     * Adds the specified {@link ExpenseGroup} to persistence.
     * @param expenseGroup The {@link ExpenseGroup} to add.
     * @return True if the addition was successful, false otherwise.
     */
    boolean addExpenseGroup(ExpenseGroup expenseGroup);

    /**
     * Updates the specified {@link ExpenseGroup}.
     * @param expenseGroup The {@link ExpenseGroup} to update.
     * @return True if the update was successful, false otherwise.
     */
    boolean updateExpenseGroup(ExpenseGroup expenseGroup);

    /**
     * Deletes the {@link ExpenseGroup} with the specified ID.
     * @param id The ID of the {@link ExpenseGroup} to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    boolean deleteExpenseGroup(long id);

    /**
     * Deletes the {@link ExpenseGroup} with the specified name.
     * @param name The name of the {@link ExpenseGroup} to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    boolean deleteExpenseGroup(String name);

}
