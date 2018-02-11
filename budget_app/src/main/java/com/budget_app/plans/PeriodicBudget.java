package com.budget_app.plans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tolenti4-INS on 2018-02-03.
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

    public PeriodicBudget(int days, long budget, String name)
    {
        m_totalDays = days;
        m_totalBudget = budget;
        m_name = name;
        reset();
    }

    public PeriodicBudget(PeriodicBudget other)
    {
        m_totalDays = other.m_totalDays;
        m_totalBudget = other.m_totalBudget;
        m_name = other.m_name;
        reset();
    }

    //endregion

    //region Getters

    public long getId() { return this.m_id; }
    public void setId(long id) { this.m_id = id; }

    public int getTotalDays() { return m_totalDays; }

    public long getTotalBudget() { return m_totalBudget; }
    public void setTotalBudget(long totalBudget)
    {
        long difference = totalBudget - m_totalBudget;
        m_totalBudget = totalBudget;
        m_currentBudget += difference;
    }

    public long getCurrentBudget() { return m_currentBudget; }

    public int getDaysPassed() { return m_daysPassed; }

    public long getAmountSpent() { return m_spent; }

    public long getRemainingAmount() { return m_totalBudget - m_spent; }

    public Date getDateLastChecked() { return m_dateLastChecked; }

    public String getName() { return m_name; }

    //endregion

    //region Public Methods

    public boolean spend(long amount) {
        long difference = m_currentBudget - amount;

        if (difference >= 0) {
            m_currentBudget = difference;
            m_spent += amount;
            return true;
        }

        return false;
    }

    public void increaseBudget(long amount) {

        if (m_spent <= amount) {
            m_spent -= amount;
        } else {
            m_currentBudget += m_spent - amount;
            m_spent = 0;
        }

    }

    //returns true if budget was reset
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

    public void reset() {
        m_daysPassed = 0;
        m_currentBudget = m_totalBudget;
        m_spent = 0;
        m_dateLastChecked = new Date();
    }

    //endregion

    //region Helper Methods

    private int convertDateToDayOfWeek(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    //endregion

}
