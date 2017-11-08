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
import com.budget_app.jt_linked_list.Node;
import com.budget_app.jt_linked_list.SortedList;

/**
 * Created by Jericho on 11/7/2017.
 */

public class DBHandler extends  SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BudgetAppData.db";

    //Expense Table Info
    private static final String EXPENSE_TABLE = "tblExpenses";
    private static final String EXPENSE_COL_ID = "ID";
    private static final String EXPENSE_COL_NAME = "Name";
    private static final String EXPENSE_COL_PRICE = "Price";
    private static final String EXPENSE_COL_CATEGORY = "Category";
    private static final String EXPENSE_COL_DESCRIPTION = "Description";

    //ExpenseGroup Table Info
    private static final String EXPENSEGROUP_TABLE = "tblExpenseGroups";
    private static final String EXPENSEGROUP_COL_ID = "ID";
    private static final String EXPENSEGROUP_COL_NAME = "Name";
    private static final String EXPENSEGROUP_COL_PRICE = "Price";
    private static final String EXPENSEGROUP_COL_CATEGORY = "Category";
    private static final String EXPENSEGROUP_COL_DESCRIPTION = "Description";
    //NOTE: Store list of expenses in a bridge table (relate EXPENSEGROUP_COL_ID to EXPENSE_COL_ID)

    //Expense-ExpenseGroup Bridge Table
    private static final String EXPENSESINGROUP_TABLE = "tblExpensesInGroup";
    private static final String EXPENSESINGROUP_COL_ID = "ID";
    private static final String EXPENSESINGROUP_COL_EXPENSEGROUPID = "ExpenseGroupID";
    private static final String EXPENSESINGROUP_COL_EXPENSEID = "ExpenseID";

    //Plans Table Info
    //TODO: Add plans

    //PurchaseHistory Table Info
    private static final String PURCHASES_TABLE = "tblPurchases";
    private static final String PURCHASES_COL_ID = "ID";
    private static final String PURCHASES_COL_ITEMTYPE = "ItemType"; //expense OR expense group
    private static final String PURCHASES_COL_ITEMID = "ItemID";
    private static final String PURCHASES_COL_DATE = "Date";
    private static final String PURCHASES_COL_QUANTITY = "Quantity";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

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

    public void addExpense(Expense expense)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EXPENSE_COL_NAME, expense.getName());
        values.put(EXPENSE_COL_PRICE, expense.getPrice());
        values.put(EXPENSE_COL_CATEGORY, expense.getCategory());
        values.put(EXPENSE_COL_DESCRIPTION, expense.getDescription());

        db.insert(EXPENSE_TABLE, null, values);
        db.close();
    }

    public void addExpenseGroup(ExpenseGroup expenseGroup)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EXPENSEGROUP_COL_NAME, expenseGroup.getName());
        values.put(EXPENSEGROUP_COL_PRICE, expenseGroup.getPrice());
        values.put(EXPENSEGROUP_COL_CATEGORY, expenseGroup.getCategory());
        values.put(EXPENSEGROUP_COL_DESCRIPTION, expenseGroup.getDescription());

        db.insert(EXPENSEGROUP_TABLE, null, values);

        //add expenses to bridge table
        Cursor c = db.rawQuery("SELECT" + EXPENSEGROUP_COL_ID + " FROM '" + EXPENSEGROUP_TABLE + "' WHERE '" + EXPENSEGROUP_COL_NAME + "' = '" + expenseGroup.getName() + "';", null);
        long expenseGroupID = Long.parseLong(c.getString(c.getColumnIndex(EXPENSEGROUP_COL_ID)));

        addExpensesInGroupToTable(db, expenseGroupID, expenseGroup.getExpenses());

        c.close();
        db.close();
    }

    //helper method to addExpenseGroup
    private void addExpensesInGroupToTable(SQLiteDatabase db, long expenseGroupID, SortedList expenses)
    {
        ContentValues values = new ContentValues();
        Node curr = expenses.getHead();
        Cursor c = null;
        String query;

        while(curr != null)
        {
            if(curr.getItem() instanceof Expense)
            {
                Expense expense = (Expense) curr.getItem();
                query = "SELECT * FROM '" + EXPENSE_TABLE + "' WHERE '" + EXPENSE_COL_NAME + "' = '" + expense.getName() + "';";
                c = db.rawQuery(query, null);
                c.moveToFirst();

                long expenseID = Long.parseLong(c.getString(c.getColumnIndex(EXPENSE_COL_ID)));

                values.put(EXPENSESINGROUP_COL_EXPENSEGROUPID, expenseGroupID);
                values.put(EXPENSESINGROUP_COL_EXPENSEID, expenseID);

                db.insert(EXPENSESINGROUP_TABLE, null, values);
                values.clear();
            }
            curr = curr.getNext();
        }

        if(c != null)
            c.close();
    }

    public void addPurchase(Purchase purchase)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        ItemInfo itemInfo = findItemInfo(db, purchase.getItem());

        values.put(PURCHASES_COL_ITEMTYPE, itemInfo.getItemType());
        values.put(PURCHASES_COL_ITEMID, itemInfo.getItemID());
        values.put(PURCHASES_COL_QUANTITY, purchase.getQuantity());
        values.put(PURCHASES_COL_DATE, purchase.getDate().toString());

        db.insert(PURCHASES_TABLE, null, values);
        db.close();
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

            String query = "SELECT * FROM '" + EXPENSE_TABLE + "' WHERE '" + EXPENSE_COL_NAME + "' = '" + expense.getName() + "';";
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();

            itemID = Long.parseLong(c.getString(c.getColumnIndex(EXPENSE_COL_ID)));
            c.close();
        }
        else if(item instanceof ExpenseGroup)
        {
            ExpenseGroup expenseGroup = (ExpenseGroup) item;
            itemType = expenseGroup.getClass().getName();

            String query = "SELECT * FROM '" + EXPENSEGROUP_TABLE + "' WHERE '" + EXPENSEGROUP_COL_NAME + "' = '" + expenseGroup.getName() + "';";
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();

            itemID = Long.parseLong(c.getString(c.getColumnIndex(EXPENSEGROUP_COL_ID)));
            c.close();
        }

        return new ItemInfo(itemType, itemID);
    }

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
