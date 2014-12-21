package de.tjanneck.migronitor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.tjanneck.migronitor.de.tjanneck.migronitor.db.MedikamenteDataSource;
import de.tjanneck.migronitor.de.tjanneck.migronitor.db.MedikamentenEinwurf;
import de.tjanneck.migronitor.de.tjanneck.migronitor.db.MigronitorDataSource;

/**
 * Created by Programmieren on 04.12.2014.
 */
public class MedikamenteNehmen extends Activity {
    private MedikamenteDataSource datasource;
    private MigronitorDataSource mdatasource;
    private HashMap<Long, Integer> einwurf;
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medikamentenehmen);

        einwurf = new HashMap<Long, Integer>();
        datasource = new MedikamenteDataSource(this);
        mdatasource = new MigronitorDataSource(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        datasource.open();
        mdatasource.open();

        MedikamenteNehmenListAdapter adapter = new MedikamenteNehmenListAdapter(MedikamenteNehmen.this, R.layout.medikamentenehmenlistitem, datasource.getActiveMedikaments());
        ListView medListView = (ListView) findViewById(R.id.MedikamenteNehmenListe);
        medListView.setAdapter(adapter);

    }

    public void OnClickNehmen(@SuppressWarnings("UnusedParameters") View v) {
        for (Map.Entry<Long, Integer> e : einwurf.entrySet()) {
            if (e.getValue() != 0) {
                MedikamentenEinwurf m = new MedikamentenEinwurf();
                m.setId(prefs.getLong("medikamentenehmenID", 1));
                m.setDatum(new Date());
                m.setMid(e.getKey());
                m.setMenge(e.getValue());
                mdatasource.createMedikamentenEinwurf(m);
                prefs.edit().putLong("medikamentenehmenID", m.getId() + 1).apply();
            }
        }
        this.finish();

    }

    public void nehmenPlusHandler(@SuppressWarnings("UnusedParameters") View v) {
        MedikamenteNehmenListAdapter.MedikamenteNehmenHolder item = (MedikamenteNehmenListAdapter.MedikamenteNehmenHolder) v.getTag();

        if (einwurf.containsKey(item.medikament.getId())) {
            int mengeNeu = einwurf.get(item.medikament.getId());
            mengeNeu++;
            einwurf.put(item.medikament.getId(), mengeNeu);
        } else {
            einwurf.put(item.medikament.getId(), 1);
        }
        item.menge.setText("" + einwurf.get(item.medikament.getId()));
        if (einwurf.get(item.medikament.getId()) != 0) {
            item.menge.setTextColor(Color.RED);
        } else {
            item.menge.setTextColor(Color.BLACK);
        }


    }

    public void nehmenMinusHandler(@SuppressWarnings("UnusedParameters") View v) {
        MedikamenteNehmenListAdapter.MedikamenteNehmenHolder item = (MedikamenteNehmenListAdapter.MedikamenteNehmenHolder) v.getTag();
        if (einwurf.containsKey(item.medikament.getId()) && einwurf.get(item.medikament.getId()) != 0) {
            int mengeNeu = einwurf.get(item.medikament.getId());
            mengeNeu--;
            einwurf.put(item.medikament.getId(), mengeNeu);
        } else {
            einwurf.put(item.medikament.getId(), 0);
        }

        item.menge.setText("" + einwurf.get(item.medikament.getId()));
        if (einwurf.get(item.medikament.getId()) != 0) {
            item.menge.setTextColor(Color.RED);
        } else {
            item.menge.setTextColor(Color.BLACK);
        }
    }
}
