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

import com.cengalabs.flatui.FlatUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeMap;

public class GraphActivity extends ActionBarActivity {
    ///LOGGING TAG///
    private static final String TAG = "GraphActivity";

    //GLOBAL VARIABLES///
    public static String xType = "";
    public static String yType = "" ;

    //Time-related stuff
    final SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
    final SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");
    final SimpleDateFormat dayFormmatter = new SimpleDateFormat("dd");
    final static int endOfDayOffSet = 86399000; //23 hrs, 59 min, 59 sec
    public static Long startAsEpoch, endAsEpoch;

    //UI Stuff
    private Spinner startYearSpinner,startMonthSpinner,startDaySpinner;
    private Spinner endYearSpinner,endMonthSpinner,endDaySpinner;
    private RadioGroup radioGroup;

    //Data structure-related
    TreeMap<Long,SensorData> sensorDataMap = MainActivity.sensorDataMap;
    ArrayList<Long> sensorDataList = MainActivity.sensorDataList;
    //Contains a copy of the sensorDataMap keyset to allow random access by index

    //<Year,Index> maps. E.G. "2014, 5" means that the first timestamp in 2014 is located
    //at index 5 in the sensorDataList
    HashMap<String,Integer> startYearsIndices = new HashMap<String,Integer>();
    HashMap<String,Integer> startMonthsIndices = new HashMap<String,Integer>();
    HashMap<String,Integer> endYearsIndices = new HashMap<String,Integer>();
    HashMap<String,Integer> endMonthsIndices = new HashMap<String,Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Initializing GraphActivity.java");
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Init FlatUI");
        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.BLOOD);
        setContentView(R.layout.activity_graph);
        getSupportActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(this, FlatUI.BLOOD, false, 2));

        Log.i(TAG, "Setting all timezones to GMT");
        yearFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        monthFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        dayFormmatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        Log.i(TAG, "Initializing GUI elements");
        startYearSpinner = (Spinner) findViewById(R.id.spinner_startyear);
        startMonthSpinner = (Spinner) findViewById(R.id.spinner_startmonth);
        startDaySpinner = (Spinner) findViewById(R.id.spinner_startday);
        endYearSpinner = (Spinner) findViewById(R.id.spinner_endyear);
        endMonthSpinner = (Spinner) findViewById(R.id.spinner_endmonth);
        endDaySpinner = (Spinner) findViewById(R.id.spinner_endday);

        Log.i(TAG, "Starting spinner init sequeunce");
        initSpinner(startYearSpinner, getStartYears(), startYearListener);

    }


    public void initSpinner(Spinner spinner, List<String> spinnerVals, AdapterView.OnItemSelectedListener listener) {
        Log.v(TAG, "Creating spinner for vals: " + spinnerVals);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,spinnerVals);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);
    }


    private List<String> getStartYears(){
        Log.i(TAG,"Obtaining data for the start year spinner");
        List<String> spinnerVals = new ArrayList<String>();
        Date currentTimestamp;
        for (int i=0; i<sensorDataList.size(); i++){
            currentTimestamp = new Date(sensorDataList.get(i));
            String currentYear = yearFormatter.format(currentTimestamp);
            if (!spinnerVals.contains(currentYear)) {//If current year not in spinnerVals, add it
                spinnerVals.add(currentYear);
                startYearsIndices.put(currentYear, i);
            }
        }
        Log.v(TAG, "Data obtained: " + spinnerVals);
        return spinnerVals;
    }

    private List<String> getStartMonths(){
        Log.i(TAG,"Obtaining data for the start month spinner");
        String yearString = startYearSpinner.getSelectedItem().toString();

        List<String> spinnerVals = new ArrayList<String>();
        Date currentTimestamp;


        for (int i= startYearsIndices.get(yearString); i<sensorDataList.size(); i++){
            currentTimestamp = new Date(sensorDataList.get(i));
            String currentYear = yearFormatter.format(currentTimestamp);
            if (!currentYear.equals(yearString)) break;

            String currentMonth = monthFormatter.format(currentTimestamp);
            if (!spinnerVals.contains(currentMonth)) {
                spinnerVals.add(currentMonth);
                startMonthsIndices.put(currentMonth, i);
            }

        }
        Log.v(TAG, "Data obtained: " + spinnerVals);
        return spinnerVals;
    }
    private List<String> getStartDays(){
        Log.i(TAG,"Obtaining data for the start day spinner");
        String monthString = startMonthSpinner.getSelectedItem().toString();

        List<String> spinnerVals = new ArrayList<String>();
        Date currentTimestamp;

        for (int i= startMonthsIndices.get(monthString); i<sensorDataList.size(); i++){
            currentTimestamp = new Date(sensorDataList.get(i));
            String currentMonth = monthFormatter.format(currentTimestamp);
            if (!currentMonth.equals(monthString)) break;

            String currentDay = dayFormmatter.format(currentTimestamp);
            if (!spinnerVals.contains(currentDay)) {
                spinnerVals.add(currentDay);
            }

        }
        Log.v(TAG, "Data obtained: " + spinnerVals);
        return spinnerVals;
    }

    private List<String> getEndYears(){
        Log.i(TAG,"Obtaining data for the end year spinner");
        List<String> spinnerVals = new ArrayList<String>();
        Date currentTimestamp;

        for (int i=0; i<sensorDataList.size(); i++){
            Long currentEpoch = sensorDataList.get(i);
            if (currentEpoch<startAsEpoch) continue;

            currentTimestamp = new Date(currentEpoch);
            String currentYear = yearFormatter.format(currentTimestamp);
            if (!spinnerVals.contains(currentYear)) {
                spinnerVals.add(currentYear);
                endYearsIndices.put(currentYear, i);
            }
        }
        Log.v(TAG, "Data obtained: " + spinnerVals);
        return spinnerVals;
    }

    private List<String> getEndMonths(){
        Log.i(TAG,"Obtaining data for the end month spinner");
        String yearString = endYearSpinner.getSelectedItem().toString();

        List<String> spinnerVals = new ArrayList<String>();
        Date currentTimestamp;

        for (int i= endYearsIndices.get(yearString); i<sensorDataList.size(); i++){
            currentTimestamp = new Date(sensorDataList.get(i));
            String currentYear = yearFormatter.format(currentTimestamp);
            if (!currentYear.equals(yearString)) break;

            String currentMonth = monthFormatter.format(currentTimestamp);
            if (!spinnerVals.contains(currentMonth)) {
                spinnerVals.add(currentMonth);
                endMonthsIndices.put(currentMonth, i);
            }

        }
        Log.v(TAG, "Data obtained: " + spinnerVals);
        return spinnerVals;
    }
    private List<String> getEndDays(){
        Log.i(TAG,"Obtaining data for the end day spinner");
        String monthString = endMonthSpinner.getSelectedItem().toString();

        List<String> spinnerVals = new ArrayList<String>();
        Date currentTimestamp;

        for (int i= endMonthsIndices.get(monthString); i<sensorDataList.size(); i++){
            currentTimestamp = new Date(sensorDataList.get(i));
            String currentMonth = monthFormatter.format(currentTimestamp);
            if (!currentMonth.equals(monthString)) break;

            String currentDay = dayFormmatter.format(currentTimestamp);
            if (!spinnerVals.contains(currentDay)) {
                spinnerVals.add(currentDay);
            }

        }
        Log.v(TAG, "Data obtained: " + spinnerVals);
        return spinnerVals;
    }


    AdapterView.OnItemSelectedListener endYearListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i(TAG, "End year spinner selected. Initizaliing content");
            initSpinner(endMonthSpinner, getEndMonths(), endMonthListener);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };
    AdapterView.OnItemSelectedListener endMonthListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i(TAG, "End month spinner selected. Initizaliing content");
            initSpinner(endDaySpinner, getEndDays(), endDayListener);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };
    AdapterView.OnItemSelectedListener endDayListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
            Log.i(TAG, "End day spinner selected. Initizaliing content");
            String endAsString = endMonthSpinner.getSelectedItem().toString() + "_" + endDaySpinner.getSelectedItem().toString() + "_" + endYearSpinner.getSelectedItem().toString();
            SimpleDateFormat toEpochFormmatter = new SimpleDateFormat("MM_dd_yy");
            toEpochFormmatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                Date d = toEpochFormmatter.parse(endAsString);
                endAsEpoch = d.getTime() + endOfDayOffSet;
                Log.i(TAG, "End date as string: " + endAsString + " as epoch " + endAsEpoch);
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}

    };

    AdapterView.OnItemSelectedListener startYearListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i(TAG, "Start year spinner selected. Initizaliing content");
            initSpinner(startMonthSpinner,getStartMonths(),startMonthListener);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };
    AdapterView.OnItemSelectedListener startMonthListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i(TAG, "Start month spinner selected. Initizaliing content");
            initSpinner(startDaySpinner, getStartDays(), startDayListener);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };
    AdapterView.OnItemSelectedListener startDayListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i(TAG, "Start day spinner selected. Initizaliing content");
            String startAsString = startMonthSpinner.getSelectedItem().toString() + "_" + startDaySpinner.getSelectedItem().toString() + "_" + startYearSpinner.getSelectedItem().toString();
            SimpleDateFormat toEpochFormmatter = new SimpleDateFormat("MM_dd_yy");
            toEpochFormmatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                Date d = toEpochFormmatter.parse(startAsString);
                startAsEpoch = d.getTime();
                initSpinner(endYearSpinner,getEndYears(),endYearListener);
                Log.i(TAG, "Start date as string: " + startAsString + " as epoch " + startAsEpoch);
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
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

