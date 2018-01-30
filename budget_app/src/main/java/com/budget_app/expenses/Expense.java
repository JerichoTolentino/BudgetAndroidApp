package com.budget_app.expenses;

import com.budget_app.jt_interfaces.Priceable;

import java.io.Serializable;


public class Expense implements Priceable, Serializable
{

	//region Constants

	private static final long DEFAULT_ID = 0;
	private static final String DEFAULT_NAME = "";
	private static final long DEFAULT_PRICE = 0;
	private static final String DEFAULT_CATEGORY = "";
	private static final String DEFAULT_DESCRIPTION = "";

	//endregion

	//region Members

	private long id;
	private String name;
	private long price;
	private String category;
	private String description;

	//endregion

	//region Constructor

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

	//endregion

	//region Getters & Setters

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

	//endregion

	@Override
	public String toString()
	{
		return "ID:\t\t" + id + "\nName:\t\t" + name + "\nPrice:\t\t" + Long.toString(price) + "\nCategory:\t" + category + "\nDescription:\t" + description + "\n------";
	}

}