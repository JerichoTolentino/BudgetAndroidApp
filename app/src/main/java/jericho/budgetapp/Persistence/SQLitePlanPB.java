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

import jericho.budgetapp.Persistence.Interfaces.PlanPBPersistence;

public class SQLitePlanPB extends SQLiteOpenHelper implements PlanPBPersistence
{

    public static final String TABLENAME = "PlanPB";
    public static final String COL_ID = "ID";
    public static final String COL_PLANID = "PlanID";
    public static final String COL_PERIODICBUDGETID = "PeriodicBudgetID";

    public SQLitePlanPB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //Create Plan-PeriodicBudget Bridge-Table
        String sql = "CREATE TABLE '" + TABLENAME + "'('"
                + COL_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '"
                + COL_PLANID + "' INTEGER NOT NULL, '"
                + COL_PERIODICBUDGETID + "' INTEGER NOT NULL"
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
    public List<Long> getPeriodicBudgetsIn(long planId)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = String.format(Locale.getDefault(),
                "SELECT * FROM %s WHERE %s = %d", TABLENAME, COL_PLANID, planId);

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        List<Long> periodicBudgets = new ArrayList<>();
        while (!cursor.isAfterLast())
        {
            long budgetId = cursor.getLong(cursor.getColumnIndex(COL_PERIODICBUDGETID));
            periodicBudgets.add(budgetId);

            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return periodicBudgets;
    }

    @Override
    public long getPlanFor(long periodicBudgetId)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = String.format(Locale.getDefault(),
                "SELECT * FROM %s WHERE %s = %d", TABLENAME, COL_PERIODICBUDGETID, periodicBudgetId);

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        long planId = cursor.getLong(cursor.getColumnIndex(COL_PLANID));

        cursor.close();
        db.close();

        return planId;
    }

    @Override
    public boolean addPeriodicBudgetTo(long periodicBudgetId, long planId)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_PERIODICBUDGETID, periodicBudgetId);
        values.put(COL_PLANID, planId);

        final long insert = db.insert(TABLENAME, null, values);
        db.close();

        return (insert != -1);
    }

    @Override
    public boolean removePeriodicBudgetFrom(long periodicBudgetId, long planId)
    {
        SQLiteDatabase db = getWritableDatabase();

        String where = String.format(Locale.getDefault(),
                "%s = %d AND %s = %d",
                COL_PERIODICBUDGETID, periodicBudgetId,
                COL_PLANID, planId);
        final int delete = db.delete(TABLENAME, where, null);
        db.close();

        return (delete == 1);
    }

    @Override
    public long deletePeriodicBudget(long periodicBudgetId)
    {
        SQLiteDatabase db = getWritableDatabase();

        String where = String.format(Locale.getDefault(), "%s = %d", COL_PERIODICBUDGETID, periodicBudgetId);
        final int delete = db.delete(TABLENAME, where, null);
        db.close();

        return delete;
    }

    @Override
    public long deletePlan(long planId)
    {
        SQLiteDatabase db = getWritableDatabase();

        String where = String.format(Locale.getDefault(), "%s = %d", COL_PLANID, planId);
        final int delete = db.delete(TABLENAME, where, null);
        db.close();

        return delete;
    }
}
