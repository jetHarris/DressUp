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
    static final String KEY_ROWID = "_id";
    static final String KEY_NAME = "name";
    static final String KEY_HEAD = "head";
    static final String KEY_TORSO = "torso";
    static final String KEY_LEGS = "legs";
    static final String KEY_FEET = "feet";
    //name of the adapter
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLE = "outfits";
    static final int DATABASE_VERSION = 2;

    static final String DATABASE_CREATE =
            "create table outfits (_id integer primary key autoincrement, "
                    + "name text not null, head integer, torso integer, legs integer, feet integer );";

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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS outfits");
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
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //deletes an outfit
    public boolean deleteOutfit(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //gets all outfits from the database
    public Cursor getAllOutfits()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_HEAD, KEY_TORSO,KEY_LEGS,KEY_FEET}, null, null, null, null, null);
    }

    //gets an outfit by id from the database
    public Cursor getOutfit(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_NAME, KEY_HEAD, KEY_TORSO,KEY_LEGS,KEY_FEET}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //gets an outfit by name from the database
    public Cursor getOutfitByName(String outfitName) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_NAME, KEY_HEAD, KEY_TORSO,KEY_LEGS,KEY_FEET}, "name = \""+outfitName+ "\"", null,
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
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}
