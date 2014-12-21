package de.tjanneck.migronitor;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tjanneck.migronitor.db.MigronitorDataSource;
import de.tjanneck.migronitor.db.Schmerzaenderung;


public class Statistiken extends Activity implements AdapterView.OnItemSelectedListener {
    final ArrayList<String> list = new ArrayList<String>();
    private final SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy");
    private MigronitorDataSource mdatasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiken);

        mdatasource = new MigronitorDataSource(this);
        mdatasource.open();

        Spinner spinner = (Spinner) findViewById(R.id.statsselect);
        list.add("Durschnitt Tage");
        list.add("Zahlen");
        list.add("Heute");
        list.add("Gestern");

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.ltest);
        if (id == 0) {

            GraphViewData[] gd = new GraphViewData[2];

            GraphViewSeries gSeries = new GraphViewSeries("", null, gd);
            GraphView graphView = new LineGraphView(this, "");
            graphView.addSeries(gSeries); // data
            graphView.setVerticalLabels(new String[]{"10", "9", "8", "7", "6", "5", "4", "3", "2", "1", "0"});
            layout.addView(graphView);
        } else {
            layout.removeAllViews();
        }
    }

    public List<Double> berechneSchmerzdurchschnittProTag() {
        List<Schmerzaenderung> sae = mdatasource.getActiveSchmerzaenderungen();
        HashMap<String, List<Integer>> calc = new HashMap<String, List<Integer>>();
        for (Schmerzaenderung s : sae) {
            String tag = dateFormater.format(s.getDatum());
            if (calc.get(tag) != null) {
                calc.get(tag).add(s.getStaerke());
            } else {
                List<Integer> hlist = new ArrayList<Integer>();
                hlist.add(s.getStaerke());
                calc.put(tag, hlist);
            }
        }


        HashMap<Date, Double> calc2 = new HashMap<Date, Double>();
        ArrayList<Date> dates = new ArrayList<Date>();
        for (Map.Entry<String, List<Integer>> e : calc.entrySet()) {
            double i = 0;
            int gesamt = 0;
            for (int s : e.getValue()) {
                gesamt += s;
                i++;
            }
            Double d = gesamt / i;
            try {
                calc2.put(dateFormater.parse(e.getKey()), d);
                dates.add(dateFormater.parse(e.getKey()));
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }

        //Sorting
        Collections.sort(dates, new Comparator<Date>() {
            @Override
            public int compare(Date lhs, Date rhs) {
                if (lhs.before(rhs))
                    return -1;
                else
                    return 1;
            }
        });
        for (Date d : dates) {

        }


        return finalList;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
