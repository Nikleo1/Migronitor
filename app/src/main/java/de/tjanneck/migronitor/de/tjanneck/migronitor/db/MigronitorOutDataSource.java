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
    public MigronitorOutDataSource(Context context, int dbv) {
        dbHelper = new MigronitorOutDbHelper(context, "migronitor." + dbv + ".db");
    }


    private final SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private final String[] allColumnsSchmerzStaerke = {MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_ID,
            MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_DATE, MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_STAERKE, MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_DELETED};
    private final String[] allColumnsAttacken = {MigronitorDbHelper.COLUMN_ATTACKEN_ID,
            MigronitorDbHelper.COLUMN_ATTACKEN_DATESTART, MigronitorDbHelper.COLUMN_ATTACKEN_DATEENDE, MigronitorDbHelper.COLUMN_ATTACKEN_BEMERKUNG};

    // Database fields
    private SQLiteDatabase database;


    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
    //Schmerzaenderungen

    public void createSchmerzaenderung(Schmerzaenderung s) {
        ContentValues values = new ContentValues();
        values.put(MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_ID, s.getId());
        values.put(MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_DATE, dateFormater.format(s.getDatum()));
        values.put(MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_STAERKE, s.getStaerke());
        values.put(MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_DELETED, false);
        database.insert(MigronitorDbHelper.TABLE_SCHMERZSTAERKE, null, values);
    }

    public void updateSchmerzaenderung(Schmerzaenderung s) {
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_ID, s.getId());
        values.put(MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_DATE, dateFormater.format(s.getDatum()));
        values.put(MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_STAERKE, s.getStaerke());
        values.put(MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_DELETED, s.isDeleted());
        //Which row to update, based on the ID
        String selection = MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_ID + " = ?";
        String[] selectionArgs = {s.getId() + ""};

        database.update(
                MigronitorDbHelper.TABLE_SCHMERZSTAERKE,
                values,
                selection,
                selectionArgs);
    }


    public List<Schmerzaenderung> getAllSchmerzaenderungen() {
        List<Schmerzaenderung> se = new ArrayList<Schmerzaenderung>();

        Cursor cursor = database.query(MigronitorDbHelper.TABLE_SCHMERZSTAERKE,
                allColumnsSchmerzStaerke, null, null, null, null, null);

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

        Cursor cursor = database.query(MigronitorDbHelper.TABLE_SCHMERZSTAERKE,
                allColumnsSchmerzStaerke, MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_DELETED + "=?", selectionargs, null, null, null);

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

    public void deleteSchmerzaenderung(Schmerzaenderung s) {
        // Define 'where' part of query.
        String selection = MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(s.getId())};
        // Issue SQL statement.
        database.delete(MigronitorDbHelper.TABLE_SCHMERZSTAERKE, selection, selectionArgs);
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

    //Attacken
    public void createAttacke(Attacke a){
        ContentValues values = new ContentValues();
        values.put(MigronitorDbHelper.COLUMN_ATTACKEN_ID, a.getId());
        values.put(MigronitorDbHelper.COLUMN_ATTACKEN_DATESTART,  dateFormater.format(a.getDatumStart()));
        values.put(MigronitorDbHelper.COLUMN_ATTACKEN_DATEENDE,  dateFormater.format(a.getDatumEnde()));
        values.put(MigronitorDbHelper.COLUMN_ATTACKEN_BEMERKUNG, a.getBemerkung());
        database.insert(MigronitorDbHelper.TABLE_ATTACKEN,null,values);
    }

    public void updateAttacke(Attacke a) {
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(MigronitorDbHelper.COLUMN_ATTACKEN_ID, a.getId());
        values.put(MigronitorDbHelper.COLUMN_ATTACKEN_DATESTART,  dateFormater.format(a.getDatumStart()));
        values.put(MigronitorDbHelper.COLUMN_ATTACKEN_DATEENDE,  dateFormater.format(a.getDatumEnde()));
        values.put(MigronitorDbHelper.COLUMN_ATTACKEN_BEMERKUNG, a.getBemerkung());
        //Which row to update, based on the ID
        String selection = MigronitorDbHelper.COLUMN_ATTACKEN_ID + " = ?";
        String[] selectionArgs = {a.getId() + ""};

        database.update(
                MigronitorDbHelper.TABLE_ATTACKEN,
                values,
                selection,
                selectionArgs);
    }

    public Attacke getAttacke(long id){
        String selection = MigronitorDbHelper.COLUMN_ATTACKEN_ID + " = ?";
        String[] selectionArgs = {id + ""};

        Cursor cursor = database.query(MigronitorDbHelper.TABLE_ATTACKEN,
                allColumnsAttacken, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        return cursorToAttacke(cursor);

    }

    public void deleteAttacke(Attacke a) {
        // Define 'where' part of query.
        String selection = MigronitorDbHelper.COLUMN_ATTACKEN_ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(a.getId())};
        // Issue SQL statement.
        database.delete(MigronitorDbHelper.TABLE_ATTACKEN, selection, selectionArgs);
    }

    public List<Attacke> getAllAttacken() {
        List<Attacke> se = new ArrayList<Attacke>();
        Cursor cursor = database.query(MigronitorDbHelper.TABLE_ATTACKEN,
                allColumnsAttacken, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Attacke a = cursorToAttacke(cursor);
            se.add(a);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return se;
    }

    private Attacke cursorToAttacke(Cursor cursor) {
        Attacke a = new Attacke();
        try {
            a.setId(cursor.getInt(0));
            a.setDatumStart(dateFormater.parse(cursor.getString(1)));
            a.setDatumEnde(dateFormater.parse(cursor.getString(2)));
            a.setBemerkung(cursor.getString(3));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return a;
    }
}