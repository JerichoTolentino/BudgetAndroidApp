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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

//TODO: Create methods in DBHandler to replace methods in BudgetAppManager
    /*
    Methods to replace:
     */

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
    //TODO: Add plans
    public static final String PLANS_TABLE = "tblPlans";
    public static final String PLANS_COL_ID = "ID";
    public static final String PLANS_COL_ANNUALINCOME = "AnnualIncome";
    public static final String PLANS_COL_ANNUALEXPENSES = "AnnualExpenses";
    public static final String PLANS_COL_ANNUALSAVINGS = "AnnualSavings";
    public static final String PLANS_COL_NAME = "Name";
    public static final String PLANS_COL_ANNUALBUDGET = "AnnualBudget";
    public static final String PLANS_COL_MONTHLYBUDGETID = "MonthlyBudgetID";
    public static final String PLANS_COL_WEEKLYBUDGETID = "WeeklyBudgetID";
    public static final String PLANS_COL_DAILYBUDGETSID = "DailyBudgetsID";
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
    public static final String PURCHASES_COL_ITEMID = "ItemID";
    public static final String PURCHASES_COL_DATE = "Date";
    public static final String PURCHASES_COL_QUANTITY = "Quantity";

    //endregion

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
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
                + PURCHASES_COL_ITEMID + "' INTEGER NOT NULL, '"
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
                + PLANS_COL_WEEKLYBUDGETID + "' INTEGER NOT NULL, '"
                + PLANS_COL_DAILYBUDGETSID + "' INTEGER NOT NULL"
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
        SQLiteDatabase db = getWritableDatabase();
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
        db.close();

        return list;
    }

    public ArrayList<ExpenseGroup> queryExpenseGroups(String where)
    {
        SQLiteDatabase db = getWritableDatabase();
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
        db.close();

        return list;
    }

    public ArrayList<Purchase> queryPurchases(String where) throws ParseException
    {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Purchase> list = new ArrayList<>();
        String id;
        String itemType;
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
            date = c.getString(c.getColumnIndex(PURCHASES_COL_DATE));
            quantity = c.getString(c.getColumnIndex(PURCHASES_COL_QUANTITY));

            itemID = c.getString(c.getColumnIndex(PURCHASES_COL_ITEMID));

            if (itemType.equals(Expense.class.getName().toString())) {
                ArrayList<Expense> expenses = queryExpenses(EXPENSE_COL_ID + " = " + Long.parseLong(itemID));
                item = (expenses.get(0));
            } else if (itemType.equals(ExpenseGroup.class.getName().toString())) {
                    ArrayList<ExpenseGroup> expenseGroups = queryExpenseGroups(EXPENSEGROUP_COL_ID + " = " + Long.parseLong(itemID));
                    item = expenseGroups.get(0);
            } else
                return null;

            Date parsedDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse(date);

            list.add(new Purchase(Long.parseLong(id), item, Integer.parseInt(quantity), parsedDate));
            c.moveToNext();
        }

        c.close();
        db.close();

        return list;
    }

    public ArrayList<Plan> queryPlans(String where) throws ParseException
    {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Plan> list = new ArrayList<>();
        String id;
        String annualIncome;
        String annualExpenses;
        String annualSavings;
        String name;
        String annualBudget;
        String monthlyBudgetID;
        String weeklyBudgetID;
        String dailyBudgetsID;

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
            annualBudget = c.getString(c.getColumnIndex(PLANS_COL_ANNUALBUDGET);
            monthlyBudgetID = c.getString(c.getColumnIndex(PLANS_COL_MONTHLYBUDGETID));
            weeklyBudgetID = c.getString(c.getColumnIndex(PLANS_COL_WEEKLYBUDGETID));
            dailyBudgetsID = c.getString(c.getColumnIndex(PLANS_COL_DAILYBUDGETSID));

            PeriodicBudget weeklyBudget = queryPeriodicBudgets(PERIODICBUDGETS_COL_ID + "=" + weeklyBudgetID).get(0);
            //PeriodicBudget[] dailyBudgets = queryPeriodicBudgets(PERIODICBUDGETS_COL_ID + "=" +);

            //TODO: Add query for bridge table to then use result to populate dailyBudgets[] array to pass into the plan below

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
        db.close();

        return list;
    }

    public ArrayList<PeriodicBudget> queryPeriodicBudgets(String where) throws ParseException
    {
        SQLiteDatabase db = getWritableDatabase();
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

        Cursor c = db.rawQuery("SELECT * FROM " + PLANS_TABLE + " WHERE " + where + ";", null);

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
        db.close();

        return list;
    }

    //endregion

    //region Expense Table Methods

    public boolean addExpense(Expense expense)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EXPENSE_COL_NAME, expense.getName());
        values.put(EXPENSE_COL_PRICE, expense.getPrice());
        values.put(EXPENSE_COL_CATEGORY, expense.getCategory());
        values.put(EXPENSE_COL_DESCRIPTION, expense.getDescription());

        final long insert = db.insert(EXPENSE_TABLE, null, values);
        db.close();

        return (insert != -1);
    }

    public boolean updateExpense(Expense expense)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EXPENSE_COL_NAME, expense.getName());
        values.put(EXPENSE_COL_PRICE, expense.getPrice());
        values.put(EXPENSE_COL_CATEGORY, expense.getCategory());
        values.put(EXPENSE_COL_DESCRIPTION, expense.getDescription());

        final int update = db.update(EXPENSE_TABLE, values, EXPENSE_COL_ID + " = " + Long.toString(expense.getId()), null);

        db.close();

        return (update == 1);
    }

    public boolean removeExpense(long ID)
    {
        SQLiteDatabase db = getWritableDatabase();

        final int delete = db.delete(EXPENSE_TABLE, EXPENSE_COL_ID + " = " + Long.toString(ID), null);

        db.close();

        return delete == 1;
    }

    //endregion

    //region ExpenseGroup Table Methods

    public boolean addExpenseToGroup(long expenseID, long expenseGroupID, int quantity)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EXPENSESINGROUP_COL_EXPENSEGROUPID, expenseGroupID);
        values.put(EXPENSESINGROUP_COL_EXPENSEID, expenseID);
        values.put(EXPENSESINGROUP_COL_QUANTITY, quantity);

        final long insert = db.insert(EXPENSESINGROUP_TABLE, null, values);

        db.close();

        return (insert != -1);
    }

    public boolean removeExpenseFromGroup(long expenseID, long expenseGroupID)
    {
        SQLiteDatabase db = getWritableDatabase();

        final int delete = db.delete(EXPENSESINGROUP_TABLE, EXPENSESINGROUP_COL_EXPENSEGROUPID + " = " + Long.toString(expenseGroupID), null);

        db.close();

        return delete == 1;
    }

    public boolean addExpenseGroup(ExpenseGroup expenseGroup)
    {
        SQLiteDatabase db = getWritableDatabase();
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

        db.close();

        return (expenseGroupID != -1 && addedExpenses);
    }

    public boolean removeExpenseGroup(long ID)
    {
        SQLiteDatabase db = getWritableDatabase();

        final int deleteLinks = removeExpensesInGroupLinks(db, ID);
        final int deleteSrc = db.delete(EXPENSEGROUP_TABLE, EXPENSEGROUP_COL_ID + " = " + Long.toString(ID) + ";", null);

        db.close();

        return (deleteLinks + deleteSrc > 0);
    }

    public boolean updateExpenseGroup(ExpenseGroup expenseGroup)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EXPENSEGROUP_COL_NAME, expenseGroup.getName());
        values.put(EXPENSEGROUP_COL_PRICE, expenseGroup.getPrice());
        values.put(EXPENSEGROUP_COL_CATEGORY, expenseGroup.getCategory());
        values.put(EXPENSEGROUP_COL_DESCRIPTION, expenseGroup.getDescription());

        final int update = db.update(EXPENSEGROUP_TABLE, values, EXPENSEGROUP_COL_ID + " = " + Long.toString(expenseGroup.getId()), null);

        removeExpensesInGroupLinks(db, expenseGroup.getId());
        addExpensesInGroupToTable(db, expenseGroup.getId(), expenseGroup.getExpenses());

        db.close();

        return (update == 1);
    }

    //endregion

    //region Purchase Table Methods

    public boolean addPurchase(Purchase purchase)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        ItemInfo itemInfo = findItemInfo(db, purchase.getItem());

        values.put(PURCHASES_COL_ITEMTYPE, itemInfo.getItemType());
        values.put(PURCHASES_COL_ITEMID, itemInfo.getItemID());
        values.put(PURCHASES_COL_QUANTITY, purchase.getQuantity());
        values.put(PURCHASES_COL_DATE, DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(purchase.getDate()));

        final long insert = db.insert(PURCHASES_TABLE, null, values);
        db.close();

        return (insert != -1);
    }

    public boolean removePurchase(long ID)
    {
        SQLiteDatabase db = getWritableDatabase();

        final int delete = db.delete(PURCHASES_TABLE, PURCHASES_COL_ID + " = " + Long.toString(ID) + ";", null);

        db.close();

        return delete == 1;
    }

    //endregion

    //region Plan Table Methods

    public boolean addPlan(Plan plan)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put()
        values.put(PURCHASES_COL_ITEMTYPE, itemInfo.getItemType());
        values.put(PURCHASES_COL_ITEMID, itemInfo.getItemID());
        values.put(PURCHASES_COL_QUANTITY, purchase.getQuantity());
        values.put(PURCHASES_COL_DATE, DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(purchase.getDate()));
        //vallues.put(PURCHASES_COL_DATE, purchase.getDate().toString());

        final long insert = db.insert(PURCHASES_TABLE, null, values);
        db.close();

        return (insert != -1);
    }

    public boolean removePlan(long ID)
    {

    }

    //endregion

    //region Category Methods
    //Note: I know this is bad...

    public HashSet<String> buildCategoryList()
    {
        SQLiteDatabase db = getWritableDatabase();
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
        db.close();

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
            quantity = c.getString(c.getColumnIndex(EXPENSESINGROUP_TABLE + "." + EXPENSESINGROUP_COL_QUANTITY));

            list.add(new ExpenseInGroup(Long.parseLong(id), name, Long.parseLong(price), category, description, Integer.parseInt(quantity)));
            c.moveToNext();
        }

        c.close();

        return list;
    }

    //endregion

}

//region Class ItemInfo

//would have liked a simple struct here, but this will do
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