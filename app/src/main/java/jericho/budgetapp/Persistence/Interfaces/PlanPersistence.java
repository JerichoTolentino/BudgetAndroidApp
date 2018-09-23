package jericho.budgetapp.Persistence.Interfaces;

import jericho.budgetapp.Model.Plan;

public interface PlanPersistence
{

    /**
     * Gets the {@link Plan} with the specified ID.
     * @param id The ID of the {@link Plan} to get.
     * @return The {@link Plan} with the specified ID.
     */
    Plan getPlan(long id);

    /**
     * Gets the {@link Plan} with the specified name.
     * @param name The name of the {@link Plan} to get.
     * @return The {@link Plan} with the specified name.
     */
    Plan getPlan(String name);

    /**
     * Adds the {@link Plan} to persistence.
     * @param plan The {@link Plan} to add.
     * @return True if the addition was successful, false otherwise.
     */
    boolean addPlan(Plan plan);

    /**
     * Updates the specified {@link Plan}.
     * @param plan The {@link Plan} to update.
     * @return True if the update was successful, false otherwise.
     */
    boolean updatePlan(Plan plan);

    /**
     * Deletes the {@link Plan} with the specified ID.
     * @param id The ID of the {@link Plan} to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    boolean deletePlan(long id);

    /**
     * Deletes the {@link Plan} with the specified name.
     * @param name The name of the {@link Plan} to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    boolean deletePlan(String name);

}
