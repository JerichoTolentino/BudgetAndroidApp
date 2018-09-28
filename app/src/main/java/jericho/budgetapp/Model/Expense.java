package jericho.budgetapp.Model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A class that models the data for an Expense, which is any object that can
 * be purchased or added to an ExpenseGroup.
 */
public class Expense implements Priceable, Serializable
{

	//region Members

	private long id = 0;
	private String name = "";
	private long price = 0;
	private String category = "";
	private String description = "";

	//endregion

	//region Constructor

	/**
	 * Initializes a new instance of an Expense.
	 */
	public Expense()
	{
	}

	/**
	 * Initializes a new instance of an Expense that is a deep-copy of the Expense passed in.
	 * @param other The Expense to copy.
	 */
	public Expense(Expense other)
	{
		this.id = other.id;
		this.price = other.price;
		this.name = other.name;
		this.category = other.category;
		this.description = other.description;
	}

	/**
	 * Initializes a new instance of an Expense with the specified fields.
	 * @param id The ID of the Expense.
	 * @param name The name of the Expense.
	 * @param price The price of the Expense.
	 * @param category The category of the Expense.
	 * @param description A description of the Expense.
	 */
	public Expense(long id, String name, long price, String category, String description)
	{
		this.id = id;
		this.name = name;
		this.price = price;
		this.category = category;
		this.description = description;
	}

	/**
	 * Initializes a new instance of an Expense with the specified fields.
	 * @param name The name of the Expense.
	 * @param price The price of the Expense.
	 * @param category The category of the Expense.
	 * @param description A description of the Expense.
	 */
	public Expense(String name, long price, String category, String description)
	{
		this.name = name;
		this.price = price;
		this.category = category;
		this.description = description;
	}

	/**
	 * Initializes a new instance of an Expense with the specified fields.
	 * @param name The name of the Expense.
	 * @param price The price of the Expense.
	 */
	public Expense(String name, long price)
	{
		this.name = name;
		this.price = price;
	}

	//endregion

	//region Getters & Setters

	/**
	 * Gets the ID of this Expense.
	 * @return The ID of this Expense.
	 */
	public long getId() { return this.id; }

	/**
	 * Sets the ID of this Expense.
	 * @param id The desired ID.
	 */
	public void setId(long id) { this.id = id; }

	/**
	 * Gets the price of this Expense.
	 * @return The price of this Expense.
	 */
	@Override
	public long getPrice()
	{
		return this.price;
	}

	/**
	 * Sets the price of this Expense.
	 * @param price The desired price.
	 */
	@Override
	public void setPrice(long price)
	{
		this.price = price;
	}

	/**
	 * Gets the name of this Expense.
	 * @return The name of this Expense.
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Sets the name of this Expense.
	 * @param name The desired name.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Gets the category of this Expense.
	 * @return The category of this Expense.
	 */
	public String getCategory()
	{
		return this.category;
	}

	/**
	 * Sets the category of this Expense.
	 * @param category The desired category.
	 */
	public void setCategory(String category)
	{
		this.category = category;
	}

	/**
	 * Gets the description of this Expense.
	 * @return The description of this Expense.
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * Sets the description of this Expense.
	 * @param description The desired description.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	//endregion

	/**
	 * Converts the contents of this object to a human-readable string.
	 * @return A human-readable string containing the contents of this object.
	 */
	@Override
	public String toString()
	{
		return "ID:\t\t" + id + "\nName:\t\t" + name + "\nPrice:\t\t" + Long.toString(price) + "\nCategory:\t" + category + "\nDescription:\t" + description + "\n------";
	}

	//region Custom Comparators

	/**
	 * Returns a new instance of an Expense comparator that compares Expenses by their names.
	 * @return An Expense comparator that compares Expenses by their names.
	 */
	public static Comparator<Expense> getNameComparator()
	{
		return new Comparator<Expense>() {
			@Override
			public int compare(Expense expense, Expense t1) {
				return expense.getName().compareTo(t1.getName());
			}
		};
	}

	//endregion

}