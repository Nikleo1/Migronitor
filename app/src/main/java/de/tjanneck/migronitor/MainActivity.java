package de.tjanneck.migronitor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.tjanneck.migronitor.de.tjanneck.migronitor.db.MigronitorDataSource;
import de.tjanneck.migronitor.de.tjanneck.migronitor.db.Schmerzaenderung;


public class MainActivity extends ActionBarActivity {

    private int schmerzstaerke;
    private boolean attacke;
    private boolean schlafen;
    private SharedPreferences prefs;
    private MigronitorDataSource migronitorDatasource;
    private SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        checkprefs();

        if (!isServiceRunning()) {
            startService();
        }

        migronitorDatasource = new MigronitorDataSource(this);
        migronitorDatasource.open();


    }

    private void startService() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, DbRotator.class);
        PendingIntent pending = PendingIntent.getService(this, 0, alarmIntent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 240 * 1000L, 86400000L, pending);
    }

    private boolean isServiceRunning() {
        try {
            Date d = dateFormater.parse(prefs.getString("lastservicecall", "01.01.2014 00:00:00"));
            Date vgl = new Date();
            vgl = new Date(vgl.getTime() - 13 * 60 * 60 * 1000);

            return !vgl.after(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }


    private void checkprefs() {
        if (prefs.getInt("schmerzstaerke", -1) == -1) {
            schmerzstaerke = 4;
            attacke = false;
            schlafen = false;
            Date d = new Date();

            prefs.edit().putInt("schmerzstaerke", schmerzstaerke);
            prefs.edit().putBoolean("attacke", attacke);
            prefs.edit().putBoolean("schlafen", schlafen);
            prefs.edit().putInt("schmerzAenderungID", 1);
            prefs.edit().putInt("dbVersion", 1);
            prefs.edit().putString("lastrotate", dateFormater.format(d)).apply();
            prefs.edit().apply();
        } else {
            schmerzstaerke = prefs.getInt("schmerzstaerke", 0);
            attacke = prefs.getBoolean("attacke", false);
            schlafen = prefs.getBoolean("schlafen", false);
            drawButton("schmerzen");
            drawButton("attacke");
            drawButton("schlafen");


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Einstellungen.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //Draws

    private void drawButton(String s) {
        if (s.equalsIgnoreCase("schmerzen")) {
            TextView t = (TextView) findViewById(R.id.schmerz);
            t.setText("" + this.schmerzstaerke);
        } else if (s.equalsIgnoreCase("attacke")) {

            Button b = (Button) findViewById(R.id.attacke);
            TextView t = (TextView) findViewById(R.id.schmerz);
            if (attacke) {
                t.setText("" + schmerzstaerke);
                b.setBackgroundColor(Color.parseColor("#41ff0600"));
                b.setText("Attacke zu Ende");
            } else {

                b.setBackgroundColor(Color.parseColor("#5a3dff49"));
                b.setText("Attacke");
            }

        } else if (s.equalsIgnoreCase("schlafen")) {
            Button b = (Button) findViewById(R.id.schlafen);
            if (schlafen) {

                b.setBackgroundColor(Color.parseColor("#C80035BE"));
                b.setText("Wieder wach");
            } else {

                b.setBackgroundColor(Color.parseColor("#4b01a0ff"));
                b.setText("Schlafen");
            }
        }


    }

    //Buttons

    public void OnClickPlus(View v) {

        if (this.schmerzstaerke < 10) {
            this.schmerzstaerke++;

            Schmerzaenderung s = new Schmerzaenderung();
            s.setId(prefs.getInt("schmerzAenderungID", 1));
            s.setDatum(new Date());
            s.setStaerke(this.schmerzstaerke);
            migronitorDatasource.createSchmerzaenderung(s);
            prefs.edit().putInt("schmerzAenderungID", s.getId() + 1).apply();

            prefs.edit().putInt("schmerzstaerke", schmerzstaerke).apply();
            this.drawButton("schmerzen");
        }
    }

    public void OnClickMinus(View v) {

        if (this.schmerzstaerke > 0) {
            this.schmerzstaerke--;

            Schmerzaenderung s = new Schmerzaenderung();
            s.setId(prefs.getInt("schmerzAenderungID", 1));
            s.setDatum(new Date());
            s.setStaerke(this.schmerzstaerke);
            migronitorDatasource.createSchmerzaenderung(s);
            prefs.edit().putInt("schmerzAenderungID", s.getId() + 1).apply();

            prefs.edit().putInt("schmerzstaerke", schmerzstaerke).apply();
            this.drawButton("schmerzen");


        }

    }

    public void OnClickAttacke(View v) {


        if (!attacke) {
            this.attacke = true;
            int attackkenpref = Integer.parseInt(prefs.getString("schmerzAttacke_list", "7"));
            if (attackkenpref > this.schmerzstaerke) {
                Schmerzaenderung s = new Schmerzaenderung();
                this.schmerzstaerke = attackkenpref;
                s.setId(prefs.getInt("schmerzAenderungID", 1));
                s.setDatum(new Date());
                s.setStaerke(this.schmerzstaerke);
                migronitorDatasource.createSchmerzaenderung(s);
            }

        } else {
            this.attacke = false;

        }
        prefs.edit().putBoolean("attacke", attacke).apply();
        this.drawButton("attacke");
    }

    public void OnClickSchlafen(View v) {

        this.schlafen = !schlafen;
        prefs.edit().putBoolean("schlafen", schlafen).apply();
        drawButton("schlafen");

    }

    public void OnClickMedikamente(View v) {

    }
}
