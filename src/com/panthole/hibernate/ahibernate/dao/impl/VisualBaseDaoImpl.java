package com.panthole.hibernate.ahibernate.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class VisualBaseDaoImpl
{

    public static final String MSG_ID = "MSG_ID";

    public static final String USER_ID = "USER_ID";

    public static final String SENDER_ID = "SENDER_ID";

    public static final String TITLE = "TITLE";

    public static final String CONTENT = "CONTENT";

    public static final String WEBSITE = "WEBSITE";

    public static final String MSG_TYPE = "MSG_TYPE";

    public static final String REMIND_DATE = "REMIND_DATE";

    public static final String MSG_SOURCE = "MSG_SOURCE";

    public static final String ACCEPT_WAY = "ACCEPT_WAY";

    public static final String REPLY_STATUS = "REPLY_STATUS";

    public static final String MSG_STATUS = "MSG_STATUS";

    public static final String SENDER_NAME = "SENDER_NAME";

    public static final String SENDER_INST_NAME = "SENDER_INST_NAME";

    public static final String MSG_GRID = "MSG_GRID";

    public static final String KEY_SEARCH = "searchData";

    private static final String TAG = "MessagesDbAdapter";

    private DatabaseHelper mDbHelper;

    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "HealthGrid2";

    private static final String FTS_VIRTUAL_TABLE = "T_V_MESSAGE";

    private static final int DATABASE_VERSION = 1;

    // Create a FTS3 Virtual Table for fast searches
    private static final String DATABASE_CREATE = "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE + " USING fts3(" + MSG_ID
            + "," + USER_ID + "," + SENDER_ID + "," + TITLE + "," + CONTENT + "," + WEBSITE + "," + MSG_TYPE + ","
            + REMIND_DATE + "," + MSG_SOURCE + "," + ACCEPT_WAY + "," + REPLY_STATUS + "," + MSG_STATUS + ","
            + SENDER_NAME + "," + SENDER_INST_NAME + "," + MSG_GRID + "," + KEY_SEARCH + "," + " UNIQUE (" + MSG_ID
            + "));";

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper
    {

        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }
    }

    public VisualBaseDaoImpl(Context ctx)
    {
        this.mCtx = ctx;
    }

    public VisualBaseDaoImpl open() throws SQLException
    {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        if (mDbHelper != null)
        {
            mDbHelper.close();
        }
    }

    public long createCustomer(String MSG_ID, String USER_ID, String SENDER_ID, String TITLE, String CONTENT,
            String WEBSITE, String MSG_TYPE, String REMIND_DATE, String MSG_SOURCE, String ACCEPT_WAY,
            String REPLY_STATUS, String MSG_STATUS, String SENDER_NAME, String SENDER_INST_NAME, String MSG_GRID)
    {

        ContentValues initialValues = new ContentValues();
        String searchValue = TITLE + " " + CONTENT + " " + SENDER_NAME + " " + SENDER_INST_NAME;
        initialValues.put(MSG_ID, MSG_ID);
        initialValues.put(USER_ID, USER_ID);
        initialValues.put(SENDER_ID, SENDER_ID);
        initialValues.put(TITLE, TITLE);
        initialValues.put(CONTENT, CONTENT);
        initialValues.put(WEBSITE, WEBSITE);
        initialValues.put(MSG_TYPE, MSG_TYPE);
        initialValues.put(REMIND_DATE, REMIND_DATE);
        initialValues.put(MSG_SOURCE, MSG_SOURCE);
        initialValues.put(ACCEPT_WAY, ACCEPT_WAY);
        initialValues.put(REPLY_STATUS, REPLY_STATUS);
        initialValues.put(MSG_STATUS, MSG_STATUS);
        initialValues.put(SENDER_NAME, SENDER_NAME);
        initialValues.put(SENDER_INST_NAME, SENDER_INST_NAME);
        initialValues.put(MSG_GRID, MSG_GRID);
        initialValues.put(KEY_SEARCH, searchValue);

        return mDb.insert(FTS_VIRTUAL_TABLE, null, initialValues);
    }

    public Cursor searchCustomer(String inputText) throws SQLException
    {
        Log.w(TAG, inputText);
        String query = "SELECT docid as _id," + MSG_ID + "," + USER_ID + "," + SENDER_ID + "," + TITLE + "," + CONTENT
                + "," + WEBSITE + "," + MSG_TYPE + "," + REMIND_DATE + "," + MSG_SOURCE + "," + ACCEPT_WAY + ","
                + REPLY_STATUS + "," + MSG_STATUS + "," + SENDER_NAME + "," + SENDER_INST_NAME + "," + MSG_GRID
                + " from " + FTS_VIRTUAL_TABLE + " where " + KEY_SEARCH + " MATCH '" + inputText + "';";
        Log.w(TAG, query);
        Cursor mCursor = mDb.rawQuery(query, null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public boolean deleteAllCustomers()
    {

        int doneDelete = 0;
        doneDelete = mDb.delete(FTS_VIRTUAL_TABLE, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

}