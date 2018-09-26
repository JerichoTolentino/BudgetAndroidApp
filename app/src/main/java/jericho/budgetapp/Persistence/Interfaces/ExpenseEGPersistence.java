package jericho.budgetapp.Persistence.Interfaces;

import java.util.List;

import jericho.budgetapp.Model.Expense;
import jericho.budgetapp.Model.ExpenseGroup;
import utilities.KeyValuePair;

public interface ExpenseEGPersistence
{

    /**
     * Gets the IDS of the {@link Expense}s associated with the specified {@link ExpenseGroup} ID.
     * @param expenseGroupId The ID of the {@link ExpenseGroup}.
     * @return A collection of {@link KeyValuePair} with the {@link Expense} IDs as keys and the quantity as values.
     */
    List<KeyValuePair<Long, Integer>> getExpensesIn(long expenseGroupId);

    /**
     * Gets the IDs of the {@link ExpenseGroup}s associated with the specified {@link Expense} ID.
     * @param expenseId The ID of the {@link Expense}.
     * @return The IDs of the  associated {@link ExpenseGroup}s.
     */
    List<Long> getExpenseGroupsWith(long expenseId);

    /**
     * Associates the specified {@link Expense} ID with the specified {@link ExpenseGroup} ID.
     * @param expenseGroupId The ID of the {@link ExpenseGroup}.
     * @param expenseId The ID of the {@link Expense}.
     * @param quantity The number of {@link Expense}s in the {@link ExpenseGroup}.
     * @return True if the addition was successful, false otherwise.
     */
    boolean addExpenseToGroup(long expenseGroupId, long expenseId, int quantity);

    /**
     * Dissociates the specified {@link Expense} ID from the specified {@link ExpenseGroup} ID.
     * @param expenseId The ID of the {@link Expense}.
     * @param expenseGroupId The ID of the {@link ExpenseGroup}.
     * @return True if the removal was successful, false otherwise.
     */
    boolean removeExpenseFromGroup(long expenseId, long expenseGroupId);

    /**
     * Dissociates the {@link Expense} from all {@link ExpenseGroup}s.
     * @param expenseId The ID of the {@link Expense} to dissociate.
     * @return The number of successful dissociations.
     */
    long deleteExpense(long expenseId);

    /**
     * Dissociates the {@link ExpenseGroup} from all its {@link Expense}s.
     * @param expenseGroupId The ID of the {@link ExpenseGroup} to dissociate.
     * @return The number of successful dissociations.
     */
    long deleteExpenseGroup(long expenseGroupId);

}
