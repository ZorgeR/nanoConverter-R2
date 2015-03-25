package com.nanoconverter.zlab;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

public class NC_preferences extends PreferenceActivity {
	
        @SuppressWarnings("deprecation")
		@Override
        protected void onCreate(Bundle savedInstanceState) {
        	
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.preferences);

            getPreferenceManager()
                    .findPreference("reorder")
                    .setOnPreferenceClickListener(
                            new OnPreferenceClickListener() {
                                public boolean onPreferenceClick(Preference preference) {

                                    Intent Activity = new Intent(getBaseContext(),NC_valute_reorder.class);
                                    startActivity(Activity);

                                    return true;
                                }
                            });

        }
}