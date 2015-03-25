package com.nanoconverter.zlab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class NC_calculator extends Activity {

    public static EditText calc_display;
    public static String[] arguments;
    public static int[] arg_idx_from;
    public static int[] arg_idx_to;
    public static String[] operand;
    public static String calc_value;
    static Vibrator vibe;
    static int vibe_long;
    static LayoutInflater inflater;
    static LinearLayout calc_layout;
    static LinearLayout calc_button_layout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        vibe_long = 50;
        /* TODO Добавить в настройки управление длинной вибрации
         * DONE Добавить в настройки, клик на дисплее, для копирования обратно в калькулятор
         */
        calc_layout = (LinearLayout) inflater.inflate(R.layout.p2_calculator, null);
        setContentView(calc_layout);

        calc_display = (EditText) findViewById(R.id.calc_display);
        calc_display.setKeyListener(null);
        if (NC_main.prefs_actions_calcresults.equals("CopySend")){
            calc_display.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!calc_display.getText().toString().equals("")){
                        ClipboardManager clipboard = (ClipboardManager)getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
                        clipboard.setText(calc_display.getText().toString());
                        NC_main.handleMessage=getApplicationContext().getResources().getString(R.string.UI_copy_done);
                        NC_main.HandleToastGood.sendEmptyMessage(0);
                    }
                }
            });
            calc_display.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        if (calc_display.getText().toString().equals("")){
                            NC_calculator.this.finish();
                        } else {
                        Double d = Double.parseDouble(calc_display.getText().toString());

                            EditText editText = (EditText) NC_main.p0_converter.findViewById(R.id.editText_FROM);
                            editText.setText(calc_display.getText().toString());
                            NC_calculator.this.finish();
                        }
                    } catch (Exception e) {/* неверное число */}
                    return true;
                }
            });
        }

        if (NC_main.prefs_actions_calcresults.equals("SendCopy")){
            calc_display.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!calc_display.getText().toString().equals("")){
                        ClipboardManager clipboard = (ClipboardManager)getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
                        clipboard.setText(calc_display.getText().toString());
                        NC_main.handleMessage=getApplicationContext().getResources().getString(R.string.UI_copy_done);
                        NC_main.HandleToastGood.sendEmptyMessage(0);
                    }
                    return true;
                }
            });
            calc_display.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (calc_display.getText().toString().equals("")){
                            NC_calculator.this.finish();
                        } else {
                        Double d = Double.parseDouble(calc_display.getText().toString());

                            EditText editText = (EditText) NC_main.p0_converter.findViewById(R.id.editText_FROM);
                            editText.setText(calc_display.getText().toString());
                            NC_calculator.this.finish();
                        }
                    } catch (Exception e) {/* неверное число */}
                }
            });
        }

        /* DONE добавить копирование в буфер обмена при клике по окну калькулятора */

        calc_button_layout = (LinearLayout) findViewById(R.id.calc_button);
        for (int i=0;i<calc_button_layout.getChildCount();i++){
            LinearLayout ButtonLayout = (LinearLayout) calc_button_layout.getChildAt(i);
            for (int j=0;j<ButtonLayout.getChildCount();j++){
                Button b = (Button) ButtonLayout.getChildAt(j); /* ButtonLayout.getChildAt(j).setOnTouchListener */
                b.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int Action=event.getAction();
                        String ActionString="";
                        switch(Action){
                        case MotionEvent.ACTION_DOWN: ActionString="ACTION_DOWN";break;
                        case MotionEvent.ACTION_UP: ActionString="ACTION_UP";break;
                        }
                        if (ActionString.equals("ACTION_DOWN")){
                            vibe.vibrate(vibe_long);
                            vibe_long=0;
                        }
                        if (ActionString.equals("ACTION_UP")){
                            vibe_long=50;
                        }
                        return false;
                    }
                });
                }
        }

        if (getIntent().getExtras()!=null){
        calc_value = getIntent().getExtras().getString("calc_value");
        calc_display.setText(calc_value);
        } else {
        calc_display.setText("");
        }

    }

    public void CalcButtonClickHandler(View view) {
        switch (view.getId()) {
            case R.id.b_clear:
                arguments = null;
                arg_idx_to = null;
                arg_idx_from = null;
                operand = null;
                calc_display.setText("");
                break;
        }
        switch (view.getId()) {
            case R.id.b_backspace:
                if (!calc_display.getText().toString().equals("")) {
                    if (!calc_display.getText().toString().substring(calc_display.getText().toString().length() - 1).equals("+") &&
                            !calc_display.getText().toString().substring(calc_display.getText().toString().length() - 1).equals("-") &&
                            !calc_display.getText().toString().substring(calc_display.getText().toString().length() - 1).equals("*") &&
                            !calc_display.getText().toString().substring(calc_display.getText().toString().length() - 1).equals("/")) {
                        String string = calc_display.getText().toString();
                        int len = string.length();
                        calc_display.setText("");
                        calc_display.append(string.subSequence(0, len - 1));
                    }
                }

                break;
        }
        switch (view.getId()) {
            case R.id.b_dot:
                calc_display.append(".");
                break;
        }
        switch (view.getId()) {
            case R.id.b_0:
                calc_display.append("0");
                break;
        }
        switch (view.getId()) {
            case R.id.b_1:
                calc_display.append("1");
                break;
        }
        switch (view.getId()) {
            case R.id.b_2:
                calc_display.append("2");
                break;
        }
        switch (view.getId()) {
            case R.id.b_3:
                calc_display.append("3");
                break;
        }
        switch (view.getId()) {
            case R.id.b_4:
                calc_display.append("4");
                break;
        }
        switch (view.getId()) {
            case R.id.b_5:
                calc_display.append("5");
                break;
        }
        switch (view.getId()) {
            case R.id.b_6:
                calc_display.append("6");
                break;
        }
        switch (view.getId()) {
            case R.id.b_7:
                calc_display.append("7");
                break;
        }
        switch (view.getId()) {
            case R.id.b_8:
                calc_display.append("8");
                break;
        }
        switch (view.getId()) {
            case R.id.b_9:
                calc_display.append("9");
                break;
        }
        switch (view.getId()) {
            case R.id.b_d:
                if (!calc_display.getText().toString().equals("")) {

                    int prev = calc_display.getText().toString().length() - 1;

                    if (!calc_display.getText().toString().substring(prev).equals("+") &&
                            !calc_display.getText().toString().substring(prev).equals("-") &&
                            !calc_display.getText().toString().substring(prev).equals("*") &&
                            !calc_display.getText().toString().substring(prev).equals("/")) {
                        if (arguments == null) {
                            arguments = new String[1];
                            arg_idx_from = new int[1];
                            arg_idx_to = new int[1];
                            operand = new String[1];

                            arguments[0] = calc_display.getText().toString();
                            arg_idx_from[0] = 0;
                            arg_idx_to[0] = calc_display.getText().toString().length();
                            operand[0] = "delete";

                            calc_display.append("/");
                        } else {

                            int len = arguments.length;
                            int total = calc_display.getText().toString().length();
                            int previous = arg_idx_to[arguments.length - 1] + 1;

                            String new_item = calc_display.getText().toString().substring(previous, total);

                            String[] new_arguments = new String[len + 1];
                            int[] new_arg_idx_from = new int[len + 1];
                            int[] new_arg_idx_to = new int[len + 1];
                            String[] new_operand = new String[len + 1];

                            for (int i = 0; i < len; i++) {
                                new_arguments[i] = arguments[i];
                                new_arg_idx_from[i] = arg_idx_to[i];
                                new_arg_idx_to[i] = arg_idx_to[i];
                                new_operand[i] = operand[i];
                            }

                            new_arguments[len] = new_item;
                            new_arg_idx_from[len] = arg_idx_to[len - 1];
                            new_arg_idx_to[len] = total;
                            new_operand[len] = "delete";

                            arguments = new_arguments;
                            arg_idx_to = new_arg_idx_to;
                            arg_idx_from = new_arg_idx_from;
                            operand = new_operand;

                            calc_display.append("/");
                        }
                    }
                }
                break;
        }
        switch (view.getId()) {
            case R.id.b_u:
                if (!calc_display.getText().toString().equals("")) {
                    int prev = calc_display.getText().toString().length() - 1;

                    if (!calc_display.getText().toString().substring(prev).equals("+") &&
                            !calc_display.getText().toString().substring(prev).equals("-") &&
                            !calc_display.getText().toString().substring(prev).equals("*") &&
                            !calc_display.getText().toString().substring(prev).equals("/")) {
                        if (arguments == null) {
                            arguments = new String[1];
                            arg_idx_from = new int[1];
                            arg_idx_to = new int[1];
                            operand = new String[1];

                            arguments[0] = calc_display.getText().toString();
                            arg_idx_from[0] = 0;
                            arg_idx_to[0] = calc_display.getText().toString().length();
                            operand[0] = "umn";

                            calc_display.append("*");
                        } else {

                            int len = arguments.length;
                            int total = calc_display.getText().toString().length();
                            int previous = arg_idx_to[arguments.length - 1] + 1;

                            String new_item = calc_display.getText().toString().substring(previous, total);

                            String[] new_arguments = new String[len + 1];
                            int[] new_arg_idx_from = new int[len + 1];
                            int[] new_arg_idx_to = new int[len + 1];
                            String[] new_operand = new String[len + 1];

                            for (int i = 0; i < len; i++) {
                                new_arguments[i] = arguments[i];
                                new_arg_idx_from[i] = arg_idx_to[i];
                                new_arg_idx_to[i] = arg_idx_to[i];
                                new_operand[i] = operand[i];
                            }

                            new_arguments[len] = new_item;
                            new_arg_idx_from[len] = arg_idx_to[len - 1];
                            new_arg_idx_to[len] = total;
                            new_operand[len] = "umn";

                            arguments = new_arguments;
                            arg_idx_to = new_arg_idx_to;
                            arg_idx_from = new_arg_idx_from;
                            operand = new_operand;

                            calc_display.append("*");
                        }
                    }
                }
                break;
        }
        switch (view.getId()) {
            case R.id.b_minus:
                if (!calc_display.getText().toString().equals("")) {
                    int prev = calc_display.getText().toString().length() - 1;

                    if (!calc_display.getText().toString().substring(prev).equals("+") &&
                            !calc_display.getText().toString().substring(prev).equals("-") &&
                            !calc_display.getText().toString().substring(prev).equals("*") &&
                            !calc_display.getText().toString().substring(prev).equals("/")) {
                        if (arguments == null) {
                            arguments = new String[1];
                            arg_idx_from = new int[1];
                            arg_idx_to = new int[1];
                            operand = new String[1];

                            arguments[0] = calc_display.getText().toString();
                            arg_idx_from[0] = 0;
                            arg_idx_to[0] = calc_display.getText().toString().length();
                            operand[0] = "minus";

                            calc_display.append("-");
                        } else {

                            int len = arguments.length;
                            int total = calc_display.getText().toString().length();
                            int previous = arg_idx_to[arguments.length - 1] + 1;

                            String new_item = calc_display.getText().toString().substring(previous, total);

                            String[] new_arguments = new String[len + 1];
                            int[] new_arg_idx_from = new int[len + 1];
                            int[] new_arg_idx_to = new int[len + 1];
                            String[] new_operand = new String[len + 1];

                            for (int i = 0; i < len; i++) {
                                new_arguments[i] = arguments[i];
                                new_arg_idx_from[i] = arg_idx_to[i];
                                new_arg_idx_to[i] = arg_idx_to[i];
                                new_operand[i] = operand[i];
                            }

                            new_arguments[len] = new_item;
                            new_arg_idx_from[len] = arg_idx_to[len - 1];
                            new_arg_idx_to[len] = total;
                            new_operand[len] = "minus";

                            arguments = new_arguments;
                            arg_idx_to = new_arg_idx_to;
                            arg_idx_from = new_arg_idx_from;
                            operand = new_operand;

                            calc_display.append("-");
                        }
                    }
                }
                break;
        }
        switch (view.getId()) {
            case R.id.b_plus:
                if (!calc_display.getText().toString().equals("")) {
                    int prev = calc_display.getText().toString().length() - 1;

                    if (!calc_display.getText().toString().substring(prev).equals("+") &&
                            !calc_display.getText().toString().substring(prev).equals("-") &&
                            !calc_display.getText().toString().substring(prev).equals("*") &&
                            !calc_display.getText().toString().substring(prev).equals("/")) {
                        if (arguments == null) {
                            arguments = new String[1];
                            arg_idx_from = new int[1];
                            arg_idx_to = new int[1];
                            operand = new String[1];

                            arguments[0] = calc_display.getText().toString();
                            arg_idx_from[0] = 0;
                            arg_idx_to[0] = calc_display.getText().toString().length();
                            operand[0] = "plus";

                            calc_display.append("+");
                        } else {

                            int len = arguments.length;
                            int total = calc_display.getText().toString().length();
                            int previous = arg_idx_to[arguments.length - 1] + 1;

                            String new_item = calc_display.getText().toString().substring(previous, total);

                            String[] new_arguments = new String[len + 1];
                            int[] new_arg_idx_from = new int[len + 1];
                            int[] new_arg_idx_to = new int[len + 1];
                            String[] new_operand = new String[len + 1];

                            for (int i = 0; i < len; i++) {
                                new_arguments[i] = arguments[i];
                                new_arg_idx_from[i] = arg_idx_to[i];
                                new_arg_idx_to[i] = arg_idx_to[i];
                                new_operand[i] = operand[i];
                            }

                            new_arguments[len] = new_item;
                            new_arg_idx_from[len] = arg_idx_to[len - 1];
                            new_arg_idx_to[len] = total;
                            new_operand[len] = "plus";

                            arguments = new_arguments;
                            arg_idx_to = new_arg_idx_to;
                            arg_idx_from = new_arg_idx_from;
                            operand = new_operand;

                            calc_display.append("+");
                        }
                    }
                }
                break;
        }
        switch (view.getId()) {
            case R.id.b_equal:
                if (!calc_display.getText().toString().equals("")) {
                    int prev = calc_display.getText().toString().length() - 1;
                    if (!calc_display.getText().toString().substring(prev).equals("+") &&
                            !calc_display.getText().toString().substring(prev).equals("-") &&
                            !calc_display.getText().toString().substring(prev).equals("*") &&
                            !calc_display.getText().toString().substring(prev).equals("/")) {
                        if (arguments != null) {
                            int len = arguments.length;
                            int total = calc_display.getText().toString().length();
                            int previous = arg_idx_to[arguments.length - 1] + 1;

                            String new_item = calc_display.getText().toString().substring(previous, total);

                            String[] new_arguments = new String[len + 1];
                            int[] new_arg_idx_from = new int[len + 1];
                            int[] new_arg_idx_to = new int[len + 1];
                            String[] new_operand = new String[len + 1];

                            for (int i = 0; i < len; i++) {
                                new_arguments[i] = arguments[i];
                                new_arg_idx_from[i] = arg_idx_to[i];
                                new_arg_idx_to[i] = arg_idx_to[i];
                                new_operand[i] = operand[i];
                            }

                            new_arguments[len] = new_item;
                            new_arg_idx_from[len] = arg_idx_to[len - 1];
                            new_arg_idx_to[len] = total;
                            new_operand[len] = "equal";

                            arguments = new_arguments;
                            arg_idx_to = new_arg_idx_to;
                            arg_idx_from = new_arg_idx_from;
                            operand = new_operand;


                            try {
                                Float f = null;
                                f = Float.parseFloat(arguments[0]);

                                for (int i = 0; i < len; i++) {

                                    if (operand[i].equals("delete")) {
                                        f = f / Float.parseFloat(arguments[i + 1]);
                                    }
                                    if (operand[i].equals("minus")) {
                                        f = f - Float.parseFloat(arguments[i + 1]);
                                    }
                                    if (operand[i].equals("plus")) {
                                        f = f + Float.parseFloat(arguments[i + 1]);
                                    }
                                    if (operand[i].equals("umn")) {
                                        f = f * Float.parseFloat(arguments[i + 1]);
                                    }

                                }

                                arguments = null;
                                arg_idx_to = null;
                                arg_idx_from = null;
                                operand = null;

                                calc_display.setText(String.valueOf(f));

                            } catch (Exception ioe) {
                                arguments = null;
                                arg_idx_to = null;
                                arg_idx_from = null;
                                operand = null;

                                calc_display.setText("Err");

                                NC_main.handleMessage="Oooooops!";
                                NC_main.HandleToastError.sendEmptyMessage(0);

                            }

                        }
                    }
                }

                break;
        }
    }


}
