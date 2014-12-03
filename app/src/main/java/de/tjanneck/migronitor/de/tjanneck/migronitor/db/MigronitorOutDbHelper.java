package de.tjanneck.migronitor.de.tjanneck.migronitor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Programmieren on 24.11.2014.
 */
class MigronitorOutDbHelper extends SQLiteOpenHelper {

    //Schmerzaenderung
    public static final String TABLE_SCHMERZSTAERKE = "schmerzstaerke";
    public static final String COLUMN_SCHMERZSTAERKE_ID = "_id";
    public static final String COLUMN_SCHMERZSTAERKE_DATE = "datum";
    public static final String COLUMN_SCHMERZSTAERKE_STAERKE = "staerke";
    public static final String COLUMN_SCHMERZSTAERKE_DELETED = "deleted";

    //Atacken
    public static final String TABLE_ATTACKEN = "attacken";
    public static final String COLUMN_ATTACKEN_ID = "_id";
    public static final String COLUMN_ATTACKEN_DATESTART = "datumStart";
    public static final String COLUMN_ATTACKEN_DATEENDE = "datumEnde";
    public static final String COLUMN_ATTACKEN_BEMERKUNG = "bemerkung";
    // Database creation sql statement
    private static final String DATABASE_SCHMERZSTAERKE_CREATE = "create table "
            + TABLE_SCHMERZSTAERKE + "(" + COLUMN_SCHMERZSTAERKE_ID
            + " integer primary key, " + COLUMN_SCHMERZSTAERKE_DATE
            + " text not null, " + COLUMN_SCHMERZSTAERKE_STAERKE + " integer, " + COLUMN_SCHMERZSTAERKE_DELETED + " boolean);";

    private static final String DATABASE_ATTACKEN_CREATE = "create table "
            + TABLE_ATTACKEN + "(" + COLUMN_ATTACKEN_ID
            + " integer primary key, " + COLUMN_ATTACKEN_DATESTART
            + " text not null, " + COLUMN_ATTACKEN_DATEENDE + " text, " + COLUMN_ATTACKEN_BEMERKUNG + " text);";

    private static final String DATABASE_NAME = "migronitor.db";
    private static final int DATABASE_VERSION = 1;


    public MigronitorOutDbHelper(Context context, String dbName) {
        super(context, dbName, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_SCHMERZSTAERKE_CREATE);
        database.execSQL(DATABASE_ATTACKEN_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_SCHMERZSTAERKE_CREATE);
        db.execSQL(DATABASE_ATTACKEN_CREATE);
    }
}
