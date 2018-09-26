package jericho.budgetapp.Logic;

import java.util.ArrayList;
import java.util.List;

import jericho.budgetapp.Model.Expense;
import jericho.budgetapp.Model.ExpenseGroup;
import jericho.budgetapp.Persistence.Interfaces.ExpenseEGPersistence;
import jericho.budgetapp.Persistence.Interfaces.ExpenseGroupPersistence;
import jericho.budgetapp.Persistence.Interfaces.ExpensePersistence;
import utilities.KeyValuePair;

public class ExpenseGroupService
{

    private ExpenseGroupPersistence m_egPersistence;
    private ExpenseEGPersistence m_bridgePersistence;
    private ExpensePersistence m_expensePersistence;

    public ExpenseGroupService(
            ExpenseGroupPersistence egPersistence,
            ExpenseEGPersistence expenseEGPersistence,
            ExpensePersistence expensePersistence)
    {
        m_egPersistence = egPersistence;
        m_bridgePersistence = expenseEGPersistence;
        m_expensePersistence = expensePersistence;
    }

    /**
     * Gets the {@link ExpenseGroup} with the specified ID.
     * @param id The ID of the {@link ExpenseGroup}.
     * @return The {@link ExpenseGroup} with the specified ID.
     */
    public ExpenseGroup getExpenseGroup(long id)
    {
        // Get ExpenseGroup
        ExpenseGroup expenseGroup = m_egPersistence.getExpenseGroup(id);

        // Get Expenses in the ExpenseGroup
        List<KeyValuePair<Expense, Integer>> expenses = this.getExpensesInGroup(id);
        for (KeyValuePair<Expense, Integer> kvp : expenses)
        {
            expenseGroup.addExpense(kvp.getKey(), kvp.getValue());
        }

        return expenseGroup;
    }

    /**
     * Gets the {@link ExpenseGroup} with the specified name.
     * @param name The name of the {@link ExpenseGroup}.
     * @return The {@link ExpenseGroup} with the specified name.
     */
    public ExpenseGroup getExpenseGroup(String name)
    {
        // Get ExpenseGroup
        ExpenseGroup expenseGroup = m_egPersistence.getExpenseGroup(name);

        // Get Expenses in the ExpenseGroup
        List<KeyValuePair<Expense, Integer>> expenses = this.getExpensesInGroup(expenseGroup.getId());
        for (KeyValuePair<Expense, Integer> kvp : expenses)
        {
            expenseGroup.addExpense(kvp.getKey(), kvp.getValue());
        }

        return expenseGroup;
    }

    /**
     * Adds the specified {@link ExpenseGroup} to persistence.
     * @param expenseGroup The {@link ExpenseGroup} to add.
     * @return The new ID if addition was successful, -1 otherwise.
     */
    public long addExpenseGroup(ExpenseGroup expenseGroup)
    {
        long expenseGroupId = m_egPersistence.addExpenseGroup(expenseGroup);
        addExpensesInGroup(expenseGroup);

        return expenseGroupId;
    }

    /**
     * Updates the specified {@link ExpenseGroup}.
     * @param expenseGroup The {@link ExpenseGroup} to update.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateExpenseGroup(ExpenseGroup expenseGroup)
    {
        boolean success = m_egPersistence.updateExpenseGroup(expenseGroup);
        removeExpensesFromGroup(expenseGroup);
        addExpensesInGroup(expenseGroup);

        return success;
    }

    /**
     * Deletes the {@link ExpenseGroup} with the specified ID.
     * @param id The ID of the {@link ExpenseGroup} to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deleteExpenseGroup(long id)
    {
        m_bridgePersistence.deleteExpenseGroup(id);
        return m_egPersistence.deleteExpenseGroup(id);
    }

    /**
     * Deletes the {@link ExpenseGroup} with the specified name.
     * <p>
     *     Note: Deleting by ID has better performance.
     * </p>
     * @param name The name of the {@link ExpenseGroup} to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deleteExpenseGroup(String name)
    {
        ExpenseGroup expenseGroup = m_egPersistence.getExpenseGroup(name);

        return deleteExpenseGroup(expenseGroup.getId());
    }

    /**
     * Gets the {@link Expense}s and their quantities in an {@link ExpenseGroup}.
     * @param expenseGroupId The ID of the {@link ExpenseGroup}.
     * @return The {@link Expense}s and their quantities in the {@link ExpenseGroup} with the specified ID.
     */
    private List<KeyValuePair<Expense, Integer>> getExpensesInGroup(long expenseGroupId)
    {
        List<KeyValuePair<Expense, Integer>> expenses = new ArrayList<>();

        List<KeyValuePair<Long, Integer>> expenseIds = m_bridgePersistence.getExpensesIn(expenseGroupId);
        for (KeyValuePair<Long, Integer> kvp : expenseIds)
        {
            Expense expense = m_expensePersistence.getExpense(kvp.getKey());
            expenses.add(new KeyValuePair<>(expense, kvp.getValue()));
        }

        return expenses;
    }

    /**
     * Associates the {@link Expense}s with the {@link ExpenseGroup}.
     * @param expenseGroup The {@link ExpenseGroup} with the {@link Expense}s to add.
     * @return The number of associations successfully created.
     */
    private int addExpensesInGroup(ExpenseGroup expenseGroup)
    {
        int numSuccess = 0;
        long expenseGroupId = expenseGroup.getId();
        for (KeyValuePair<Expense, Integer> kvp : expenseGroup.getExpenses())
        {
            boolean success = m_bridgePersistence.addExpenseToGroup(expenseGroupId, kvp.getKey().getId(), kvp.getValue());
            if (success)
                numSuccess++;
        }

        return numSuccess;
    }

    /**
     * Dissociates the {@link Expense}s from the {@link ExpenseGroup}.
     * @param expenseGroup The {@link ExpenseGroup} with the {@link Expense}s to remove.
     * @return The number of successful dissociations.
     */
    private int removeExpensesFromGroup(ExpenseGroup expenseGroup)
    {
        int numSuccess = 0;
        long expenseGroupId = expenseGroup.getId();
        for (KeyValuePair<Expense, Integer> kvp : expenseGroup.getExpenses())
        {
            boolean success = m_bridgePersistence.removeExpenseFromGroup(kvp.getKey().getId(), expenseGroupId);
            if (success)
                numSuccess++;
        }

        return numSuccess;
    }

}
