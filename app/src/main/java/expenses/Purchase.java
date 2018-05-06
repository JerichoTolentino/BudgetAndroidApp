package expenses;

import interfaces.Priceable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

/**
 * A class that stores important information whenever an Expense is purchased.
 */
public class Purchase implements Serializable, Comparable
{

	//region Members

	private long id = 0;
	private Priceable item;
	private Date date = new Date();
	private int quantity = 0;

	//endregion

	//region Constructor

	/**
	 * Initializes a new instance of a Purchase.
	 */
	public Purchase()
	{
	}

	/**
	 * Initializes a new instance of a Purchase that is a deep-copy of the Purchase passed in.
	 * @param other The Purchase to copy.
	 */
	public Purchase(Purchase other)
	{
		this.id = other.id;
		this.item = other.item;
		this.date = other.date;
		this.quantity = other.quantity;
	}

	/**
	 * Initializes a new instance of a Purchase with the specified fields.
	 * @param id The ID of the Purchase.
	 * @param item The item purchased.
	 * @param quantity The quantity of the item purchased.
	 * @param date The date of the purchase.
	 */
	public Purchase(long id, Priceable item, int quantity, Date date)
	{
		this.id = id;
		this.item = item;
		this.quantity = quantity;
		this.date = date;
	}

	/**
	 * Initializes a new instance of a Purchase with the specified fields.
	 * @param item The item purchased.
	 * @param quantity The quantity of the item purchased.
	 * @param date The date of the purchase.
	 */
	public Purchase(Priceable item, int quantity, Date date)
	{
		this.item = item;
		this.date = date;
		this.quantity = quantity;
	}

	/**
	 * Initializes a new instance of a Purchase with the specified fields.
	 * @param item The item purchased.
	 * @param quantity The quantity of the item purchased.
	 */
	public Purchase(Priceable item, int quantity)
	{
		this.item = item;
		this.quantity = quantity;
	}

	//endregion

	//region Getters & Setters

	/**
	 * Gets the ID of this Purchase.
	 * @return The ID of this Purchase.
	 */
	public long getId() { return this.id; }

	/**
	 * Sets the ID of this Purchase.
	 * @param id The desired ID.
	 */
	public void setId(long id) { this.id = id; }

	/**
	 * Gets the item of this Purchase.
	 * @return The item of this Purchase.
	 */
	public Priceable getItem()
	{
		return item;
	}

	/**
	 * Sets the item of this Purchase.
	 * @param item The desired item.
	 */
	public void setItem(Priceable item)
	{
		this.item = item;
	}

	/**
	 * Gets the date this Purchase was made.
	 * @return The date this Purchase was made.
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * Sets the date this Purchase was made.
	 * @param date The desired date.
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}

	/**
	 * Gets the quantity of items in this Purchase.
	 * @return The quantity of items in this Purchase.
	 */
	public int getQuantity()
	{
		return quantity;
	}

	/**
	 * Sets the quantity of items in this Purchase.
	 * @param quantity The desired quantity.
	 */
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
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
		return ("ID:\t" + Long.toString(this.id) + "\nItem:\n" + this.item.toString() + "\nTime:\t" + this.date.toString() + "\nQuantity:\t" + this.quantity);
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

			return (int)(other.id - this.id);
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