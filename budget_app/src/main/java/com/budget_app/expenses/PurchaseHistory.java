package com.budget_app.expenses;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import com.budget_app.jt_interfaces.*;

public class PurchaseHistory implements Stringable, Serializable
{

	//region Members

	ArrayList<Purchase> m_purchases;

	//endregion

	//region Constructor

	public PurchaseHistory()
	{
		this.m_purchases = new ArrayList<>();
	}
	
	public PurchaseHistory(PurchaseHistory other)
	{
		this.m_purchases = other.m_purchases;
	}

	//endregion

	//region Getters & Setters

	public ArrayList<Purchase> getPurchases()
	{
		return this.m_purchases;
	}

	public void setPurchases(ArrayList<Purchase> purchases)
	{
		this.m_purchases = purchases;
	}

	//endregion

	//region Functionality Methods

	//returns a list of the m_purchases on the specified date
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

		Collections.sort(purchasesOnDate, Purchase.getDateComparator());

		return purchasesOnDate;
	}

	//returns a wrapped list of the m_purchases between the specified dates
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

		Collections.sort(purchasesBetween, Purchase.getDateComparator());

		return purchasesBetween;
	}

	//endregion

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
