package expenses;

import interfaces.Priceable;
import utilities.KeyValuePair;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

/**
 * A class that manages a group of Expenses such it can be treated as an Expense as well.
 */
public class ExpenseGroup implements Priceable, Serializable
{

	//region Members

	private long id = 0;
	private long price = 0;
	private String name = "";
	private String category = "";
	private String description = "";
	private HashMap<Long, KeyValuePair<Expense, Integer>> expenses = new HashMap<>();

	//endregion

	//region Constructors

	/**
	 * Initializes a new instance of an ExpenseGroup.
	 */
	public ExpenseGroup()
	{

	}

	/**
	 * Initializes a new instance of an ExpenseGroup that is a deep-copy of the ExpenseGroup passed in.
	 * @param other The ExpenseGroup to copy.
	 */
	public ExpenseGroup(ExpenseGroup other)
	{
		this.id = other.id;
		this.price = other.price;
		this.name = other.name;
		this.category = other.category;
		this.description = other.description;
        for (KeyValuePair<Expense, Integer> e : other.getExpenses())
            this.expenses.put(e.getKey().getId(), e);

        updatePrice();
	}

	/**
	 * Initializes a new instance of an ExpenseGroup with the specified fields.
	 * @param id The ID of the ExpenseGroup.
	 * @param name The name of the ExpenseGroup.
	 * @param category The category of the ExpenseGroup.
	 * @param description The description of the ExpenseGroup.
	 * @param expenses A list of Expenses to add to the ExpenseGroup.
	 */
	public ExpenseGroup(long id, String name, long price, String category, String description, Collection<KeyValuePair<Expense, Integer>> expenses)
	{
		this.id = id;
		this.price = price;
		this.name = name;
		this.category = category;
		this.description = description;
		for (KeyValuePair<Expense, Integer> kvp : expenses)
		    this.expenses.put(kvp.getKey().getId(), kvp);

		updatePrice();
	}

    /** TODO: Remove this...
     * Dummy constructor for purchase history.
     * @param name The name of the dummy ExpenseGroup.
     * @param price The name of the dummy ExpenseGroup.
     * @param category The category of the dummy ExpenseGroup.
     * @param description The description of the dummy ExpenseGroup.
     */
	public ExpenseGroup(String name, long price, String category, String description)
	{
		this.price = price;
		this.name = name;
		this.category = category;
		this.description = description;
	}

	//endregion

	//region Getters & Setters

    /**
     * Gets the ID of this ExpenseGroup.
     * @return The ID of this ExpenseGroup.
     */
	public long getId() { return this.id; }

    /**
     * Sets the ID of this ExpenseGroup.
     * @param id The desired ID.
     */
	public void setId(long id) { this.id = id; }

    /**
     * Gets the name of this ExpenseGroup.
     * @return The name of this ExpenseGroup.
     */
	public String getName()
	{
		return name;
	}

    /**
     * Sets the name of this ExpenseGroup.
     * @param name The desired name.
     */
	public void setName(String name)
	{
		this.name = name;
	}

    /**
     * Gets the category of this ExpenseGroup.
     * @return The category of this ExpenseGroup.
     */
	public String getCategory()
	{
		return category;
	}

    /**
     * Sets the category of this ExpenseGroup.
     * @param category The desired category.
     */
	public void setCategory(String category)
	{
		this.category = category;
	}

    /**
     * Gets the description of this ExpenseGroup.
     * @return The description of this ExpenseGroup.
     */
	public String getDescription()
	{
		return description;
	}

    /**
     * Sets the description of this ExpenseGroup.
     * @param description The desired description.
     */
	public void setDescription(String description)
	{
		this.description = description;
	}

    /**
     * Gets the collection of Expenses in this ExpenseGroup.
     * @return The collection of Expenses in this ExpenseGroup.
     */
	public Iterable<KeyValuePair<Expense, Integer>> getExpenses()
	{
		return expenses.values();
	}

	//endregion

	//region Implemented Methods

    /**
     * Gets the price of this ExpenseGroup.
     * @return The price of this ExpenseGroup.
     */
	@Override
	public long getPrice()
	{
		return this.price;
	}

    /**
     * Sets the price of this ExpenseGroup.
     * @param price The price of this ExpenseGroup.
     */
	@Override
	public void setPrice(long price)
	{
		this.price = price;
	}

    /**
     * Converts the contents of this object into a human-readable string.
     * @return A human-readable string of the contents of this object.
     */
	@Override
	public String toString()
	{
		return "ID:\t" + Long.toString(this.id) + "\nName:\t" + name + "\nPrice:\t" + Long.toString(price)
				+ "\nCategory:\t" + category + "\nDescription:\t" + description + "\nExpenses:\n" + expenses.toString();
	}

	//endregion

	//region Functionality Methods

    /**
     * Adds an Expense to this ExpenseGroup and updates the price.
     * @param expense The Expense to add.
     */
	public void addExpense(Expense expense, int quantity)
	{
	    // Add to the existing quantity if it exists, otherwise add the pair to the collection
        KeyValuePair<Expense, Integer> existing = expenses.get(expense.getId());
	    if (existing != null)
            existing.setValue(existing.getValue() + quantity);
        else
		    expenses.put(expense.getId(), new KeyValuePair<>(expense, quantity));

		updatePrice();
	}

    /**
     * Removes an Expense from this ExpenseGroup and updates the price.
     * Return value indicates the success of the removal.
     * @param expense The Expense to remove.
     * @return True if the removal was a success, false otherwise.
     */
	public boolean removeExpense(Expense expense)
	{
		boolean removed = expenses.remove(expense.getId()) != null;

		if(removed)
			updatePrice();

		return removed;
	}

	//endregion

	//region Helper Methods

    /**
     * Updates the price to reflect the Expenses currently in this ExpenseGroup.
     */
	public void updatePrice()
	{
		long price = 0;

		for (KeyValuePair<Expense, Integer> kvp : expenses.values())
			price += kvp.getKey().getPrice() * kvp.getValue();

		this.price = price;
	}

	//endregion

	//region Custom Comparators

    /**
     * Returns a new instance of a Comparator that compares ExpenseGroups by their names.
     * @return A new instance of a Comparator that compares ExpenseGroups by their names.
     */
	public static Comparator<ExpenseGroup> getNameComparator()
	{
		return new Comparator<ExpenseGroup>() {
			@Override
			public int compare(ExpenseGroup expenseGroup, ExpenseGroup t1) {
				return expenseGroup.getName().compareTo(t1.getName());
			}
		};
	}

	//endregion

}