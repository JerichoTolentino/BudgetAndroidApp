package databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.budget_app.expenses.Expense;
import com.budget_app.expenses.ExpenseGroup;
import com.budget_app.expenses.Purchase;
import com.budget_app.jt_interfaces.Priceable;
import com.budget_app.jt_linked_list.LinkedList;
import com.budget_app.jt_linked_list.Node;
import com.budget_app.jt_linked_list.NodeFactory;
import com.budget_app.jt_linked_list.SortedList;
import com.budget_app.jt_linked_list.StringItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by Jericho on 11/7/2017.
 */

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

    //Plans Table Info
    //TODO: Add plans

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
                + EXPENSESINGROUP_COL_EXPENSEID + "' INTEGER NOT NULL"
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

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + EXPENSE_TABLE + "';");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + EXPENSEGROUP_TABLE + "';");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + PURCHASES_TABLE + "';");
        this.onCreate(sqLiteDatabase);
    }

    //endregion

    //region Public Methods

    //region Query Methods

    public LinkedList queryExpenses(String where)
    {
        SQLiteDatabase db = getWritableDatabase();
        LinkedList list = new LinkedList();
        String id;
        String name;
        String price;
        String category;
        String description;

        if (where == null || where == "")
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

            list.insertFront(new Expense(Long.parseLong(id), name, Long.parseLong(price), category, description));
            c.moveToNext();
        }

        c.close();
        db.close();

        return list;
    }

    public LinkedList queryExpenseGroups(String where)
    {
        SQLiteDatabase db = getWritableDatabase();
        LinkedList list = new LinkedList();
        String id;
        String name;
        String price;
        String category;
        String description;

        long expenseGroupID;
        LinkedList expenses;

        if (where == null || where == "")
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

            list.insertFront(new ExpenseGroup(Long.parseLong(id), name, Long.parseLong(price), category, description, new SortedList(expenses)));
            c.moveToNext();
        }

        c.close();
        db.close();

        return list;
    }

    public LinkedList queryPurchases(String where) throws ParseException
    {
        SQLiteDatabase db = getWritableDatabase();
        LinkedList list = new LinkedList();
        String id;
        String itemType;
        String date;
        String quantity;

        String itemID;
        Priceable item = null;
        LinkedList itemList;

        if (where == null || where == "")
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

            if(itemType.equals(Expense.class.getName()))
            {
                itemList = queryExpenses(EXPENSE_COL_ID + " = " + Long.parseLong(itemID));
                item = (Expense)(itemList.getHead().getItem());
            }
            else if(itemType.equals(ExpenseGroup.class.getName()))
            {
                itemList = queryExpenseGroups(EXPENSEGROUP_COL_ID + " = " + Long.parseLong(itemID));
                item = (ExpenseGroup)(itemList.getHead().getItem());
            }

            list.insertFront(new Purchase(Long.parseLong(id), item, Integer.parseInt(quantity), DateFormat.getDateInstance().parse(date)));
            c.moveToNext();
        }

        c.close();
        db.close();

        return list;
    }

    //TODO: Put this in a method that queries bridge table (or separate into two mehtods)
            /*
        SELECT * FROM
	        ExpenseGroups As GroupedExpenses INNER JOIN
	        (ExpensesInGroup INNER JOIN Expenses ON ExpensesInGroup.ExpenseID = Expenses.ID)
	        ON GroupedExpenses.ID = ExpensesInGroup.ExpenseGroupID
	        WHERE GroupedExpenses.ExpenseGroupID = [expenseGroupID];
         */

//    Cursor c = db.rawQuery("SELECT " + select + " FROM " + EXPENSEGROUP_TABLE
//            + " INNER JOIN (" + EXPENSESINGROUP_TABLE + " INNER JOIN " + EXPENSE_TABLE + " ON "
//            + EXPENSESINGROUP_TABLE + "." + EXPENSESINGROUP_COL_EXPENSEID + "=" + EXPENSE_TABLE
//            + "." + EXPENSE_COL_ID + ") ON " + EXPENSEGROUP_TABLE + "." + EXPENSEGROUP_COL_ID
//            + "=" + EXPENSESINGROUP_TABLE + "." + EXPENSESINGROUP_COL_EXPENSEGROUPID
//            + " WHERE " + where + ";", null);

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

    public boolean addExpenseToGroup(long expenseID, long expenseGroupID)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EXPENSESINGROUP_COL_EXPENSEGROUPID, expenseGroupID);
        values.put(EXPENSESINGROUP_COL_EXPENSEID, expenseID);

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
        values.put(PURCHASES_COL_DATE, purchase.getDate().toString());

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

    //region Category Methods
    //Note: I know this is bad...

    public LinkedList buildCategoryList()
    {
        SQLiteDatabase db = getWritableDatabase();
        LinkedList list = new LinkedList();
        String category = "";

        Cursor c = db.rawQuery("SELECT * FROM " + EXPENSE_TABLE + " WHERE 1 ;", null);
        c.moveToFirst();

        while(!c.isAfterLast())
        {
            category = c.getString(c.getColumnIndex((EXPENSE_COL_CATEGORY)));
            if (category != null)
            {
                StringItem item = new StringItem(category);

                if (list.findNode(item) == null)
                {
                    list.insertFront(item);
                }
            }
            c.moveToNext();
        }
        c.close();
        db.close();

        return list;
    }

    //endregion

    //endregion

    //region Helper Methods

    //helper method to addExpenseGroup
    private boolean addExpensesInGroupToTable(SQLiteDatabase db, long expenseGroupID, SortedList expenses)
    {
        ContentValues values = new ContentValues();
        Node curr = expenses.getHead();
        boolean success = true;

        while(curr != null)
        {
            if(curr.getItem() instanceof Expense)
            {
                Expense expense = (Expense) curr.getItem();

                values.put(EXPENSESINGROUP_COL_EXPENSEGROUPID, expenseGroupID);
                values.put(EXPENSESINGROUP_COL_EXPENSEID, expense.getId());

                final long insert = db.insert(EXPENSESINGROUP_TABLE, null, values);
                if(insert == -1)
                    success = false;

                values.clear();
            }
            curr = curr.getNext();
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
    private LinkedList getExpensesInGroup(SQLiteDatabase db, long expenseGroupID)
    {
        LinkedList list = new LinkedList();
        String id;
        String name;
        String price;
        String category;
        String description;

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

            list.insertFront(new Expense(Long.parseLong(id), name, Long.parseLong(price), category, description));

            c.moveToNext();
        }

        c.close();

        return list;
    }

    //endregion

//    public void deleteProduct(String productName)
//    {
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("DELETE FROM '" + TABLE_PRODUCTS + "' WHERE " + COLUMN_NAME + " = '" + productName + "';");
//    }
//
//    public String dbToString()
//    {
//        String dbString = "";
//        SQLiteDatabase db = getWritableDatabase();
//        String query = "SELECT * FROM '" + TABLE_PRODUCTS + "' WHERE 1;";
//
//        Cursor c = db.rawQuery(query, null);
//        c.moveToFirst();
//
//        while(!c.isAfterLast())
//        {
//            if(c.getString(c.getColumnIndex(COLUMN_NAME)) != null)
//            {
//                dbString += c.getString(c.getColumnIndex(COLUMN_ID)) + "\t" + c.getString(c.getColumnIndex(COLUMN_NAME));
//                dbString += "\n";
//            }
//            c.moveToNext();
//        }
//
//        c.close();
//        db.close();
//        return dbString;
//    }

}

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
