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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import seniordesign.cosmicraydetector.androidplot.CountVsPressureActivity;
import seniordesign.cosmicraydetector.androidplot.CountVsTimeActivity;
import seniordesign.cosmicraydetector.graphview.GraphViewActivity;
import seniordesign.cosmicraydetector.hellocharts.HelloChartActivity;
import seniordesign.cosmicraydetector.mpandroidchart.MPAChartActivity;


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
    private Spinner endYearSpinner,endMonthSpinner,endDaySpinner;


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

        boolean start = true;
        initYearSpinner(start);
        initYearSpinner(!start);
    }

    public void initYearSpinner(final boolean isStart) {
        Spinner yearSpinner;
        if (isStart) yearSpinner = startYearSpinner;
        else yearSpinner = endYearSpinner;

        List<String> spinnerVals = new ArrayList<String>();
        Iterator<Long> keysIterator = MainActivity.sensorDataMap.keySet().iterator();
        Date currentTimestamp;

        while (keysIterator.hasNext()){
            currentTimestamp = new Date(keysIterator.next());
            String currentYear = yearFormatter.format(currentTimestamp);
            if (!spinnerVals.contains(currentYear))
                spinnerVals.add(currentYear);
        }
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,spinnerVals);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                initMonthSpinner(isStart);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void initMonthSpinner(final boolean isStart){
        Spinner monthSpinner; String yearString;
        if (isStart){
            monthSpinner = startMonthSpinner;
            yearString = startYearSpinner.getSelectedItem().toString();
        }
        else{
            monthSpinner = endMonthSpinner;
            yearString = endYearSpinner.getSelectedItem().toString();
        }

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
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,spinnerVals);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                initDaySpinner(isStart);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void initDaySpinner(final boolean isStart){
        Spinner daySpinner; String monthString;
        if (isStart){
            daySpinner = startDaySpinner;
            monthString = startMonthSpinner.getSelectedItem().toString();
        }
        else{
            daySpinner = endDaySpinner;
            monthString = endMonthSpinner.getSelectedItem().toString();
        }

        List<String> spinnerVals = new ArrayList<String>();
        Iterator<Long> keysIterator = MainActivity.sensorDataMap.keySet().iterator();
        Date currentTimestamp;

        while (keysIterator.hasNext()){
            currentTimestamp = new Date(keysIterator.next());
            String currentMonth = monthFormatter.format(currentTimestamp);
            if (!currentMonth.equals(monthString)) continue;

            else {
                String currentDay = dayFormmatter.format(currentTimestamp);
                if (!spinnerVals.contains(currentDay))
                    spinnerVals.add(currentDay);
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,spinnerVals);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);
    }

    public void onClickCountvsTime(View view) {
        Log.i(TAG, "Launching AndroidPlot Count vs. Time");
       // Intent myIntent = new Intent(GraphActivity.this, CountVsTimeActivity.class);
        Intent myIntent = new Intent(GraphActivity.this, CountVsTimeActivity.class);
        GraphActivity.this.startActivity(myIntent);
    }


    public void onClickCountvsPressure(View view) {
        Log.i(TAG, "Launching AndroidPlot Count vs. Pressure");
        Intent myIntent = new Intent(GraphActivity.this, CountVsPressureActivity.class);
        GraphActivity.this.startActivity(myIntent);
    }

    public void onClickGraphView(View View){
        Log.i(TAG, "Launching GraphView");
        Intent myIntent = new Intent(GraphActivity.this, GraphViewActivity.class);
        GraphActivity.this.startActivity(myIntent);
    }

    public void onClickMPAChart(View View){

        if(xType.equals("") || yType.equals("")){
            Toast.makeText(this, "Please Select X and Y Axis Values", Toast.LENGTH_LONG).show();
        }
        else {
            Log.i(TAG, "Launching MPAChart");
            Intent myIntent = new Intent(GraphActivity.this, MPAChartActivity.class);
            GraphActivity.this.startActivity(myIntent);
        }

    }

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
        Log.i(TAG,"X_RADIO_GROUP CLICKED");
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.x_date:
                if (checked) {
                    xType = "Date";
                    Log.i(TAG, "Set XTYPE to Date, xType is" + xType);
                }
                    break;
            case R.id.x_count:
                if (checked){
                    xType = "Count";
                    Log.i(TAG, "Set XTYPE to Count, xType is" + xType);
                }
                    break;
            case R.id.x_temp:
                if (checked) {
                    xType = "Temperature";
                    Log.i(TAG, "Set XTYPE to Temperature, xType is" + xType);
                }
                    break;
            case R.id.x_pres:
                if (checked){
                    xType = "Pressure";
                    Log.i(TAG, "Set XTYPE to Pressure, xType is" + xType);
                }
                    break;
            case R.id.x_humd:
                if (checked){
                    xType = "Humidity";
                    Log.i(TAG, "Set XTYPE to Humidity, xType is" + xType);
                }
                break;
        }
    }

    public void onYRadioGroupClicked(View view) {
        Log.i(TAG,"Y_RADIO_GROUP CLICKED");
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();


        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.y_date:
                if (checked) {
                    yType = "Date";
                    Log.i(TAG, "Set YTYPE to Date, yType is" + yType);
                }
                break;
            case R.id.y_count:
                if (checked){
                    yType = "Count";
                    Log.i(TAG, "Set YTYPE to Count, yType is" + yType);
                }
                break;
            case R.id.y_temp:
                if (checked) {
                    yType = "Temperature";
                    Log.i(TAG, "Set YTYPE to Temperature, yType is" + yType);
                }
                break;
            case R.id.y_pres:
                if (checked){
                    yType = "Pressure";
                    Log.i(TAG, "Set YTYPE to Pressure, yType is" + yType);
                }
                break;
            case R.id.y_humd:
                if (checked){
                    yType = "Humidity";
                    Log.i(TAG, "Set YTYPE to Humidity, yType is" + yType);
                }
                break;
        }
    }
}

