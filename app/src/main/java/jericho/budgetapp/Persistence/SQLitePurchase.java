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

import jericho.budgetapp.Model.Purchase;
import jericho.budgetapp.Persistence.Interfaces.PurchasePersistence;

public class SQLitePurchase extends SQLiteOpenHelper implements PurchasePersistence
{

    // Purchase Table Info
    private static final String TABLENAME = "Purchases";
    private static final String COL_ID = "ID";
    private static final String COL_ITEMNAME = "ItemName";
    private static final String COL_ITEMPRICE = "ItemPrice";
    private static final String COL_DATE = "Date";
    private static final String COL_QUANTITY = "Quantity";

    public SQLitePurchase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //Create Purchases Table
        String sql = "CREATE TABLE '" + TABLENAME + "'('"
                + COL_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '"
                + COL_ITEMNAME + "' TEXT NOT NULL, '"
                + COL_ITEMPRICE + "' INTEGER NOT NULL, '"
                + COL_DATE + "' TEXT NOT NULL, '"
                + COL_QUANTITY + "' INTEGER NOT NULL"
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
    public Purchase getPurchase(long id)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = String.format(Locale.getDefault(),
                "SELECT * FROM %s WHERE %s = %d;", TABLENAME, COL_ID, id);

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        long purchaseID = cursor.getLong(cursor.getColumnIndex(COL_ID));
        String itemName = cursor.getString(cursor.getColumnIndex(COL_ITEMNAME));
        long itemPrice = cursor.getLong(cursor.getColumnIndex(COL_ITEMPRICE));
        String date = cursor.getString(cursor.getColumnIndex(COL_DATE));
        int quantity = cursor.getInt(cursor.getColumnIndex(COL_QUANTITY));

        Date parsedDate;
        try
        {
            parsedDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).parse(date);
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

        return new Purchase(purchaseID, itemName, itemPrice, parsedDate, quantity);
    }

    @Override
    public long addPurchase(Purchase purchase)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_ITEMNAME, purchase.getItemName());
        values.put(COL_ITEMPRICE, purchase.getItemPrice());
        values.put(COL_DATE, DateFormat.getDateTimeInstance(
                        DateFormat.SHORT, DateFormat.MEDIUM).format(purchase.getDate()));
        values.put(COL_QUANTITY, purchase.getQuantity());

        final long insert = db.insert(TABLENAME, null, values);
        db.close();

        return insert;
    }

    @Override
    public boolean updatePurchase(Purchase purchase)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_ITEMNAME, purchase.getItemName());
        values.put(COL_ITEMPRICE, purchase.getItemPrice());
        values.put(COL_DATE, DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.MEDIUM).format(purchase.getDate()));
        values.put(COL_QUANTITY, purchase.getQuantity());

        String where = String.format(Locale.getDefault(), "%s = %d", COL_ID, purchase.getId());
        final long update = db.update(TABLENAME, values, where, null);
        db.close();

        return (update == -1);
    }

    @Override
    public boolean deletePurchase(long id)
    {
        SQLiteDatabase db = getWritableDatabase();

        String where = String.format(Locale.getDefault(), "%s = %d", COL_ID, id);
        final int delete = db.delete(TABLENAME, where, null);
        db.close();

        return (delete == 1);
    }
}
