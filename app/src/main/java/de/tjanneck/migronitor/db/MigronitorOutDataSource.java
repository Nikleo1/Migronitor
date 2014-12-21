package de.tjanneck.migronitor.db;

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
    private final SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private final String[] allColumnsSchmerzStaerke = {MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_ID,
            MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_DATE, MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_STAERKE, MigronitorDbHelper.COLUMN_SCHMERZSTAERKE_DELETED};
    private final String[] allColumnsAttacken = {MigronitorDbHelper.COLUMN_ATTACKEN_ID,
            MigronitorDbHelper.COLUMN_ATTACKEN_DATESTART, MigronitorDbHelper.COLUMN_ATTACKEN_DATEENDE, MigronitorDbHelper.COLUMN_ATTACKEN_BEMERKUNG};
    private final String[] allColumnsSchlafen = {MigronitorDbHelper.COLUMN_SCHLAFEN_ID,
            MigronitorDbHelper.COLUMN_SCHLAFEN_SCHLAFEN, MigronitorDbHelper.COLUMN_SCHLAFEN_WACH};
    private final String[] allColumnsMnehmen = {MigronitorDbHelper.COLUMN_MNEHMEN_ID,
            MigronitorDbHelper.COLUMN_MNEHMEN_DATUM, MigronitorDbHelper.COLUMN_MNEHMEN_MID, MigronitorDbHelper.COLUMN_MNEHMEN_MENGE};
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

    //Schlaf
    public void createSchlaf(Schlaf s) {
        ContentValues values = new ContentValues();
        values.put(MigronitorDbHelper.COLUMN_SCHLAFEN_ID, s.getId());
        values.put(MigronitorDbHelper.COLUMN_SCHLAFEN_SCHLAFEN, dateFormater.format(s.getSchlafen()));
        values.put(MigronitorDbHelper.COLUMN_SCHLAFEN_WACH, dateFormater.format(s.getWach()));
        database.insert(MigronitorDbHelper.TABLE_SCHLAFEN, null, values);
    }

    public void updateSchlaf(Schlaf s) {
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(MigronitorDbHelper.COLUMN_SCHLAFEN_ID, s.getId());
        values.put(MigronitorDbHelper.COLUMN_SCHLAFEN_SCHLAFEN, dateFormater.format(s.getSchlafen()));
        values.put(MigronitorDbHelper.COLUMN_SCHLAFEN_WACH, dateFormater.format(s.getWach()));
        //Which row to update, based on the ID
        String selection = MigronitorDbHelper.COLUMN_SCHLAFEN_ID + " = ?";
        String[] selectionArgs = {s.getId() + ""};

        database.update(
                MigronitorDbHelper.TABLE_SCHLAFEN,
                values,
                selection,
                selectionArgs);
    }

    public Schlaf getSchlaf(long id) {
        String selection = MigronitorDbHelper.COLUMN_SCHLAFEN_ID + " = ?";
        String[] selectionArgs = {id + ""};

        Cursor cursor = database.query(MigronitorDbHelper.TABLE_SCHLAFEN,
                allColumnsSchlafen, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        return cursorToSchlaf(cursor);

    }

    public void deleteSchlaf(Schlaf s) {
        // Define 'where' part of query.
        String selection = MigronitorDbHelper.COLUMN_SCHLAFEN_ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(s.getId())};
        // Issue SQL statement.
        database.delete(MigronitorDbHelper.TABLE_SCHLAFEN, selection, selectionArgs);
    }

    public List<Schlaf> getAllSchlaf() {
        List<Schlaf> se = new ArrayList<Schlaf>();
        Cursor cursor = database.query(MigronitorDbHelper.TABLE_SCHLAFEN,
                allColumnsSchlafen, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Schlaf s = cursorToSchlaf(cursor);
            se.add(s);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return se;
    }

    private Schlaf cursorToSchlaf(Cursor cursor) {
        Schlaf s = new Schlaf();
        try {
            s.setId(cursor.getLong(0));
            s.setSchlafen(dateFormater.parse(cursor.getString(1)));
            s.setWach(dateFormater.parse(cursor.getString(2)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return s;
    }

    //Attacken
    public void createAttacke(Attacke a) {
        ContentValues values = new ContentValues();
        values.put(MigronitorDbHelper.COLUMN_ATTACKEN_ID, a.getId());
        values.put(MigronitorDbHelper.COLUMN_ATTACKEN_DATESTART, dateFormater.format(a.getDatumStart()));
        values.put(MigronitorDbHelper.COLUMN_ATTACKEN_DATEENDE, dateFormater.format(a.getDatumEnde()));
        values.put(MigronitorDbHelper.COLUMN_ATTACKEN_BEMERKUNG, a.getBemerkung());
        database.insert(MigronitorDbHelper.TABLE_ATTACKEN, null, values);
    }

    public void updateAttacke(Attacke a) {
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(MigronitorDbHelper.COLUMN_ATTACKEN_ID, a.getId());
        values.put(MigronitorDbHelper.COLUMN_ATTACKEN_DATESTART, dateFormater.format(a.getDatumStart()));
        values.put(MigronitorDbHelper.COLUMN_ATTACKEN_DATEENDE, dateFormater.format(a.getDatumEnde()));
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

    public Attacke getAttacke(long id) {
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
            a.setId(cursor.getLong(0));
            a.setDatumStart(dateFormater.parse(cursor.getString(1)));
            a.setDatumEnde(dateFormater.parse(cursor.getString(2)));
            a.setBemerkung(cursor.getString(3));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return a;
    }

    //Medikamente Nehmen
    public void createMedikamentenEinwurf(MedikamentenEinwurf m) {
        ContentValues values = new ContentValues();
        values.put(MigronitorDbHelper.COLUMN_MNEHMEN_ID, m.getId());
        values.put(MigronitorDbHelper.COLUMN_MNEHMEN_DATUM, dateFormater.format(m.getDatum()));
        values.put(MigronitorDbHelper.COLUMN_MNEHMEN_MID, m.getMid());
        values.put(MigronitorDbHelper.COLUMN_MNEHMEN_MENGE, m.getMenge());
        database.insert(MigronitorDbHelper.TABLE_MNEHMEN, null, values);
    }

    public void updateMedikamentenEinwurf(MedikamentenEinwurf m) {
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(MigronitorDbHelper.COLUMN_MNEHMEN_ID, m.getId());
        values.put(MigronitorDbHelper.COLUMN_MNEHMEN_DATUM, dateFormater.format(m.getDatum()));
        values.put(MigronitorDbHelper.COLUMN_MNEHMEN_MID, m.getMid());
        values.put(MigronitorDbHelper.COLUMN_MNEHMEN_MENGE, m.getMenge());
        //Which row to update, based on the ID
        String selection = MigronitorDbHelper.COLUMN_MNEHMEN_ID + " = ?";
        String[] selectionArgs = {m.getId() + ""};

        database.update(
                MigronitorDbHelper.TABLE_MNEHMEN,
                values,
                selection,
                selectionArgs);
    }

    public MedikamentenEinwurf getMedikamentenEinwurf(long id) {
        String selection = MigronitorDbHelper.COLUMN_MNEHMEN_ID + " = ?";
        String[] selectionArgs = {id + ""};

        Cursor cursor = database.query(MigronitorDbHelper.TABLE_MNEHMEN,
                allColumnsMnehmen, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        return cursorToMedikamentenEinwurf(cursor);

    }

    public void deleteMedikamentenEinwurf(MedikamentenEinwurf m) {
        // Define 'where' part of query.
        String selection = MigronitorDbHelper.COLUMN_MNEHMEN_ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(m.getId())};
        // Issue SQL statement.
        database.delete(MigronitorDbHelper.TABLE_MNEHMEN, selection, selectionArgs);
    }

    public List<MedikamentenEinwurf> getAllMedikamentenEinwuerfe() {
        List<MedikamentenEinwurf> me = new ArrayList<MedikamentenEinwurf>();
        Cursor cursor = database.query(MigronitorDbHelper.TABLE_MNEHMEN,
                allColumnsMnehmen, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MedikamentenEinwurf m = cursorToMedikamentenEinwurf(cursor);
            me.add(m);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return me;
    }

    private MedikamentenEinwurf cursorToMedikamentenEinwurf(Cursor cursor) {
        MedikamentenEinwurf m = new MedikamentenEinwurf();
        try {
            m.setId(cursor.getLong(0));
            m.setDatum(dateFormater.parse(cursor.getString(1)));
            m.setMid(cursor.getInt(2));
            m.setMenge(cursor.getInt(3));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return m;
    }
}
