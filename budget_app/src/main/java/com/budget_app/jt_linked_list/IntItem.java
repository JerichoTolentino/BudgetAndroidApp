package com.budget_app.jt_linked_list;

import com.budget_app.error_handler.*;
import com.budget_app.jt_interfaces.Compareable;

public class IntItem extends NodeItem 
{
	private int data;
	
	//--------------------//
	//--- Constructors ---//
	//--------------------//
	
	public IntItem()
	{
		this.data = 0;
	}
	
	public IntItem(IntItem other)
	{
		this.data = other.data;
	}
	
	public IntItem(int data)
	{
		this.data = data;
	}
	
	//-------------------------//
	//--- Getters & Setters ---//
	//-------------------------//s
	
	public int getData()
	{
		return this.data;
	}
	
	public void setData(int data)
	{
		this.data = data;
	}

	//---------------------------//
	//--- Implemented Methods ---//
	//---------------------------//

	@Override
	public boolean equals(Compareable other) 
	{
		boolean result = false;
		
		if (other instanceof IntItem)
		{
			if(((IntItem)other).data == this.data)
				result = true;
		}
		else
			ErrorHandler.printFailedDowncastErr("Compareable", this, "equals()");
		
		return result;
	}

	@Override
	public int compare(Compareable other) 
	{
		int result = -1;
		
		if (other instanceof IntItem)
		{
			result = this.data - ((IntItem)other).data;
		}
		else
			ErrorHandler.printFailedDowncastErr("Compareable", this, "compare()");
		
		return result;
	}
	
	@Override
	public String toString()
	{
		return Integer.toString(data);
	}

	@Override
	public String toString_CSV() 
	{
		return Integer.toString(data);
	}
	
	//-----------------------------//
	//--- Functionality Methods ---//
	//-----------------------------//
	


}
