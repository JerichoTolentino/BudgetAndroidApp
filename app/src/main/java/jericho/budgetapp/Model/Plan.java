package jericho.budgetapp.Model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Class that represents a budget Plan with annual goals and daily, weekly, and monthly budgets.
 */
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
	private PeriodicBudget m_weeklyBudget;	// TODO: Remove weekly budgets entirely? (or make this only have a weekly budget which is just an aggregate of DailyBudgets)
	private PeriodicBudget[] m_dailyBudgets;

	//endregion

	//region Constructor

    /**
     * Initializes a new instance of a Plan.
     */
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

    /**
     * Initializes a new instance of a Plan with the specified fields.
     * @param name The name of the Plan.
     * @param annualIncome The annual income.
     * @param annualExpenses The annual expenses.
     * @param annualSavings The annual savings goal.
     */
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

    /**
     * Initializes a new instance of a {@link Plan}.
     * @param id The ID.
     * @param name The name.
     * @param annualIncome The annual income.
     * @param annualExpenses The annual expenses.
     * @param annualSavings The annual savings.
     * @param annualBudget The annual budget.
     */
	public Plan(long id, String name, long annualIncome, long annualExpenses, long annualSavings, long annualBudget)
    {
        this.m_id = id;
        this.m_name = name;
        this.m_annualIncome = annualIncome;
        this.m_annualExpenses = annualExpenses;
        this.m_annualSavings = annualSavings;
        this.m_annualBudget = annualBudget;
    }

    /**
     * Initializes a new instance of a Plan with the specified fields.
     * <p>
     *     Note: The number of daily budgets must be 7. (A week has 7 days)
     * </p>
     * @param name The name of the Plan.
     * @param annualIncome The annual income.
     * @param annualExpenses The annual expenses.
     * @param annualSavings The annual savings goal.
     * @param weeklyBudget The allocated weekly budget.
     * @param dailyBudgets The allocated daily budgets.
     */
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

    /**
     * Initializes a new instance of a Plan with the specified fields.
     * <p>
     *     This constructor was designed to create a Plan object from a database record.
     * </p>
     * <p>
     *     Note: The number of daily budgets must be 7. (A week has 7 days)
     * </p>
     * @param id The ID of the Plan.
     * @param name The name of the Plan.
     * @param annualIncome The annual income.
     * @param annualExpenses The annual expenses.
     * @param annualSavings The annual savings goal.
     * @param annualBudget The calculated annual budget.
     * @param weeklyBudget The allocated weekly budget.
     * @param dailyBudgets The allocated daily budgets.
     */
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

    /**
     * Initializes a new instance of a Plan that is a deep-copy of the Plan passed in.
     * @param other The Plan to copy.
     */
	public Plan(Plan other)
	{
		this.m_id = other.m_id;
		this.m_name = other.m_name;
		this.m_annualIncome = other.m_annualIncome;
		this.m_annualExpenses = other.m_annualExpenses;
		this.m_annualSavings = other.m_annualSavings;
		this.m_annualBudget = other.m_annualBudget;
		this.m_monthlyBudget = new PeriodicBudget(other.m_monthlyBudget);
		this.m_weeklyBudget = new PeriodicBudget(other.m_weeklyBudget);
		System.arraycopy(other.m_dailyBudgets, 0, this.m_dailyBudgets, 0, m_dailyBudgets.length);
	}

	//endregion

	//region Getters & Setters

    /**
     * Gets the ID of this Plan.
     * @return The ID of this Plan.
     */
	public long getId()
    {
        return this.m_id;
    }

    /**
     * Sets the ID of this Plan.
     * @param id The desired ID.
     */
	public void setId(long id)
    {
        this.m_id = id;
    }

    /**
     * Gets the name of this Plan.
     * @return The name of this Plan.
     */
	public String getName()
	{
		return this.m_name;
	}

    /**
     * Sets the name of this Plan.
     * @param name The desired name.
     */
	public void setName(String name)
	{
		this.m_name = name;
	}

    /**
     * Gets the annual income in this Plan.
     * @return The annual income in this Plan.
     */
	public long getAnnualIncome()
    {
        return this.m_annualIncome;
    }

    /**
     * Sets the annual income in this Plan.
     * @param annualIncome The desired annual income.
     */
	public void setAnnualIncome(long annualIncome)
    {
        m_annualIncome = annualIncome;
    }

    /**
     * Gets the annual expenses in this Plan.
     * @return The annual expenses in this Plan.
     */
	public long getAnnualExpenses()
    {
        return this.m_annualExpenses;
    }

    /**
     * Sets the annual expenses in this Plan.
     * @param annualExpenses The desired annual expenses.
     */
	public void setAnnualExpenses(long annualExpenses)
    {
        m_annualExpenses = annualExpenses;
    }

    /**
     * Gets the annual savings goal of this Plan.
     * @return The annual savings goal of this Plan.
     */
	public long getAnnualSavings()
    {
        return this.m_annualSavings;
    }

    /**
     * Sets the annual savings goal of this Plan.
     * @param annualSavings The desired annual savings goal.
     */
	public void setAnnualSavings(long annualSavings)
    {
        m_annualSavings = annualSavings;
    }

    /**
     * Gets the annual budget of this Plan.
     * @return The annual budget of this Plan.
     */
	public long getAnnualBudget()
    {
        return this.m_annualBudget;
    }

    /**
     * Sets the annual budget of this Plan.
     * @param annualBudget The desired annual budget.
     */
	public void setAnnualBudget(long annualBudget)
    {
        m_annualBudget = annualBudget;
    }

    /**
     * Gets the weekly budget of this Plan.
     * @return The weekly budget of this Plan.
     */
	public PeriodicBudget getWeeklyBudget()
    {
        return this.m_weeklyBudget;
    }

    /**
     * Gets the array of daily budgets in this Plan.
     * @return The array of daily budgets in this Plan.
     */
	public PeriodicBudget[] getDailyBudgets()
    {
        return this.m_dailyBudgets;
    }

    /**
     * Sets the array of daily budgets in the Plan.
     * @param budgets The array of daily budgets in the Plan.
     */
    public void setDailyBudgets(PeriodicBudget[] budgets) { m_dailyBudgets = budgets; }

    /**
     * Gets the daily budget on the specified day of week.
     * @param day The 1-indexed integer representation of the day of week.
     * @return The PeriodicBudget for the specified day of week.
     */
	public PeriodicBudget getDailyBudgetOn(int day) {
		if (day > 7)
			return null;
		else
		{
			return m_dailyBudgets[day - 1];
		}
	}

	//endregion

	//region Public Methods

    /**
     * Spends the specified amount on the budget for the specified day of week.
     * @param amount The amount to spend.
     * @param dayOfWeek The day of the week.
     */
	public void spend(long amount, int dayOfWeek)
	{
		PeriodicBudget dailyBudget = m_dailyBudgets[dayOfWeek - 1];

		dailyBudget.spend(amount);
		m_weeklyBudget.spend(amount);
		//m_monthlyBudget.spend(amount); //TODO: Implement monthly budgets
	}

    /**
     * Updates the days passed for the budgets in this plan.
     * Return value indicates whether the budgets were reset or not.
     * @param curr The current date.
     * @return True if the budgets were reset, false otherwise.
     */
	public boolean updateBudgets(Date curr)
	{
		if (m_weeklyBudget.update(curr))
		{
			for (PeriodicBudget dailyBudget : m_dailyBudgets)
				dailyBudget.reset();

			return true;
		}

		return false;
	}

    /**
     * Converts the content of this object to a human-readable string.
     * @return A human-readable string of the contents of this object.
     */
	@Override
	public String toString()
	{
		return ("Plan Name:\t" + this.m_name + "\nAnnual Budget:\t" + Double.toString(this.m_annualBudget) + "\n-------");
	}

	//endregion

	//region Helper Methods

    /**
     * Initializes a default plan on initial construction.
     */
	private void initializeDefaultPlan()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		//int numDaysThisYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);

		long monthlyBudget = Math.round(m_annualBudget / 12.00);
		long actualWeeklyBudget = Math.round(m_annualBudget / 52.00);
		//long dailyBudget = m_annualBudget / numDaysThisYear;
		long dailyBudget = Math.round(actualWeeklyBudget / 7.00);
		long weeklyBudget = dailyBudget * 7;					//TODO: This just makes it look like the math is correct.. (still some rounding error wrt annual amounts)
		m_annualSavings += actualWeeklyBudget - weeklyBudget;	//TODO: This makes the math correct-ish (just add money lost from rounding to savings)
																//TODO: Deal with the leftover days in a year (the days at start/end of year)

		//PeriodicBudget monthly = new PeriodicBudget()
		m_weeklyBudget = new PeriodicBudget(7, weeklyBudget, "weekly");
		m_dailyBudgets = new PeriodicBudget[7];

		for (int i = 0; i < m_dailyBudgets.length; i++)
			m_dailyBudgets [i] = new PeriodicBudget(1, dailyBudget, indexToDayOfWeek(i + 1));
	}

    /**
     * Maps 1-indexed integers to their corresponding day of the week string names.
     * @param index The 1-indexed day offset of a week.
     * @return The name of the day.
     */
	private String indexToDayOfWeek(int index)
	{
		switch (index)
		{
			case 1:
				return DAYOFWEEK_SUNDAY;
			case 2:
				return DAYOFWEEK_MONDAY;
			case 3:
				return DAYOFWEEK_TUESDAY;
			case 4:
				return DAYOFWEEK_WEDNESDAY;
			case 5:
				return DAYOFWEEK_THURSDAY;
			case 6:
				return DAYOFWEEK_FRIDAY;
			case 7:
				return DAYOFWEEK_SATURDAY;
			default:
				return "";
		}

	}

	//endregion

}
