package jericho.budgetapp.Persistence.Interfaces;

import java.util.List;
import jericho.budgetapp.Model.PeriodicBudget;
import jericho.budgetapp.Model.Plan;

public interface PlanPBPersistence
{

    /**
     * Gets the list of {@link PeriodicBudget} IDs in the specified {@link Plan}.
     * @param planId The ID of the {@link Plan}.
     * @return The list of {@link PeriodicBudget} IDs in the {@link Plan}.
     */
    List<Long> getPeriodicBudgetsIn(long planId);

    /**
     * Gets the {@link Plan} containing the {@link PeriodicBudget} with the specified ID.
     * @param periodicBudgetId The ID of the {@link PeriodicBudget}.
     * @return The ID of the {@link Plan} containing the {@link PeriodicBudget}.
     */
    long getPlanFor(long periodicBudgetId);

    /**
     * Associate the {@link PeriodicBudget} with the specified ID to the {@link Plan} with the specified ID.
     * @param periodicBudgetId The ID of the {@link PeriodicBudget}.
     * @param planId The ID of the {@link Plan}.
     * @return True if the addition was successful, false otherwise.
     */
    boolean addPeriodicBudgetTo(long periodicBudgetId, long planId);

    /**
     * Dissociate the {@link PeriodicBudget} with the specified ID from the {@link Plan} with the specified ID.
     * @param periodicBudgetId The ID of the {@link PeriodicBudget}.
     * @param planId The ID of the {@link Plan}.
     * @return True if the dissociation was successful, false otherwise.
     */
    boolean removePeriodicBudgetFrom(long periodicBudgetId, long planId);

    /**
     * Deletes all associations the {@link PeriodicBudget} with the specified ID is a part of.
     * @param periodicBudgetId The ID of the {@link PeriodicBudget}.
     * @return The number of associations deleted.
     */
    long deletePeriodicBudget(long periodicBudgetId);

    /**
     * Deletes all associations the {@link Plan} with the specified ID is a part of.
     * @param planId The ID of the {@link Plan}.
     * @return The number of associations deleted.
     */
    long deletePlan(long planId);

}
