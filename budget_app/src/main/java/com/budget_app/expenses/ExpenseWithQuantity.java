package com.budget_app.expenses;

import com.budget_app.expenses.Expense;
import com.budget_app.jt_interfaces.Compareable;

/**
 * Created by tolenti4-INS on 2018-01-28.
 */

public class ExpenseWithQuantity extends Expense {

    private int m_quantity;

    public ExpenseWithQuantity()
    {
        super();
        m_quantity = 0;
    }

    public ExpenseWithQuantity(int quantity)
    {
        super();
        m_quantity = quantity;
    }

    public ExpenseWithQuantity(Expense other, int quantity) {
        super(other);
        m_quantity = quantity;
    }

    public ExpenseWithQuantity(String name, long price, String category, String description, int quantity) {
        super(name, price, category, description);
        m_quantity = quantity;
    }

    public ExpenseWithQuantity(String name, long price, int quantity) {
        super(name, price);
        m_quantity = quantity;
    }

    public ExpenseWithQuantity(String name, int quantity) {
        super(name);
        m_quantity = quantity;
    }

    public ExpenseWithQuantity(long id, String name, long price, String category, String description, int quantity) {
        super(id, name, price, category, description);
        m_quantity = quantity;
    }

    public int getQuantity()
    {
        return m_quantity;
    }

    public void setQuantity(int quantity)
    {
        m_quantity = quantity;
    }

    public Expense getExpense() { return this; }
}
