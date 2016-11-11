package com.example.luke.jocelyndressup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by lwong on 9/28/2015.
 */

//modified by Luke Harris for use in the JocelynDressUp app
public class DBAdapter {

    //columns that will be used
    //outfits
    static final String KEY_ROWID = "_id";
    static final String KEY_NAME = "name";
    static final String KEY_HEAD = "head";
    static final String KEY_TORSO = "torso";
    static final String KEY_LEGS = "legs";
    static final String KEY_FEET = "feet";

    //items table
    static final String KEY_ROW_ITEM_ID = "_id";
    static final String KEY_ITEM_NAME = "item_name";
    static final String KEY_PRICE = "price";
    static final String KEY_VENDOR_NAME = "vendor_name";
    static final String KEY_SENDER_ID = "sender_id";
    static final String KEY_TYPE = "type";
    //name of the adapter
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLE_OUTFITS = "outfits";
    static final String DATABASE_TABLE_ITEMS = "items";
    static final int DATABASE_VERSION = 5;

    static final String DATABASE_CREATE =
            "create table outfits (_id integer primary key autoincrement, "
                    + "name text not null, head integer, torso integer, legs integer, feet integer );";
    static final String ITEMS_TABLE_CREATE =  "create table items (_id integer primary key autoincrement, "
            + "item_name text, price DECIMAL (6,2), vendor_name text, sender_id integer, type text );";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
                db.execSQL(ITEMS_TABLE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //default feet
            ContentValues values1 = new ContentValues();
            values1.put(KEY_ITEM_NAME, "yellow shoes");
            values1.put(KEY_PRICE, 15.99);
            values1.put(KEY_VENDOR_NAME, "winners");
            values1.put(KEY_SENDER_ID, 0);
            values1.put(KEY_TYPE, "feet");
            db.insert(DATABASE_TABLE_ITEMS, null, values1);

            ContentValues values2 = new ContentValues();
            values2.put(KEY_ITEM_NAME, "blue shoes");
            values2.put(KEY_PRICE, 20.99);
            values2.put(KEY_VENDOR_NAME, "winners");
            values2.put(KEY_SENDER_ID, 0);
            values2.put(KEY_TYPE, "feet");
            db.insert(DATABASE_TABLE_ITEMS, null, values2);

            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_NAME, "brown shoes");
            values.put(KEY_PRICE, 10.99);
            values.put(KEY_VENDOR_NAME, "winners");
            values.put(KEY_SENDER_ID, 0);
            values.put(KEY_TYPE, "feet");
            db.insert(DATABASE_TABLE_ITEMS, null, values);



            //default heads
            ContentValues values5 = new ContentValues();
            values5.put(KEY_ITEM_NAME, "yellow head");
            values5.put(KEY_PRICE, 10.0);
            values5.put(KEY_VENDOR_NAME, "winners");
            values5.put(KEY_SENDER_ID, 0);
            values5.put(KEY_TYPE, "head");
            db.insert(DATABASE_TABLE_ITEMS, null, values5);

            ContentValues values6 = new ContentValues();
            values6.put(KEY_ITEM_NAME, "blue head");
            values6.put(KEY_PRICE, 5.0);
            values6.put(KEY_VENDOR_NAME, "winners");
            values6.put(KEY_SENDER_ID, 0);
            values6.put(KEY_TYPE, "head");
            db.insert(DATABASE_TABLE_ITEMS, null, values6);

            ContentValues values4 = new ContentValues();
            values4.put(KEY_ITEM_NAME, "brown head");
            values4.put(KEY_PRICE, 0.0);
            values4.put(KEY_VENDOR_NAME, "winners");
            values4.put(KEY_SENDER_ID, 0);
            values4.put(KEY_TYPE, "head");
            db.insert(DATABASE_TABLE_ITEMS, null, values4);


            //default legs
            ContentValues values8 = new ContentValues();
            values8.put(KEY_ITEM_NAME, "yellow pants");
            values8.put(KEY_PRICE, 26.0);
            values8.put(KEY_VENDOR_NAME, "winners");
            values8.put(KEY_SENDER_ID, 0);
            values8.put(KEY_TYPE, "legs");
            db.insert(DATABASE_TABLE_ITEMS, null, values8);

            ContentValues values9 = new ContentValues();
            values9.put(KEY_ITEM_NAME, "blue pants");
            values9.put(KEY_PRICE, 6.67);
            values9.put(KEY_VENDOR_NAME, "winners");
            values9.put(KEY_SENDER_ID, 0);
            values9.put(KEY_TYPE, "legs");
            db.insert(DATABASE_TABLE_ITEMS, null, values9);

            ContentValues values7 = new ContentValues();
            values7.put(KEY_ITEM_NAME, "orange pants");
            values7.put(KEY_PRICE, 16.0);
            values7.put(KEY_VENDOR_NAME, "winners");
            values7.put(KEY_SENDER_ID, 0);
            values7.put(KEY_TYPE, "legs");
            db.insert(DATABASE_TABLE_ITEMS, null, values7);


            //default torsos
            ContentValues values11 = new ContentValues();
            values11.put(KEY_ITEM_NAME, "yellow shirt");
            values11.put(KEY_PRICE, 69.97);
            values11.put(KEY_VENDOR_NAME, "winners");
            values11.put(KEY_SENDER_ID, 0);
            values11.put(KEY_TYPE, "torso");
            db.insert(DATABASE_TABLE_ITEMS, null, values11);

            ContentValues values12 = new ContentValues();
            values12.put(KEY_ITEM_NAME, "blue shirt");
            values12.put(KEY_PRICE, 16.67);
            values12.put(KEY_VENDOR_NAME, "winners");
            values12.put(KEY_SENDER_ID, 0);
            values12.put(KEY_TYPE, "torso");
            db.insert(DATABASE_TABLE_ITEMS, null, values12);

            ContentValues values10 = new ContentValues();
            values10.put(KEY_ITEM_NAME, "brown shirt");
            values10.put(KEY_PRICE, 6.67);
            values10.put(KEY_VENDOR_NAME, "winners");
            values10.put(KEY_SENDER_ID, 0);
            values10.put(KEY_TYPE, "torso");
            db.insert(DATABASE_TABLE_ITEMS, null, values10);





        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS outfits");
            db.execSQL("DROP TABLE IF EXISTS items");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //inserts an outfit into the database
    public long insertOutfit(String name, int head, int torso, int legs, int feet)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_HEAD, head);
        initialValues.put(KEY_TORSO, torso);
        initialValues.put(KEY_LEGS, legs);
        initialValues.put(KEY_FEET, feet);
        return db.insert(DATABASE_TABLE_OUTFITS, null, initialValues);
    }

    //inserts an item into the database
    public long insertItem(String name, float price, String vendName, int senderID, String type)
    {
        ContentValues values10 = new ContentValues();
        values10.put(KEY_ITEM_NAME, name);
        values10.put(KEY_PRICE, price);
        values10.put(KEY_VENDOR_NAME, vendName);
        values10.put(KEY_SENDER_ID, senderID);
        values10.put(KEY_TYPE, type);
        return db.insert(DATABASE_TABLE_ITEMS, null, values10);
    }

    //deletes an outfit
    public boolean deleteOutfit(long rowId)
    {
        return db.delete(DATABASE_TABLE_OUTFITS, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //deletes an item
    public boolean deleteItem(long rowId)
    {
        return db.delete(DATABASE_TABLE_ITEMS, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //gets all outfits from the database
    public Cursor getAllOutfits()
    {
        return db.query(DATABASE_TABLE_OUTFITS, new String[] {KEY_ROWID, KEY_NAME,
                KEY_HEAD, KEY_TORSO,KEY_LEGS,KEY_FEET}, null, null, null, null, null);
    }

    //gets all items from the database
    public Cursor getAllItems()
    {
        return db.query(DATABASE_TABLE_ITEMS, new String[] {KEY_ROWID, KEY_ITEM_NAME,
                KEY_PRICE, KEY_VENDOR_NAME,KEY_SENDER_ID,KEY_TYPE}, null, null, null, null, null);
    }

    //gets an outfit by id from the database
    public Cursor getOutfit(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_OUTFITS, new String[] {KEY_ROWID,
                                KEY_NAME, KEY_HEAD, KEY_TORSO,KEY_LEGS,KEY_FEET}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //gets an item by id from the database
    public Cursor getItem(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_ITEMS, new String[] {KEY_ROWID,
                                KEY_ITEM_NAME, KEY_PRICE, KEY_VENDOR_NAME,KEY_SENDER_ID,KEY_TYPE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //gets an outfit by name from the database
    public boolean deleteOutfitByName(String outfitName) throws SQLException
    {
        return db.delete(DATABASE_TABLE_OUTFITS, "name = \""+outfitName+ "\"", null) > 0;
    }

    //deletes an outfit by name from the database
    public Cursor getOutfitByName(String outfitName) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_OUTFITS, new String[] {KEY_ROWID,
                                KEY_NAME, KEY_HEAD, KEY_TORSO,KEY_LEGS,KEY_FEET}, "name = \""+outfitName+ "\"", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //get a group of items by type
    public Cursor getItemsByType(String type) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_ITEMS, new String[] {KEY_ROWID,
                                KEY_ITEM_NAME, KEY_PRICE, KEY_VENDOR_NAME,KEY_SENDER_ID,KEY_TYPE}, "type = \""+type+ "\"", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates an outfit---
    public boolean updateOutfit(long rowId, String name, int head,int torso, int legs, int feet)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_HEAD, head);
        args.put(KEY_TORSO, torso);
        args.put(KEY_LEGS, legs);
        args.put(KEY_FEET, feet);
        return db.update(DATABASE_TABLE_OUTFITS, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---updates an item---
    public boolean updateItem(long rowId, String name, float price, String vendName, int senderID, String type)
    {

        ContentValues args = new ContentValues();
        args.put(KEY_ITEM_NAME, name);
        args.put(KEY_PRICE, price);
        args.put(KEY_VENDOR_NAME, vendName);
        args.put(KEY_SENDER_ID, senderID);
        args.put(KEY_TYPE, type);
        return db.update(DATABASE_TABLE_ITEMS, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}
