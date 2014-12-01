package de.tjanneck.migronitor.de.tjanneck.migronitor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Programmieren on 24.11.2014.
 */
public class MigronitorDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_SCHMERZSTAERKE = "schmerzstaerke";
    public static final String COLUMN_SCHMERZSTAERKE_ID = "_id";
    public static final String COLUMN_SCHMERZSTAERKE_DATE = "datum";
    public static final String COLUMN_SCHMERZSTAERKE_STAERKE = "staerke";
    public static final String COLUMN_SCHMERZSTAERKE_DELETED = "deleted";
    // Database creation sql statement
    private static final String DATABASE_SCHMERZSTAERKE_CREATE = "create table "
            + TABLE_SCHMERZSTAERKE + "(" + COLUMN_SCHMERZSTAERKE_ID
            + " integer primary key, " + COLUMN_SCHMERZSTAERKE_DATE
            + " text not null, " + COLUMN_SCHMERZSTAERKE_STAERKE + " integer, " + COLUMN_SCHMERZSTAERKE_DELETED + " boolean);";
    private static final String DATABASE_NAME = "migronitor.db";
    private static final int DATABASE_VERSION = 1;


    public MigronitorDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_SCHMERZSTAERKE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_SCHMERZSTAERKE_CREATE);
    }

}