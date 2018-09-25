package jericho.budgetapp.Persistence.Interfaces;

import jericho.budgetapp.Model.PeriodicBudget;

public interface PeriodicBudgetPersistence
{

    /**
     * Gets the {@link PeriodicBudget} with the specified ID.
     * @param id The ID of the {@link PeriodicBudget} to get.
     * @return The {@link PeriodicBudget} with the specified ID.
     */
    PeriodicBudget getPeriodicBudget(long id);

    /**
     * Adds the specified {@link PeriodicBudget} to persistence.
     * @param periodicBudget The {@link PeriodicBudget} to add.
     * @return The new ID if addition was successful, -1 otherwise.
     */
    long addPeriodicBudget(PeriodicBudget periodicBudget);

    /**
     * Updates the specified {@link PeriodicBudget}.
     * @param periodicBudget The {@link PeriodicBudget} to update.
     * @return True if the update was successful, false otherwise.
     */
    boolean updatePeriodicBudget(PeriodicBudget periodicBudget);

    /**
     * Deletes the specified {@link PeriodicBudget}.
     * @param id The ID of the {@link PeriodicBudget} to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    boolean deletePeriodicBudget(long id);

}
