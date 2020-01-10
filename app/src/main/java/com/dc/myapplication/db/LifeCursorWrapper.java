package com.dc.myapplication.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.dc.myapplication.db.LifeDbSchema.LifeTable;
import com.dc.myapplication.model.Life;

import java.util.Date;
import java.util.UUID;

public class LifeCursorWrapper extends CursorWrapper {

    public LifeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Life getLife() {

        String uuidString = getString(getColumnIndex(LifeTable.Cols.UUID));
        String title = getString(getColumnIndex(LifeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(LifeTable.Cols.DATE));
        String Description = getString(getColumnIndex(LifeTable.Cols.DESCRIPTION));
        int isStar = getInt(getColumnIndex(LifeTable.Cols.STAR));

        Life life = new Life(UUID.fromString(uuidString));
        life.setTitle(title);
        life.setDate(new Date(date));
        life.setDescription(Description);
        life.setStar(isStar != 0);

        return life;
    }
}
