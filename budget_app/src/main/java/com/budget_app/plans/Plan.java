package com.budget_app.plans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Plan implements Serializable
{

	//region Members

	// non-editable members
	private final long m_annualIncome;
	private final long m_annualExpenses;
	private final long m_annualSavings;


	private String m_name;
	private long m_annualBudget;

	// works independently of weekly/daily budgets
	private PeriodicBudget m_monthlyBudget;

	// work together (daily budget is limited by weekly budget)
	private PeriodicBudget m_weeklyBudget;
	private PeriodicBudget[] m_dailyBudgets;

	//endregion

	//region Constructor

	public Plan(String name, long annualIncome, long annualExpenses, long annualSavings)
	{
		this.m_name = name;
		this.m_annualIncome = annualIncome;
		this.m_annualExpenses = annualExpenses;
		this.m_annualSavings = annualSavings;
		this.m_annualBudget = annualIncome - annualExpenses - annualSavings;

		initializeDefaultPlan();
	}

	public Plan(String name, long annualIncome, long annualExpenses, long annualSavings, PeriodicBudget weeklyBudget, PeriodicBudget[] dailyBudgets)
	{
		if (dailyBudgets.length != 7)
			assert false;

		this.m_name = name;
		this.m_annualIncome = annualIncome;
		this.m_annualExpenses = annualExpenses;
		this.m_annualSavings = annualSavings;
		this.m_annualBudget = annualIncome - annualExpenses - annualSavings;

		this.m_weeklyBudget = weeklyBudget;
		this.m_dailyBudgets = dailyBudgets;
	}

	public Plan(Plan other)
	{
		this.m_name = other.m_name;
		this.m_annualIncome = other.m_annualIncome;
		this.m_annualExpenses = other.m_annualExpenses;
		this.m_annualSavings = other.m_annualSavings;
		this.m_annualBudget = other.m_annualBudget;
		this.m_monthlyBudget = other.m_monthlyBudget;
		this.m_weeklyBudget = other.m_weeklyBudget;
		System.arraycopy(other.m_dailyBudgets, 0, this.m_dailyBudgets, 0, m_dailyBudgets.length);
	}

	//endregion

	//region Getters & Setters

	public String getName()
	{
		return this.m_name;
	}
	
	public void setName(String name)
	{
		this.m_name = name;
	}

	public long getAnnualIncome() { return this.m_annualIncome; }

	public long getAnnualExpenses() { return this.m_annualExpenses; }

	public long getAnnualSavings() { return this.m_annualSavings; }

	public long getAnnualBudget()
	{
		return this.m_annualBudget;
	}

	public PeriodicBudget getWeeklyBudget() { return this.m_weeklyBudget; }

	public PeriodicBudget[] getDailyBudgets() { return this.m_dailyBudgets; }

	public PeriodicBudget getDailyBudgetOn(int day) {
		if (day > 6)
			return null;
		else {
			return m_dailyBudgets[day];
		}
	}

	//endregion

	@Override
	public String toString()
	{
		return ("Plan Name:\t" + this.m_name + "\nAnnual Budget:\t" + Double.toString(this.m_annualBudget) + "\n-------");
	}

	//region Helper Methods

	public void initializeDefaultPlan()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int numDaysThisYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);

		long monthlyBudget = m_annualBudget / 12;
		long weeklyBudget = m_annualBudget / 52;
		long dailyBudget = m_annualBudget / numDaysThisYear;

		//PeriodicBudget monthly = new PeriodicBudget()
		m_weeklyBudget = new PeriodicBudget(7, weeklyBudget);
		m_dailyBudgets = new PeriodicBudget[7];

		for (int i = 0; i < m_dailyBudgets .length; i++)
			m_dailyBudgets [i] = new PeriodicBudget(1, dailyBudget);
	}

	//endregion

}
