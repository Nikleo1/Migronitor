package de.tjanneck.migronitor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Programmieren on 24.11.2014.
 */
public class MedikamenteDataSource {

    private final MedikamentenDbHelper dbHelper;
    private final String[] allColumns = {MedikamentenDbHelper.COLUMN_ID,
            MedikamentenDbHelper.COLUMN_NAME, MedikamentenDbHelper.COLUMN_ACTIVE};
    // Database fields
    private SQLiteDatabase database;

    public MedikamenteDataSource(Context context) {
        dbHelper = new MedikamentenDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Medikament createMedikament(String name) {
        ContentValues values = new ContentValues();
        values.put(MedikamentenDbHelper.COLUMN_NAME, name);
        values.put(MedikamentenDbHelper.COLUMN_ACTIVE, true);
        long insertId = database.insert(MedikamentenDbHelper.TABLE_MEDIKAMENTE, null,
                values);
        Cursor cursor = database.query(MedikamentenDbHelper.TABLE_MEDIKAMENTE,
                allColumns, MedikamentenDbHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Medikament newMedikament = cursorToMedikament(cursor);
        cursor.close();
        return newMedikament;
    }

    public void updateMedikament(Medikament m) {
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(MedikamentenDbHelper.COLUMN_NAME, m.getName());
        values.put(MedikamentenDbHelper.COLUMN_ACTIVE, m.isActive());
        //Which row to update, based on the ID
        String selection = MedikamentenDbHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {m.getId() + ""};

        database.update(
                MedikamentenDbHelper.TABLE_MEDIKAMENTE,
                values,
                selection,
                selectionArgs);
    }

    public void deleteMedikament(Medikament med) {
        long id = med.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MedikamentenDbHelper.TABLE_MEDIKAMENTE, MedikamentenDbHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Medikament> getAllMedikaments() {
        List<Medikament> meds = new ArrayList<Medikament>();

        Cursor cursor = database.query(MedikamentenDbHelper.TABLE_MEDIKAMENTE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Medikament med = cursorToMedikament(cursor);
            meds.add(med);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return meds;
    }


    public List<Medikament> getActiveMedikaments() {
        List<Medikament> meds = new ArrayList<Medikament>();
        String[] selectionargs = {1 + ""};

        Cursor cursor = database.query(MedikamentenDbHelper.TABLE_MEDIKAMENTE,
                allColumns, MedikamentenDbHelper.COLUMN_ACTIVE + "=?", selectionargs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Medikament med = cursorToMedikament(cursor);
            meds.add(med);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return meds;
    }

    private Medikament cursorToMedikament(Cursor cursor) {
        Medikament med = new Medikament();
        med.setId(cursor.getLong(0));
        med.setName(cursor.getString(1));
        med.setActive(Boolean.getBoolean(cursor.getString(2)));
        return med;
    }
}
