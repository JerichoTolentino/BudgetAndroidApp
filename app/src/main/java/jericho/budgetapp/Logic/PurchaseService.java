package jericho.budgetapp.Logic;

import jericho.budgetapp.Model.Purchase;
import jericho.budgetapp.Persistence.Interfaces.PurchasePersistence;

public class PurchaseService
{

    private PurchasePersistence m_purchasePersistence;

    /**
     * Initializes a new instance of a {@link PurchaseService}.
     * @param purchasePersistence The {@link PurchasePersistence} to be used for persisting Purchases.
     */
    public PurchaseService(PurchasePersistence purchasePersistence)
    {
        m_purchasePersistence = purchasePersistence;
    }

    /**
     * Gets the {@link Purchase} with the specified ID.
     * @param id The ID of the {@link Purchase} to get.
     * @return The {@link Purchase} with the specified ID.
     */
    public Purchase getPurchase(long id)
    {
        return m_purchasePersistence.getPurchase(id);
    }

    /**
     * Adds the specified {@link Purchase} to persistence.
     * @param purchase The {@link Purchase} to add.
     * @return The new ID if addition was successful, -1 otherwise.
     */
    public long addPurchase(Purchase purchase)
    {
        return m_purchasePersistence.addPurchase(purchase);
    }

    /**
     * Updates the specified {@link Purchase}.
     * @param purchase The {@link Purchase} to update.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updatePurchase(Purchase purchase)
    {
        return m_purchasePersistence.updatePurchase(purchase);
    }

    /**
     * Deletes the specified {@link Purchase}.
     * @param id The ID of the {@link Purchase} to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deletePurchase(long id)
    {
        return m_purchasePersistence.deletePurchase(id);
    }

}
