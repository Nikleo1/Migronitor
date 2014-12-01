package de.tjanneck.migronitor.de.tjanneck.migronitor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Programmieren on 24.11.2014.
 */
public class MedikamentenDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_MEDIKAMENTE = "medikamente";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ACTIVE = "active";
    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_MEDIKAMENTE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_ACTIVE + " boolean);";
    private static final String DATABASE_NAME = "migronitorglobaldata.db";
    private static final int DATABASE_VERSION = 1;

    public MedikamentenDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_CREATE);
    }

}