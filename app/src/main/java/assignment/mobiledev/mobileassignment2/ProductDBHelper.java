/**
 * Author: Devante Wilson - 100554361
 *
 * Class takes care of the creation of the database table
 * and the population of that table with sample product data.
 */

package assignment.mobiledev.mobileassignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDBHelper extends SQLiteOpenHelper
{
    // define instance variables
    private static final String DATABASE_NAME = "ProductDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String PRODUCT_TABLE_NAME = "ProductTable";
    private String PRODUCT_ID = "ID";
    private String PRODUCT_NAME = "Name";
    private String PRODUCT_PRICE = "Price";
    private String PRODUCT_DESCRIPTION = "Description";

    // constructor
    public ProductDBHelper(Context context)
    {
        // instantiate parent class
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // create database table
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_PRODUCT_TABLE =
                "CREATE TABLE " + PRODUCT_TABLE_NAME + "("
                + PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PRODUCT_NAME +" TEXT,"
                + PRODUCT_DESCRIPTION +" TEXT,"
                + PRODUCT_PRICE +" DECIMAL"+")" + ";";
        db.execSQL(CREATE_PRODUCT_TABLE);

    }

    // receive product object and add it the database - an error returns a status object.
    public Boolean addProduct(Product prod)
    {
        try
        {
            // set database object
            SQLiteDatabase db = this.getWritableDatabase();
            // set content values object
            ContentValues values = new ContentValues();

            // insert values into the database
            values.put(PRODUCT_ID, prod.getProductId());
            values.put(PRODUCT_NAME, prod.getName());
            values.put(PRODUCT_DESCRIPTION, prod.getDescription());
            values.put(PRODUCT_PRICE, prod.getPrice());
            db.insertOrThrow(PRODUCT_TABLE_NAME, null, values);
            db.close();
            return Boolean.TRUE;
        }
        catch(SQLiteConstraintException e)
        {
            return Boolean.FALSE;
        }
    }

    // remove product from the database
    public Boolean deleteProduct(int productId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(PRODUCT_TABLE_NAME, PRODUCT_ID + " = "+productId, null);
        if (result == -1)
        {
            return Boolean.FALSE;
        }
        else
        {
            return  Boolean.TRUE;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // select values from database
    public Cursor getValues()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+PRODUCT_TABLE_NAME, null);
        return c;

    }
}