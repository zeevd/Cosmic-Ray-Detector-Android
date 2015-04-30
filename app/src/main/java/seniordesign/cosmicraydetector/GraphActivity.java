package seniordesign.cosmicraydetector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
    final SimpleDateFormat monthDayFormmatter = new SimpleDateFormat("MM/dd");
    final SimpleDateFormat hourFormatter = new SimpleDateFormat("hh:mm a");

    //GLOBAL VARIABLES///
    public static String xType;
    public static String yType ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        initSpinners();

    }

    private void initSpinners(){
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
        Log.i(TAG, "Launching MPAChart");
        Intent myIntent = new Intent(GraphActivity.this, MPAChartActivity.class);
        GraphActivity.this.startActivity(myIntent);

    }

    public void onClickHelloChart(View View){
        Log.i(TAG, "Launching HelloChart");
        Intent myIntent = new Intent(GraphActivity.this, HelloChartActivity.class);
        GraphActivity.this.startActivity(myIntent);

    }
}

