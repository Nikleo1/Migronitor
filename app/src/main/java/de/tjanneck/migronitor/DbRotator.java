package de.tjanneck.migronitor;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.tjanneck.migronitor.db.Attacke;
import de.tjanneck.migronitor.db.MedikamenteDataSource;
import de.tjanneck.migronitor.db.MedikamentenEinwurf;
import de.tjanneck.migronitor.db.MigronitorDataSource;
import de.tjanneck.migronitor.db.MigronitorOutDataSource;
import de.tjanneck.migronitor.db.Schlaf;
import de.tjanneck.migronitor.db.Schmerzaenderung;

/**
 * Created by Programmieren on 27.11.2014.
 */
public class DbRotator extends IntentService {


    private final SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");


    public DbRotator() {
        super("DbRotator");
    }

    public void onHandleIntent(Intent intent) {
        System.out.println("Service run");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Date now = new Date();
        prefs.edit().putString("lastservicecall", dateFormater.format(now)).apply();


        try {
            Date lastrotate = dateFormater.parse(prefs.getString("lastrotate", ""));

            Date vgl = new Date(now.getTime() - 604800000L); //   7 * 24 * 60 * 60 * 1000 86400000L
            Date vglr = new Date(now.getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000 604800000L
            // Daten Tauschen
            MigronitorOutDataSource mos = new MigronitorOutDataSource(this, prefs.getInt("dbVersion", 1));
            MigronitorDataSource ms = new MigronitorDataSource(this);

            mos.open();
            ms.open();
            List<Schmerzaenderung> sae = ms.getAllSchmerzaenderungen();
            System.out.println("Checking " + sae.size() + " Schmerzaenderungen");
            for (Schmerzaenderung s : sae) {
                if (s.getDatum().before(vgl)) {
                    mos.createSchmerzaenderung(s);
                    ms.deleteSchmerzaenderung(s);
                }
            }
            System.out.println("Schmerzaenderungen transferred");

            List<Attacke> att = ms.getAllAttacken();
            System.out.println("Checking " + att.size() + " Attacken");
            for (Attacke a : att) {
                if (a.getDatumStart().before(vgl) && a.getDatumStart() != a.getDatumEnde()) {
                    mos.createAttacke(a);
                    ms.deleteAttacke(a);
                }
            }
            System.out.println("Attacken transferred");

            List<Schlaf> schl = ms.getAllSchlaf();
            System.out.println("Checking " + schl.size() + " Schlaf");
            for (Schlaf s : schl) {
                if (s.getSchlafen().before(vgl) && s.getSchlafen() != s.getWach()) {
                    mos.createSchlaf(s);
                    ms.deleteSchlaf(s);
                }
            }
            System.out.println("Schlaf transferred");

            List<MedikamentenEinwurf> ml = ms.getAllMedikamentenEinwuerfe();
            System.out.println("Checking " + ml.size() + " MedikamentenEinwuerfe");
            for (MedikamentenEinwurf m : ml) {
                if (m.getDatum().before(vgl)) {
                    mos.createMedikamentenEinwurf(m);
                    ms.deleteMedikamentenEinwurf(m);
                }
            }
            System.out.println("MedikamentenEinwuerfe transferred");

            mos.close();
            ms.close();
            System.out.println("Trannsferring Datas completed");
            //Rotieren
            if (lastrotate.before(vglr)) {
                System.out.println("Database rotating ...");
                int id = prefs.getInt("dbVersion", 1);
                MedikamenteDataSource mds = new MedikamenteDataSource(this);
                mds.open();
                mds.close();
                System.out.println("Kopiersklave start");
                Kopiersklave.copyFile(this.getDatabasePath("migronitor." + id + ".db").getParent(), "/migronitor." + id + ".db");
                Kopiersklave.copyFile(this.getDatabasePath("migronitorglobaldata.db").getParent(), "/migronitorglobaldata.db");
                System.out.println("Löschen");
                File f = this.getDatabasePath("migronitor." + id + ".db");
                //noinspection ResultOfMethodCallIgnored
                f.delete();

                id++;
                prefs.edit().putInt("dbVersion", id).apply();
                String s = dateFormater.format(now);
                prefs.edit().putString("lastrotate", s).apply();

                mos = new MigronitorOutDataSource(this, prefs.getInt("dbVersion", 1));
                mos.open();

                mos.close();
                System.out.println("Database rotated successfully !");
            } else {
                System.out.println("Database ok");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
