package jericho.budgetapp.Persistence.Interfaces;

import jericho.budgetapp.Model.Purchase;

public interface PurchasePersistence
{

    /**
     * Gets the {@link Purchase} with the specified ID.
     * @param id The ID of the {@link Purchase} to get.
     * @return The {@link Purchase} with the specified ID.
     */
    Purchase getPurchase(long id);

    /**
     * Adds the specified {@link Purchase} to persistence.
     * @param purchase The {@link Purchase} to add.
     * @return The new ID if addition was successful, -1 otherwise.
     */
    long addPurchase(Purchase purchase);

    /**
     * Updates the specified {@link Purchase}.
     * @param purchase The {@link Purchase} to update.
     * @return True if the update was successful, false otherwise.
     */
    boolean updatePurchase(Purchase purchase);

    /**
     * Deletes the specified {@link Purchase}.
     * @param id The ID of the {@link Purchase} to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    boolean deletePurchase(long id);

}
