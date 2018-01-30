package com.budget_app.expenses;

import com.budget_app.jt_interfaces.*;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class Purchase implements Serializable, Comparable
{

	//region Members

	private long id;
	private Priceable item;
	private Date date;
	private int quantity;

	//endregion

	//region Constructor

	public Purchase()
	{
		this.id = 0;
		this.item = null;
		this.date = new Date();
		this.quantity = 0;
	}

	public Purchase(Purchase other)
	{
		this.id = other.id;
		this.item = other.item;
		this.date = other.date;
		this.quantity = other.quantity;
	}

	public Purchase(long id, Priceable item, int quantity, Date date)
	{
		this.id = id;
		this.item = item;
		this.quantity = quantity;
		this.date = date;
	}

	public Purchase(Priceable item, int quantity, Date date)
	{
		this.id = 0;
		this.item = item;
		this.date = date;
		this.quantity = quantity;
	}

	//constructor using the current local date time as time
	public Purchase(Priceable item, int quantity)
	{
		this.id = 0;
		this.item = item;
		this.date = new Date();
		this.quantity = quantity;
	}

	//endregion

	//region Getters & Setters

	public long getId() { return this.id; }

	public void setId(long id) { this.id = id; }

	public Priceable getItem()
	{
		return item;
	}

	public void setItem(Priceable item)
	{
		this.item = item;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}

	//endregion

	//region Implemented Methods

	@Override
	public String toString()
	{
		return ("ID:\t" + Long.toString(this.id) + "\nItem:\n" + this.item.toString() + "\nTime:\t" + this.date.toString() + "\nQuantity:\t" + this.quantity);
	}

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

	public static Comparator<Purchase> getDateComparator()
	{
		return new Comparator<Purchase>()
		{
			public int compare(Purchase one, Purchase two)
			{
				return one.getDate().compareTo(two.getDate());
			}
		};
	}

	//endregion

}