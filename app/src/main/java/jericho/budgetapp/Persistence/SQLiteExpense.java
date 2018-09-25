package jericho.budgetapp.Persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

import jericho.budgetapp.Model.Expense;
import jericho.budgetapp.Persistence.Interfaces.ExpensePersistence;

public class SQLiteExpense extends SQLiteOpenHelper implements ExpensePersistence
{

    //Expense Table Info
    private static final String TABLENAME = "Expenses";
    private static final String COL_ID = "ID";
    private static final String COL_NAME = "Name";
    private static final String COL_PRICE = "Price";
    private static final String COL_CATEGORY = "Category";
    private static final String COL_DESCRIPTION = "Description";

    public SQLiteExpense(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    /**
     * Initializes the Expense table.
     * @param db The {@link SQLiteDatabase} to initialize the table with.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Create Expense Table
        String sql = "CREATE TABLE '" + TABLENAME + "'('"
                + COL_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '"
                + COL_NAME + "' TEXT UNIQUE NOT NULL, '"
                + COL_PRICE + "' INTEGER NOT NULL, '"
                + COL_CATEGORY + "' TEXT, '"
                + COL_DESCRIPTION + "' TEXT"
                + ");";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + TABLENAME + "';");

        this.onCreate(sqLiteDatabase);
    }

    @Override
    public Expense getExpense(long id)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = String.format(Locale.getDefault(),
                "SELECT * FROM %s WHERE %s = %d;", TABLENAME, COL_ID, id);

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        long expenseID = cursor.getLong(cursor.getColumnIndex(COL_ID));
        String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
        long price = cursor.getLong(cursor.getColumnIndex(COL_PRICE));
        String category = cursor.getString(cursor.getColumnIndex(COL_CATEGORY));
        String description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION));

        cursor.close();
        db.close();

        return new Expense(expenseID, name, price, category, description);
    }

    @Override
    public Expense getExpense(String name)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = String.format("SELECT * FROM %s WHERE %s = '%s';", TABLENAME, COL_NAME, name);

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
        String expenseName = cursor.getString(cursor.getColumnIndex(COL_NAME));
        long price = cursor.getLong(cursor.getColumnIndex(COL_PRICE));
        String category = cursor.getString(cursor.getColumnIndex(COL_CATEGORY));
        String description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION));

        cursor.close();
        db.close();

        return new Expense(id, expenseName, price, category, description);
    }

    @Override
    public long addExpense(Expense expense)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME, expense.getName());
        values.put(COL_PRICE, expense.getPrice());
        values.put(COL_CATEGORY, expense.getCategory());
        values.put(COL_DESCRIPTION, expense.getDescription());

        final long insert = db.insert(TABLENAME, null, values);
        db.close();

        return insert;
    }

    @Override
    public boolean updateExpense(Expense expense)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME, expense.getName());
        values.put(COL_PRICE, expense.getPrice());
        values.put(COL_CATEGORY, expense.getCategory());
        values.put(COL_DESCRIPTION, expense.getDescription());

        String where = String.format(Locale.getDefault(), "%s = %d", COL_ID, expense.getId());
        final int update = db.update(TABLENAME, values, where, null);
        db.close();

        return (update == 1);
    }

    @Override
    public boolean deleteExpense(long id)
    {
        SQLiteDatabase db = getWritableDatabase();

        String where = String.format(Locale.getDefault(), "%s = %d", COL_ID, id);
        final int delete = db.delete(TABLENAME, where, null);
        db.close();

        return (delete == 1);
    }

    @Override
    public boolean deleteExpense(String name)
    {
        SQLiteDatabase db = getWritableDatabase();

        String where = String.format(Locale.getDefault(), "%s = '%s'", COL_NAME, name);
        final int delete = db.delete(TABLENAME, where, null);
        db.close();

        return (delete == 1);
    }
}
