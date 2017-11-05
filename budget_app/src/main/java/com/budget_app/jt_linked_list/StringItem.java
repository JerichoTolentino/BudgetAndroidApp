package com.budget_app.jt_linked_list;

import com.budget_app.error_handler.*;
import com.budget_app.jt_interfaces.Compareable;

public class StringItem extends NodeItem 
{
	
	private String data;
	
	//--------------------//
	//--- Constructors ---//
	//--------------------//
	
	public StringItem()
	{
		this.data = "";
	}
	
	public StringItem(StringItem other)
	{
		this.data = other.data;
	}
	
	public StringItem(String data)
	{
		this.data = data;
	}
	
	//-------------------------//
	//--- Getters & Setters ---//
	//-------------------------//
	
	public String getData()
	{
		return this.data;
	}
	
	public void setData(String data)
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
		
		if(other instanceof StringItem)
		{
			if(((StringItem)other).data.equals(this.data))
				result =  true;
		}
		else
			ErrorHandler.printFailedDowncastErr("Compareable", this, "equals()");
		
		return result;
	}

	@Override
	public int compare(Compareable other) 
	{
		int result = -1;
		
		if(other instanceof StringItem)
		{
			result = this.data.compareTo(((StringItem)other).data);
		}
		else
			ErrorHandler.printFailedDowncastErr("Compareable", this, "compare()");
		
		return result;
	}
	
	@Override
	public String toString()
	{
		return data;
	}
	
	@Override
	public String toString_CSV()
	{
		return '"' + data + '"';
	}
	
	//-----------------------------//
	//--- Functionality Methods ---//
	//-----------------------------//
	

	
}
