package com.budget_app.plans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Plan implements Serializable
{

	//region Constants
	private static final long DEFAULT_ID = 0;
	private static final String DAYOFWEEK_SUNDAY = "sunday";
	private static final String DAYOFWEEK_MONDAY = "monday";
	private static final String DAYOFWEEK_TUESDAY = "tuesday";
	private static final String DAYOFWEEK_WEDNESDAY = "wednesday";
	private static final String DAYOFWEEK_THURSDAY = "thursday";
	private static final String DAYOFWEEK_FRIDAY = "friday";
	private static final String DAYOFWEEK_SATURDAY = "saturday";

	//endregion

	//region Members

	private long m_id;
	private String m_name;
	private long m_annualBudget;
	private long m_annualIncome;
	private long m_annualExpenses;
	private long m_annualSavings;

	// works independently of weekly/daily budgets
	private PeriodicBudget m_monthlyBudget;

	// work together (daily budget is limited by weekly budget)
	private PeriodicBudget m_weeklyBudget;
	private PeriodicBudget[] m_dailyBudgets;

	//endregion

	//region Constructor

	public Plan()
	{
		this.m_id = -1;
		this.m_name = "";
		this.m_annualIncome = 0;
		this.m_annualExpenses = 0;
		this.m_annualSavings = 0;
		this.m_annualBudget = 0;

		initializeDefaultPlan();
	}

	public Plan(String name, long annualIncome, long annualExpenses, long annualSavings)
	{
		this.m_id = DEFAULT_ID;
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

		this.m_id = DEFAULT_ID;
		this.m_name = name;
		this.m_annualIncome = annualIncome;
		this.m_annualExpenses = annualExpenses;
		this.m_annualSavings = annualSavings;
		this.m_annualBudget = annualIncome - annualExpenses - annualSavings;

		this.m_weeklyBudget = weeklyBudget;
		this.m_dailyBudgets = dailyBudgets;
	}

	public Plan(long id, String name, long annualIncome, long annualExpenses, long annualSavings, long annualBudget, PeriodicBudget weeklyBudget, PeriodicBudget[] dailyBudgets)
	{
		if (dailyBudgets.length != 7)
			assert false;

		this.m_id = id;
		this.m_name = name;
		this.m_annualIncome = annualIncome;
		this.m_annualExpenses = annualExpenses;
		this.m_annualSavings = annualSavings;
		this.m_annualBudget = annualBudget;

		this.m_weeklyBudget = weeklyBudget;
		this.m_dailyBudgets = dailyBudgets;
	}

	public Plan(Plan other)
	{
		this.m_id = other.m_id;
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

	public long getId() { return this.m_id; }
	public void setId(long id) { this.m_id = id; }

	public String getName()
	{
		return this.m_name;
	}
	public void setName(String name)
	{
		this.m_name = name;
	}

	public long getAnnualIncome() { return this.m_annualIncome; }
	public void setAnnualIncome(long annualIncome) { m_annualIncome = annualIncome; }

	public long getAnnualExpenses() { return this.m_annualExpenses; }
	public void setAnnualExpenses(long annualExpenses) { m_annualExpenses = annualExpenses; }

	public long getAnnualSavings() { return this.m_annualSavings; }
	public void setAnnualSavings(long annualSavings) { m_annualSavings = annualSavings; }

	public long getAnnualBudget() { return this.m_annualBudget; }
	public void setAnnualBudget(long annualBudget) { m_annualBudget = annualBudget; }

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

	//region Public Methods

	public void recalculateBudgets()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int numDaysThisYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);

		long monthlyBudget = m_annualBudget / 12;
		long weeklyBudget = m_annualBudget / 52;
		long dailyBudget = m_annualBudget / numDaysThisYear;

		//PeriodicBudget monthly = new PeriodicBudget()
		m_weeklyBudget = new PeriodicBudget(7, weeklyBudget, "weekly");
		m_dailyBudgets = new PeriodicBudget[7];

		for (int i = 0; i < m_dailyBudgets .length; i++)
			m_dailyBudgets [i] = new PeriodicBudget(1, dailyBudget, indexToDayOfWeek(i));
	}

	@Override
	public String toString()
	{
		return ("Plan Name:\t" + this.m_name + "\nAnnual Budget:\t" + Double.toString(this.m_annualBudget) + "\n-------");
	}

	//endregion

	//region Helper Methods

	private void initializeDefaultPlan()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int numDaysThisYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);

		long monthlyBudget = m_annualBudget / 12;
		long weeklyBudget = m_annualBudget / 52;
		long dailyBudget = m_annualBudget / numDaysThisYear;

		//PeriodicBudget monthly = new PeriodicBudget()
		m_weeklyBudget = new PeriodicBudget(7, weeklyBudget, "weekly");
		m_dailyBudgets = new PeriodicBudget[7];

		for (int i = 0; i < m_dailyBudgets .length; i++)
			m_dailyBudgets [i] = new PeriodicBudget(1, dailyBudget, indexToDayOfWeek(i));
	}

	private String indexToDayOfWeek(int index)
	{
		switch (index)
		{
			case 0:
				return DAYOFWEEK_SUNDAY;
			case 1:
				return DAYOFWEEK_MONDAY;
			case 2:
				return DAYOFWEEK_TUESDAY;
			case 3:
				return DAYOFWEEK_WEDNESDAY;
			case 4:
				return DAYOFWEEK_THURSDAY;
			case 5:
				return DAYOFWEEK_FRIDAY;
			case 6:
				return DAYOFWEEK_SATURDAY;
			default:
				return "";
		}

	}

	//endregion

}
