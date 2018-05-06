package expenses;

//TODO: Get rid of this entire class

/**
 * A class that models an Expense that is stored in an ExpenseGroup.
 */
public class ExpenseInGroup extends Expense {

    //region Members

    private int m_quantity;

    //endregion

    //region Constructor

    public ExpenseInGroup()
    {
        super();
        m_quantity = 0;
    }

    public ExpenseInGroup(int quantity)
    {
        super();
        m_quantity = quantity;
    }

    public ExpenseInGroup(Expense other, int quantity) {
        super(other);
        m_quantity = quantity;
    }

    public ExpenseInGroup(String name, long price, String category, String description, int quantity) {
        super(name, price, category, description);
        m_quantity = quantity;
    }

    public ExpenseInGroup(String name, long price, int quantity) {
        super(name, price);
        m_quantity = quantity;
    }

    public ExpenseInGroup(long id, String name, long price, String category, String description, int quantity) {
        super(id, name, price, category, description);
        m_quantity = quantity;
    }

    //endregion

    //region Getters & Setters

    public int getQuantity()
    {
        return m_quantity;
    }

    public void setQuantity(int quantity)
    {
        m_quantity = quantity;
    }

    public Expense getExpense() { return this; }

    //endregion

}
