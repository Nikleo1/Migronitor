package de.tjanneck.migronitor.db;

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
    // Database creation sql statement
    private static final String DATABASE_SCHMERZSTAERKE_CREATE = "create table "
            + TABLE_SCHMERZSTAERKE + "(" + COLUMN_SCHMERZSTAERKE_ID
            + " integer primary key, " + COLUMN_SCHMERZSTAERKE_DATE
            + " text not null, " + COLUMN_SCHMERZSTAERKE_STAERKE + " integer, " + COLUMN_SCHMERZSTAERKE_DELETED + " boolean);";
    //Atacken
    public static final String TABLE_ATTACKEN = "attacken";
    public static final String COLUMN_ATTACKEN_ID = "_id";
    public static final String COLUMN_ATTACKEN_DATESTART = "datumStart";
    public static final String COLUMN_ATTACKEN_DATEENDE = "datumEnde";
    public static final String COLUMN_ATTACKEN_BEMERKUNG = "bemerkung";
    private static final String DATABASE_ATTACKEN_CREATE = "create table "
            + TABLE_ATTACKEN + "(" + COLUMN_ATTACKEN_ID
            + " integer primary key, " + COLUMN_ATTACKEN_DATESTART
            + " text not null, " + COLUMN_ATTACKEN_DATEENDE + " text, " + COLUMN_ATTACKEN_BEMERKUNG + " text);";
    // Schlaf
    public static final String TABLE_SCHLAFEN = "schlafen";
    public static final String COLUMN_SCHLAFEN_ID = "_id";
    public static final String COLUMN_SCHLAFEN_SCHLAFEN = "datumSchlafen";
    public static final String COLUMN_SCHLAFEN_WACH = "datumWach";
    private static final String DATABASE_SCHLAFEN_CREATE = "create table "
            + TABLE_SCHLAFEN + "(" + COLUMN_SCHLAFEN_ID
            + " integer primary key, " + COLUMN_SCHLAFEN_SCHLAFEN
            + " text not null, " + COLUMN_SCHLAFEN_WACH + " text);";
    // Medikamentenehmen
    public static final String TABLE_MNEHMEN = "medikamentenehmen";
    public static final String COLUMN_MNEHMEN_ID = "_id";
    public static final String COLUMN_MNEHMEN_DATUM = "datum";
    public static final String COLUMN_MNEHMEN_MID = "mid";
    public static final String COLUMN_MNEHMEN_MENGE = "menge";
    private static final String DATABASE_MNEHMEN_CREATE = "create table "
            + TABLE_MNEHMEN + "(" + COLUMN_MNEHMEN_ID
            + " integer primary key, " + COLUMN_MNEHMEN_DATUM
            + " text not null, " + COLUMN_MNEHMEN_MID + " integer, " + COLUMN_MNEHMEN_MENGE + " integer);";
    private static final String DATABASE_NAME = "migronitor.db";
    private static final int DATABASE_VERSION = 3;

    public MigronitorOutDbHelper(Context context, String dbName) {
        super(context, dbName, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_SCHMERZSTAERKE_CREATE);
        database.execSQL(DATABASE_ATTACKEN_CREATE);
        database.execSQL(DATABASE_SCHLAFEN_CREATE);
        database.execSQL(DATABASE_MNEHMEN_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_MNEHMEN_CREATE);
    }

}