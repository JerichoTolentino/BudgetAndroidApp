package plans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Class that represents the current state of a budget allocated for a specified period of time in days.
 */
public class PeriodicBudget implements Serializable
{

    //region Members

    private final int m_totalDays;

    private long m_totalBudget;
    private long m_id;
    private String m_name;
    private int m_daysPassed;
    private long m_currentBudget;
    private long m_spent;
    private Date m_dateLastChecked;

    //endregion

    //region Constructor

    /**
     * Initializes a new instance of a PeriodicBudget with the specified fields.
     * @param id The ID of the PeriodicBudget.
     * @param days The total number of days the budget is allocated for.
     * @param budget The total amount budgeted.
     * @param name The name of the PeriodicBudget.
     * @param daysPassed The number of days that have passed since the start of the budget period.
     * @param currentBudget The amount currently available in the budget.
     * @param spent The amount spent in the budget.
     * @param dateLastChecked The date the budget was last checked.
     */
    public PeriodicBudget(long id, int days, long budget, String name, int daysPassed, int currentBudget, int spent, Date dateLastChecked)
    {
        m_id = id;
        m_totalDays = days;
        m_totalBudget = budget;
        m_name = name;
        m_daysPassed = daysPassed;
        m_currentBudget = currentBudget;
        m_spent = spent;
        m_dateLastChecked = dateLastChecked;
    }

    /**
     * Initializes a new instance of a PeriodicBudget with the specified fields.
     * @param days The total number of days the budget is allocated for.
     * @param budget The total amount budgeted.
     * @param name The name of the PeriodicBudget.
     */
    public PeriodicBudget(int days, long budget, String name)
    {
        m_totalDays = days;
        m_totalBudget = budget;
        m_name = name;
        reset();
    }

    /**
     * Initializes a new instance of a PeriodicBudget that is a deep-copy of the PeriodicBudget passed in.
     * @param other The PeriodicBudget to copy.
     */
    public PeriodicBudget(PeriodicBudget other)
    {
        m_id = other.m_id;
        m_totalDays = other.m_totalDays;
        m_totalBudget = other.m_totalBudget;
        m_name = other.m_name;
        m_daysPassed = other.m_daysPassed;
        m_currentBudget = other.m_currentBudget;
        m_spent = other.m_spent;
        m_dateLastChecked = new Date(other.m_dateLastChecked.getTime());
    }

    //endregion

    //region Getters

    /**
     * Gets the ID of this PeriodicBudget.
     * @return The ID of this PeriodicBudget.
     */
    public long getId()
    {
        return this.m_id;
    }

    /**
     * Sets the ID of this PeriodicBudget.
     * @param id The desired ID.
     */
    public void setId(long id)
    {
        this.m_id = id;
    }

    /**
     * Gets the total number of days this PeriodicBudget is allocated for.
     * @return The total number of days this PeriodicBudget is allocated for.
     */
    public int getTotalDays()
    {
        return m_totalDays;
    }

    /**
     * Gets the total amount budgeted.
     * @return The total amount budgeted.
     */
    public long getTotalBudget()
    {
        return m_totalBudget;
    }

    /**
     * Sets the total amount budgeted.
     * @param totalBudget The desired amount.
     */
    public void setTotalBudget(long totalBudget)
    {
        long difference = totalBudget - m_totalBudget;
        m_totalBudget = totalBudget;
        m_currentBudget += difference;
    }

    /**
     * Gets the currently available budget.
     * @return The amount currently available in the budget.
     */
    public long getCurrentBudget()
    {
        return m_currentBudget;
    }

    /**
     * Gets the number of days that have passed since the beginning of the period.
     * @return The number of days that have passed since the beginning of the period.
     */
    public int getDaysPassed()
    {
        return m_daysPassed;
    }

    /**
     * Gets the amount that has been spent in this budget.
     * @return The amount that has been spent in this budget.
     */
    public long getAmountSpent()
    {
        return m_spent;
    }

    /**
     * Gets the date this budget was last checked.
     * <p>
     *     A PeriodicBudget is reset whenever the days passed exceeds the total number of days
     *     allocated in the period. This date represents the last date this object was checked for
     *     this case.
     * </p>
     * @return The date this budget was last checked.
     */
    public Date getDateLastChecked()
    {
        return m_dateLastChecked;
    }

    /**
     * Gets the name of this PeriodicBudget.
     * @return The name of this PeriodicBudget.
     */
    public String getName() { return m_name; }

    //endregion

    //region Public Methods

    /**
     * Spends the specified amount in this budget.
     * @param amount The amount to spend.
     */
    public void spend(long amount) {

        long difference = m_currentBudget - amount;
        m_currentBudget = difference;
        m_spent += amount;
    }

    /**
     * Increases this budget by the specified amount.
     * @param amount The amount to increase the budget by.
     */
    public void increaseBudget(long amount) {

        if (m_spent <= amount) {
            m_spent -= amount;
        } else {
            m_currentBudget += m_spent - amount;
            m_spent = 0;
        }

    }

    /**
     * Updates the number of days passed for this budget's period.
     * Return value indicates whether the budget was reset or not.
     * @param curr The current date to compare the last date checked with.
     * @return True if the budget was reset, otherwise false.
     */
    public boolean update(Date curr) {
        int startDate = convertDateToDayOfWeek(m_dateLastChecked);
        int endDate = convertDateToDayOfWeek(curr);

        m_daysPassed += endDate - startDate;
        m_dateLastChecked = curr;

        if (m_daysPassed >= m_totalDays) {
            reset();
            return true;
        }

        return false;
    }

    /**
     * Resets this budget to its initial state with full available funds and no days passed.
     */
    public void reset() {
        m_daysPassed = 0;
        m_currentBudget = m_totalBudget;
        m_spent = 0;
        m_dateLastChecked = new Date();
    }

    //endregion

    //region Helper Methods

    /**
     * Converts a date to the integer representation of the day of the week.
     * @param date The date to convert.
     * @return The integer representation of the day of the week.
     */
    private int convertDateToDayOfWeek(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    //endregion

}
