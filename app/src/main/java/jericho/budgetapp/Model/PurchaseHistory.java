package jericho.budgetapp.Model;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;

/**
 * A class that maintains a history of Purchases made.
 */
public class PurchaseHistory implements Serializable
{

	//region Members

	private ArrayList<Purchase> m_purchases = new ArrayList<>();

	//endregion

	//region Constructor

	/**
	 * Initializes a new instance of a PurchaseHistory.
	 */
	public PurchaseHistory()
	{
	}

	/**
	 * Initializes a new instance of a PurchaseHistory that is a deep-copy of existing PurchaseHistory passed in.
	 * @param other The PurchaseHistory instance to copy.
	 */
	public PurchaseHistory(PurchaseHistory other)
	{
		m_purchases.addAll(other.getPurchases()); 	//TODO: make this actually do a deep copy (just copies the references to each Purchase.
	}

	//endregion

	//region Getters & Setters

	/**
	 * Gets the list of all purchases in the history.
	 * @return The list of purchases in the history.
	 */
	public ArrayList<Purchase> getPurchases()
	{
		return this.m_purchases;
	}

	//endregion

	//region Functionality Methods

	/**
	 * Returns a list of the purchases on the specified date.
	 * @param date The date to filter the purchases by.
	 * @return A list of purchases made on a specified date.
	 */
	public ArrayList<Purchase> getPurchasesOn(Date date)
	{
		ArrayList<Purchase> purchasesOnDate = new ArrayList<>();

		for (Purchase p : m_purchases)
		{
			if (p.getDate().equals(date))
			{
				purchasesOnDate.add(p);
			}
		}

		Collections.sort(purchasesOnDate, Purchase.getAscendingDateComparator());

		return purchasesOnDate;
	}

	/**
	 * Returns a wrapped list of the purchases between the specified dates
	 * @param min The earliest date to include in the results.
	 * @param max The latest date to include in the results.
	 * @return A list of purchases made between the specified date bounds.
	 */
	public ArrayList<Purchase> getPurchasesBetween(Date min, Date max)
	{
		ArrayList<Purchase> purchasesBetween = new ArrayList<>();

		for (Purchase p : m_purchases)
		{
			if (p.getDate().compareTo(min) >= 0 && p.getDate().compareTo(max) <= 0)
			{
				purchasesBetween.add(p);
			}
		}

		Collections.sort(purchasesBetween, Purchase.getAscendingDateComparator());

		return purchasesBetween;
	}

	//endregion

	/**
	 * Converts this object to a human-readable string.
	 * @return A human-readable string containing the contents of this object.
	 */
	@Override
	public String toString()
	{
		String output = "";
		
		for (Purchase p : m_purchases)
		{
			output += p.toString() + "\n";
		}
		
		return output;
	}
	
}
