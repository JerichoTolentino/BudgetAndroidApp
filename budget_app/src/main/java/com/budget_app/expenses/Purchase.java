package com.budget_app.expenses;

import com.budget_app.jt_interfaces.*;
import com.budget_app.jt_linked_list.NodeItem;
import com.budget_app.error_handler.ErrorHandler;
import java.util.Date;

public class Purchase extends NodeItem
{

	private Priceable item;
	private Date date;
	private int quantity;
	
	//--------------------//
	//--- Constructors ---//
	//--------------------//

	public Purchase()
	{
		this.item = null;
		this.date = new Date();
		this.quantity = 0;
	}
	
	public Purchase(Purchase other)
	{
		this.item = other.item;
		this.date = other.date;
		this.quantity = other.quantity;
	}
	
	public Purchase(Priceable item, int quantity, Date date)
	{
		this.item = item;
		this.date = date;
		this.quantity = quantity;
	}
	
	//constructor using the current local date time as time
	public Purchase(Priceable item, int quantity)
	{
		this.item = item;
		this.date = new Date();
		this.quantity = quantity;
	}

	//-------------------------//
	//--- Getters & Setters ---//
	//-------------------------//

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

	//---------------------------//
	//--- Implemented Methods ---//
	//---------------------------//

	@Override
	public boolean equals(Compareable other) 
	{
		Purchase temp;
		
		if (other instanceof Purchase)
		{
			temp = (Purchase)other;
			return (this.item == temp.item && this.date == temp.date && this.quantity == temp.quantity);
		}
		else
			ErrorHandler.printFailedDowncastErr("Compareable", this, "equals()");
		
		return false;
	}

	@Override
	public int compare(Compareable other) 
	{
		Purchase temp;
		
		if (other instanceof Purchase)
		{
			temp = (Purchase)other;
			return (this.date.compareTo(temp.date));
		}
		else
			ErrorHandler.printFailedDowncastErr("Compareable", this, "compare()");
		
		return -1;
	}

	@Override
	public String toString() 
	{
		return ("Item:\n" + this.item.toString() + "\nTime:\t" + this.date.toString() + "\nQuantity:\t" + this.quantity);
	}

	@Override
	public String toString_CSV()
	{
		Expense tempExpense;
		ExpenseGroup tempExpenseGroup;
		String output = "";
		
		if(this.item instanceof Expense)
		{
			tempExpense = (Expense)this.item;
			output += '"' + tempExpense.getName() + "\"," + Long.toString(tempExpense.getPrice());
		}
		else if(this.item instanceof ExpenseGroup)
		{
			tempExpenseGroup = (ExpenseGroup)this.item;
			output += '"' + tempExpenseGroup.getName() + "\"," + Long.toString(tempExpenseGroup.getPrice());
		}
		else
			ErrorHandler.printFailedDowncastErr("Priceable", this, "toString_CSV()");
		
		if(!output.equals(""))
		{
			output += "," + this.quantity + "," + this.date.toString() + ";";
		}
		else
			ErrorHandler.printErr("Could not properly downcast item!", this, "toString_CSV()");
		
		return output;
	}
	
	//-----------------------------//
	//--- Functionality Methods ---//
	//-----------------------------//



	//----------------------//
	//--- Helper Methods ---//
	//----------------------//





	
}
