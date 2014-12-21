package de.tjanneck.migronitor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import de.tjanneck.migronitor.db.Medikament;

/**
 * Created by Programmieren on 04.12.2014.
 */
public class MedikamenteNehmenListAdapter extends ArrayAdapter<Medikament> {

    private List<Medikament> items;
    private int layoutResourceId;
    private Context context;

    public MedikamenteNehmenListAdapter(Context context, int layoutResourceId, List<Medikament> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MedikamenteNehmenHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new MedikamenteNehmenHolder();
        holder.medikament = items.get(position);
        holder.menge = (TextView) row.findViewById(R.id.nehmen_menge);
        holder.name = (TextView) row.findViewById(R.id.nehmen_name);
        holder.plus = (Button) row.findViewById(R.id.nehmen_plus);
        holder.minus = (Button) row.findViewById(R.id.nehmen_minus);

        holder.plus.setTag(holder);
        holder.minus.setTag(holder);


        row.setTag(holder);

        setupItem(holder);
        return row;
    }

    private void setupItem(MedikamenteNehmenHolder holder) {
        holder.name.setText(holder.medikament.getName());
        holder.menge.setText(String.valueOf(0));
    }

    public static class MedikamenteNehmenHolder {
        Medikament medikament;
        TextView menge;
        TextView name;
        Button plus;
        Button minus;
    }
}
