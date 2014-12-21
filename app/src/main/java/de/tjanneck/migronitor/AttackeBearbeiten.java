package de.tjanneck.migronitor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import de.tjanneck.migronitor.de.tjanneck.migronitor.db.Attacke;
import de.tjanneck.migronitor.de.tjanneck.migronitor.db.MigronitorDataSource;


public class AttackeBearbeiten extends Activity {

    private final SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private MigronitorDataSource mdatasource;
    private Attacke a;
    private EditText bemerkung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attacke_bearbeiten);
        Bundle b = getIntent().getExtras();
        long id = b.getLong("id", 0);

        mdatasource = new MigronitorDataSource(this);
        mdatasource.open();

        a = mdatasource.getAttacke(id);

        TextView tv = (TextView) findViewById(R.id.attackendatumstart);
        tv.setText(dateFormater.format(a.getDatumStart()));
        TextView tv1 = (TextView) findViewById(R.id.attackendatumende);
        tv1.setText(dateFormater.format(a.getDatumEnde()));

        bemerkung = (EditText) findViewById(R.id.editBemerkung);
        bemerkung.setText(a.getBemerkung());

    }

    public void speichernHandler(@SuppressWarnings("UnusedParameters") View v) {
        a.setBemerkung(bemerkung.getText().toString());
        mdatasource.updateAttacke(a);
        Toast.makeText(this.getBaseContext(), "Änderung gespeichert", Toast.LENGTH_LONG).show();

        finish();
    }


}
