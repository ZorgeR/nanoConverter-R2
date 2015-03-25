package com.nanoconverter.zlab;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class NC_valute_reorder extends ListActivity {

    public static SharedPreferences prefs;	            /* Держатель для настроек приложения */

    private static String[] valute_reordered;
    private IconicAdapter adapter=null;
    private ArrayList<String> array=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        prefs = PreferenceManager.getDefaultSharedPreferences(NC_main.mainContext);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.reorder_main);

        if(prefs.getString("valute_reordered","null").equals("null")){
        valute_reordered = NC_main.valute_all_ID;
        } else {
            if (prefs.getString("valute_reordered","null").split(",").length<NC_main.valute_all_ID.length){

                valute_reordered = new String[NC_main.valute_all_ID.length];

                for (int i=0;i<prefs.getString("valute_reordered","null").split(",").length;i++){
                    valute_reordered[i] = prefs.getString("valute_reordered","null").split(",")[i];
                }
                for (int i=prefs.getString("valute_reordered","null").split(",").length;i<NC_main.valute_all_ID.length;i++){
                    valute_reordered[i] = NC_main.valute_all_ID[i];
                }

            }   else {
                valute_reordered = prefs.getString("valute_reordered","null").split(",");
            }
        }

        array = new ArrayList<String>(Arrays.asList(valute_reordered));

        TouchListView tlv=(TouchListView)getListView();
        adapter=new IconicAdapter();
        setListAdapter(adapter);

        tlv.setDropListener(onDrop);
        tlv.setRemoveListener(onRemove);

    }
    private TouchListView.DropListener onDrop=new TouchListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            String item=adapter.getItem(from);

            adapter.remove(item);
            adapter.insert(item, to);

            String valute_reordered_new="";

            for (int i=0;i<adapter.getCount();i++){
                valute_reordered_new=valute_reordered_new+adapter.getItem(i)+",";
            }

            prefs.edit().putString("valute_reordered",valute_reordered_new).commit();
        }
    };

    private TouchListView.RemoveListener onRemove=new TouchListView.RemoveListener() {
        @Override
        public void remove(int which) {
            adapter.remove(adapter.getItem(which));
        }
    };

    class IconicAdapter extends ArrayAdapter<String> {
        IconicAdapter() {
            super(NC_valute_reorder.this, R.layout.reorder_row, array);
        }

        public View getView(int position, View convertView,
                            ViewGroup parent) {
            View row=convertView;

            if (row==null) {
                LayoutInflater inflater=getLayoutInflater();

                row=inflater.inflate(R.layout.reorder_row, parent, false);
            }
            LayoutInflater inflater=getLayoutInflater();

            row=inflater.inflate(R.layout.reorder_row, parent, false);
            TextView label=(TextView)row.findViewById(R.id.label);

            label.setText(array.get(position));

            return(row);
        }
    }

}
