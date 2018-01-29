package com.budget_app.expenses;

import com.budget_app.error_handler.ErrorHandler;
import com.budget_app.jt_interfaces.Compareable;
import com.budget_app.jt_interfaces.Priceable;
import com.budget_app.jt_linked_list.Node;
import com.budget_app.jt_linked_list.NodeItem;
import com.budget_app.jt_linked_list.SortedList;

import java.io.Serializable;

public class ExpenseGroup extends NodeItem implements Priceable, Serializable
{
	private static final long DEFAULT_ID = 0;
	private static final long DEFAULT_PRICE = 0;
	private static final String DEFAULT_NAME = "";
	private static final String DEFAULT_CATEGORY = "Uncategorized";
	private static final String DEFAULT_DESCRIPTION = "";

	private long id;
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
		this.id = DEFAULT_ID;
		this.price = DEFAULT_PRICE;
		this.name = DEFAULT_NAME;
		this.category = DEFAULT_CATEGORY;
		this.description = DEFAULT_DESCRIPTION;
		this.expenses = new SortedList();
	}

	public ExpenseGroup(ExpenseGroup other)
	{
		this.id = other.id;
		this.price = other.price;
		this.name = other.name;
		this.category = other.category;
		this.description = other.description;
		this.expenses = new SortedList(other.expenses);
		updatePrice();
	}

	public ExpenseGroup(String name, String category, String description, SortedList expenses)
	{
		this.id = DEFAULT_ID;
		this.name = name;
		this.price = 0;
		this.category = category;
		this.description = description;
		this.expenses = expenses;
		updatePrice();
	}

	public ExpenseGroup(String name, String category, String description)
	{
		this.id = DEFAULT_ID;
		this.name = name;
		this.price = 0;
		this.category = category;
		this.description = description;
		this.expenses = new SortedList();
		updatePrice();
	}

	public ExpenseGroup(long id, String name, long price, String category, String description, SortedList expenses)
	{
		this.id = id;
		this.price = price;
		this.name = name;
		this.category = category;
		this.description = description;
		this.expenses = expenses;
		updatePrice();
	}

	public ExpenseGroup(String name, long price, String category, String description, SortedList expenses)
	{
		this.id = DEFAULT_ID;
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
		this.id = DEFAULT_ID;
		this.name = name;
		this.price = DEFAULT_PRICE;
		this.category = DEFAULT_CATEGORY;
		this.description = DEFAULT_DESCRIPTION;
		this.expenses = null;
	}

	//-------------------------//
	//--- Getters & Setters ---//
	//-------------------------//

	public long getId() { return this.id; }

	public void setId(long id) { this.id = id; }

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
		boolean result = false;

		if (other instanceof ExpenseGroup)
		{
			temp = (ExpenseGroup)other;
			result = this.name.equals(temp.name) &&
					this.id == temp.id &&
					this.price == temp.price &&
					this.category.equals(temp.category) &&
					this.description.equals(temp.description) &&
					this.expenses.equals(temp.expenses);
		}
		else
			ErrorHandler.printFailedDowncastErr("Compareable", this, "equals()");

		return result;
	}

	@Override
	public int compare(Compareable other)
	{
		ExpenseGroup temp;
		int result = -1;

		if (other instanceof ExpenseGroup)
		{
			temp = (ExpenseGroup)other;
			result = this.name.compareTo(temp.name);

			if(result == 0)
				result = (int)(this.price - temp.price);	//Note: this is probably bad
			if(result == 0)
				result = this.category.compareTo(temp.category);
			if(result == 0)
				result = this.description.compareTo(temp.description);
			if(result == 0)
				result = (int)(this.id - temp.id);			//Note: this is probably bad
		}
		else
			ErrorHandler.printFailedDowncastErr("Compareable", this, "compare()");

		return result;
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
		return "ID:\t" + Long.toString(this.id) + "\nName:\t" + name + "\nPrice:\t" + Long.toString(price)
				+ "\nCategory:\t" + category + "\nDescription:\t" + description + "\nExpenses:\n" + expenses.toString();
	}

	@Override
	public String toString_CSV()
	{
		String output;
		Node curr = this.expenses.getHead();

		output = Long.toString(this.id) + ",\"" + this.name + "\",\"" + this.category + "\",\"" + this.description + "\"";

		while(curr != null)
		{
			if(curr.getItem() instanceof Expense)
				output += ",\"" + ((Expense)curr.getItem()).getName() + "\"";
			else
				ErrorHandler.printFailedDowncastErr("NodeItem", this, "toString_CSV()");

			curr = curr.getNext();
		}

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