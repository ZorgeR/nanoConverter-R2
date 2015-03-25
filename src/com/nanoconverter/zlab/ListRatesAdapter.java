package com.nanoconverter.zlab;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ListRatesAdapter extends ArrayAdapter<ListRates> implements OnItemClickListener {

    Context context; 
    int layoutResourceId;    
    ListRates data[] = null;
    
    public ListRatesAdapter(Context context, int layoutResourceId, ListRates[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ListElementsHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ListElementsHolder();
            holder.ratesFLAG = (ImageView)row.findViewById(R.id.ratesFLAG);
            holder.ratesID = (TextView)row.findViewById(R.id.ratesID);
            holder.ratesDESC = (TextView)row.findViewById(R.id.ratesDESC);
            holder.ratesVALUE = (EditText)row.findViewById(R.id.ratesVALUE);
            row.setTag(holder);
        }
        else
        {
            holder = (ListElementsHolder)row.getTag();
        }

        ListRates rates = data[position];
        holder.ratesFLAG.setImageResource(rates.flag);
        holder.ratesID.setText(rates.ratesID);
        holder.ratesDESC.setText(rates.ratesDISC);
        holder.ratesVALUE.setText(rates.ratesVALUE);

        return row;
    }

    static class ListElementsHolder
    {
       ImageView ratesFLAG;
        TextView ratesID;
        TextView ratesDESC;
        EditText ratesVALUE;
    }

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

}