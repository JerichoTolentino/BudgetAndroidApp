package com.budget_app.expenses;

import com.budget_app.jt_interfaces.Priceable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class ExpenseGroup implements Priceable, Serializable
{

	//region Constants

	private static final long DEFAULT_ID = 0;
	private static final long DEFAULT_PRICE = 0;
	private static final String DEFAULT_NAME = "";
	private static final String DEFAULT_CATEGORY = "Uncategorized";
	private static final String DEFAULT_DESCRIPTION = "";

	//endregion

	//region Members

	private long id;
	private long price;
	private String name;
	private String category;
	private String description;
	private ArrayList<ExpenseInGroup> expenses;

	//endregion

	//region Constructors

	public ExpenseGroup()
	{
		this.id = DEFAULT_ID;
		this.price = DEFAULT_PRICE;
		this.name = DEFAULT_NAME;
		this.category = DEFAULT_CATEGORY;
		this.description = DEFAULT_DESCRIPTION;
		this.expenses = new ArrayList<>();
	}

	public ExpenseGroup(ExpenseGroup other)
	{
		this.id = other.id;
		this.price = other.price;
		this.name = other.name;
		this.category = other.category;
		this.description = other.description;
		this.expenses = new ArrayList<>(other.getExpenses());
		updatePrice();
	}

	public ExpenseGroup(String name, String category, String description, ArrayList<ExpenseInGroup> expenses)
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
		this.expenses = new ArrayList<>();
		updatePrice();
	}

	public ExpenseGroup(long id, String name, long price, String category, String description, ArrayList<ExpenseInGroup> expenses)
	{
		this.id = id;
		this.price = price;
		this.name = name;
		this.category = category;
		this.description = description;
		this.expenses = expenses;
		updatePrice();
	}

	public ExpenseGroup(String name, long price, String category, String description, ArrayList<ExpenseInGroup> expenses)
	{
		this.id = DEFAULT_ID;
		this.price = price;
		this.name = name;
		this.category = category;
		this.description = description;
		this.expenses = expenses;
		updatePrice();
	}

	//endregion

	//region Getters & Setters

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

	public ArrayList<ExpenseInGroup> getExpenses()
	{
		return expenses;
	}

	public void setExpenses(ArrayList<ExpenseInGroup> expenses)
	{
		this.expenses = expenses;
		updatePrice();
	}

	//endregion

	//region Implemented Methods

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

	//endregion

	//region Functionality Methods

	//adds an expense to the group
	public void addExpense(ExpenseInGroup expense)
	{
		expenses.add(expense);
		updatePrice();
	}

	//remove an expense from the group; returns false if failed
	public boolean removeExpense(ExpenseInGroup expense)
	{
		boolean result = (expenses.remove(expense));

		if(result)
			updatePrice();

		return result;
	}

	//endregion

	//region Helper Methods

	//updates the price to be the sum of all prices in 'expenses'
	public void updatePrice()
	{
		long price = 0;

		for (ExpenseInGroup expense : expenses)
		{
			price += expense.getPrice() * expense.getQuantity();
		}

		this.price = price;
	}

	//endregion

	//region Custom Comparators

	public static Comparator<ExpenseGroup> getNameComparator()
	{
		return new Comparator<ExpenseGroup>() {
			@Override
			public int compare(ExpenseGroup expenseGroup, ExpenseGroup t1) {
				return expenseGroup.getName().compareTo(t1.getName());
			}
		};
	}

	//endregion

}