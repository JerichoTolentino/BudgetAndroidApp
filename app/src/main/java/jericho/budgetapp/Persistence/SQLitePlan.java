package jericho.budgetapp.Persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

import jericho.budgetapp.Persistence.Interfaces.PlanPersistence;
import jericho.budgetapp.Model.Plan;

public class SQLitePlan extends SQLiteOpenHelper implements PlanPersistence
{

    //Plans Table Info
    private static final String TABLENAME = "Plans";
    private static final String COL_ID = "ID";
    private static final String COL_ANNUALINCOME = "AnnualIncome";
    private static final String COL_ANNUALEXPENSES = "AnnualExpenses";
    private static final String COL_ANNUALSAVINGS = "AnnualSavings";
    private static final String COL_NAME = "Name";
    private static final String COL_ANNUALBUDGET = "AnnualBudget";
    private static final String COL_MONTHLYBUDGETID = "MonthlyBudgetID";
    private static final String COL_WEEKLYBUDGETID = "WeeklyBudgetID";

    public SQLitePlan(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //Create Plans Table
        String sql = "CREATE TABLE '" + TABLENAME + "'('"
                + COL_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '"
                + COL_ANNUALINCOME + "' INTEGER NOT NULL, '"
                + COL_ANNUALEXPENSES + "' INTEGER NOT NULL, '"
                + COL_ANNUALSAVINGS + "' INTEGER NOT NULL, '"
                + COL_NAME + "' TEXT UNIQUE NOT NULL, '"
                + COL_ANNUALBUDGET + "' INTEGER NOT NULL, '"
                + COL_MONTHLYBUDGETID + "' INTEGER NOT NULL, '"
                + COL_WEEKLYBUDGETID + "' INTEGER NOT NULL"
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
    public Plan getPlan(long id)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = String.format(Locale.getDefault(),
                "SELECT * FROM %s WHERE %s = %d;", TABLENAME, COL_ID, id);

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        long planID = cursor.getLong(cursor.getColumnIndex(COL_ID));
        String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
        long annualIncome = cursor.getLong(cursor.getColumnIndex(COL_ANNUALINCOME));
        long annualExpenses = cursor.getLong(cursor.getColumnIndex(COL_ANNUALEXPENSES));
        long annualSavings = cursor.getLong(cursor.getColumnIndex(COL_ANNUALSAVINGS));
        long annualBudget = cursor.getLong(cursor.getColumnIndex(COL_ANNUALBUDGET));

        cursor.close();
        db.close();

        return new Plan(planID, name, annualIncome, annualExpenses, annualSavings, annualBudget);
    }

    @Override
    public Plan getPlan(String name)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = String.format(Locale.getDefault(),
                "SELECT * FROM %s WHERE %s = %s;", TABLENAME, COL_NAME, name);

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
        String planName = cursor.getString(cursor.getColumnIndex(COL_NAME));
        long annualIncome = cursor.getLong(cursor.getColumnIndex(COL_ANNUALINCOME));
        long annualExpenses = cursor.getLong(cursor.getColumnIndex(COL_ANNUALEXPENSES));
        long annualSavings = cursor.getLong(cursor.getColumnIndex(COL_ANNUALSAVINGS));
        long annualBudget = cursor.getLong(cursor.getColumnIndex(COL_ANNUALBUDGET));

        cursor.close();
        db.close();

        return new Plan(id, planName, annualIncome, annualExpenses, annualSavings, annualBudget);
    }

    @Override
    public long addPlan(Plan plan)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_ID, plan.getId());
        values.put(COL_NAME, plan.getName());
        values.put(COL_ANNUALINCOME, plan.getAnnualIncome());
        values.put(COL_ANNUALEXPENSES, plan.getAnnualExpenses());
        values.put(COL_ANNUALSAVINGS, plan.getAnnualSavings());
        values.put(COL_ANNUALBUDGET, plan.getAnnualBudget());

        final long insert = db.insert(TABLENAME, null, values);
        db.close();

        return insert;
    }

    @Override
    public boolean updatePlan(Plan plan)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_ID, plan.getId());
        values.put(COL_NAME, plan.getName());
        values.put(COL_ANNUALINCOME, plan.getAnnualIncome());
        values.put(COL_ANNUALEXPENSES, plan.getAnnualExpenses());
        values.put(COL_ANNUALSAVINGS, plan.getAnnualSavings());
        values.put(COL_ANNUALBUDGET, plan.getAnnualBudget());

        String where = String.format(Locale.getDefault(), "%s = %d", COL_ID, plan.getId());
        final int update = db.update(TABLENAME, values, where, null);
        db.close();

        return (update == 1);
    }

    @Override
    public boolean deletePlan(long id)
    {
        SQLiteDatabase db = getWritableDatabase();

        String where = String.format(Locale.getDefault(), "%s = %d", COL_ID, id);
        final int delete = db.delete(TABLENAME, where, null);
        db.close();

        return (delete == 1);
    }

    @Override
    public boolean deletePlan(String name)
    {
        SQLiteDatabase db = getWritableDatabase();

        String where = String.format(Locale.getDefault(), "%s = '%s'", COL_NAME, name);
        final int delete = db.delete(TABLENAME, where, null);
        db.close();

        return (delete == 1);
    }
}
