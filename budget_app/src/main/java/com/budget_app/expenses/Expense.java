package com.budget_app.expenses;

import com.budget_app.jt_interfaces.Compareable;
import com.budget_app.jt_interfaces.Priceable;
import com.budget_app.jt_linked_list.NodeItem;
import com.budget_app.error_handler.ErrorHandler;

//import java.sql.Date;

public class Expense extends NodeItem implements Priceable
{
	private static final String DEFAULT_NAME = "DEFAULT_NAME";
	private static final long DEFAULT_PRICE = 0;
	private static final String DEFAULT_CATEGORY = "Uncategorized";
	private static final String DEFAULT_DESCRIPTION = "";
	
	private String name;
	private long price;
	private String category;
	private String description;
	
	//--------------------//
	//--- Constructors ---//
	//--------------------//

	public Expense()
	{
		this.price = DEFAULT_PRICE;
		this.name = DEFAULT_NAME;
		this.category = DEFAULT_CATEGORY;
		this.description = DEFAULT_DESCRIPTION;
	}
	
	public Expense(Expense other)
	{
		this.price = other.price;
		this.name = other.name;
		this.category = other.category;
		this.description = other.description;
	}
	
	public Expense(String name, long price, String category, String description)
	{
		this.name = name;
		this.price = price;
		this.category = category;
		this.description = description;
	}
	
	public Expense(String name, long price)
	{
		this.name = name;
		this.price = price;
		this.category = DEFAULT_CATEGORY;
		this.description = DEFAULT_DESCRIPTION;
	}
	
	//dummy constructor for lookup
	public Expense(String name)
	{
		this.name = name;
		this.price = 0;
		this.category = "DummyExpense";
		this.description = "DummyExpense";
	}

	//-------------------------//
	//--- Getters & Setters ---//
	//-------------------------//

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
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public String getCategory()
	{
		return this.category;
	}
	
	public void setCategory(String category)
	{
		this.category = category;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}

	//---------------------------//
	//--- Implemented Methods ---//
	//---------------------------//

	//modified to only use name for lookup via name functionality
	@Override
	public boolean equals(Compareable other) 
	{
		Expense temp;
		boolean result = false;
		
		if (other instanceof Expense)
		{
			temp = (Expense)other;
			
			if(this.name.equals(temp.getName()))
				result = true;	
		}
		else
			ErrorHandler.printFailedDowncastErr("Compareable", this, "equals()");
		
		return result;
	}

	@Override
	public int compare(Compareable other) 
	{
		Expense temp;
		int result = -1;
		
		if (other instanceof Expense)
		{
			temp = (Expense)other;
			result = this.name.compareTo(temp.getName());
		}
		else
			ErrorHandler.printFailedDowncastErr("Compareable", this, "compare()");
		return result;
	}

	@Override
	public String toString() 
	{
		return "Name:\t\t" + name + "\nPrice:\t\t" + Long.toString(price) + "\nCategory:\t" + category + "\nDescription:\t" + description + "\n------";
	}

	@Override
	public String toString_CSV() 
	{
		return '"' + this.name + "\"," + Long.toString(this.price) + ",\"" + this.category + "\",\"" + this.description + "\";";
	}
	
	//-----------------------------//
	//--- Functionality Methods ---//
	//-----------------------------//

	

	//----------------------//
	//--- Helper Methods ---//
	//----------------------//



}
