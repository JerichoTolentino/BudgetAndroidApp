package com.budget_app.expenses;

import com.budget_app.jt_linked_list.*;
import com.budget_app.error_handler.ErrorHandler;
import com.budget_app.jt_interfaces.*;

public class ExpenseGroup extends NodeItem implements Priceable 
{
	private static final long DEFAULT_PRICE = 0;
	private static final String DEFAULT_NAME = "";
	private static final String DEFAULT_CATEGORY = "Uncategorized";
	private static final String DEFAULT_DESCRIPTION = "";

	private long price;
	private String name;
	private String category;
	private String description;
	private SortedList expenses;
	
	//--------------------//
	//--- Constructors ---//
	//--------------------//

	public ExpenseGroup()
	{
		this.price = DEFAULT_PRICE;
		this.name = DEFAULT_NAME;
		this.category = DEFAULT_CATEGORY;
		this.description = DEFAULT_DESCRIPTION;
		this.expenses = new SortedList();
	}
	
	public ExpenseGroup(ExpenseGroup other)
	{
		this.price = other.price;
		this.name = other.name;
		this.category = other.category;
		this.description = other.description;
		this.expenses = new SortedList(other.expenses);
		updatePrice();
	}
	
	public ExpenseGroup(String name, String category, String description, SortedList expenses)
	{
		this.name = name;
		this.price = 0;
		this.category = category;
		this.description = description;
		this.expenses = expenses;
		updatePrice();
	}
	
	public ExpenseGroup(String name, String category, String description)
	{
		this.name = name;
		this.price = 0;
		this.category = category;
		this.description = description;
		this.expenses = new SortedList();
		updatePrice();
	}
	
	public ExpenseGroup(String name, long price, String category, String description)
	{
		this.price = price;
		this.name = name;
		this.category = category;
		this.description = description;
		this.expenses = new SortedList();
	}
	
	public ExpenseGroup(String name, long price, String category, String description, SortedList expenses)
	{
		this.price = price;
		this.name = name;
		this.category = category;
		this.description = description;
		this.expenses = expenses;
		updatePrice();
	}

	//dummy constructor for lookup
	public ExpenseGroup(String name)
	{
		this.name = name;
		this.price = DEFAULT_PRICE;
		this.category = DEFAULT_CATEGORY;
		this.description = DEFAULT_DESCRIPTION;
		this.expenses = null;
	}
	
	//-------------------------//
	//--- Getters & Setters ---//
	//-------------------------//

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getCategory() 
	{
		return category;
	}

	public void setCategory(String category) 
	{
		this.category = category;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public SortedList getExpenses() 
	{
		return expenses;
	}

	public void setExpenses(SortedList expenses) 
	{
		this.expenses = expenses;
		updatePrice();
	}

	//---------------------------//
	//--- Implemented Methods ---//
	//---------------------------//

	//modified to only use name for lookup via name functionality
	@Override
	public boolean equals(Compareable other) 
	{
		ExpenseGroup temp;
		
		if (other instanceof ExpenseGroup)
		{
			temp = (ExpenseGroup)other;
			return (this.name.equals(temp.getName()));
		}
		else
			ErrorHandler.printFailedDowncastErr("Compareable", this, "equals()");
		
		return false;
	}

	//compares the prices of the two expense groups
	@Override
	public int compare(Compareable other) 
	{
		ExpenseGroup temp;
		
		if (other instanceof ExpenseGroup)
		{
			temp = (ExpenseGroup)other;
			return (int)(this.price - temp.price);
		}
		else
			ErrorHandler.printFailedDowncastErr("Compareable", this, "compare()");
		
		return 0;
	}

	@Override
	public long getPrice() 
	{
		return this.price;
	}

	@Override
	public void setPrice(long price) 
	{
		this.price = price;
	}

	@Override
	public String toString() 
	{
		return "Name:\t" + name + "\nPrice:\t" + Long.toString(price) + "\nCategory:\t" + category + 
				"\nDescription:\t" + description + "\nExpenses:\n" + expenses.toString();
	}
	
	@Override
	public String toString_CSV() 
	{
		String output = "";
		Node curr = this.expenses.getHead();
		
		output = '"' + this.name + "\",\"" + this.category + "\",\"" + this.description;
		
		while(curr != null)
		{
			if(curr.getItem() instanceof Expense)
			{
			output += "\",\"" + ((Expense)curr.getItem()).getName();
			}
			else
				ErrorHandler.printFailedDowncastErr("NodeItem", this, "toString_CSV()");
				
			curr = curr.getNext();
		}
		
		output += "\";";
		
		return output;
	}

	//-----------------------------//
	//--- Functionality Methods ---//
	//-----------------------------//

	//adds an expense to the group
	public void addExpense(Expense expense)
	{
		expenses.insertSorted(expense);
		updatePrice();
	}
	
	//remove an expense from the group; returns false if failed
	public boolean removeExpense(Expense expense)
	{
		boolean result = (expenses.removeNode(expense) != null);
		
		if(result)
			updatePrice();
		
		return result;
	}

	//----------------------//
	//--- Helper Methods ---//
	//----------------------//

	//updates the price to be the sum of all prices in 'expenses'
	public void updatePrice()
	{
		Node curr = expenses.getHead();
		NodeItem item;
		long totalPrice = 0;
		
		while(curr != null)
		{
			item = curr.getItem();
			
			if(item instanceof Expense)
				totalPrice += ((Expense)item).getPrice();
			else
				ErrorHandler.printFailedDowncastErr("NodeItem", this, "calculatePrice()");
			
			curr = curr.getNext();
		}
		
		this.price = totalPrice;
	}

}
