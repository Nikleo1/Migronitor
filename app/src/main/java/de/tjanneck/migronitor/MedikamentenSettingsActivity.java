package de.tjanneck.migronitor;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import de.tjanneck.migronitor.de.tjanneck.migronitor.db.Medikament;
import de.tjanneck.migronitor.de.tjanneck.migronitor.db.MedikamenteDataSource;

/**
 * Created by Programmieren on 24.11.2014.
 */
public class MedikamentenSettingsActivity extends ListActivity {
    private MedikamenteDataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medikamentensettings);

        datasource = new MedikamenteDataSource(this);
        datasource.open();

        loadAdapter();

    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Medikament> adapter = (ArrayAdapter<Medikament>) getListAdapter();

        switch (view.getId()) {
            case R.id.add:
                this.creationRequest();
                break;
            case R.id.delete:
                this.deletionRequest();
                break;
            case R.id.edit:
                this.editRequest();
                break;
        }
        adapter.notifyDataSetChanged();
    }

    private void editRequest() {

        @SuppressWarnings("unchecked") ArrayAdapter<Medikament> adapter = (ArrayAdapter<Medikament>) getListAdapter();
        int cntChoice = this.getListView().getCount();
        SparseBooleanArray selected = this.getListView().getCheckedItemPositions();
        for (int i = 0; i < cntChoice; i++) {
            if (selected.get(i)) {
                //adapter.getItem(i).setActive(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Medikamentenname");

                // Set up the input
                final EditText input = new EditText(this);
                input.setText(adapter.getItem(i).getName());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                final int finalI = i;
                builder.setPositiveButton("Ändern", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!input.getText().toString().equalsIgnoreCase("")) {
                            @SuppressWarnings("unchecked") ArrayAdapter<Medikament> adapter = (ArrayAdapter<Medikament>) getListAdapter();

                            adapter.getItem(finalI).setName(input.getText().toString());
                            adapter.getItem(finalI).setActive(true);
                            datasource.updateMedikament(adapter.getItem(finalI));
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                this.getListView().setItemChecked(i, false);
            }

        }

    }

    private void deletionRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bestätigen");

        // Set up the input
        final TextView tw = new TextView(this);
        tw.setText("Wollen sie wirklich alle ausgewählten Medikamente aus der Liste löschen ?");
        builder.setView(tw);

        // Set up the buttons
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete();

            }
        });
        builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void delete() {
        @SuppressWarnings("unchecked") ArrayAdapter<Medikament> adapter = (ArrayAdapter<Medikament>) getListAdapter();
        int cntChoice = this.getListView().getCount();
        SparseBooleanArray selected = this.getListView().getCheckedItemPositions();

        for (int i = 0; i < cntChoice; i++) {
            if (selected.get(i)) {
                adapter.getItem(i).setActive(false);
                datasource.updateMedikament(adapter.getItem(i));
                this.getListView().setItemChecked(i, false);

            }

        }
        this.loadAdapter();
    }

    private void loadAdapter() {
        List<Medikament> values = datasource.getActiveMedikaments();
        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Medikament> adapter = new ArrayAdapter<Medikament>(this, android.R.layout.simple_list_item_multiple_choice, values);
        this.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        setListAdapter(adapter);
    }

    void creationRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Medikamentenname");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setBackgroundColor(Color.WHITE);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                @SuppressWarnings("unchecked") ArrayAdapter<Medikament> adapter = (ArrayAdapter<Medikament>) getListAdapter();
                adapter.add(datasource.createMedikament(input.getText().toString()));

            }
        });
        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

}

