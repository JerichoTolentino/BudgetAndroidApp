package jericho.budgetapp.Persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import jericho.budgetapp.Model.Expense;
import jericho.budgetapp.Model.ExpenseGroup;
import jericho.budgetapp.Model.Purchase;
import jericho.budgetapp.Model.Priceable;
import jericho.budgetapp.Model.PeriodicBudget;
import jericho.budgetapp.Model.Plan;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import jericho.budgetapp.BuildConfig;
import utilities.KeyValuePair;

public class DBHandler extends SQLiteOpenHelper
{

    //region Constants

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BudgetAppData.db";

    //Expense Table Info
    public static final String EXPENSE_TABLE = "tblExpenses";
    public static final String EXPENSE_COL_ID = "ID";
    public static final String EXPENSE_COL_NAME = "Name";
    public static final String EXPENSE_COL_PRICE = "Price";
    public static final String EXPENSE_COL_CATEGORY = "Category";
    public static final String EXPENSE_COL_DESCRIPTION = "Description";

    //ExpenseGroup Table Info
    public static final String EXPENSEGROUP_TABLE = "tblExpenseGroups";
    public static final String EXPENSEGROUP_COL_ID = "ID";
    public static final String EXPENSEGROUP_COL_NAME = "Name";
    public static final String EXPENSEGROUP_COL_PRICE = "Price";
    public static final String EXPENSEGROUP_COL_CATEGORY = "Category";
    public static final String EXPENSEGROUP_COL_DESCRIPTION = "Description";
    //NOTE: Store list of expenses in a bridge table (relate EXPENSEGROUP_COL_ID to EXPENSE_COL_ID)

    //Expense-ExpenseGroup Bridge Table
    public static final String EXPENSESINGROUP_TABLE = "tblExpensesInGroup";
    public static final String EXPENSESINGROUP_COL_ID = "ID";
    public static final String EXPENSESINGROUP_COL_EXPENSEGROUPID = "ExpenseGroupID";
    public static final String EXPENSESINGROUP_COL_EXPENSEID = "ExpenseID";
    public static final String EXPENSESINGROUP_COL_QUANTITY = "Quantity";

    //Plans Table Info
    public static final String PLANS_TABLE = "tblPlans";
    public static final String PLANS_COL_ID = "ID";
    public static final String PLANS_COL_ANNUALINCOME = "AnnualIncome";
    public static final String PLANS_COL_ANNUALEXPENSES = "AnnualExpenses";
    public static final String PLANS_COL_ANNUALSAVINGS = "AnnualSavings";
    public static final String PLANS_COL_NAME = "Name";
    public static final String PLANS_COL_ANNUALBUDGET = "AnnualBudget";
    public static final String PLANS_COL_MONTHLYBUDGETID = "MonthlyBudgetID";
    public static final String PLANS_COL_WEEKLYBUDGETID = "WeeklyBudgetID";
    //public static final String PLANS_COL_DAILYBUDGETSID = "DailyBudgetsID";
    // Linked to PeriodicBudgets table via DailyBudgetsInPlan bridge-table

    //Period Budgets Table info
    public static final String PERIODICBUDGETS_TABLE = "tblPeriodicBudgets";
    public static final String PERIODICBUDGETS_COL_ID = "ID";
    public static final String PERIODICBUDGETS_COL_NAME = "Name";
    public static final String PERIODICBUDGETS_COL_TOTALDAYS = "TotalDays";
    public static final String PERIODICBUDGETS_COL_TOTALBUDGET = "TotalBudget";
    public static final String PERIODICBUDGETS_COL_DAYSPASSED = "DaysPassed";
    public static final String PERIODICBUDGETS_COL_CURRENTBUDGET = "CurrentBudget";
    public static final String PERIODICBUDGETS_COL_SPENT = "Spent";
    public static final String PERIODICBUDGETS_COL_DATELASTCHECKED = "DateLastChecked";

    //Daily Budgets In Plan Bridge-Table Info
    public static final String DAILYBUDGETSINPLAN_TABLE = "tblDailyBudgetsInPlan";
    public static final String DAILYBUDGETSINPLAN_COL_ID = "ID";
    public static final String DAILYBUDGETSINPLAN_COL_PLANID = "PlanID";
    public static final String DAILYBUDGETSINPLAN_COL_PERIODICBUDGETID = "PeriodicBudgetID";

    //PurchaseHistory Table Info
    public static final String PURCHASES_TABLE = "tblPurchases";
    public static final String PURCHASES_COL_ID = "ID";
    public static final String PURCHASES_COL_ITEMTYPE = "ItemType"; //expense OR expense group
    //public static final String PURCHASES_COL_ITEMID = "ItemID";
    public static final String PURCHASES_COL_ITEMNAME = "ItemName";
    public static final String PURCHASES_COL_ITEMPRICE = "ItemPrice";
    public static final String PURCHASES_COL_DATE = "Date";
    public static final String PURCHASES_COL_QUANTITY = "Quantity";

    //endregion

    //region Members

    DatabaseWrapper m_dbWrapper;

    //endregion

    /**
     * Initializes a new instance of a DBHandler with the specified fields.
     * @param context The context.
     * @param name The name of the database. (Unused in constructor)
     * @param factory The CursorFactory.
     * @param version The version of the database. (Unused in constructor)
     * @see SQLiteOpenHelper#SQLiteOpenHelper(Context, String, SQLiteDatabase.CursorFactory, int)
     */
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

        m_dbWrapper = new DatabaseWrapper();
    }

    //region Event Handlers

    /**
     * Creates and executes the SQL to create all the tables in the database.
     * @param sqLiteDatabase
     * @see SQLiteOpenHelper#onCreate(SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //Create Expense Table
        String sql = "CREATE TABLE '" + EXPENSE_TABLE + "'('"
                + EXPENSE_COL_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '"
                + EXPENSE_COL_NAME + "' TEXT NOT NULL, '"
                + EXPENSE_COL_PRICE + "' INTEGER NOT NULL, '"
                + EXPENSE_COL_CATEGORY + "' TEXT, '"
                + EXPENSE_COL_DESCRIPTION + "' TEXT"
                + ");";

        sqLiteDatabase.execSQL(sql);

        //Create ExpenseGroup Table
        sql = "CREATE TABLE '" + EXPENSEGROUP_TABLE + "'('"
                + EXPENSEGROUP_COL_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '"
                + EXPENSEGROUP_COL_NAME + "' TEXT NOT NULL, '"
                + EXPENSEGROUP_COL_PRICE + "' INTEGER NOT NULL, '"
                + EXPENSEGROUP_COL_CATEGORY + "' TEXT, '"
                + EXPENSEGROUP_COL_DESCRIPTION + "' TEXT"
                + ");";

        sqLiteDatabase.execSQL(sql);

        //Create Expense-ExpenseGroup Bridge Table
        sql = "CREATE TABLE '" + EXPENSESINGROUP_TABLE + "'('"
                + EXPENSESINGROUP_COL_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '"
                + EXPENSESINGROUP_COL_EXPENSEGROUPID + "' INTEGER NOT NULL, '"
                + EXPENSESINGROUP_COL_EXPENSEID + "' INTEGER NOT NULL, '"
                + EXPENSESINGROUP_COL_QUANTITY + "' INTEGER NOT NULL"
                + ");";

        sqLiteDatabase.execSQL(sql);

        //Create Purchases Table
        sql = "CREATE TABLE '" + PURCHASES_TABLE + "'('"
                + PURCHASES_COL_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '"
                //+ PURCHASES_COL_ITEMID + "' INTEGER NOT NULL, '"
                + PURCHASES_COL_ITEMNAME + "' TEXT NOT NULL, '"
                + PURCHASES_COL_ITEMPRICE + "' INTEGER NOT NULL, '"
                + PURCHASES_COL_ITEMTYPE + "' TEXT NOT NULL, '"
                + PURCHASES_COL_DATE + "' TEXT NOT NULL, '"
                + PURCHASES_COL_QUANTITY + "' INTEGER NOT NULL"
                + ");";

        sqLiteDatabase.execSQL(sql);

        //Create Plans Table
        sql = "CREATE TABLE '" + PLANS_TABLE + "'('"
                + PLANS_COL_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '"
                + PLANS_COL_ANNUALINCOME + "' INTEGER NOT NULL, '"
                + PLANS_COL_ANNUALEXPENSES + "' INTEGER NOT NULL, '"
                + PLANS_COL_ANNUALSAVINGS + "' INTEGER NOT NULL, '"
                + PLANS_COL_NAME + "' TEXT NOT NULL, '"
                + PLANS_COL_ANNUALBUDGET + "' INTEGER NOT NULL, '"
                + PLANS_COL_MONTHLYBUDGETID + "' INTEGER NOT NULL, '"
                + PLANS_COL_WEEKLYBUDGETID + "' INTEGER NOT NULL"//, '"
                //+ PLANS_COL_DAILYBUDGETSID + "' INTEGER NOT NULL"
                + ");";

        sqLiteDatabase.execSQL(sql);

        //Create PeriodicBudgets Table
        sql = "CREATE TABLE '" + PERIODICBUDGETS_TABLE + "'('"
                + PERIODICBUDGETS_COL_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '"
                + PERIODICBUDGETS_COL_NAME + "' TEXT NOT NULL, '"
                + PERIODICBUDGETS_COL_TOTALDAYS + "' INTEGER NOT NULL, '"
                + PERIODICBUDGETS_COL_TOTALBUDGET + "' INTEGER NOT NULL, '"
                + PERIODICBUDGETS_COL_DAYSPASSED + "' INTEGER NOT NULL, '"
                + PERIODICBUDGETS_COL_CURRENTBUDGET + "' INTEGER NOT NULL, '"
                + PERIODICBUDGETS_COL_SPENT + "' INTEGER NOT NULL, '"
                + PERIODICBUDGETS_COL_DATELASTCHECKED + "' TEXT NOT NULL"
                + ");";

        sqLiteDatabase.execSQL(sql);

        //Create Daily Budgets In Plan Bridge-Table
        sql = "CREATE TABLE '" + DAILYBUDGETSINPLAN_TABLE + "'('"
                + DAILYBUDGETSINPLAN_COL_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '"
                + DAILYBUDGETSINPLAN_COL_PLANID + "' INTEGER NOT NULL, '"
                + DAILYBUDGETSINPLAN_COL_PERIODICBUDGETID + "' INTEGER NOT NULL"
                + ");";

        sqLiteDatabase.execSQL(sql);

    }

    /**
     * Recreates all tables in the database.
     * @param sqLiteDatabase The database object
     * @param i The original version.
     * @param i1 The new version.
     * @see SQLiteOpenHelper#onUpgrade(SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + EXPENSE_TABLE + "';");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + EXPENSEGROUP_TABLE + "';");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + EXPENSESINGROUP_TABLE + "';");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + PURCHASES_TABLE + "';");
        this.onCreate(sqLiteDatabase);
    }

    //endregion

    //region Public Methods

    //region Query Methods

    /**
     * Executes a query on the Expense table with the specified where clause.
     * @param where The where clause. If null, returns all items in the table.
     * @return The list of Expenses that match the where predicate.
     */
    public ArrayList<Expense> queryExpenses(String where)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ArrayList<Expense> list = new ArrayList<>();
        String id;
        String name;
        String price;
        String category;
        String description;

        if (where == null || where.equals(""))
            where = "1";

        Cursor c = db.rawQuery("SELECT * FROM " + EXPENSE_TABLE + " WHERE " + where + ";", null);

        c.moveToFirst();

        while(!c.isAfterLast())
        {
            id = c.getString(c.getColumnIndex(EXPENSE_COL_ID));
            name = c.getString(c.getColumnIndex((EXPENSE_COL_NAME)));
            price = c.getString(c.getColumnIndex((EXPENSE_COL_PRICE)));
            category = c.getString(c.getColumnIndex((EXPENSE_COL_CATEGORY)));
            description = c.getString(c.getColumnIndex((EXPENSE_COL_DESCRIPTION)));

            list.add(new Expense(Long.parseLong(id), name, Long.parseLong(price), category, description));
            c.moveToNext();
        }

        c.close();
        m_dbWrapper.CloseDatabase();

        return list;
    }

    /**
     * Executes a query on the ExpenseGroup table with the specified where clause.
     * @param where The where clause. If null, returns all items in the table.
     * @return The list of ExpenseGroups that match the where predicate.
     */
    public ArrayList<ExpenseGroup> queryExpenseGroups(String where)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ArrayList<ExpenseGroup> list = new ArrayList<>();
        String id;
        String name;
        String price;
        String category;
        String description;

        long expenseGroupID;
        ArrayList<KeyValuePair<Expense, Integer>> expenses;

        if (where == null || where.equals(""))
            where = "1";

        Cursor c = db.rawQuery("SELECT * FROM " + EXPENSEGROUP_TABLE + " WHERE " + where + ";", null);

        c.moveToFirst();

        while(!c.isAfterLast())
        {
            id = c.getString(c.getColumnIndex(EXPENSEGROUP_COL_ID));
            name = c.getString(c.getColumnIndex(EXPENSEGROUP_COL_NAME));
            price = c.getString(c.getColumnIndex(EXPENSEGROUP_COL_PRICE));
            category = c.getString(c.getColumnIndex(EXPENSEGROUP_COL_CATEGORY));
            description = c.getString(c.getColumnIndex(EXPENSEGROUP_COL_DESCRIPTION));

            expenseGroupID = Long.parseLong(c.getString(c.getColumnIndex(EXPENSEGROUP_COL_ID)));
            expenses = getExpensesInGroup(db, expenseGroupID);

            list.add(new ExpenseGroup(Long.parseLong(id), name, Long.parseLong(price), category, description, expenses));
            c.moveToNext();
        }

        c.close();
        m_dbWrapper.CloseDatabase();

        return list;
    }

    /**
     * Executes a query on the Purchase table with the specified where clause.
     * @param where The where clause. If null, returns all items in the table.
     * @return The list of Purchases that match the where predicate.
     * @throws ParseException Thrown if data from the database fails to be parsed to its corresponding data type.
     */
    public ArrayList<Purchase> queryPurchases(String where) throws ParseException
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ArrayList<Purchase> list = new ArrayList<>();
        String id;
        String itemType;
        String itemName;
        String itemPrice;
        String date;
        String quantity;

        String itemID;
        Priceable item = null;

        if (where == null || where.equals(""))
            where = "1";

        Cursor c = db.rawQuery("SELECT * FROM " + PURCHASES_TABLE + " WHERE " + where + ";", null);

        c.moveToFirst();

        while(!c.isAfterLast())
        {
            id = c.getString(c.getColumnIndex(PURCHASES_COL_ID));
            itemType = c.getString(c.getColumnIndex(PURCHASES_COL_ITEMTYPE));
            itemName = c.getString(c.getColumnIndex(PURCHASES_COL_ITEMNAME));
            itemPrice = c.getString(c.getColumnIndex(PURCHASES_COL_ITEMPRICE));
            date = c.getString(c.getColumnIndex(PURCHASES_COL_DATE));
            quantity = c.getString(c.getColumnIndex(PURCHASES_COL_QUANTITY));

            if (itemType.equals(Expense.class.getName()))
                item = new Expense(itemName, Long.parseLong(itemPrice), "Dummy", "Dummy expense for purchase history");
            else if (itemType.equals(ExpenseGroup.class.getName()))
                item = new ExpenseGroup(itemName, Long.parseLong(itemPrice), "Dummy", "Dummy expense group for purchase history");

            Date parsedDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse(date);

            list.add(new Purchase(Long.parseLong(id), item, Integer.parseInt(quantity), parsedDate));
            c.moveToNext();
        }

        c.close();
        m_dbWrapper.CloseDatabase();

        return list;
    }

    /**
     * Executes a query on the Plans table with the specified where clause.
     * @param where The where clause. If null, returns all items in the table.
     * @return The list of Plans that match the where predicate.
     * @throws ParseException Thrown if data from the database fails to be parsed to its corresponding data type.
     */
    public ArrayList<Plan> queryPlans(String where) throws ParseException
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ArrayList<Plan> list = new ArrayList<>();
        String id;
        String annualIncome;
        String annualExpenses;
        String annualSavings;
        String name;
        String annualBudget;
        String monthlyBudgetID;
        String weeklyBudgetID;

        if (where == null || where.equals(""))
            where = "1";

        Cursor c = db.rawQuery("SELECT * FROM " + PLANS_TABLE + " WHERE " + where + ";", null);

        c.moveToFirst();

        while(!c.isAfterLast())
        {
            id = c.getString(c.getColumnIndex(PLANS_COL_ID));
            annualIncome = c.getString(c.getColumnIndex(PLANS_COL_ANNUALINCOME));
            annualExpenses = c.getString(c.getColumnIndex(PLANS_COL_ANNUALEXPENSES));
            annualSavings = c.getString(c.getColumnIndex(PLANS_COL_ANNUALSAVINGS));
            name = c.getString(c.getColumnIndex(PLANS_COL_NAME));
            annualBudget = c.getString(c.getColumnIndex(PLANS_COL_ANNUALBUDGET));
            monthlyBudgetID = c.getString(c.getColumnIndex(PLANS_COL_MONTHLYBUDGETID));
            weeklyBudgetID = c.getString(c.getColumnIndex(PLANS_COL_WEEKLYBUDGETID));
            //dailyBudgetsID = c.getString(c.getColumnIndex(PLANS_COL_DAILYBUDGETSID));

            // query weekly budget
            PeriodicBudget weeklyBudget = queryPeriodicBudgets(PERIODICBUDGETS_COL_ID + "=" + weeklyBudgetID).get(0);

            // query bridge-table
            ArrayList<DailyBudgetInPlan> bridgeIDs = queryDailyBudgetsInPlanTable(DAILYBUDGETSINPLAN_COL_PLANID + "=" + id);
            PeriodicBudget[] dailyBudgets = new PeriodicBudget[bridgeIDs.size()];

            if (BuildConfig.DEBUG && bridgeIDs.size() != 7) throw new AssertionError();

            // build daily budgets via bridge-table query results
            for (int i = 0; i < bridgeIDs.size(); i++) {
                long periodicBudgetID = bridgeIDs.get(i).getPeriodicBudgetID();
                PeriodicBudget dailyBudget = queryPeriodicBudgets(PERIODICBUDGETS_COL_ID + "=" + String.valueOf(periodicBudgetID)).get(0);
                dailyBudgets[i] = dailyBudget;
            }

            Plan plan = new Plan(Long.parseLong(id),
                                 name,
                                 Long.parseLong(annualIncome),
                                 Long.parseLong(annualExpenses),
                                 Long.parseLong(annualSavings),
                                 Long.parseLong(annualBudget),
                                 weeklyBudget,
                                 dailyBudgets);

            list.add(plan);
            c.moveToNext();
        }

        c.close();
        m_dbWrapper.CloseDatabase();

        return list;
    }

    /**
     * Executes a query on the DailyBudgetsInPlan table with the specified where clause.
     * @param where The where clause. If null, returns all items in the table.
     * @return The list of DailyBudgetInPlan objects that match the where predicate.
     */
    public ArrayList<DailyBudgetInPlan> queryDailyBudgetsInPlanTable(String where)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ArrayList<DailyBudgetInPlan> list = new ArrayList<>();
        String id;
        String planID;
        String periodicBudgetID;

        if (where == null || where.equals(""))
            where = "1";

        Cursor c = db.rawQuery("SELECT * FROM " + DAILYBUDGETSINPLAN_TABLE + " WHERE " + where + ";", null);

        c.moveToFirst();

        while(!c.isAfterLast())
        {
            id = c.getString(c.getColumnIndex(DAILYBUDGETSINPLAN_COL_ID));
            planID = c.getString(c.getColumnIndex(DAILYBUDGETSINPLAN_COL_PLANID));
            periodicBudgetID = c.getString(c.getColumnIndex(DAILYBUDGETSINPLAN_COL_PERIODICBUDGETID));

            DailyBudgetInPlan dailyBudgetInPlan = new DailyBudgetInPlan(Long.parseLong(id),
                                                                        Long.parseLong(planID),
                                                                        Long.parseLong(periodicBudgetID));
            list.add(dailyBudgetInPlan);
            c.moveToNext();
        }

        c.close();
        m_dbWrapper.CloseDatabase();

        return list;
    }

    /**
     * Executes a query on the PeriodicBudgets table with the specified where clause.
     * @param where The where clause. If null, returns all items in the table.
     * @return The list of PeriodicBudgets that match the predicate.
     * @throws ParseException Thrown if data from the database fails to be parsed to its corresponding data type.
     */
    public ArrayList<PeriodicBudget> queryPeriodicBudgets(String where) throws ParseException
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ArrayList<PeriodicBudget> list = new ArrayList<>();
        String id;
        String name;
        String totalDays;
        String totalBudget;
        String daysPassed;
        String currentBudget;
        String spent;
        String dateLastChecked;

        if (where == null || where.equals(""))
            where = "1";

        Cursor c = db.rawQuery("SELECT * FROM " + PERIODICBUDGETS_TABLE + " WHERE " + where + ";", null);

        c.moveToFirst();

        while(!c.isAfterLast())
        {
            id = c.getString(c.getColumnIndex(PERIODICBUDGETS_COL_ID));
            name = c.getString(c.getColumnIndex(PERIODICBUDGETS_COL_NAME));
            totalDays = c.getString(c.getColumnIndex(PERIODICBUDGETS_COL_TOTALDAYS));
            totalBudget = c.getString(c.getColumnIndex(PERIODICBUDGETS_COL_TOTALBUDGET));
            daysPassed = c.getString(c.getColumnIndex(PERIODICBUDGETS_COL_DAYSPASSED));
            currentBudget = c.getString(c.getColumnIndex(PERIODICBUDGETS_COL_CURRENTBUDGET));
            spent = c.getString(c.getColumnIndex(PERIODICBUDGETS_COL_SPENT));
            dateLastChecked = c.getString(c.getColumnIndex(PERIODICBUDGETS_COL_DATELASTCHECKED));

            Date parsedDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse(dateLastChecked);

            PeriodicBudget periodicBudget = new PeriodicBudget(Long.parseLong(id),
                    Integer.parseInt(totalDays),
                    Long.parseLong(totalBudget),
                    name,
                    Integer.parseInt(daysPassed),
                    Integer.parseInt(currentBudget),
                    Integer.parseInt(spent),
                    parsedDate);


            list.add(periodicBudget);
            c.moveToNext();
        }

        c.close();
        m_dbWrapper.CloseDatabase();

        return list;
    }

    //endregion

    //region Expense Table Methods

    /**
     * Adds an Expense to the Expense table
     * @param expense The Expense to add.
     * @return True if addition was successful, false otherwise.
     */
    public boolean addExpense(Expense expense)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ContentValues values = new ContentValues();

        values.put(EXPENSE_COL_NAME, expense.getName());
        values.put(EXPENSE_COL_PRICE, expense.getPrice());
        values.put(EXPENSE_COL_CATEGORY, expense.getCategory());
        values.put(EXPENSE_COL_DESCRIPTION, expense.getDescription());

        final long insert = db.insert(EXPENSE_TABLE, null, values);
        m_dbWrapper.CloseDatabase();

        return (insert != -1);
    }

    /**
     * Updates an existing record of an Expense in the Expense table.
     * @param expense The Expense to update.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateExpense(Expense expense)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ContentValues values = new ContentValues();

        values.put(EXPENSE_COL_NAME, expense.getName());
        values.put(EXPENSE_COL_PRICE, expense.getPrice());
        values.put(EXPENSE_COL_CATEGORY, expense.getCategory());
        values.put(EXPENSE_COL_DESCRIPTION, expense.getDescription());

        final int update = db.update(EXPENSE_TABLE, values, EXPENSE_COL_ID + " = " + Long.toString(expense.getId()), null);

        m_dbWrapper.CloseDatabase();

        return (update == 1);
    }

    /**
     * Removes a record from the Expense table with the specified ID.
     * @param ID The ID of the record to remove.
     * @return True if the record was removed, false otherwise.
     */
    public boolean removeExpense(long ID)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        final int delete = db.delete(EXPENSE_TABLE, EXPENSE_COL_ID + " = " + Long.toString(ID), null);

        m_dbWrapper.CloseDatabase();

        return delete == 1;
    }

    //endregion

    //region ExpenseGroup Table Methods

    /**
     * Adds an existing Expense to an existing ExpenseGroup by creating a record in the Expense-ExpenseGroup bridge table with their IDs.
     * @param expenseID The ID of the Expense to add.
     * @param expenseGroupID The ID of the ExpenseGroup to add to.
     * @param quantity The number of Expenses to add.
     * @return True if the addition was successful, false otherwise.
     */
    public boolean addExpenseToGroup(long expenseID, long expenseGroupID, int quantity)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ContentValues values = new ContentValues();

        values.put(EXPENSESINGROUP_COL_EXPENSEGROUPID, expenseGroupID);
        values.put(EXPENSESINGROUP_COL_EXPENSEID, expenseID);
        values.put(EXPENSESINGROUP_COL_QUANTITY, quantity);

        final long insert = db.insert(EXPENSESINGROUP_TABLE, null, values);

        m_dbWrapper.CloseDatabase();

        return (insert != -1);
    }

    /**
     * Removes an existing Expense from an existing ExpenseGroup by removing the associating record in the Expense-ExpenseGroup bridge table.
     * @param expenseID The ID of the Expense to remove.
     * @param expenseGroupID The ID of the ExpenseGroup to remove from.
     * @return True if the removal was successful, false otherwise.
     */
    public boolean removeExpenseFromGroup(long expenseID, long expenseGroupID)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();

        final int delete = db.delete(EXPENSESINGROUP_TABLE, EXPENSESINGROUP_COL_EXPENSEGROUPID + " = " + Long.toString(expenseGroupID), null);

        m_dbWrapper.CloseDatabase();

        return delete == 1;
    }

    /**
     * Adds an ExpenseGroup to the ExpenseGroup table.
     * @param expenseGroup The ExpenseGroup to add.
     * @return True if the addition was successful, false otherwise.
     */
    public boolean addExpenseGroup(ExpenseGroup expenseGroup)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ContentValues values = new ContentValues();

        values.put(EXPENSEGROUP_COL_NAME, expenseGroup.getName());
        values.put(EXPENSEGROUP_COL_PRICE, expenseGroup.getPrice());
        values.put(EXPENSEGROUP_COL_CATEGORY, expenseGroup.getCategory());
        values.put(EXPENSEGROUP_COL_DESCRIPTION, expenseGroup.getDescription());

        final long expenseGroupID = db.insert(EXPENSEGROUP_TABLE, null, values);

        //add expenses to bridge table
        boolean addedExpenses = false;
        if(expenseGroupID != -1)
            addedExpenses = addExpensesInGroupToTable(db, expenseGroupID, expenseGroup.getExpenses());

        m_dbWrapper.CloseDatabase();

        return (expenseGroupID != -1 && addedExpenses);
    }

    /**
     * Removes the record with the specified ID from the ExpenseGroup table.
     * @param ID The ID of the record to remove.
     * @return True if the removal was successful, false otherwise.
     */
    public boolean removeExpenseGroup(long ID)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();

        final int deleteLinks = removeExpensesInGroupLinks(db, ID);
        final int deleteSrc = db.delete(EXPENSEGROUP_TABLE, EXPENSEGROUP_COL_ID + " = " + Long.toString(ID) + ";", null);

        m_dbWrapper.CloseDatabase();

        return (deleteLinks + deleteSrc > 0);
    }

    /**
     * Updates an existing ExpenseGroup in the ExpenseGroup table.
     * @param expenseGroup The ExpenseGroup to update.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateExpenseGroup(ExpenseGroup expenseGroup)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ContentValues values = new ContentValues();

        values.put(EXPENSEGROUP_COL_NAME, expenseGroup.getName());
        values.put(EXPENSEGROUP_COL_PRICE, expenseGroup.getPrice());
        values.put(EXPENSEGROUP_COL_CATEGORY, expenseGroup.getCategory());
        values.put(EXPENSEGROUP_COL_DESCRIPTION, expenseGroup.getDescription());

        final int update = db.update(EXPENSEGROUP_TABLE, values, EXPENSEGROUP_COL_ID + " = " + Long.toString(expenseGroup.getId()), null);

        removeExpensesInGroupLinks(db, expenseGroup.getId());
        addExpensesInGroupToTable(db, expenseGroup.getId(), expenseGroup.getExpenses());

        m_dbWrapper.CloseDatabase();

        return (update == 1);
    }

    //endregion

    //region Purchase Table Methods

    /**
     * Adds a Purchase to the Purchase table.
     * @param purchase The Purchase to add.
     * @return True if the addition was successful, false otherwise.
     */
    public boolean addPurchase(Purchase purchase)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ContentValues values = new ContentValues();

        ItemInfo itemInfo = findItemInfo(db, purchase.getItem());

        values.put(PURCHASES_COL_ITEMTYPE, itemInfo.getItemType());
        //values.put(PURCHASES_COL_ITEMID, itemInfo.getItemID());
        values.put(PURCHASES_COL_ITEMNAME, purchase.getItem().getName());
        values.put(PURCHASES_COL_ITEMPRICE, purchase.getItem().getPrice());
        values.put(PURCHASES_COL_QUANTITY, purchase.getQuantity());
        values.put(PURCHASES_COL_DATE, DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(purchase.getDate()));

        final long insert = db.insert(PURCHASES_TABLE, null, values);
        m_dbWrapper.CloseDatabase();

        return (insert != -1);
    }

    /**
     * Removes a record with the specified ID from the Purchase table.
     * @param ID The ID of the record to remove.
     * @return True if the removal was successful, false otherwise.
     */
    public boolean removePurchase(long ID)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();

        final int delete = db.delete(PURCHASES_TABLE, PURCHASES_COL_ID + " = " + Long.toString(ID) + ";", null);

        m_dbWrapper.CloseDatabase();

        return delete == 1;
    }

    //endregion

    //region Plan Table Methods

    /**
     * Adds a Plan to the Plans table.
     * @param plan The Plan to add.
     * @return True if the addition was successful, false otherwise.
     */
    public boolean addPlan(Plan plan)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ContentValues values = new ContentValues();

        long weeklyBudgetID = addPeriodicBudget(plan.getWeeklyBudget());

        values.put(PLANS_COL_ANNUALINCOME, plan.getAnnualIncome());
        values.put(PLANS_COL_ANNUALEXPENSES, plan.getAnnualExpenses());
        values.put(PLANS_COL_ANNUALSAVINGS, plan.getAnnualSavings());
        values.put(PLANS_COL_NAME, plan.getName());
        values.put(PLANS_COL_ANNUALBUDGET, plan.getAnnualBudget());
        values.put(PLANS_COL_MONTHLYBUDGETID, 0);
        //values.put(PLANS_COL_MONTHLYBUDGETID, plan.getMonthlyBudget().getId());
        values.put(PLANS_COL_WEEKLYBUDGETID, weeklyBudgetID);

        final long planID = db.insert(PLANS_TABLE, null, values);

        // add daily budgets and update bridge-table
        for (PeriodicBudget dailyBudget : plan.getDailyBudgets()) {
            long dailyBudgetID = addPeriodicBudget(dailyBudget);
            DailyBudgetInPlan dailyBudgetInPlan = new DailyBudgetInPlan(planID, dailyBudgetID);

            // add link to bridge-table
            addDailyBudgetInPlan(dailyBudgetInPlan);
        }

        m_dbWrapper.CloseDatabase();

        return (planID != -1);
    }

    /**
     * Removes a record with the specified ID from the Plans table.
     * @param ID The ID of the record to remove.
     * @return True if the removal was successful, false otherwise.
     * @throws ParseException
     */
    public boolean removePlan(long ID) throws ParseException
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();

        Plan plan = queryPlans(PLANS_COL_ID + "=" + Long.toString(ID) + ";").get(0);

        // remove links in bridge-table
        removeDailyBudgetsInPlan(plan.getId());

        // remove weekly and daily budgets from periodic budgets table
        removePeriodicBudget(plan.getWeeklyBudget().getId());
        for (PeriodicBudget dailyBudget: plan.getDailyBudgets())
            removePeriodicBudget(dailyBudget.getId());

        // remove plan from plans table
        final int delete = db.delete(PLANS_TABLE, PLANS_COL_ID + " = " + Long.toString(ID) + ";", null);

        m_dbWrapper.CloseDatabase();

        return delete == 1;
    }

    /**
     * Updates an existing Plan in the Plans table.
     * @param plan The Plan to update.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updatePlan(Plan plan)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ContentValues values = new ContentValues();

        values.put(PLANS_COL_ANNUALINCOME, plan.getAnnualIncome());
        values.put(PLANS_COL_ANNUALEXPENSES, plan.getAnnualExpenses());
        values.put(PLANS_COL_ANNUALSAVINGS, plan.getAnnualSavings());
        values.put(PLANS_COL_NAME, plan.getName());
        values.put(PLANS_COL_ANNUALBUDGET, plan.getAnnualBudget());
        values.put(PLANS_COL_MONTHLYBUDGETID, 0);
        //values.put(PLANS_COL_MONTHLYBUDGETID, plan.getMonthlyBudget().getId());

        updatePeriodicBudget(plan.getWeeklyBudget());
        for (PeriodicBudget p : plan.getDailyBudgets())
            updatePeriodicBudget(p);

        final int update = db.update(PLANS_TABLE, values, PLANS_COL_ID + " = " + Long.toString(plan.getId()) + ";", null);

        m_dbWrapper.CloseDatabase();

        return (update == 1);
    }

    //endregion

    //region Periodic Budget Table Methods

    /**
     * Adds a PeriodicBudget to the PeriodicBudgets table.
     * @param periodicBudget The PeriodicBudget to add.
     * @return True if the addition was successful, false otherwise.
     */
    public long addPeriodicBudget(PeriodicBudget periodicBudget)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ContentValues values = new ContentValues();

        values.put(PERIODICBUDGETS_COL_NAME, periodicBudget.getName());
        values.put(PERIODICBUDGETS_COL_CURRENTBUDGET, periodicBudget.getCurrentBudget());
        values.put(PERIODICBUDGETS_COL_DATELASTCHECKED, DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(periodicBudget.getDateLastChecked()));
        values.put(PERIODICBUDGETS_COL_DAYSPASSED, periodicBudget.getDaysPassed());
        values.put(PERIODICBUDGETS_COL_SPENT, periodicBudget.getAmountSpent());
        values.put(PERIODICBUDGETS_COL_TOTALBUDGET, periodicBudget.getTotalBudget());
        values.put(PERIODICBUDGETS_COL_TOTALDAYS, periodicBudget.getTotalDays());

        final long insert = db.insert(PERIODICBUDGETS_TABLE, null, values);
        m_dbWrapper.CloseDatabase();

        return insert;
    }

    /**
     * Removes a record with the specified ID from the PeriodicBudgets table.
     * @param ID The ID of the record to remove.
     * @return True if the removal was successful, false otherwise.
     */
    public boolean removePeriodicBudget(long ID)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();

        final int delete = db.delete(PERIODICBUDGETS_TABLE, PERIODICBUDGETS_COL_ID + " = " + Long.toString(ID) + ";", null);

        m_dbWrapper.CloseDatabase();

        return delete == 1;
    }

    /**
     * Updates an existing PeriodicBudget in the PeriodicBudgets table.
     * @param periodicBudget The PeriodicBudget to update.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updatePeriodicBudget(PeriodicBudget periodicBudget)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ContentValues values = new ContentValues();

        values.put(PERIODICBUDGETS_COL_NAME, periodicBudget.getName());
        values.put(PERIODICBUDGETS_COL_CURRENTBUDGET, periodicBudget.getCurrentBudget());
        values.put(PERIODICBUDGETS_COL_DATELASTCHECKED, DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(periodicBudget.getDateLastChecked()));
        values.put(PERIODICBUDGETS_COL_DAYSPASSED, periodicBudget.getDaysPassed());
        values.put(PERIODICBUDGETS_COL_SPENT, periodicBudget.getAmountSpent());
        values.put(PERIODICBUDGETS_COL_TOTALBUDGET, periodicBudget.getTotalBudget());
        values.put(PERIODICBUDGETS_COL_TOTALDAYS, periodicBudget.getTotalDays());

        final int update = db.update(PERIODICBUDGETS_TABLE, values, PERIODICBUDGETS_COL_ID + " = " + Long.toString(periodicBudget.getId()) + ";", null);
        m_dbWrapper.CloseDatabase();

        return update == 1;
    }

    //endregion

    //region Daily Budget In Plan Table Methods

    /**
     * Adds a DailyBudgetInPlan to the DailyBudgetsInPlan table.
     * @param dailyBudgetInPlan The DailyBudgetInPlan to add.
     * @return True if the addition was successful, false otherwise.
     */
    public boolean addDailyBudgetInPlan(DailyBudgetInPlan dailyBudgetInPlan)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ContentValues values = new ContentValues();

        values.put(DAILYBUDGETSINPLAN_COL_PERIODICBUDGETID, dailyBudgetInPlan.getPeriodicBudgetID());
        values.put(DAILYBUDGETSINPLAN_COL_PLANID, dailyBudgetInPlan.getPlanID());

        final long insert = db.insert(DAILYBUDGETSINPLAN_TABLE, null, values);
        m_dbWrapper.CloseDatabase();

        return (insert != -1);
    }

    /**
     * Removes a record with the specified ID from the DailyBudgetsInPlan table.
     * @param planID The ID of the record to remove.
     * @return True if the removal was successful, false otherwise.
     */
    public boolean removeDailyBudgetsInPlan(long planID)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();

        final int delete = db.delete(DAILYBUDGETSINPLAN_TABLE, DAILYBUDGETSINPLAN_COL_PLANID + " = " + Long.toString(planID) + ";", null);

        m_dbWrapper.CloseDatabase();

        return delete == 1;
    }

    /**
     * Updates an existing DailyBudgetInPlan in the DailyBudgetsInPlan table.
     * @param dailyBudgetInPlan The DailyBudgetInPlan to update.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateDailyBudgetInPlan(DailyBudgetInPlan dailyBudgetInPlan)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        ContentValues values = new ContentValues();

        values.put(DAILYBUDGETSINPLAN_COL_PERIODICBUDGETID, dailyBudgetInPlan.getPeriodicBudgetID());
        values.put(DAILYBUDGETSINPLAN_COL_PLANID, dailyBudgetInPlan.getPlanID());

        final long update = db.update(DAILYBUDGETSINPLAN_TABLE, values, DAILYBUDGETSINPLAN_COL_ID + "=" + Long.toString(dailyBudgetInPlan.getID()) + ";", null);
        m_dbWrapper.CloseDatabase();

        return (update == 1);
    }

    //endregion

    //region Category Methods
    //Note: I know this is bad...

    /**
     * Builds the set of categories of Expenses in the Expense table.
     * @return The set of all categories found in the Expense table.
     */
    public HashSet<String> buildCategoryList()
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();

        HashSet<String> set = new HashSet<>();
        String category = "";

        Cursor c = db.rawQuery("SELECT * FROM " + EXPENSE_TABLE + " WHERE 1 ;", null);
        c.moveToFirst();

        while(!c.isAfterLast())
        {
            category = c.getString(c.getColumnIndex((EXPENSE_COL_CATEGORY)));
            if (category != null) {
                set.add(category);
            }
            c.moveToNext();
        }
        c.close();
        m_dbWrapper.CloseDatabase();

        return set;
    }

    //endregion

    //endregion

    //region Helper Methods

    /**
     * Adds a record in the Expense-ExpenseGroup bridge table for the specified ExpenseGroup ID and for each ExpenseInGroup
     * @param db The database object to execute.
     * @param expenseGroupID The ID of the ExpenseGroup to add to.
     * @param expenses The collection of Expenses to add.
     * @return True if all the additions were successful, false otherwise false.
     */
    private boolean addExpensesInGroupToTable(SQLiteDatabase db, long expenseGroupID, Iterable<KeyValuePair<Expense, Integer>> expenses)
    {
        ContentValues values = new ContentValues();
        boolean success = true;

        for (KeyValuePair<Expense, Integer> kvp : expenses)
        {
            values.put(EXPENSESINGROUP_COL_EXPENSEGROUPID, expenseGroupID);
            values.put(EXPENSESINGROUP_COL_EXPENSEID, kvp.getKey().getId());
            values.put(EXPENSESINGROUP_COL_QUANTITY, kvp.getValue());

            final long insert = db.insert(EXPENSESINGROUP_TABLE, null, values);
            if(insert == -1)
                success = false;

            values.clear();
        }

        return success;
    }


    /**
     * Generates a new ItemInfo from the specified Priceable.
     * @param db The database object to execute.
     * @param item The Priceable item to look for.
     * @return The ItemInfo for the specified Priceable item.
     */
    private ItemInfo findItemInfo(SQLiteDatabase db, Priceable item)
    {
        String itemType = "";
        long itemID = 0;

        if(item instanceof Expense)
        {
            Expense expense = (Expense) item;
            itemType = expense.getClass().getName();
            itemID = expense.getId();
        }
        else if(item instanceof ExpenseGroup)
        {
            ExpenseGroup expenseGroup = (ExpenseGroup) item;
            itemType = expenseGroup.getClass().getName();
            itemID = expenseGroup.getId();
        }

        return new ItemInfo(itemType, itemID);
    }

    /**
     * Removes all records in the Expense-ExpenseGroup bridge table with the specified ExpenseGroup ID.
     * @param db The database object to execute.
     * @param expenseGroupID The ID of the ExpenseGroup to remove from the bridge table.
     * @return The number of records deleted.
     */
    private int removeExpensesInGroupLinks(SQLiteDatabase db, long expenseGroupID)
    {
        return db.delete(EXPENSESINGROUP_TABLE, EXPENSESINGROUP_COL_EXPENSEGROUPID + " = " + Long.toString(expenseGroupID) + ";", null);
    }

    /**
     * Retrieves the list of Expenses in the ExpenseGroup with the specified ID.
     * @param db The database object to execute.
     * @param expenseGroupID The ID of the ExpenseGroup to retrieve Expenses from.
     * @return The list of Expenses in the ExpenseGroup with the specified ID.
     */
    private ArrayList<KeyValuePair<Expense, Integer>> getExpensesInGroup(SQLiteDatabase db, long expenseGroupID)
    {
        ArrayList<KeyValuePair<Expense, Integer>> list = new ArrayList<>();
        String id;
        String name;
        String price;
        String category;
        String description;
        String quantity;

        Cursor c = db.rawQuery("SELECT * FROM " + EXPENSESINGROUP_TABLE + " INNER JOIN " + EXPENSE_TABLE + " ON "
                + EXPENSESINGROUP_TABLE + "." + EXPENSESINGROUP_COL_EXPENSEID + "=" + EXPENSE_TABLE + "."
                + EXPENSE_COL_ID + " WHERE " + EXPENSESINGROUP_TABLE + "." + EXPENSESINGROUP_COL_EXPENSEGROUPID
                + "=" + Long.toString(expenseGroupID), null);

        c.moveToFirst();

        while(!c.isAfterLast())
        {
            id = c.getString(c.getColumnIndex(EXPENSE_COL_ID));
            name = c.getString(c.getColumnIndex(EXPENSE_COL_NAME));
            price = c.getString(c.getColumnIndex(EXPENSE_COL_PRICE));
            category = c.getString(c.getColumnIndex(EXPENSE_COL_CATEGORY));
            description = c.getString(c.getColumnIndex(EXPENSE_COL_DESCRIPTION));
            quantity = c.getString(c.getColumnIndex(EXPENSESINGROUP_TABLE + "." + EXPENSESINGROUP_COL_QUANTITY));   //TODO: Exception thrown here from SQLite with '.' in column name

            list.add(new KeyValuePair<>(new Expense(Long.parseLong(id), name, Long.parseLong(price), category, description), Integer.parseInt(quantity)));
            c.moveToNext();
        }

        c.close();

        return list;
    }

    //endregion


    //region DatabaseWrapper

    /**
     * A SQLiteDatabase wrapper that handles the opening and closing of the database object to simplify
     * the shared use of the database object between multiple methods.
     * <p>
     *     This wrapper has a count of the number of times it has been opened, which is decreased whenever
     *     it is closed. The database object will only open if the count is 0 and it will only close if
     *     the count is 1 before closing.
     * </p>
     */
    class DatabaseWrapper
    {
        private SQLiteDatabase m_db;
        private int m_numReferences;

        /**
         * Initializes a new instance of a DatabaseWrapper.
         */
        public DatabaseWrapper()
        {
            // Do nothing
        }

        /**
         * Increments the reference count and opens the database if necessary.
         * @return The writable database object.
         */
        public SQLiteDatabase OpenDatabase()
        {
            m_numReferences += 1;

            if (m_db == null || !m_db.isOpen())
                m_db = getWritableDatabase();

            return m_db;
        }

        /**
         * Decrements the reference count and closes the database if necessary.
         * @return True if the database was closed or if the reference count was decremented, false otherwise.
         */
        public boolean CloseDatabase()
        {
            if (m_numReferences > 0) {
                m_numReferences -= 1;

                if (m_numReferences == 0)
                    m_db.close();

                return true;
            }
            return false;
        }

    }

    //endregion

}

//region Class ItemInfo

/**
 * A class that stores an item's type name and its ID in the database.
 */
class ItemInfo
{
    private String itemType;
    private long itemID;

    /**
     * Initializes a new instance of an ItemInfo with the specified fields.
     * @param type The name of the item's type.
     * @param id The database ID of the item.
     */
    public ItemInfo(String type, long id)
    {
        this.itemType = type;
        this.itemID = id;
    }

    /**
     * Gets the name of the item's type.
     * @return The name of the item's type.
     */
    public String getItemType()
    {
        return this.itemType;
    }

    /**
     * Gets the database ID of the item.
     * @return The database ID of the item.
     */
    public long getItemID()
    {
        return this.itemID;
    }

    /**
     * Sets the name of the item's type.
     * @param type The desired name.
     */
    public void setItemType(String type)
    {
        this.itemType = type;
    }

    /**
     * Sets the database ID of the item.
     * @param id The desired ID.
     */
    public void setItemID(long id)
    {
        this.itemID = id;
    }
}

//endregion

//region Class DailyBudgetInPlan

/**
 * A class that representing a PeriodicBudget that is one of the daily budgets in a Plan.
 */
class DailyBudgetInPlan
{
    private long m_ID;
    private long m_planID;
    private long m_periodicBudgetID;

    /**
     * Initializes a new instance of a DailyBudgetInPlan with the specified fields.
     * @param planID The database ID of the Plan.
     * @param periodicBudgetID The database ID of the PeriodicBudget.
     */
    public DailyBudgetInPlan(long planID, long periodicBudgetID)
    {
        m_ID = -1;
        m_planID = planID;
        m_periodicBudgetID = periodicBudgetID;
    }

    /**
     * Initializes a new instance of a DailyBudgetInPlan with the specified fields.
     * @param id The database ID of the DailyBudgetInPlan.
     * @param planID The database ID of the Plan.
     * @param periodicBudgetID The database ID of the PeriodicBudget.
     */
    public DailyBudgetInPlan(long id, long planID, long periodicBudgetID)
    {
        m_ID = id;
        m_planID = planID;
        m_periodicBudgetID = periodicBudgetID;
    }

    /**
     * Gets the database ID of this DailyBudgetInPlan.
     * @return The database ID of this DailyBudgetInPlan.
     */
    public long getID() { return m_ID; }

    /**
     * Gets the database ID of the Plan.
     * @return The databaseID of the Plan.
     */
    public long getPlanID() { return m_planID; }

    /**
     * Gets the database ID of the PeriodicBudget.
     * @return The database ID of the PeriodicBudget.
     */
    public long getPeriodicBudgetID() { return m_periodicBudgetID; }

}

//endregion