package de.tjanneck.migronitor.de.tjanneck.migronitor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Programmieren on 24.11.2014.
 */
public class MigronitorOutDataSource {

    private final MigronitorOutDbHelper dbHelper;
    private final String[] allColumns = {MigronitorOutDbHelper.COLUMN_SCHMERZSTAERKE_ID,
            MigronitorOutDbHelper.COLUMN_SCHMERZSTAERKE_DATE, MigronitorOutDbHelper.COLUMN_SCHMERZSTAERKE_STAERKE, MigronitorOutDbHelper.COLUMN_SCHMERZSTAERKE_DELETED};
    private final SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    // Database fields
    private SQLiteDatabase database;

    public MigronitorOutDataSource(Context context, int dbv) {
        dbHelper = new MigronitorOutDbHelper(context, "migronitor." + dbv + ".db");
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createSchmerzaenderung(Schmerzaenderung s) {
        ContentValues values = new ContentValues();
        values.put(MigronitorOutDbHelper.COLUMN_SCHMERZSTAERKE_ID, s.getId());
        values.put(MigronitorOutDbHelper.COLUMN_SCHMERZSTAERKE_DATE, dateFormater.format(s.getDatum()));
        values.put(MigronitorOutDbHelper.COLUMN_SCHMERZSTAERKE_STAERKE, s.getStaerke());
        values.put(MigronitorOutDbHelper.COLUMN_SCHMERZSTAERKE_DELETED, false);
        database.insert(MigronitorOutDbHelper.TABLE_SCHMERZSTAERKE, null, values);
    }

    public void updateSchmerzaenderung(Schmerzaenderung s) {
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(MigronitorOutDbHelper.COLUMN_SCHMERZSTAERKE_ID, s.getId());
        values.put(MigronitorOutDbHelper.COLUMN_SCHMERZSTAERKE_DATE, dateFormater.format(s.getDatum()));
        values.put(MigronitorOutDbHelper.COLUMN_SCHMERZSTAERKE_STAERKE, s.getStaerke());
        values.put(MigronitorOutDbHelper.COLUMN_SCHMERZSTAERKE_DELETED, s.isDeleted());
        //Which row to update, based on the ID
        String selection = MigronitorOutDbHelper.COLUMN_SCHMERZSTAERKE_ID + " = ?";
        String[] selectionArgs = {s.getId() + ""};

        database.update(
                MigronitorOutDbHelper.TABLE_SCHMERZSTAERKE,
                values,
                selection,
                selectionArgs);
    }


    public List<Schmerzaenderung> getAllSchmerzaenderungen() {
        List<Schmerzaenderung> se = new ArrayList<Schmerzaenderung>();

        Cursor cursor = database.query(MigronitorOutDbHelper.TABLE_SCHMERZSTAERKE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Schmerzaenderung s = cursorToSchmerzaenderung(cursor);
            se.add(s);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return se;
    }

    public List<Schmerzaenderung> getActiveSchmerzaenderungen() {
        List<Schmerzaenderung> se = new ArrayList<Schmerzaenderung>();
        String[] selectionargs = {0 + ""};

        Cursor cursor = database.query(MigronitorOutDbHelper.TABLE_SCHMERZSTAERKE,
                allColumns, MigronitorOutDbHelper.COLUMN_SCHMERZSTAERKE_DELETED + "=?", selectionargs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Schmerzaenderung s = cursorToSchmerzaenderung(cursor);
            se.add(s);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return se;
    }

    private Schmerzaenderung cursorToSchmerzaenderung(Cursor cursor) {
        Schmerzaenderung s = new Schmerzaenderung();
        try {
            s.setId(cursor.getInt(0));
            s.setDatum(dateFormater.parse(cursor.getString(1)));
            s.setStaerke(cursor.getInt(2));
            s.setDeleted(Boolean.getBoolean(cursor.getString(3)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return s;
    }
}