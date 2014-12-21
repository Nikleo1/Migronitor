package de.tjanneck.migronitor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import de.tjanneck.migronitor.db.Attacke;

/**
 * Created by Programmieren on 04.12.2014.
 */
public class AttackenBearbeitenListAdapter extends ArrayAdapter<Attacke> {

    private final SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private final SimpleDateFormat dateFormaterAttacke = new SimpleDateFormat("HH:mm:ss");
    private List<Attacke> items;
    private int layoutResourceId;
    private Context context;

    public AttackenBearbeitenListAdapter(Context context, int layoutResourceId, List<Attacke> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AttackeBearbeitenHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new AttackeBearbeitenHolder();
        holder.attacke = items.get(position);
        holder.datum = (TextView) row.findViewById(R.id.attacke_bearbeiten_datum);
        holder.dauer = (TextView) row.findViewById(R.id.attacke_bearbeiten_dauer);

        row.setTag(holder);

        setupItem(holder);
        return row;
    }

    private void setupItem(AttackeBearbeitenHolder holder) {
        long diff = holder.attacke.getDatumEnde().getTime() - holder.attacke.getDatumStart().getTime();


        holder.datum.setText(dateFormater.format(holder.attacke.getDatumStart()));
        String d = "";
        long h = diff / (1000 * 60 * 60);
        long m = (diff - h * 60 * 60 * 1000) / (1000 * 60);
        long s = (diff - h * 60 * 60 * 1000 - m * 1000 * 60) / 1000;
        if (h != 0) {
            d = h + " Std " + m + " Min " + s + " Sek";
        } else {
            if (m != 0) {
                d = d + m + " Min " + s + " Sek";
            } else {
                d = d + s + " Sek";
            }
        }
        holder.dauer.setText(d);
    }

    public static class AttackeBearbeitenHolder {
        Attacke attacke;
        TextView datum;
        TextView dauer;

    }
}
