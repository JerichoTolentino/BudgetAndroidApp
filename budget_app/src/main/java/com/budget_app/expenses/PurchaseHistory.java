package com.budget_app.expenses;

import com.budget_app.jt_linked_list.*;

import java.io.Serializable;
import java.util.Date;
import com.budget_app.jt_interfaces.*;

import com.budget_app.error_handler.ErrorHandler;

public class PurchaseHistory implements Stringable, CSVExportable, Serializable
{
	
	SortedList purchases;
	
	//--------------------//
	//--- Constructors ---//
	//--------------------//

	public PurchaseHistory()
	{
		this.purchases = new SortedList();
	}
	
	public PurchaseHistory(PurchaseHistory other)
	{
		this.purchases = other.purchases;
	}

	//-------------------------//
	//--- Getters & Setters ---//
	//-------------------------//

	public SortedList getPurchases()
	{
		return this.purchases;
	}
	
	//returns a wrapped list of the purchases on the specified date
	public SortedList getPurchasesOn(Date date)
	{
		SortedList purchasesOn = null;
		Purchase currPurchase;
		Node curr = purchases.getHead();
		Node prev = null;
		int purchasesOnSize = 0;
		
		while(curr != null)
		{
			prev = curr.getPrev();
			if(curr.getItem() instanceof Purchase)
			{
				currPurchase = (Purchase)curr.getItem();
				
				//if the date of curr is the desired date (list should be ordered!)
				if(date.toString().equals(currPurchase.getDate().toString()))
				{
					//should only happen once.. create SortedList and set it's head
					if(purchasesOnSize == 0)
					{	
						purchasesOn = new SortedList();
						purchasesOn.setHead(curr);
					}
					
					purchasesOnSize += 1;
				}
				else if(purchasesOnSize != 0) 	//happens right after going out of range of desired date (prev holds tail)
					break;						//break out of while loop (at this point, prev == most recent entry of desired date)
			}
			else
				ErrorHandler.printFailedDowncastErr("NodeItem", this, "getPurchasesOn()");
			curr = curr.getNext();
		}
		
		//set the tail and size, if the list exists
		if(purchasesOn != null)
		{
			purchasesOn.setTail(prev);
			purchasesOn.setSize(purchasesOnSize);
		}
		
		return purchasesOn;
	}

	//returns a wrapped list of the purchases between the specified dates
	public SortedList getPurchasesBetween(Date min, Date max)
	{
		SortedList purchasesOn = null;
		Purchase currPurchase;
		Node curr = purchases.getHead();
		Node prev = null;
		int purchasesOnSize = 0;
		
		while(curr != null)
		{
			prev = curr.getPrev();
			if(curr.getItem() instanceof Purchase)
			{
				currPurchase = (Purchase)curr.getItem();
				
				//if the date of curr is within the desired range (list should be ordered!)
				if(min.compareTo(currPurchase.getDate()) <= 0 && max.compareTo(currPurchase.getDate()) >= 0)
				{
					//should only happen once.. create SortedList and set it's head
					if(purchasesOnSize == 0)
					{	
						purchasesOn = new SortedList();
						purchasesOn.setHead(curr);
					}
					
					purchasesOnSize += 1;
				}
				else if(purchasesOnSize != 0) 	//happens right after going out of range of desired date (prev holds tail)
					break;						//break out of while loop (at this point, prev == most recent entry of desired date)
			}
			else
				ErrorHandler.printFailedDowncastErr("NodeItem", this, "getPurchasesOn()");
			curr = curr.getNext();
		}
		
		//set the tail and size, if the list exists
		if(purchasesOn != null)
		{
			purchasesOn.setTail(prev);
			purchasesOn.setSize(purchasesOnSize);
		}
		
		return purchasesOn;
	}
	
	public void setPurchases(SortedList purchases)
	{
		this.purchases = purchases;
	}

	//---------------------------//
	//--- Implemented Methods ---//
	//---------------------------//



	//-----------------------------//
	//--- Functionality Methods ---//
	//-----------------------------//

	//adds a purchase to the history
	public void addPurchase(Purchase purchase)
	{
		purchases.insertSorted(purchase);
	}
	
	//removes a purchase from the history; returns false if not found
	public boolean removePurchase(Purchase purchase)
	{
		boolean result = (purchases.removeNode(purchase) != null);
		
		return result;
	}
	
	@Override
	public String toString()
	{
		String output = "";
		Node curr = purchases.getHead();
		
		while(curr != null)
		{
			output += curr.getItem().toString() + "\n";
			curr = curr.getNext();
		}
		
		return output;
	}

	@Override
	public String toString_CSV() 
	{
		String output = "";
		Node curr = purchases.getHead();
		
		while(curr != null)
		{
			output += curr.getItem().toString_CSV() + "\n";
			curr = curr.getNext();
		}
		
		
		return output;
	}

	//----------------------//
	//--- Helper Methods ---//
	//----------------------//

		
	
}
