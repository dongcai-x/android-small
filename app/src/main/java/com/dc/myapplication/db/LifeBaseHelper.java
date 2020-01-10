package com.dc.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dc.myapplication.db.LifeDbSchema.LifeTable;

public class LifeBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "lifeBase.db";

    public LifeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + LifeTable.NAME +
                "(" +
                " _id integer primary key autoincrement, " +
                LifeTable.Cols.UUID + ", " +
                LifeTable.Cols.TITLE + ", " +
                LifeTable.Cols.DATE + ", " +
                LifeTable.Cols.DESCRIPTION + "," +
                LifeTable.Cols.STAR +
                        ")"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
