package jericho.budgetapp.Model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

/**
 * A class that stores important information whenever an Expense is purchased.
 */
public class Purchase implements Serializable, Comparable
{

	//region Members

	private long m_id = 0;
	private String m_itemName;
	private long m_itemPrice;
	private Date m_date = new Date();
	private int m_quantity = 0;

	//endregion

	//region Constructor

	/**
	 * Initializes a new instance of a Purchase.
	 */
	public Purchase()
	{
	}

	public Purchase(long id, String itemName, long itemPrice, Date date, int quantity)
    {
        this.m_id = id;
        this.m_itemName = itemName;
        this.m_itemPrice = itemPrice;
        this.m_date = date;
        this.m_quantity = quantity;
    }

	//endregion

	//region Getters & Setters

	/**
	 * Gets the ID of this Purchase.
	 * @return The ID of this Purchase.
	 */
	public long getId() { return this.m_id; }

	/**
	 * Sets the ID of this Purchase.
	 * @param id The desired ID.
	 */
	public void setId(long id) { this.m_id = id; }

	/**
	 * Gets the name of the item purchased.
	 * @return The name of the item purchased.
	 */
	public String getItemName()
	{
		return m_itemName;
	}

	/**
	 * Sets the name of the item purchased.
	 * @param itemName The name of the item purchased.
	 */
	public void setItemName(String itemName)
	{
		this.m_itemName = itemName;
	}

	/**
	 * Gets the price of the item purchased.
	 * @return The price of the item purchased.
	 */
	public long getItemPrice()
	{
		return m_itemPrice;
	}

	/**
	 * Sets the price of the item purchased.
	 * @param itemPrice The price of the item purchased.
	 */
	public void setItemPrice(long itemPrice)
	{
		this.m_itemPrice = itemPrice;
	}

	/**
	 * Gets the date this Purchase was made.
	 * @return The date this Purchase was made.
	 */
	public Date getDate()
	{
		return m_date;
	}

	/**
	 * Sets the date this Purchase was made.
	 * @param date The desired date.
	 */
	public void setDate(Date date)
	{
		this.m_date = date;
	}

	/**
	 * Gets the quantity of items in this Purchase.
	 * @return The quantity of items in this Purchase.
	 */
	public int getQuantity()
	{
		return m_quantity;
	}

	/**
	 * Sets the quantity of items in this Purchase.
	 * @param quantity The desired quantity.
	 */
	public void setQuantity(int quantity)
	{
		this.m_quantity = quantity;
	}

	//endregion

	//region Implemented Methods

	/**
	 * Converts the contents of this object to a human-readable string.
	 * @return A human-readable string containing the contents of this object.
	 */
	@Override
	public String toString()
	{
		return ("ID:\t" + Long.toString(this.m_id) +
				"\nItem Name:\t" + this.m_itemName +
				"\nItem Price:\t" + this.m_itemPrice +
				"\nTime:\t" + this.m_date.toString() +
				"\nQuantity:\t" + this.m_quantity);
	}

	//TODO: think about this (why return 0 if object isn't a Purchase?..)

    /**
     * Compares Purchases by their ID.
     * @param o The object to compare with.
     * @return The difference of this ID and the other Purchase's ID.
     */
	@Override
	public int compareTo(Object o) {

		if (o instanceof Purchase)
		{
			Purchase other = (Purchase) o;

			return (int)(other.m_id - this.m_id);
		}

		return 0;
	}

	//endregion

	//region Custom Comparators

    /**
     * Returns a new instance of a Comparator that compares Purchases by their dates in ascending order.
     * @return A new instance of a Comparator that compares Purchases by their dates in ascending order.
     */
	public static Comparator<Purchase> getAscendingDateComparator()
	{
		return new Comparator<Purchase>()
		{
			public int compare(Purchase one, Purchase two)
			{
				return one.getDate().compareTo(two.getDate());
			}
		};
	}

    /**
     * Returns a new instance of a Comparator that compares Purchases by their dates in descending order.
     * @return A new instance of a Comparator that compares Purchases by their dates in descending order.
     */
	public static Comparator<Purchase> getDescendendingDateComparator()
	{
		return new Comparator<Purchase>() {
			@Override
			public int compare(Purchase purchase, Purchase t1) {
				return t1.getDate().compareTo(purchase.getDate());
			}
		};
	}

	//endregion

}