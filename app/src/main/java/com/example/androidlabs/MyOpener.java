package com.example.androidlabs;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyOpener extends SQLiteOpenHelper{
    protected final static String DATABASE_NAME = "ChatDB";
    protected final static int VERSION_NUM = 1;
    public final static  String TABLE_NAME = "ChatTable";
    public final static String COL_MESSAGE = "MESSAGE";
    public final static String COL_SEND_RECEIVE = "SEND_OR_RECEIVE";
    public final static String COL_ID = "_ID";

    //    public final static String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME
//            + "( "+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_MESSAGE+" TEXT, "+COL_SEND_RECEIVE+" INTEGER)";

    //constructor
    public MyOpener(Context ctx) {
        super(ctx, DATABASE_NAME, null,VERSION_NUM);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME
                + "( "+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_MESSAGE+" TEXT, "+COL_SEND_RECEIVE+" INTEGER)"); //  boolean use integer
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // write information to log
        Log.i("Database upgrade", "Old version:" + oldVersion + "New version:" + newVersion);
        //drop the old table
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        //create new table
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // write information to log
        Log.i("Database downgrade", "Old version:" + oldVersion + "New version:" + newVersion);
        //drop the old table
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        //create new table
        onCreate(db);
    }

}
