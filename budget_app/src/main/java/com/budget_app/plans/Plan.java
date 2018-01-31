package com.budget_app.plans;

import com.budget_app.error_handler.ErrorHandler;
import com.budget_app.jt_interfaces.Compareable;

import java.io.Serializable;

public class Plan implements Serializable
{
	private String name;
	private long annualBudget;
	private long annualSavings;
	
	//--------------------//
	//--- Constructors ---//
	//--------------------//

	public Plan()
	{
		this.name = "";
		this.annualBudget = 0;
		this.annualSavings = 0;
	}
	
	public Plan(Plan other)
	{
		this.name = other.name;
		this.annualBudget = other.annualBudget;
		this.annualSavings = other.annualSavings;
	}
	
	public Plan(String name, long annualBudget, long annualSavings)
	{
		this.name = name;
		this.annualBudget = annualBudget;
		this.annualSavings = annualSavings;
	}

	//-------------------------//
	//--- Getters & Setters ---//
	//-------------------------//

	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public long getAnnualBudget()
	{
		return this.annualBudget;
	}
	
	public void setAnnualBudget(long annualBudget)
	{
		this.annualBudget = annualBudget;
	}
	
	public long getAnnualSavings()
	{
		return this.annualSavings;
	}
	
	public void setAnnualSavings(long annualSavings)
	{
		this.annualSavings = annualSavings;
	}

	//---------------------------//
	//--- Implemented Methods ---//
	//---------------------------//
	
	@Override
	public String toString()
	{
		return ("Plan Name:\t" + this.name + "\nAnnual Budget:\t" + Long.toString(this.annualBudget) + "\nAnnual Savings:\t" + Long.toString(this.annualSavings) + "\n-------");
	}

	//-----------------------------//
	//--- Functionality Methods ---//
	//-----------------------------//

	//TODO: Temporary method; make a more meaningful algorithm..
	public boolean createBudget(long annualIncome, long annualExpenses, long annualSavingsGoal)
	{
		boolean possible = false;
		long annualProfit = annualIncome - annualExpenses;
		
		if(possible = (annualProfit >= annualSavingsGoal))
		{
			this.annualBudget = annualProfit;
			this.annualSavings = annualSavingsGoal;
		}
		
		return possible;
	}

	//----------------------//
	//--- Helper Methods ---//
	//----------------------//



}
