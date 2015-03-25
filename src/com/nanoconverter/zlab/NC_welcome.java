package com.nanoconverter.zlab;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class NC_welcome extends Activity  {

	HorizontalPager nc_pager_welcome;				/* Держатель страниц */
	RelativeLayout welcome_layout;
	LayoutInflater inflater;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nc_pager_welcome = new HorizontalPager(getApplicationContext());
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        	welcome_layout = (RelativeLayout) inflater.inflate(R.layout.welcome_p1, null);
        	nc_pager_welcome.addView(welcome_layout, 0);
	        welcome_layout = (RelativeLayout) inflater.inflate(R.layout.welcome_p2, null);
	        nc_pager_welcome.addView(welcome_layout, 1);
	        welcome_layout = (RelativeLayout) inflater.inflate(R.layout.welcome_p3, null);
	        nc_pager_welcome.addView(welcome_layout, 2);
	        welcome_layout = (RelativeLayout) inflater.inflate(R.layout.welcome_p4, null);
	        nc_pager_welcome.addView(welcome_layout, 3);
	        welcome_layout = (RelativeLayout) inflater.inflate(R.layout.welcome_p5, null);
	        nc_pager_welcome.addView(welcome_layout, 4);
	        welcome_layout = (RelativeLayout) inflater.inflate(R.layout.welcome_p6, null);
	        nc_pager_welcome.addView(welcome_layout, 5);
		setContentView(nc_pager_welcome);
    }
    
    /* next button */
    public void onButtonPressedNext(View view) {
    	switch (view.getId()) {
        case R.id.button1:
        	nc_pager_welcome.setCurrentScreen((nc_pager_welcome.getCurrentScreen()+1), true);}
       }
    
    /* update button */
    public void onButtonPressedUpdate(View view) {
    	switch (view.getId()) {
        case R.id.button1:
        	
        	}
       }
}
