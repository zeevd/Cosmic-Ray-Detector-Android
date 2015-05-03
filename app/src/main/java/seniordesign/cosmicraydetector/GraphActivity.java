package seniordesign.cosmicraydetector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import seniordesign.cosmicraydetector.androidplot.CountVsPressureActivity;
import seniordesign.cosmicraydetector.androidplot.CountVsTimeActivity;


public class GraphActivity extends ActionBarActivity {
    ///LOGGING TAG///
    private static final String TAG = "GraphActivity";

    final SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
    final SimpleDateFormat monthFormmatter = new SimpleDateFormat("MM");
    final SimpleDateFormat dayFormmatter = new SimpleDateFormat("dd");
    final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss a");

    Spinner startYearSpinner, startMonthSpinner, startDaySpinner, startTimeSpinner;
    Spinner endYearSpinner, endMonthSpinner, endDaySpinner, endTimeSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        startYearSpinner = (Spinner) findViewById(R.id.spinner_startyear);
        startMonthSpinner = (Spinner) findViewById(R.id.spinner_startmonth);
        startDaySpinner = (Spinner) findViewById(R.id.spinner_startday);
        startTimeSpinner = (Spinner) findViewById(R.id.spinner_starttime);

        endYearSpinner = (Spinner) findViewById(R.id.spinner_endyear);
        endMonthSpinner = (Spinner) findViewById(R.id.spinner_endmonth);
        endDaySpinner = (Spinner) findViewById(R.id.spinner_endday);
        endTimeSpinner = (Spinner) findViewById(R.id.spinner_endtime);

        initSpinner(startYearSpinner,yearFormatter);
        initSpinner(startMonthSpinner,monthFormmatter);
        initSpinner(startDaySpinner, dayFormmatter);
        initSpinner(startTimeSpinner, timeFormatter);

        initSpinner(endYearSpinner,yearFormatter);
        initSpinner(endMonthSpinner,monthFormmatter);
        initSpinner(endDaySpinner, dayFormmatter);
        initSpinner(endTimeSpinner, timeFormatter);

    }

    private void initSpinner(Spinner spinner, SimpleDateFormat formatter){
        List<String> spinnerStrings = new ArrayList<String>();
        Iterator<Long> keysIterator = MainActivity.sensorDataMap.keySet().iterator();

        while (keysIterator.hasNext()){
            String str = formatter.format(new Date(keysIterator.next()));
            if (!spinnerStrings.contains(str))
                spinnerStrings.add(str);
        }


        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,spinnerStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setOnItemSelectedListener(new SpinnerListener());

        spinner.setAdapter(adapter);

    }


    /*private void initSpinners(){
        //TODO: MODULARIZE and implement dependencies

        Spinner startYearSpinner = (Spinner) findViewById(R.id.spinner_startyear);
        Spinner startMonthDaySpinner = (Spinner) findViewById(R.id.spinner_startmonthday);
        Spinner startHourSpinner = (Spinner) findViewById(R.id.spinner_starthour);
        Spinner endYearSpinner = (Spinner) findViewById(R.id.spinner_endyear);
        Spinner endMonthDaySpinner = (Spinner) findViewById(R.id.spinner_enddaymonth);
        Spinner endHourSpinner = (Spinner) findViewById(R.id.spinner_endhour);


        List<String> yearsList = new ArrayList<String>();
        List<String> monthsDaysList = new ArrayList<String>();
        List<String> hoursList = new ArrayList<String>();

        //Long[] keys = MainActivity.sensorDataMap.keySet().toArray(new Long[MainActivity.sensorDataMap.size()]);
        Iterator<Long> keysIterator;
        //TODO: INJECT DEPENDENCIES YEAR -> MONTH/DAY -> HOUR

        //YEARS
        //for (int i=0; i<keys.length; i++) {
        keysIterator = MainActivity.sensorDataMap.keySet().iterator();
        while (keysIterator.hasNext()){
            String dayAsString = yearFormatter.format(new Date(keysIterator.next()));
            if (!yearsList.contains(dayAsString))
                yearsList.add(dayAsString);
        }
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,yearsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startYearSpinner.setAdapter(adapter);
        endYearSpinner.setAdapter(adapter);
        //YEARS

        //MONTHS+DAYS
        //for (int i=0; i<keys.length; i++) {
        keysIterator = MainActivity.sensorDataMap.keySet().iterator();
        while (keysIterator.hasNext()){
            Date currentTimestamp = new Date(keysIterator.next());
            //if current timestamp has incorrect year, continue
            //else
            String dayAsString = monthDayFormmatter.format(currentTimestamp);
            if (!monthsDaysList.contains(dayAsString))
                monthsDaysList.add(dayAsString);
        }

        adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,monthsDaysList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startMonthDaySpinner.setAdapter(adapter);
        endMonthDaySpinner.setAdapter(adapter);
        //MONTHS+DAYS

        //HOURS
        //for (int i=0; i<keys.length; i++) {
        keysIterator = MainActivity.sensorDataMap.keySet().iterator();
        while (keysIterator.hasNext()){
            Date currentTimestamp = new Date(keysIterator.next());
            //if we're on the wrong day, continue
            //else
            String dayAsString = hourFormatter.format(currentTimestamp);
            if (!hoursList.contains(dayAsString))   //TODO: remove to improve performance?
                hoursList.add(dayAsString);
        }

        adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,hoursList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startHourSpinner.setAdapter(adapter);
        endHourSpinner.setAdapter(adapter);
        //HOURS

    }
*/
    public void onClickCountvsTime(View view) {
        Log.i(TAG, "Launching AndroidPlot Count vs. Time");
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

