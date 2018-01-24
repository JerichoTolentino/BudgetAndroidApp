package com.budget_app.expenses;

import com.budget_app.jt_interfaces.Compareable;
import com.budget_app.jt_interfaces.Priceable;
import com.budget_app.jt_linked_list.NodeItem;
import com.budget_app.error_handler.ErrorHandler;

//import java.sql.Date;

public class Expense extends NodeItem implements Priceable
{
	private static final long DEFAULT_ID = 0;
	private static final String DEFAULT_NAME = "DEFAULT_NAME";
	private static final long DEFAULT_PRICE = 0;
	private static final String DEFAULT_CATEGORY = "Uncategorized";
	private static final String DEFAULT_DESCRIPTION = "";

	private long id;
	private String name;
	private long price;
	private String category;
	private String description;

	//--------------------//
	//--- Constructors ---//
	//--------------------//

	public Expense()
	{
		this.id = DEFAULT_ID;
		this.price = DEFAULT_PRICE;
		this.name = DEFAULT_NAME;
		this.category = DEFAULT_CATEGORY;
		this.description = DEFAULT_DESCRIPTION;
	}

	public Expense(Expense other)
	{
		this.id = other.id;
		this.price = other.price;
		this.name = other.name;
		this.category = other.category;
		this.description = other.description;
	}

	public Expense(long id, String name, long price, String category, String description)
	{
		this.id = id;
		this.name = name;
		this.price = price;
		this.category = category;
		this.description = description;
	}

	public Expense(String name, long price, String category, String description)
	{
		this.id = DEFAULT_ID;
		this.name = name;
		this.price = price;
		this.category = category;
		this.description = description;
	}

	public Expense(String name, long price)
	{
		this.id = DEFAULT_ID;
		this.name = name;
		this.price = price;
		this.category = DEFAULT_CATEGORY;
		this.description = DEFAULT_DESCRIPTION;
	}

	//dummy constructor for lookup
	public Expense(String name)
	{
		this.id = DEFAULT_ID;
		this.name = name;
		this.price = 0;
		this.category = "DummyExpense";
		this.description = "DummyExpense";
	}

	//-------------------------//
	//--- Getters & Setters ---//
	//-------------------------//

	public long getId() { return this.id; }

	public void setId(long id) { this.id = id; }

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

	@Override
	public boolean equals(Compareable other)
	{
		Expense temp;
		boolean result = false;

		if (other instanceof Expense)
		{
			temp = (Expense)other;

			result = this.name.equals(temp.name) &&
					this.id == temp.id &&
					this.price == temp.price &&
					this.category.equals(temp.category) &&
					this.description.equals(temp.description);

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
	public String toString()
	{
		return "ID:\t\t" + id + "\nName:\t\t" + name + "\nPrice:\t\t" + Long.toString(price) + "\nCategory:\t" + category + "\nDescription:\t" + description + "\n------";
	}

	@Override
	public String toString_CSV()
	{
		return Long.toString(this.id) + ",\"" + this.name + "\"," + Long.toString(this.price) + ",\"" + this.category + "\",\"" + this.description + "\";";
	}

	//-----------------------------//
	//--- Functionality Methods ---//
	//-----------------------------//



	//----------------------//
	//--- Helper Methods ---//
	//----------------------//



}