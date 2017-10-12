package com.homebit.androidtest.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.google.gson.Gson;
import com.homebit.androidtest.model.User.User;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pardypanda05 on 17/2/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "HomeBitTest";
    private static int DATABASE_VERSION = 1;

    private final static String TABLE_USER_DATA = "userData";


    private final static String COLUMN_USER_ID = "id";
    private final static String COLUMN_USER_DETAIL_JSON = "userDetails";

    private final static String TAG = "Sqlite exception ";


    /**
     * Construct a new database helper object
     * @param context The current context for the application or activity
     */


    public DatabaseHandler(Context context)
    {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
        //_openHelper = new DatabaseHandler(context);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("create table " + TABLE_USER_DATA + " ("+
                COLUMN_USER_ID +" Integer, " +
                COLUMN_USER_DETAIL_JSON+" text)");



        //_openHelper = this;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addUserDetails(ArrayList<User> arrayListUser)
    {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            for (int i = 0; i < arrayListUser.size(); i++) {
                User userDetail = arrayListUser.get(i);

                Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USER_DATA + " WHERE " + COLUMN_USER_ID + " = " + userDetail.getId() + "", null);
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {

                    ContentValues row = new ContentValues();
                    row.put(COLUMN_USER_ID, userDetail.getId());
                    row.put(COLUMN_USER_DETAIL_JSON, new Gson().toJson(userDetail, User.class));

                    //db.update(TABLE_KML_FILE,row, COLUMN_KMLFILE_FIREBASE_KEY + " = ?",new String[]{key});
                    db.update(TABLE_USER_DATA, row, COLUMN_USER_ID + " = '" + userDetail.getId() + "'", null);

                    System.out.println("SQL result Update -" + userDetail.getId());
                } else {

                    ContentValues row = new ContentValues();
                    row.put(COLUMN_USER_ID, userDetail.getId());
                    row.put(COLUMN_USER_DETAIL_JSON, new Gson().toJson(userDetail, User.class));

                    db.insert(TABLE_USER_DATA, null, row);

                    System.out.println("SQL result Add -" + userDetail.getId());
                }
                cursor.close();
            }
            db.close();
        }catch (SQLException e){
            System.out.println(TAG + " - "+e);
        }
    }
    public void signOutClearData()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+ TABLE_USER_DATA);
        db.close();
    }

}
