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

import com.jjoe64.graphview.CustomLabelFormatter;
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
import de.tjanneck.migronitor.helpers.ZeitSklave;


public class Statistiken extends Activity implements AdapterView.OnItemSelectedListener {
    final ArrayList<String> list = new ArrayList<String>();
    private final SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy");
    private final SimpleDateFormat dateFormater1 = new SimpleDateFormat("HH:mm");
    private MigronitorDataSource mdatasource;
    private double first;
    private double last;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiken);

        mdatasource = new MigronitorDataSource(this);
        mdatasource.open();

        Spinner spinner = (Spinner) findViewById(R.id.statsselect);
        //list.add("Durschnitt Tage");
        //list.add("Zahlen");
        list.add("Heute");
        list.add("Gestern");
        list.add("Vorgestern");

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.ltest);
        layout.removeAllViews();
        if (id == 0 || id == 1|| id == 2) {

            Date d = new Date();
            d = new Date(d.getTime() - id * 1000 * 60 * 60 * 24);

            List<Schmerzaenderung> sae = mdatasource.getSchmerzaenderungenDate(d);
            GraphViewData[] gd = new GraphViewData[sae.size()];
            if (!sae.isEmpty()) {
                first = ZeitSklave.DateToGraph(sae.get(1).getDatum());
                last = ZeitSklave.DateToGraph(sae.get(sae.size() - 1).getDatum());
            }
            for (int i = 0; i < sae.size(); i++) {
                gd[i] = new GraphViewData(ZeitSklave.DateToGraph(sae.get(i).getDatum()), sae.get(i).getStaerke());
            }


            GraphViewSeries gSeries = new GraphViewSeries("", null, gd);

            GraphView graphView = new LineGraphView(this, "");


            graphView.setVerticalLabels(new String[]{"10", "9", "8", "7", "6", "5", "4", "3", "2", "1", "0"});

            graphView.setManualYAxisBounds(10, 0);
            graphView.setScalable(true);
            graphView.setScrollable(true);
            graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    // TODO Auto-generated method stub
                    if (isValueX) {
                        long t = (long) (1000 * value);
                        return ZeitSklave.LongToTime(t);
                    }
                    return "" + value;
                }
            });
            graphView.addSeries(gSeries); // data
            if ((last - first) < 7200) {
                graphView.setViewPort(first, last - first);
            } else {
                graphView.setViewPort(first, 7200);
            }
            layout.addView(graphView);

        }
    }



    /*public List<Double> berechneSchmerzdurchschnittProTag() {

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
        //auffüllen
        Date now = new Date();
        int h = 0;
        for(int i = 6; i >= 0 ; i--){
          Date vgl = new Date(now.getTime() - h*1000*60*60*24);
          if(!dates.get(i).equals(vgl)){
              dates.add()
          }
            h++;
        }

        //transfering
        ArrayList<Double> finalList = new ArrayList<Double>();
        for (Date d : dates) {
            finalList.add(calc2.get(d));
        }


        return finalList;
    }*/

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
