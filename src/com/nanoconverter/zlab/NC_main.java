package com.nanoconverter.zlab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SuppressLint("SimpleDateFormat")
public class NC_main extends Activity {

    /* TODO ��������� ��� ���� ���������� */
    public static NC_main mainContext = null;
    public static HorizontalPager nc_pager;				/* ��������� ������� */
    public static Document doc;							/* ������ � ���������� XML */
    public static ProgressDialog dialog_XML;			/* ������ ��� AsyncTask */
    public static String[] valute_ID;					/* ������ ID ���� ����� �������� ����� */
    public static String[] valute_value;				/* ������ �������� ���� ����� �������� ����� */
    public static String[] valute_value_show;			/* ������ ������������ �������� �� ������� ������ */
    public static String[] valute_value_calc;			/* ������ ������������ �������� ��� �������� */
    public static String[] valute_name;					/* ������ �������� ���� ����� �������� ����� */
    public static String[] valute_all_ID;				/* ������ ID ���� ����� */
    public static String[] valute_all_name;				/* ������ ������������ ���� ����� */
    public static String[] valute_all_ID_reordered;		/* ������ ID ���� ����� � ���������� ������������ */
    public static String[] valute_ID_reordered;         /* ������ ID ������������ �����, � ���������� ������������ */
    public static int valute_all;						/* ���������� ���� ID � ������������ */
    public static SharedPreferences prefs;	            /* ��������� ��� �������� ���������� */
    public static String prefs_source;					/* ��������� � ���������� �������� */
    public static boolean prefs_inverse;				/* ��������� � ���������� ��������� �����*/
    public static String prefs_theme;					/* ��������� � ���������� ���� */
    public static String prefs_actions_sendresults;     /* �������� �� ���� ���������� */
    public static String prefs_actions_listitem;        /* �������� �� ������ ������� ������ */
    public static String prefs_actions_calcresults;     /* �������� �� ������� ������������ */
    public static String prefs_updates;                 /* ������ ���������� */
    public static Set<String> prefs_currency_active;	/* ��������� � ���������� �������� ������ */
    public static String prefs_currency_def;			/* ��������� � ���������� ������ ��-��������� */
    public static String dateCurent;					/* ��������� ��� ������� ���� */
    public static String dateXML;						/* ��������� ��� �������� ���� */
    public static int len;							    /* ��������� ������ ������ ������ �� ����� */
    public static int true_len;						    /* ��������� ������ ������������� ������ */
    public static int idx_from = 0;						/* ������������� ����������� ��� ������ ������ */
    public static int idx_to = 0;							/* ������������� ����������� ��� ������ ������ */
    public static int idx_to_clear;                     /* ������������� ������ ������ */
    public static int idx_from_clear;                   /* ������������� ������ ������ */
    public static String handleMessage;
    public static SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy");
    static LayoutInflater inflater;
    static LinearLayout p0_converter, p1_rates;          /* ��������� � �����*/
    static RelativeLayout main;                         /* ������ ������ �������� */

    static Handler HandleToastError = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast toast = Toast.makeText(mainContext, handleMessage, Toast.LENGTH_SHORT);
            LinearLayout ToastView = (LinearLayout) toast.getView();
            ImageView imageWorld = new ImageView(mainContext);
            imageWorld.setImageResource(R.drawable.ui_icon_error);
            ToastView.addView(imageWorld, 0);
            toast.show();
        }
    };

    static Handler HandleToastGood = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast toast = Toast.makeText(mainContext, handleMessage, Toast.LENGTH_SHORT);
            LinearLayout ToastView = (LinearLayout) toast.getView();
            ImageView imageWorld = new ImageView(mainContext);
            imageWorld.setImageResource(R.drawable.ui_icon_good);
            ToastView.addView(imageWorld, 0);
            toast.show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mainContext = this;

        /* ��������� ��������� */
        prefs = PreferenceManager.getDefaultSharedPreferences(mainContext);
        getPrefs();

        super.onCreate(savedInstanceState);

        /* ���������� ������� view */
        nc_pager = new HorizontalPager(getApplicationContext());
        inflater = (LayoutInflater) mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /* ���������� ������� ���� ID � ������������ ��� �����, � ����� �� ���������� */
        valute_all_ID = getResources().getStringArray(R.array.valute_all_ID);
        valute_all_name = getResources().getStringArray(R.array.valute_all_name);
        valute_all = valute_all_ID.length;

        /* ������������� */
        dateCurent = dateformat.format(new Date());								/* ������� ���� */
        dateXML = null;															/* ��������� ��� �������� ���� */

        update_scheduler();

        /* DONE ����������� ���������� ������������
        /* DONE �������� about �� graph *
		/* DONE �������� �������� � ������������� � p3_about */

        setContentView(nc_pager);
        nc_pager.setOnScreenSwitchListener(onScreenSwitchListener);
    }

    /* �������� ��� ����� ��������� ������ */
    private final HorizontalPager.OnScreenSwitchListener onScreenSwitchListener =
            new HorizontalPager.OnScreenSwitchListener() {
                public void onScreenSwitched(final int screen) {
                /* TODO ������ � shared, ��������� �������� ������� (screen) � ��������������� �� ��� ��������. */
                    //Log.e("SCREEN", String.valueOf(nc_pager.getCurrentScreen()));
                }
            };
/* �������� ��� ����� ��������� ������ */

    public static void update_VAR() {
            	/* ����� ��������� ������������� �����, ������� ���� � ������ ����� */
        true_len = 0;
        for (int j = 0; j < len; j++) {
            if (prefs_currency_active.contains(valute_ID[j])) {
                true_len++;
            }
        }
        valute_value_calc = new String[true_len];	/* ������ ��������, ��� ����������� �� ������� ���������� */
        valute_value_show = new String[true_len];	/* ������ ��������, ��� ����������� ����� ������������ ������ ��-��������� */
        valute_ID_reordered = new String[true_len]; /* ������ ��������, �������� ������ ������� ID � ���������� ������������ */

        int count = 0;
        for (int i = 0; i < valute_all; i++) {
            if (prefs_currency_active.contains(valute_all_ID_reordered[i])) {
                for (int j = 0; j < len; j++) {
                    if (valute_ID[j].equals(valute_all_ID_reordered[i])) {
                        valute_ID_reordered[count] = valute_all_ID_reordered[i];
                        valute_value_calc[count] = valute_value[j];
                        count++;
                    }
                }
            }
        }

                /* ������� ������������ �� ������ ������ */
        for (int j = 0; j < valute_all; j++) {
            for (int i = 0; i < true_len; i++) {
                if (valute_all_ID[j].equals(valute_ID_reordered[i])) {
                    valute_name[i] = valute_all_name[j];
                }
            }
        }

    }

    /* ���������� ������� ���������� */
    public static void update_P1(final Context mC) {

            	/* radioGroup_Main_FROM */
        RadioButton[] rb_from = new RadioButton[prefs_currency_active.size()];
        final RadioGroup radioGroup_Main_FROM = new RadioGroup(mainContext);
            	/* radioGroup_Main_TO */
        RadioButton[] rb_to = new RadioButton[prefs_currency_active.size()];
        final RadioGroup radioGroup_Main_TO = new RadioGroup(mainContext);

        p0_converter = (LinearLayout) inflater.inflate(R.layout.p0_converter, null);
        final ImageButton rotate_Button = (ImageButton) p0_converter.findViewById(R.id.imageButton_Rotate);
        final EditText editText_FROM = (EditText) p0_converter.findViewById(R.id.editText_FROM);
        final EditText editText_TO = (EditText) p0_converter.findViewById(R.id.editText_TO);
        //editText_TO.setKeyListener(null); /* turn off text editable */
        editText_TO.setFocusable(false);

        if (prefs_actions_sendresults.equals("CopySend")){
            editText_TO.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent detail = new Intent(mC, NC_calculator.class);
                    detail.putExtra("calc_value", editText_TO.getText().toString());
                    mC.startActivity(detail);
                    return true;
                }
            });
                editText_TO.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!editText_TO.getText().toString().equals("")){
                        ClipboardManager clipboard = (ClipboardManager)mC.getSystemService(CLIPBOARD_SERVICE);
                        clipboard.setText(editText_TO.getText().toString());
                        handleMessage=mC.getResources().getString(R.string.UI_copy_done);
                        HandleToastGood.sendEmptyMessage(0);
                        }
                    }
                    });
        }

        if (prefs_actions_sendresults.equals("SendCopy")){
            editText_TO.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!editText_TO.getText().toString().equals("")){
                        ClipboardManager clipboard = (ClipboardManager)mC.getSystemService(CLIPBOARD_SERVICE);
                        clipboard.setText(editText_TO.getText().toString());
                        handleMessage=mC.getResources().getString(R.string.UI_copy_done);
                        HandleToastGood.sendEmptyMessage(0);
                    }
                    return true;
                }
            });
            editText_TO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent detail = new Intent(mC, NC_calculator.class);
                    detail.putExtra("calc_value", editText_TO.getText().toString());
                    mC.startActivity(detail);
                }
            });
        }


        editText_FROM.addTextChangedListener(new TextWatcher() { /* ��������� ����� � ��������� ���� */
            public void afterTextChanged(Editable arg0) {
                if (!editText_FROM.getText().toString().equals("") && !editText_FROM.getText().toString().equals(".")) {
                    editText_TO.setText(String.valueOf(
                            Float.parseFloat(editText_FROM.getText().toString()) * Float.parseFloat(valute_value_calc[idx_from_clear]) / Float.parseFloat(valute_value_calc[idx_to_clear])));
                } else {
                    editText_TO.setText("");
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        }
        );

        for (int j = 0; j < true_len; j++) {
            /* DONE ����� ���� ���� ����, �������� value_ID
        	 * DONE �������� OnRadioButtonPressedListener
        	 */
        			    /* ������� ������ � ������ */
            int resID = mainContext.getResources().getIdentifier("flag_" + valute_ID_reordered[j].toLowerCase(), "drawable", "com.nanoconverter.zlab");         /* ��������� ������� ����� ��� ����� ID */
            if (resID == 0) {
                resID = mainContext.getResources().getIdentifier("flag_united", "drawable", "com.nanoconverter.zlab");
            }
            Drawable flag = mainContext.getResources().getDrawable(resID);

                        /* from radio button */
            rb_from[j] = new RadioButton(mainContext);
            rb_from[j].setText(valute_ID_reordered[j]);
            rb_from[j].setCompoundDrawablesWithIntrinsicBounds(flag, null, null, null);
            rb_from[j].setCompoundDrawablePadding(10);
            rb_from[j].setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT)); /* ����������� �� ��� ������ */
            radioGroup_Main_FROM.addView(rb_from[j]);
                        /* add from divider */
            View div_from = new View(mainContext);
            div_from.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, 1)); /* ����������� �� ��� ������ */
            div_from.setBackgroundColor(Color.parseColor("#88aaaaaa"));
            radioGroup_Main_FROM.addView(div_from);

                        /* to radio button */
            rb_to[j] = new RadioButton(mainContext);
            rb_to[j].setText(valute_ID_reordered[j]);
            rb_to[j].setCompoundDrawablesWithIntrinsicBounds(flag, null, null, null);
            rb_to[j].setCompoundDrawablePadding(10);
            rb_to[j].setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT)); /* ����������� �� ��� ������ */
            radioGroup_Main_TO.addView(rb_to[j]);
                        /* add to divider */
            View div_to = new View(mainContext);
            div_to.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, 1)); /* ����������� �� ��� ������ */
            div_to.setBackgroundColor(Color.parseColor("#88aaaaaa"));
            radioGroup_Main_TO.addView(div_to);

        }

        radioGroup_Main_FROM.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() /* ��������� ������ ������ ������ */ {
            public void onCheckedChanged(RadioGroup rGroup, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) rGroup.findViewById(checkedId);
                idx_from = rGroup.indexOfChild(checkedRadioButton);

                        /* ������� divider */
                if (idx_from != 0) {
                    idx_from_clear = idx_from / 2;
                } else {
                    idx_from_clear = idx_from;
                }

                if (!editText_FROM.getText().toString().equals("") && !editText_FROM.getText().toString().equals(".")) {
                    editText_TO.setText(String.valueOf(
                            Float.parseFloat(editText_FROM.getText().toString()) * Float.parseFloat(valute_value_calc[idx_from_clear]) / Float.parseFloat(valute_value_calc[idx_to_clear])));
                }
            }
        });

        radioGroup_Main_TO.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() /* ��������� ������ ������ ������ */ {
            public void onCheckedChanged(RadioGroup rGroup, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) rGroup.findViewById(checkedId);
                idx_to = rGroup.indexOfChild(checkedRadioButton);

                        /* ������� divider */
                if (idx_to != 0) {
                    idx_to_clear = idx_to / 2;
                } else {
                    idx_to_clear = idx_to;
                }

                if (!editText_FROM.getText().toString().equals("") && !editText_FROM.getText().toString().equals(".")) {
                    editText_TO.setText(String.valueOf(
                            Float.parseFloat(editText_FROM.getText().toString()) * Float.parseFloat(valute_value_calc[idx_from_clear]) / Float.parseFloat(valute_value_calc[idx_to_clear])));
                }
            }
        });

        rotate_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int from = idx_from;
                idx_from = idx_to;
                idx_to = from;
                radioGroup_Main_FROM.check(radioGroup_Main_FROM.getChildAt(idx_from).getId());
                radioGroup_Main_TO.check(radioGroup_Main_TO.getChildAt(idx_to).getId());

                if (!editText_FROM.getText().toString().equals("") && !editText_FROM.getText().toString().equals(".")) {
                    editText_TO.setText(String.valueOf(
                            Float.parseFloat(editText_FROM.getText().toString()) * Float.parseFloat(valute_value_calc[idx_from_clear]) / Float.parseFloat(valute_value_calc[idx_to_clear])));
                }
            }
        });

        ViewGroup parent_of_radioGroup_FROM = (ViewGroup) p0_converter.findViewById(R.id.sv_layout_from);
        parent_of_radioGroup_FROM.removeAllViewsInLayout();
        parent_of_radioGroup_FROM.addView(radioGroup_Main_FROM);

        ViewGroup parent_of_radioGroup_TO = (ViewGroup) p0_converter.findViewById(R.id.sv_layout_to);
        parent_of_radioGroup_TO.removeAllViewsInLayout();
        parent_of_radioGroup_TO.addView(radioGroup_Main_TO);

                /* ������ */
        if (idx_from < radioGroup_Main_FROM.getChildCount()) {
            radioGroup_Main_FROM.check(radioGroup_Main_FROM.getChildAt(idx_from).getId());
        } else {
            radioGroup_Main_FROM.check(radioGroup_Main_FROM.getChildAt(0).getId());
        }

        if (idx_to < radioGroup_Main_TO.getChildCount()) {
            radioGroup_Main_TO.check(radioGroup_Main_TO.getChildAt(idx_to).getId());
        } else {
            radioGroup_Main_TO.check(radioGroup_Main_TO.getChildAt(0).getId());
        }

        if (nc_pager.getChildAt(0) != null) {
            nc_pager.removeViewAt(0);
        }
        nc_pager.addView(p0_converter, 0);
    }

    /* ���������� ������� ������ */
    public static void update_P2() {
            	/* DONE ����� ������ � ID ���� ����� �� �������� �����
        		 * DONE ����� ���������� ������ TextEdit
            	 * DONE ��������� ���������� ��������� ����� �� �������, ���������� static
        		 * DONE ����������� ������ ��� �����, ID ������� ���� � prefs_currency_active[]
        		 * DONE ����� value_name, �������� �������������� ����������� ���� �����
        		 * DONE ���� ������ ��� � ������ �����, �� ��� ������� � ����������, ����� ���� ��-�� ���� ��� ���� ������� ������� ListOfRates ����� null
        		 * DONE ��������� ������ � p1_rates, ��� ����������� ������
        		 */

        ListRates[] listOfRates = new ListRates[true_len];

        BigDecimal y;

        String valute_value_default = "1";
        for (int j = 0; j < true_len; j++) {
            if (prefs_currency_def.equals(valute_ID_reordered[j])) {
                valute_value_default = valute_value_calc[j];
            }
        }

        for (int j = 0; j < true_len; j++) {
        			/* DONE ����������� ������������ ������ ��-���������
        			 * DONE �������� ��������� �����
        			 */
            if (prefs_inverse) {
                y = new BigDecimal(Double.parseDouble(valute_value_default) / Double.parseDouble(valute_value_calc[j]));
            } else {
                y = new BigDecimal(Double.parseDouble(valute_value_calc[j]) / Double.parseDouble(valute_value_default));
            }
            y = y.setScale(4, BigDecimal.ROUND_HALF_UP);  // �������� ���������� ������� �����

            valute_value_show[j] = y.toString();
        }

        for (int j = 0; j < true_len; j++) {
            /* DONE	����� ���� ���� ����, �������� value_ID
             */
        			/* ������� ������ � ������ */
            int resID = mainContext.getResources().getIdentifier("flag_" + valute_ID_reordered[j].toLowerCase(), "drawable", "com.nanoconverter.zlab"); /* ��������� ������� ����� ��� ����� ID */
            if (resID == 0) {
                resID = mainContext.getResources().getIdentifier("flag_united", "drawable", "com.nanoconverter.zlab");
            }

            listOfRates[j] = new ListRates(resID, valute_ID_reordered[j], valute_name[j], valute_value_show[j]);
        }

        		/* TODO �������� ������ ������ �� ������� �� ������ ��� � ����. (���� �� ���� �����) */
        final ListView listOfRatesRender = new ListView(mainContext);
        final ListRatesAdapter listOfRatesAdapter = new ListRatesAdapter(mainContext, R.layout.p1_rates_list_item, listOfRates);
        listOfRatesRender.setAdapter(listOfRatesAdapter);
        listOfRatesRender.setDividerHeight(1);


        if (prefs_actions_listitem.equals("LongClick")) {
            listOfRatesRender.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                     /* DONE ����� ID � ����� ����������
                	  * DONE ������� ��������� focusable ����������
                	  * DONE ��� ����� �� ������, ���������� ����� ������������ ����
    				  * DONE �������� ��������� � ���������
    				  * DONE ��������� ��������� ����, ��� ������������ ���� ��������
    				  */
                    prefs_currency_def = valute_ID_reordered[position];
                    prefs.edit().putString("prefs_currency_def", valute_ID_reordered[position]).commit();

                    BigDecimal y;
                    String valute_value_default = valute_value_calc[position];

                    for (int j = 0; j < true_len; j++) {
                        if (prefs_inverse) {
                            y = new BigDecimal(Double.parseDouble(valute_value_default) / Double.parseDouble(valute_value_calc[j]));
                        } else {
                            y = new BigDecimal(Double.parseDouble(valute_value_calc[j]) / Double.parseDouble(valute_value_default));
                        }
                        y = y.setScale(4, BigDecimal.ROUND_HALF_UP);  // �������� ���������� ������� �����

                        listOfRatesAdapter.getItem(j).ratesVALUE = y.toString();
                    }

                    handleMessage = valute_ID_reordered[position] + " = 1.00";
                    HandleToastGood.sendEmptyMessage(0);
                    listOfRatesAdapter.notifyDataSetChanged();

                    return false;
                }
            });
        } else if (prefs_actions_listitem.equals("Click")) {
            listOfRatesRender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long id) {
                    prefs_currency_def = valute_ID_reordered[position];
                    prefs.edit().putString("prefs_currency_def", valute_ID_reordered[position]).commit();

                    BigDecimal y;
                    String valute_value_default = valute_value_calc[position];

                    for (int j = 0; j < true_len; j++) {
                        if (prefs_inverse) {
                            y = new BigDecimal(Double.parseDouble(valute_value_default) / Double.parseDouble(valute_value_calc[j]));
                        } else {
                            y = new BigDecimal(Double.parseDouble(valute_value_calc[j]) / Double.parseDouble(valute_value_default));
                        }
                        y = y.setScale(4, BigDecimal.ROUND_HALF_UP);  // �������� ���������� ������� �����

                        listOfRatesAdapter.getItem(j).ratesVALUE = y.toString();
                    }

                    handleMessage = valute_ID_reordered[position] + " = 1.00";
                    HandleToastGood.sendEmptyMessage(0);
                    listOfRatesAdapter.notifyDataSetChanged();

                }
            });

        }


        p1_rates = (LinearLayout) inflater.inflate(R.layout.p1_rates, null);

        ViewGroup parent_of_listView = (ViewGroup) p1_rates.findViewById(R.id.parent_of_listrates);
        parent_of_listView.removeAllViewsInLayout();
        parent_of_listView.addView(listOfRatesRender);

        if (nc_pager.getChildAt(1) != null) {
            nc_pager.removeViewAt(1);
        }
        nc_pager.addView(p1_rates, 1);
    }

    /* ������� ���� ���������� */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_update: {

                if (dateXML == null) {
                    dateXML = dateCurent;
                } /* ���� ���� �� ������� � data picker, �� ������ �� ������� */

                new NC_XML_get().execute(prefs_source, dateXML);
            /* TODO �������� data picker
             * DONE ������� �������� ������� ��������� ����
             * DONE ������� �������� ������� ���������� �����
             */

                return true;
            }
            case R.id.menu_converter: {
                nc_pager.setCurrentScreen(0, true);
                return true;
            }
            case R.id.menu_rates: {
                nc_pager.setCurrentScreen(1, true);
                return true;
            }
            case R.id.menu_calculator: {
           	/* DONE ������� ����� ����������� */
                Intent Activity = new Intent(getBaseContext(), com.nanoconverter.zlab.NC_calculator.class);
                startActivity(Activity);
                return true;
            }
            case R.id.menu_graph: {
           	/* TODO ������� ����� Graph */
                return true;
            }
            case R.id.menu_settings: {
                Intent PreferencesActivity = new Intent(getBaseContext(), com.nanoconverter.zlab.NC_preferences.class);
                startActivity(PreferencesActivity);
                return true;
            }
            case R.id.menu_about: {
        	/* DONE ������� ����� ABOUT */
                Intent Activity = new Intent(getBaseContext(), com.nanoconverter.zlab.NC_about.class);
                startActivity(Activity);
                return true;
            }
            case R.id.menu_quit: {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
/* ������� ���� ���������� */

    /* ��������� ��������� */
    @SuppressLint("NewApi")
    private void getPrefs() {
        prefs_source = prefs.getString("prefs_source", "CBR");
        prefs_inverse = prefs.getBoolean("prefs_inverse", false);
        prefs_theme = prefs.getString("prefs_theme", "NanoGlow");
        prefs_currency_def = prefs.getString("prefs_currency_def", "USD");
        prefs_actions_listitem = prefs.getString("prefs_actions_listitem", "LongClick");
        prefs_actions_sendresults = prefs.getString("prefs_actions_sendresults", "SendCopy");
        prefs_actions_calcresults = prefs.getString("prefs_actions_calcresults", "SendCopy");
        prefs_updates = prefs.getString("prefs_updates", "manual");

        /* TODO �������� ��������� ��� ������ API */
        Set<String> set = new HashSet<String>(Arrays.asList(getResources().getStringArray(R.array.valute_all_ID)));
        prefs_currency_active = prefs.getStringSet("prefs_currency_active", set);

        /* ��������� ���������� ���� */
        if (prefs_theme.equals("HoloLightDarkBar")) {
            setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
        } else if (prefs_theme.equals("Holo")) {
            setTheme(android.R.style.Theme_Holo);
        } else if (prefs_theme.equals("HoloLight")) {
            setTheme(android.R.style.Theme_Holo_Light);
        } else if (prefs_theme.equals("HoloLightStripe")) {
            setTheme(android.R.style.Theme_Holo_Light);
        } else if (prefs_theme.equals("HoloLightDarkBarStripe")) {
            setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
        } else if (prefs_theme.equals("HoloStripe")) {
            setTheme(android.R.style.Theme_Holo);
        }

        /* ���������� */
        if (prefs.getString("valute_reordered", "null").equals("null")) {
            valute_all_ID_reordered = NC_main.valute_all_ID;
        } else {
            valute_all_ID_reordered = prefs.getString("valute_reordered", "null").split(",");
        }
    }

    private void setPrefs() {
        if (prefs_theme.equals("NanoGlow")) {
            nc_pager.setBackgroundDrawable(getResources().getDrawable(R.drawable.bckgr_glow_repeat));
        } else if (prefs_theme.equals("NanoClassic")) {
            nc_pager.setBackgroundDrawable(getResources().getDrawable(R.drawable.bckgr_nano_mirror));
        } else if (prefs_theme.equals("HoloStripe") || prefs_theme.equals("HoloLightStripe") || prefs_theme.equals("HoloLightDarkBarStripe") || prefs_theme.equals("HoloStripe")) {
            nc_pager.setBackgroundDrawable(getResources().getDrawable(R.drawable.bckgr_stripe_repeat));
        }
    }

    private void update_scheduler() {

        File root = android.os.Environment.getExternalStorageDirectory(); 		/* ���� �� SD */
        File dir = new File(root.getAbsolutePath() + "/nanoConverter/xmls"); 	/* ���� �� �������� � XML */
        File file = new File(dir, prefs_source + "_" + dateCurent + ".xml");			/* ���� �� ����� XML */

        if (prefs_updates.equals("auto")) {
            if (dateXML == null) {
                dateXML = dateCurent;
            }
            new NC_XML_get().execute(prefs_source, dateXML);
        } else if (prefs_updates.equals("manual")) {
            if (file.exists()) {            /* XML ���� */
			/* ��������� �������� � ����� ������ � ������ */
                Document doc; /* ������ ��� ������ XML */
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db;
                try {
                    db = dbf.newDocumentBuilder();
                    doc = db.parse(new InputSource(new InputStreamReader(new FileInputStream(file))));
                    new NC_XML_parse().execute(doc); /* � ���������� �� ������ */
                } catch (Exception e) {/* ������ �������� ����� ������� */}
            } else {
			/* XML ����������� */
                main = (RelativeLayout) inflater.inflate(R.layout.main, null);
                nc_pager.addView(main, 0);
            }
        } else if (prefs_updates.equals("background")) {
            if (file.exists()) {            /* XML ���� */
			/* ��������� �������� � ����� ������ � ������ */
                Document doc; /* ������ ��� ������ XML */
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db;
                try {
                    db = dbf.newDocumentBuilder();
                    doc = db.parse(new InputSource(new InputStreamReader(new FileInputStream(file))));
                    new NC_XML_parse().execute(doc); /* � ���������� �� ������ */
                } catch (Exception e) {/* ������ �������� ����� ������� */}
            }

            if (dateXML == null) {
                dateXML = dateCurent;
            }
            new NC_XML_get().execute(prefs_source, dateXML);
        } else if (prefs_updates.equals("perday")) {
            /* TODO perday */
            main = (RelativeLayout) inflater.inflate(R.layout.main, null);
            nc_pager.addView(main, 0);
        }
    }

    /* OnResume */
    protected void onResume() {
        getPrefs();
        setPrefs();

        /* ����������� ���������� � ���������� ��������� ����������� */
        idx_from = Integer.parseInt(prefs.getString("idx_from", "0"));
        idx_to = Integer.parseInt(prefs.getString("idx_to", "0"));

   	 /* TODO ���������� �� ������������ getPrefs
   	  * DONE �������� �������� �� null, if true, ��������� ����� ��������� ���������
   	  */
        super.onResume();
    }

    /* OnStop */
    protected void onStop() {
        super.onStop();
        /* ��������� ��������� ����������� � ���������� */
        prefs.edit().putString("idx_from", String.valueOf(idx_from)).commit();
        prefs.edit().putString("idx_to", String.valueOf(idx_to)).commit();

    }
}
