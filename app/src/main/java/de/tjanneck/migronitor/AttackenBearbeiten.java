package de.tjanneck.migronitor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.List;

import de.tjanneck.migronitor.de.tjanneck.migronitor.db.Attacke;
import de.tjanneck.migronitor.de.tjanneck.migronitor.db.MigronitorDataSource;

/**
 * Created by Programmieren on 04.12.2014.
 */
public class AttackenBearbeiten extends Activity implements AdapterView.OnItemClickListener {

    private final SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private final SimpleDateFormat dateFormaterAttacke = new SimpleDateFormat("HH:mm");
    private MigronitorDataSource mdatasource;
    private SharedPreferences prefs;
    private List<Attacke> attacken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attackenbearbeiten);

        mdatasource = new MigronitorDataSource(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        mdatasource.open();

        AttackenBearbeitenListAdapter adapter = new AttackenBearbeitenListAdapter(AttackenBearbeiten.this, R.layout.attackebearbeitenlistitem, mdatasource.getLastAttacken());
        ListView attListView = (ListView) findViewById(R.id.AttackenListe);
        attListView.setAdapter(adapter);
        attListView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        AttackenBearbeitenListAdapter.AttackeBearbeitenHolder item = (AttackenBearbeitenListAdapter.AttackeBearbeitenHolder) view.getTag();
        Intent intent = new Intent(AttackenBearbeiten.this, AttackeBearbeiten.class);
        Bundle b = new Bundle();
        b.putLong("id", item.attacke.getId()); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
    }



}


