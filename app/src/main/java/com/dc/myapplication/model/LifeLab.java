package com.dc.myapplication.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dc.myapplication.db.LifeBaseHelper;
import com.dc.myapplication.db.LifeCursorWrapper;
import com.dc.myapplication.db.LifeDbSchema.LifeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LifeLab {

    private static LifeLab sLifeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static LifeLab getsLifeLab(Context context) {
        if (sLifeLab == null)
            sLifeLab = new LifeLab(context);
        return sLifeLab;
    }

    public void addLife(Life life) {

        ContentValues values = getContentValues(life);
        mDatabase.insert(LifeTable.NAME, null, values);
        //Crimes.add(life);
    }

    private LifeLab(Context context) {

        mContext = context.getApplicationContext();
        mDatabase = new LifeBaseHelper(mContext)
                .getWritableDatabase();

    }

    public void delLife(Life life) {

        mDatabase.delete("lives", LifeTable.Cols.UUID + " = ?",
                new String[] {life.getId().toString()}
                );
        //Crimes.remove(life);
    }

    public List<Life> getLives() {

        List<Life> lives = new ArrayList<>();
        LifeCursorWrapper cursor = queryLives(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                lives.add(cursor.getLife());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return lives;
    }

    public Life getLife(UUID id) {

        LifeCursorWrapper cursor = queryLives(
                LifeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getLife();
        } finally {
        cursor.close();
        }
    }

    public void updateLife(Life life) {

        String uuidString = life.getId().toString();
        ContentValues values = getContentValues(life);
        mDatabase.update(LifeTable.NAME, values,
                LifeTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private static ContentValues getContentValues(Life life) {

        ContentValues values = new ContentValues();
        values.put(LifeTable.Cols.UUID, life.getId().toString());
        values.put(LifeTable.Cols.TITLE, life.getTitle());
        values.put(LifeTable.Cols.DATE, life.getDate().getTime());
        values.put(LifeTable.Cols.DESCRIPTION, life.getDescription());
        values.put(LifeTable.Cols.STAR, life.isStar());
        return values;
    }

    private LifeCursorWrapper queryLives(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                LifeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new LifeCursorWrapper(cursor);
    }

    public File getPhotoFile(Life life) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, life.getPhotoFilename());
    }

}
