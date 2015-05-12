package seniordesign.cosmicraydetector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import seniordesign.cosmicraydetector.hellocharts.HelloChartActivity;

public class GraphActivity extends ActionBarActivity {
    ///LOGGING TAG///
    private static final String TAG = "GraphActivity";

    final SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
    final SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");
    final SimpleDateFormat dayFormmatter = new SimpleDateFormat("dd");

    //GLOBAL VARIABLES///
    public static String xType = "";
    public static String yType = "" ;

    private Spinner startYearSpinner,startMonthSpinner,startDaySpinner;

    Long startAsEpoch;
    private Spinner endYearSpinner,endMonthSpinner,endDaySpinner;

    private RadioGroup radioGroup;
    private RadioButton buttonClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        startYearSpinner = (Spinner) findViewById(R.id.spinner_startyear);
        startMonthSpinner = (Spinner) findViewById(R.id.spinner_startmonth);
        startDaySpinner = (Spinner) findViewById(R.id.spinner_startday);

        endYearSpinner = (Spinner) findViewById(R.id.spinner_endyear);
        endMonthSpinner = (Spinner) findViewById(R.id.spinner_endmonth);
        endDaySpinner = (Spinner) findViewById(R.id.spinner_endday);

        initSpinner(startYearSpinner, getStartYears(), startYearListener);

    }


    public void initSpinner(Spinner spinner, List<String> spinnerVals, AdapterView.OnItemSelectedListener listener) {
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,spinnerVals);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);
    }

    private Iterator<Long> getEndIterator() {
        List<String> spinnerVals = new ArrayList<String>();
        Iterator<Long> keysIterator = MainActivity.sensorDataMap.keySet().iterator();
        Date currentTimestamp;
        while (keysIterator.hasNext()) {
            Long currentEpoch = keysIterator.next();
            if (currentEpoch >= startAsEpoch) break;
        }
        return keysIterator;
    }

    private List<String> getStartYears(){
        List<String> spinnerVals = new ArrayList<String>();
        Iterator<Long> keysIterator = MainActivity.sensorDataMap.keySet().iterator();
        Date currentTimestamp;
        while (keysIterator.hasNext()){
            currentTimestamp = new Date(keysIterator.next());
            String currentYear = yearFormatter.format(currentTimestamp);
            if (!spinnerVals.contains(currentYear))
                spinnerVals.add(currentYear);
        }
        return spinnerVals;
    }
    private List<String> getStartMonths(){
        String yearString = startYearSpinner.getSelectedItem().toString();

        List<String> spinnerVals = new ArrayList<String>();
        Iterator<Long> keysIterator = MainActivity.sensorDataMap.keySet().iterator();
        Date currentTimestamp;

        while (keysIterator.hasNext()){
            currentTimestamp = new Date(keysIterator.next());
            String currentYear = yearFormatter.format(currentTimestamp);
            if (!currentYear.equals(yearString)) continue;

            else {
                String currentMonth = monthFormatter.format(currentTimestamp);
                if (!spinnerVals.contains(currentMonth))
                    spinnerVals.add(currentMonth);
            }
        }
        return spinnerVals;
    }
    private List<String> getStartDays(){
        String yearString = startYearSpinner.getSelectedItem().toString();
        String monthString = startMonthSpinner.getSelectedItem().toString();

        List<String> spinnerVals = new ArrayList<String>();
        Iterator<Long> keysIterator = MainActivity.sensorDataMap.keySet().iterator();
        Date currentTimestamp;

        while (keysIterator.hasNext()){
            currentTimestamp = new Date(keysIterator.next());
            String currentMonth = monthFormatter.format(currentTimestamp);
            String currentYear = yearFormatter.format(currentTimestamp);
            if (currentMonth.equals(monthString) && currentYear.equals(yearString)){
                String currentDay = dayFormmatter.format(currentTimestamp);
                if (!spinnerVals.contains(currentDay))
                    spinnerVals.add(currentDay);
            }
        }
        return spinnerVals;
    }

    private List<String> getEndYears(){
        List<String> spinnerVals = new ArrayList<String>();
        Iterator<Long> keysIterator = getEndIterator();
        Date currentTimestamp;
        while (keysIterator.hasNext()){
            currentTimestamp = new Date(keysIterator.next());
            String currentYear = yearFormatter.format(currentTimestamp);
            if (!spinnerVals.contains(currentYear))
                spinnerVals.add(currentYear);
        }
        return spinnerVals;
    }

    private List<String> getEndMonths(){
        String yearString = endYearSpinner.getSelectedItem().toString();

        List<String> spinnerVals = new ArrayList<String>();
        Iterator<Long> keysIterator = getEndIterator();
        Date currentTimestamp;

        while (keysIterator.hasNext()){
            currentTimestamp = new Date(keysIterator.next());
            String currentYear = yearFormatter.format(currentTimestamp);
            if (!currentYear.equals(yearString)) continue;

            else {
                String currentMonth = monthFormatter.format(currentTimestamp);
                if (!spinnerVals.contains(currentMonth))
                    spinnerVals.add(currentMonth);
            }
        }
        return spinnerVals;
    }
    private List<String> getEndDays(){
        String yearString = endYearSpinner.getSelectedItem().toString();
        String monthString = endMonthSpinner.getSelectedItem().toString();

        List<String> spinnerVals = new ArrayList<String>();
        Iterator<Long> keysIterator = getEndIterator();
        Date currentTimestamp;

        while (keysIterator.hasNext()){
            currentTimestamp = new Date(keysIterator.next());
            String currentMonth = monthFormatter.format(currentTimestamp);
            String currentYear = yearFormatter.format(currentTimestamp);
            if (currentMonth.equals(monthString) && currentYear.equals(yearString)){
                String currentDay = dayFormmatter.format(currentTimestamp);
                if (!spinnerVals.contains(currentDay))
                    spinnerVals.add(currentDay);
            }
        }
        return spinnerVals;
    }


    AdapterView.OnItemSelectedListener endYearListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            initSpinner(endMonthSpinner,getEndMonths(),endMonthListener);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };
    AdapterView.OnItemSelectedListener endMonthListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            initSpinner(endDaySpinner, getEndDays(), endDayListener);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };
    AdapterView.OnItemSelectedListener endDayListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){}

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };

    AdapterView.OnItemSelectedListener startYearListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            initSpinner(startMonthSpinner,getStartMonths(),startMonthListener);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };
    AdapterView.OnItemSelectedListener startMonthListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            initSpinner(startDaySpinner, getStartDays(), startDayListener);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };
    AdapterView.OnItemSelectedListener startDayListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String startAsString = startMonthSpinner.getSelectedItem().toString() + "_" + startDaySpinner.getSelectedItem().toString() + "_" + startYearSpinner.getSelectedItem().toString();
            SimpleDateFormat toEpochFormmatter = new SimpleDateFormat("MM_dd_yy");
            try {
                Date d = toEpochFormmatter.parse(startAsString);
                startAsEpoch = d.getTime();
                initSpinner(endYearSpinner,getEndYears(),endYearListener);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };



    public void onClickHelloChart(View View){
        if(xType.equals("") || yType.equals("")){
            Toast.makeText(this, "Please Select X and Y Axis Values", Toast.LENGTH_LONG).show();
        }
        else {
            Log.i(TAG, "Launching HelloChart");
            Intent myIntent = new Intent(GraphActivity.this, HelloChartActivity.class);
            GraphActivity.this.startActivity(myIntent);
        }

    }

    public void onXRadioGroupClicked(View view) {

        //Enable All Buttons for the  Y-Radio Group
        radioGroup = (RadioGroup) findViewById(R.id.y_group);
        ((RadioButton) radioGroup.findViewById(R.id.y_date)).setEnabled(true);
        ((RadioButton) radioGroup.findViewById(R.id.y_count)).setEnabled(true);
        ((RadioButton) radioGroup.findViewById(R.id.y_temp)).setEnabled(true);
        ((RadioButton) radioGroup.findViewById(R.id.y_pres)).setEnabled(true);
        ((RadioButton) radioGroup.findViewById(R.id.y_humd)).setEnabled(true);

        //Get X-RadioButton Group
        Log.i(TAG,"X_RADIO_GROUP CLICKED");
        radioGroup = (RadioGroup) findViewById(R.id.x_group);

        //Find which radioButton within the group was pressed
        switch(radioGroup.getCheckedRadioButtonId()) {
            case R.id.x_date:
                if(((RadioButton) radioGroup.findViewById(R.id.x_date)).isChecked()){
                    xType = "Date";
                    Log.i(TAG, "Set XTYPE to Date, xType is" + xType);
                    //disable the y-pair
                    radioGroup = (RadioGroup) findViewById(R.id.y_group);
                    ((RadioButton) radioGroup.findViewById(R.id.y_date)).setEnabled(false);
                }
                    break;
            case R.id.x_count:
                if(((RadioButton) radioGroup.findViewById(R.id.x_count)).isChecked()){
                    xType = "Count";
                    Log.i(TAG, "Set XTYPE to Count, xType is" + xType);
                    //disable the y-pair
                    radioGroup = (RadioGroup) findViewById(R.id.y_group);
                    ((RadioButton) radioGroup.findViewById(R.id.y_count)).setEnabled(false);
                }
                    break;
            case R.id.x_temp:
                if(((RadioButton) radioGroup.findViewById(R.id.x_temp)).isChecked()){
                    xType = "Temperature";
                    Log.i(TAG, "Set XTYPE to Temperature, xType is" + xType);
                    //disable the y-pair
                    radioGroup = (RadioGroup) findViewById(R.id.y_group);
                    ((RadioButton) radioGroup.findViewById(R.id.y_temp)).setEnabled(false);
                }
                    break;
            case R.id.x_pres:
                if(((RadioButton) radioGroup.findViewById(R.id.x_pres)).isChecked()){
                    xType = "Pressure";
                    Log.i(TAG, "Set XTYPE to Pressure, xType is" + xType);
                    //disable the y-pair
                    radioGroup = (RadioGroup) findViewById(R.id.y_group);
                    ((RadioButton) radioGroup.findViewById(R.id.y_pres)).setEnabled(false);
                }
                    break;
            case R.id.x_humd:
                if(((RadioButton) radioGroup.findViewById(R.id.x_humd)).isChecked()){
                    xType = "Humidity";
                    Log.i(TAG, "Set XTYPE to Humidity, xType is" + xType);
                    //disable the y-pair
                    radioGroup = (RadioGroup) findViewById(R.id.y_group);
                    ((RadioButton) radioGroup.findViewById(R.id.y_humd)).setEnabled(false);
                }
                break;
        }
    }

    public void onYRadioGroupClicked(View view) {

        //Enable All Buttons for the  X-Radio Group
        radioGroup = (RadioGroup) findViewById(R.id.x_group);
        ((RadioButton) radioGroup.findViewById(R.id.x_date)).setEnabled(true);
        ((RadioButton) radioGroup.findViewById(R.id.x_count)).setEnabled(true);
        ((RadioButton) radioGroup.findViewById(R.id.x_temp)).setEnabled(true);
        ((RadioButton) radioGroup.findViewById(R.id.x_pres)).setEnabled(true);
        ((RadioButton) radioGroup.findViewById(R.id.x_humd)).setEnabled(true);

        //Get Y-RadioButton Group
        Log.i(TAG,"Y_RADIO_GROUP CLICKED");
        radioGroup = (RadioGroup) findViewById(R.id.y_group);

        //Find which radioButton within the group was pressed
        switch(radioGroup.getCheckedRadioButtonId()) {
            case R.id.y_date:
                if(((RadioButton) radioGroup.findViewById(R.id.y_date)).isChecked()){
                    yType = "Date";
                    Log.i(TAG, "Set YTYPE to Date, yType is" + yType);
                    //disable the x-pair
                    radioGroup = (RadioGroup) findViewById(R.id.x_group);
                    ((RadioButton) radioGroup.findViewById(R.id.x_date)).setEnabled(false);
                }
                break;
            case R.id.y_count:
                if(((RadioButton) radioGroup.findViewById(R.id.y_count)).isChecked()){
                    yType = "Count";
                    Log.i(TAG, "Set YTYPE to Count, yType is" + yType);
                    //disable the x-pair
                    radioGroup = (RadioGroup) findViewById(R.id.x_group);
                    ((RadioButton) radioGroup.findViewById(R.id.x_count)).setEnabled(false);
                }
                break;
            case R.id.y_temp:
                if(((RadioButton) radioGroup.findViewById(R.id.y_temp)).isChecked()){
                    yType = "Temperature";
                    Log.i(TAG, "Set YTYPE to Temperature, yType is" + yType);
                    //disable the x-pair
                    radioGroup = (RadioGroup) findViewById(R.id.x_group);
                    ((RadioButton) radioGroup.findViewById(R.id.x_temp)).setEnabled(false);
                }
                break;
            case R.id.y_pres:
                if(((RadioButton) radioGroup.findViewById(R.id.y_pres)).isChecked()){
                    yType = "Pressure";
                    Log.i(TAG, "Set YTYPE to Pressure, yType is" + yType);
                    //disable the x-pair
                    radioGroup = (RadioGroup) findViewById(R.id.x_group);
                    ((RadioButton) radioGroup.findViewById(R.id.x_pres)).setEnabled(false);
                }
                break;
            case R.id.y_humd:
                if(((RadioButton) radioGroup.findViewById(R.id.y_humd)).isChecked()){
                    yType = "Humidity";
                    Log.i(TAG, "Set YTYPE to Humidity, yType is" + yType);
                    //disable the x-pair
                    radioGroup = (RadioGroup) findViewById(R.id.x_group);
                    ((RadioButton) radioGroup.findViewById(R.id.x_humd)).setEnabled(false);
                }
                break;
        }
    }
}

