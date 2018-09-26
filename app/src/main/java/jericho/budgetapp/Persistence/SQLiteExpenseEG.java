package jericho.budgetapp.Persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jericho.budgetapp.Persistence.Interfaces.ExpenseEGPersistence;
import utilities.KeyValuePair;

public class SQLiteExpenseEG extends SQLiteOpenHelper implements ExpenseEGPersistence
{

    //Expense-ExpenseGroup Bridge Table
    private static final String TABLENAME = "ExpenseEG";
    private static final String COL_ID = "ID";
    private static final String COL_EXPENSEGROUPID = "ExpenseGroupID";
    private static final String COL_EXPENSEID = "ExpenseID";
    private static final String COL_NUMEXPENSES = "NumExpenses";

    public SQLiteExpenseEG(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //Create Expense-ExpenseGroup Bridge Table
        String sql = "CREATE TABLE '" + TABLENAME + "'('"
                + COL_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '"
                + COL_EXPENSEGROUPID + "' INTEGER NOT NULL, '"
                + COL_EXPENSEID + "' INTEGER NOT NULL, '"
                + COL_NUMEXPENSES + "' INTEGER NOT NULL"
                + ");";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + TABLENAME + "';");
        this.onCreate(sqLiteDatabase);
    }

    @Override
    public List<KeyValuePair<Long, Integer>> getExpensesIn(long expenseGroupId)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = String.format(Locale.getDefault(),
                "SELECT * FROM %s WHERE %s = %d", TABLENAME, COL_EXPENSEGROUPID, expenseGroupId);

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        List<KeyValuePair<Long, Integer>> expenses = new ArrayList<>();
        while (!cursor.isAfterLast())
        {
            long expenseId = cursor.getLong(cursor.getColumnIndex(COL_EXPENSEID));
            int quantity = cursor.getInt(cursor.getColumnIndex(COL_NUMEXPENSES));

            expenses.add(new KeyValuePair<>(expenseId, quantity));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return expenses;
    }

    @Override
    public List<Long> getExpenseGroupsWith(long expenseId)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = String.format(Locale.getDefault(),
                "SELECT * FROM %s WHERE %s = %d", TABLENAME, COL_EXPENSEID, expenseId);

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        List<Long> expenseGroups = new ArrayList<>();
        while (!cursor.isAfterLast())
        {
            long expenseGroupId = cursor.getLong(cursor.getColumnIndex(COL_EXPENSEGROUPID));

            expenseGroups.add(expenseGroupId);
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return expenseGroups;
    }

    @Override
    public boolean addExpenseToGroup(long expenseGroupId, long expenseId, int quantity)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_EXPENSEGROUPID, expenseGroupId);
        values.put(COL_EXPENSEID, expenseId);
        values.put(COL_NUMEXPENSES, quantity);

        final long insert = db.insert(TABLENAME, null, values);
        db.close();

        return (insert != -1);
    }

    @Override
    public boolean removeExpenseFromGroup(long expenseId, long expenseGroupId)
    {
        SQLiteDatabase db = getWritableDatabase();

        String where = String.format(Locale.getDefault(),
                "%s = %d AND %s = %d",
                COL_EXPENSEID, expenseId,
                COL_EXPENSEGROUPID, expenseGroupId);
        final int delete = db.delete(TABLENAME, where, null);
        db.close();

        return (delete == 1);
    }

    @Override
    public long deleteExpense(long expenseId)
    {
        SQLiteDatabase db = getWritableDatabase();

        String where = String.format(Locale.getDefault(),"%s = %d", COL_EXPENSEID, expenseId);
        final int delete = db.delete(TABLENAME, where, null);
        db.close();

        return delete;
    }

    @Override
    public long deleteExpenseGroup(long expenseGroupId)
    {
        SQLiteDatabase db = getWritableDatabase();

        String where = String.format(Locale.getDefault(),"%s = %d", COL_EXPENSEGROUPID, expenseGroupId);
        final int delete = db.delete(TABLENAME, where, null);
        db.close();

        return delete;
    }
}
