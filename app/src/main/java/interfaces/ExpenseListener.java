package interfaces;

/**
 * Interface that specifies the functionality required to display the contents of an Expense.
 */
public interface ExpenseListener
{

    /**
     * Informs the listener that the ID has changed.
     * @param id The new ID.
     */
    void idChanged(long id);

    /**
     * Informs the listener that the price has changed.
     * @param price The new price.
     */
    void priceChanged(long price);

    /**
     * Informs the listener that the name has changed.
     * @param name The new name.
     */
    void nameChanged(String name);

    /**
     * Informs the listener that the category has changed.
     * @param category The new category.
     */
    void categoryChanged(String category);

    /**
     * Informs the listener that the description has changed.
     * @param description The new description.
     */
    void descriptionChanged(String description);

}
