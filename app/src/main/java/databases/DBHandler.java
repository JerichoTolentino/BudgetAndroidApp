package databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.budget_app.expenses.Expense;
import com.budget_app.expenses.ExpenseGroup;
import com.budget_app.expenses.ExpenseInGroup;
import com.budget_app.expenses.Purchase;
import com.budget_app.jt_interfaces.Priceable;
import com.budget_app.plans.PeriodicBudget;
import com.budget_app.plans.Plan;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import jericho.budgetapp.BuildConfig;

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

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

        m_dbWrapper = new DatabaseWrapper();
    }

    //region Event Handlers

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
                + PURCHASES_COL_ITEMNAME + "' TEXT NO NULL, '"
                + PURCHASES_COL_ITEMPRICE + "' INTEGER NOT NULL, '"
                + PURCHASES_COL_ITEMTYPE + "' TEXT NOT NULL, '"
                + PURCHASES_COL_DATE + "' TEXT NOT NULL, '"
                + PURCHASES_COL_QUANTITY + "' INTEGER NOT NULL"
                + ");";

        sqLiteDatabase.execSQL(sql);

        //Create Plans Table
        //TODO: Add plans table
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
        ArrayList<ExpenseInGroup> expenses;

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

            //itemID = c.getString(c.getColumnIndex(PURCHASES_COL_ITEMID));

            //if (itemType.equals(Expense.class.getName().toString())) {
            //    ArrayList<Expense> expenses = queryExpenses(EXPENSE_COL_ID + " = " + Long.parseLong(itemID));
            //    item = (expenses.get(0));
            //} else if (itemType.equals(ExpenseGroup.class.getName().toString())) {
            //        ArrayList<ExpenseGroup> expenseGroups = queryExpenseGroups(EXPENSEGROUP_COL_ID + " = " + Long.parseLong(itemID));
            //        item = expenseGroups.get(0);
            //} else
            //    return null;

            Date parsedDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse(date);

            list.add(new Purchase(Long.parseLong(id), item, Integer.parseInt(quantity), parsedDate));
            c.moveToNext();
        }

        c.close();
        m_dbWrapper.CloseDatabase();

        return list;
    }

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

    public boolean removeExpense(long ID)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();
        final int delete = db.delete(EXPENSE_TABLE, EXPENSE_COL_ID + " = " + Long.toString(ID), null);

        m_dbWrapper.CloseDatabase();

        return delete == 1;
    }

    //endregion

    //region ExpenseGroup Table Methods

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

    public boolean removeExpenseFromGroup(long expenseID, long expenseGroupID)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();

        final int delete = db.delete(EXPENSESINGROUP_TABLE, EXPENSESINGROUP_COL_EXPENSEGROUPID + " = " + Long.toString(expenseGroupID), null);

        m_dbWrapper.CloseDatabase();

        return delete == 1;
    }

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

    public boolean removeExpenseGroup(long ID)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();

        final int deleteLinks = removeExpensesInGroupLinks(db, ID);
        final int deleteSrc = db.delete(EXPENSEGROUP_TABLE, EXPENSEGROUP_COL_ID + " = " + Long.toString(ID) + ";", null);

        m_dbWrapper.CloseDatabase();

        return (deleteLinks + deleteSrc > 0);
    }

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

    public boolean removePurchase(long ID)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();

        final int delete = db.delete(PURCHASES_TABLE, PURCHASES_COL_ID + " = " + Long.toString(ID) + ";", null);

        m_dbWrapper.CloseDatabase();

        return delete == 1;
    }

    //endregion

    //region Plan Table Methods

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

    public boolean removePeriodicBudget(long ID)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();

        final int delete = db.delete(PERIODICBUDGETS_TABLE, PERIODICBUDGETS_COL_ID + " = " + Long.toString(ID) + ";", null);

        m_dbWrapper.CloseDatabase();

        return delete == 1;
    }

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

    public boolean removeDailyBudgetsInPlan(long planID)
    {
        SQLiteDatabase db = m_dbWrapper.OpenDatabase();

        final int delete = db.delete(DAILYBUDGETSINPLAN_TABLE, DAILYBUDGETSINPLAN_COL_PLANID + " = " + Long.toString(planID) + ";", null);

        m_dbWrapper.CloseDatabase();

        return delete == 1;
    }

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

    //helper method to addExpenseGroup
    private boolean addExpensesInGroupToTable(SQLiteDatabase db, long expenseGroupID, ArrayList<ExpenseInGroup> expenses)
    {
        ContentValues values = new ContentValues();
        boolean success = true;

        for (ExpenseInGroup e : expenses)
        {
            values.put(EXPENSESINGROUP_COL_EXPENSEGROUPID, expenseGroupID);
            values.put(EXPENSESINGROUP_COL_EXPENSEID, e.getId());
            values.put(EXPENSESINGROUP_COL_QUANTITY, e.getQuantity());

            final long insert = db.insert(EXPENSESINGROUP_TABLE, null, values);
            if(insert == -1)
                success = false;

            values.clear();
        }

        return success;
    }

    //helper method to addPurchase
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

    //helper method to removeExpenseGroup
    private int removeExpensesInGroupLinks(SQLiteDatabase db, long expenseGroupID)
    {
        return db.delete(EXPENSESINGROUP_TABLE, EXPENSESINGROUP_COL_EXPENSEGROUPID + " = " + Long.toString(expenseGroupID) + ";", null);
    }

    //helper method for queryExpenseGroups
    private ArrayList<ExpenseInGroup> getExpensesInGroup(SQLiteDatabase db, long expenseGroupID)
    {
        ArrayList<ExpenseInGroup> list = new ArrayList<>();
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
            quantity = c.getString(c.getColumnIndex(EXPENSESINGROUP_TABLE + "." + EXPENSESINGROUP_COL_QUANTITY));   //TODO: Exception thrown here from hacky code in SQLite with '.' in column name

            list.add(new ExpenseInGroup(Long.parseLong(id), name, Long.parseLong(price), category, description, Integer.parseInt(quantity)));
            c.moveToNext();
        }

        c.close();

        return list;
    }

    //endregion


    //region DatabaseWrapper

    class DatabaseWrapper
    {
        private SQLiteDatabase m_db;
        private int m_numReferences;

        public DatabaseWrapper()
        {
            // Do nothing
        }

        public SQLiteDatabase OpenDatabase()
        {
            m_numReferences += 1;

            if (m_db == null || !m_db.isOpen())
                m_db = getWritableDatabase();

            return m_db;
        }

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

class ItemInfo
{
    private String itemType;
    private long itemID;

    public ItemInfo(String type, long id)
    {
        this.itemType = type;
        this.itemID = id;
    }

    public String getItemType()
    {
        return this.itemType;
    }

    public long getItemID()
    {
        return this.itemID;
    }

    public void setItemType(String type)
    {
        this.itemType = type;
    }

    public void setItemID(long id)
    {
        this.itemID = id;
    }
}

//endregion

//region Class DailyBudgetInPlan

class DailyBudgetInPlan
{
    private long m_ID;
    private long m_planID;
    private long m_periodicBudgetID;

    public DailyBudgetInPlan(long planID, long periodicBudgetID)
    {
        m_ID = -1;
        m_planID = planID;
        m_periodicBudgetID = periodicBudgetID;
    }

    public DailyBudgetInPlan(long id, long planID, long periodicBudgetID)
    {
        m_ID = id;
        m_planID = planID;
        m_periodicBudgetID = periodicBudgetID;
    }

    public long getID() { return m_ID; }
    public long getPlanID() { return m_planID; }
    public long getPeriodicBudgetID() { return m_periodicBudgetID; }

}

//endregion