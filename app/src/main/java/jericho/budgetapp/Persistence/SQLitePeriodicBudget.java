package jericho.budgetapp.Persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import jericho.budgetapp.Model.PeriodicBudget;
import jericho.budgetapp.Persistence.Interfaces.PeriodicBudgetPersistence;

public class SQLitePeriodicBudget extends SQLiteOpenHelper implements PeriodicBudgetPersistence
{

    //Period Budgets Table info
    private static final String TABLENAME = "PeriodicBudgets";
    private static final String COL_ID = "ID";
    private static final String COL_NAME = "Name";
    private static final String COL_TOTALDAYS = "TotalDays";
    private static final String COL_TOTALBUDGET = "TotalBudget";
    private static final String COL_DAYSPASSED = "DaysPassed";
    private static final String COL_CURRENTBUDGET = "CurrentBudget";
    private static final String COL_SPENT = "Spent";
    private static final String COL_DATELASTCHECKED = "DateLastChecked";

    public SQLitePeriodicBudget(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //Create PeriodicBudgets Table
        String sql = "CREATE TABLE '" + TABLENAME + "'('"
                + COL_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '"
                + COL_NAME + "' TEXT NOT NULL, '"
                + COL_TOTALDAYS + "' INTEGER NOT NULL, '"
                + COL_TOTALBUDGET + "' INTEGER NOT NULL, '"
                + COL_DAYSPASSED + "' INTEGER NOT NULL, '"
                + COL_CURRENTBUDGET + "' INTEGER NOT NULL, '"
                + COL_SPENT + "' INTEGER NOT NULL, '"
                + COL_DATELASTCHECKED + "' TEXT NOT NULL"
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
    public PeriodicBudget getPeriodicBudget(long id)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = String.format(Locale.getDefault(),
                "SELECT * FROM %s WHERE %s = %d;", TABLENAME, COL_ID, id);

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
        int totalDays = cursor.getInt(cursor.getColumnIndex(COL_TOTALDAYS));
        long totalBudget = cursor.getLong(cursor.getColumnIndex(COL_TOTALBUDGET));
        int daysPassed = cursor.getInt(cursor.getColumnIndex(COL_DAYSPASSED));
        long currentBudget = cursor.getLong(cursor.getColumnIndex(COL_CURRENTBUDGET));
        long spent = cursor.getLong(cursor.getColumnIndex(COL_SPENT));
        String dateLastChecked = cursor.getString(cursor.getColumnIndex(COL_DATELASTCHECKED));

        Date parsedDate;
        try
        {
            parsedDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse(dateLastChecked);
        }
        catch (ParseException e)
        {
            throw new PersistenceException(e);
        }
        finally
        {
            cursor.close();
            db.close();
        }

        return new PeriodicBudget(id, totalDays, totalBudget, name, daysPassed, currentBudget, spent, parsedDate);
    }

    @Override
    public boolean addPeriodicBudget(PeriodicBudget periodicBudget)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME, periodicBudget.getName());
        values.put(COL_TOTALDAYS, periodicBudget.getTotalDays());
        values.put(COL_TOTALBUDGET, periodicBudget.getTotalBudget());
        values.put(COL_DAYSPASSED, periodicBudget.getDaysPassed());
        values.put(COL_CURRENTBUDGET, periodicBudget.getCurrentBudget());
        values.put(COL_SPENT, periodicBudget.getAmountSpent());
        values.put(COL_DATELASTCHECKED, DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.MEDIUM).format(periodicBudget.getDateLastChecked()));

        final long insert = db.insert(TABLENAME, null, values);
        db.close();

        return (insert != -1);
    }

    @Override
    public boolean updatePeriodicBudget(PeriodicBudget periodicBudget)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME, periodicBudget.getName());
        values.put(COL_TOTALDAYS, periodicBudget.getTotalDays());
        values.put(COL_TOTALBUDGET, periodicBudget.getTotalBudget());
        values.put(COL_DAYSPASSED, periodicBudget.getDaysPassed());
        values.put(COL_CURRENTBUDGET, periodicBudget.getCurrentBudget());
        values.put(COL_SPENT, periodicBudget.getAmountSpent());
        values.put(COL_DATELASTCHECKED, DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.MEDIUM).format(periodicBudget.getDateLastChecked()));

        String where = String.format(Locale.getDefault(), "%s = %d", COL_ID, periodicBudget.getId());
        final int update = db.update(TABLENAME, values, where, null);
        db.close();

        return (update == 1);
    }

    @Override
    public boolean deletePeriodicBudget(long id)
    {
        SQLiteDatabase db = getWritableDatabase();

        String where = String.format(Locale.getDefault(), "%s = %d", COL_ID, id);
        final int delete = db.delete(TABLENAME, where, null);
        db.close();

        return (delete == 1);
    }
}
