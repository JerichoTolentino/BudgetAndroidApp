package com.budget_app.jt_hash_map;

import com.budget_app.jt_linked_list.SortedList;

public class HashKeyValue 
{
	private String key;
	private SortedList list;
	
	//--------------------//
	//--- Constructors ---//
	//--------------------//

	public HashKeyValue()
	{
		key = "";
		list = new SortedList();
	}
	
	public HashKeyValue(HashKeyValue other)
	{
		key = other.key;
		list = new SortedList(other.list);
	}
	
	public HashKeyValue(String key, SortedList list)
	{
		this.key = key;
		this.list = new SortedList(list);
	}

	//-------------------------//
	//--- Getters & Setters ---//
	//-------------------------//

	public String getKey()
	{
		return this.key;
	}
	
	public void setKey(String key)
	{
		this.key = key;
	}

	public SortedList getList()
	{
		return this.list;
	}
	
	public void setList(SortedList list)
	{
		this.list = list;
	}
	
	//-----------------------------//
	//--- Functionality Methods ---//
	//-----------------------------//



	//----------------------//
	//--- Helper Methods ---//
	//----------------------//




	
}
