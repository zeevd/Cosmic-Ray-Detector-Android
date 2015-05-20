package seniordesign.cosmicraydetector;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cengalabs.flatui.FlatUI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class HelloChartActivity extends ActionBarActivity {

    ///LOGGING TAG///
    private static final String TAG = "HelloChartActivity";


    private LineChartView chart;
    private LineChartData data;
    private final long day = 86401000;

    private boolean hasLines = true;
    private boolean hasPoints = true;
    private boolean isFilled = false;
    private boolean isCubic = false;
    private boolean oneDay = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Initializing HelloChartActivity.java");
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Init FlatUI");
        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.BLOOD);
        setContentView(R.layout.activity_hello_chart);
        getSupportActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(this, FlatUI.BLOOD, false, 2));


        // Get X and Y types to know what values to graph
        String xType = GraphActivity.xType;
        String yType = GraphActivity.yType;
        Log.i(TAG, "Graphing Y: " + yType + " X: " + xType);


        //List of vales to be graphed
        List<PointValue> values = new ArrayList<>();

        //Graph Generation
        float xValue, yValue;

        //List of Axis Labels
        List<AxisValue> xAxisValue = new ArrayList<>();
        List<AxisValue> yAxisValue = new ArrayList<>();


        SensorData sensorData;
        Set<Long> keySet = MainActivity.sensorDataMap.keySet();

        Log.i(TAG, "Graphing from Start Date: " + new Date(GraphActivity.startAsEpoch).toString());
        Log.i(TAG, "\n To End Date: " + new Date(GraphActivity.endAsEpoch).toString());
        Log.i(TAG, "Difference in Long is " + (GraphActivity.endAsEpoch - GraphActivity.startAsEpoch));

        //one day check
        oneDay = (GraphActivity.endAsEpoch - GraphActivity.startAsEpoch <= day);



        for(Long key : keySet){
            if (key < GraphActivity.startAsEpoch) continue;
            if (key > GraphActivity.endAsEpoch) break;

            sensorData = MainActivity.sensorDataMap.get(key);

            //get X based on type
            if(xType.equalsIgnoreCase("temperature")){
                xValue = sensorData.getTemperature().floatValue();
            }
            else if(xType.equalsIgnoreCase("pressure")){
                xValue = sensorData.getPressure().floatValue();
            }
            else if(xType.equalsIgnoreCase("date")){
                xValue = Long.valueOf(sensorData.getDate().getTime()).floatValue();
                AxisValue xLabel = new AxisValue(xValue);
                SimpleDateFormat dateFormat;
                if(oneDay){
                    dateFormat = new SimpleDateFormat("HH:mm:ss",Locale.US);
                }
                else{
                    dateFormat = new SimpleDateFormat("MM-dd-yyyy",Locale.US);
                }
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                String dateLabel = dateFormat.format(sensorData.getDate());
                xLabel.setLabel(dateLabel);
                xAxisValue.add(xLabel);
            }
            else if(xType.equalsIgnoreCase("humidity")){
                xValue = sensorData.getHumidity().floatValue();
            }
            else{// count
                xValue = sensorData.getCount().floatValue();
            }

            //get Y based on type
            if(yType.equalsIgnoreCase("temperature")){
                yValue = sensorData.getTemperature().floatValue();
            }
            else if(yType.equalsIgnoreCase("pressure")){
                yValue = sensorData.getPressure().floatValue();
            }
            else if(yType.equalsIgnoreCase("date")){
                yValue = Long.valueOf(sensorData.getDate().getTime()).floatValue();
                AxisValue yLabel = new AxisValue(yValue);
                SimpleDateFormat dateFormat;
                if(oneDay){
                    dateFormat = new SimpleDateFormat("HH:mm:ss",Locale.US);
                }
                else{
                    dateFormat = new SimpleDateFormat("MM-dd-yyyy",Locale.US);
                }
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                String dateLabel = dateFormat.format(sensorData.getDate());
                yLabel.setLabel(dateLabel);
                yAxisValue.add(yLabel);
            }
            else if(yType.equalsIgnoreCase("humidity")){
                yValue = sensorData.getHumidity().floatValue();
            }
            else{//count
                yValue = sensorData.getCount().floatValue();
            }

            //Create point and add to list
            PointValue point = new PointValue(xValue,yValue);
            values.add(point);
        }// end of for loop

        Line line = new Line(values).setColor(Color.RED).setCubic(false);
        List<Line> lines = new ArrayList<>(1);

        //Initial Line Setup
        line.setPointRadius(4);
        line.setStrokeWidth(2);
        line.setHasLabelsOnlyForSelected(true);


        if(xType.equalsIgnoreCase("pressure")){
            line.setFormatter(new SimpleLineChartValueFormatter(1));
        }

        lines.add(line);

        data = new LineChartData();
        data.setLines(lines);

        Axis xAxis = new Axis().setHasLines(true);
        Axis yAxis = new Axis().setHasLines(true);

        if(xType.equalsIgnoreCase("Date")){
            xAxis = new Axis(xAxisValue).setHasLines(true);
        }
        if(yType.equalsIgnoreCase("Date")){
            yAxis = new Axis(yAxisValue).setHasLines(true);
        }

        //X AXIS SETUP
        String xAxisLabel, yAxisLabel;
        if(xType.equalsIgnoreCase("temperature")){
            xAxisLabel = "Temperature (C)";
        }
        else if(xType.equalsIgnoreCase("pressure")){
            xAxisLabel = "Pressure (hPa)";
            xAxis.setFormatter(new SimpleAxisValueFormatter(1));
        }
        else if(xType.equalsIgnoreCase("date")){
            if(oneDay){
                xAxisLabel = "Date ";
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
                dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                xAxisLabel = xAxisLabel.concat(dateFormatter.format(new Date(
                        Long.valueOf((GraphActivity.endAsEpoch + GraphActivity.startAsEpoch)/2))));
                xAxisLabel = xAxisLabel.concat(" (HH:mm:ss)");
            }
            else{
                xAxisLabel = "Date (MM-DD-YY)";
            }
        }
        else if(xType.equalsIgnoreCase("humidity")){
            xAxisLabel = "Humidity (%)";
        }
        else{// count
            xAxisLabel = "Cosmic Ray Counts";
        }
        xAxis.setName(xAxisLabel);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setMaxLabelChars(10);
        xAxis.setInside(false);

        //Y AXIS SETUP
        if(yType.equalsIgnoreCase("temperature")){
            yAxisLabel = "Temperature (C)";
        }
        else if(yType.equalsIgnoreCase("pressure")){
            yAxisLabel = "Pressure (hPa)";
            yAxis.setFormatter(new SimpleAxisValueFormatter(1));
        }
        else if(yType.equalsIgnoreCase("date")){
            if(oneDay){
                yAxisLabel = "Date ";
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
                dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                yAxisLabel = yAxisLabel.concat(dateFormatter.format(new Date(
                        Long.valueOf((GraphActivity.endAsEpoch + GraphActivity.startAsEpoch)/2))));
                yAxisLabel = yAxisLabel.concat(" (HH:mm:ss)");
            }
            else{
                yAxisLabel = "Date (MM-DD-YY)";
            }
        }
        else if(yType.equalsIgnoreCase("humidity")){
            yAxisLabel = "Humidity (%)";
        }
        else{// count
            yAxisLabel = "Cosmic Ray Counts";
        }
        yAxis.setName(yAxisLabel);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setMaxLabelChars(10);
        yAxis.setInside(false);

        //Setup Chart Data
        data.setAxisXBottom(xAxis);
        data.setAxisYLeft(yAxis);
        data.setValueLabelsTextColor(Color.BLACK);
        data.setBaseValue(Float.NEGATIVE_INFINITY);

        if(yType.equalsIgnoreCase("temperature")){
            data.setBaseValue(0);
        }
        else if(yType.equalsIgnoreCase("pressure")){
            data.setBaseValue(0);
        }
        else if(yType.equalsIgnoreCase("date")){
            data.setBaseValue(Float.NEGATIVE_INFINITY);
        }
        else if(yType.equalsIgnoreCase("humidity")){
            data.setBaseValue(0);
        }
        else{// count
            data.setBaseValue(Float.NEGATIVE_INFINITY);
        }


        //Get ChartView
        chart = (LineChartView) findViewById(R.id.chart);
        chart.setLineChartData(data);

        Viewport v = new Viewport(chart.getMaximumViewport());

        //Setup X Viewpoints


        //Setup Y Viewpoints

        v.top += 5;
        v.bottom -= 5;

        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
        chart.setViewportCalculationEnabled(true);
        chart.setValueSelectionEnabled(true);
        chart.setValueTouchEnabled(true);
        ValueTouchListener touch = new ValueTouchListener();
        touch.xType = xType;
        touch.yType = yType;
        Log.i(TAG,"ValueTouchListener X-TYPE: " + touch.xType);
        Log.i(TAG,"ValueTouchListener Y-TYPE: " + touch.yType);
        chart.setOnValueTouchListener(touch);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hello_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_togglePoints) {
            hasPoints = chart.getLineChartData().getLines().get(0).hasPoints();
            hasPoints = !hasPoints;
            Log.i(TAG, "Toggle Points - Value: " + hasPoints);
            chart.getLineChartData().getLines().get(0).setHasPoints(hasPoints);
            chart.invalidate();

            return true;
        }
        if (id == R.id.action_toggleLine) {
            hasLines = chart.getLineChartData().getLines().get(0).hasLines();
            hasLines = !hasLines;
            Log.i(TAG, "Toggle Lines - Value: " + hasLines);
            chart.getLineChartData().getLines().get(0).setHasLines(hasLines);
            chart.invalidate();
            return true;
        }
        if (id == R.id.action_toggleCubic) {
            isCubic = chart.getLineChartData().getLines().get(0).isCubic();
            isCubic = !isCubic;
            Log.i(TAG, "Toggle Cubic - Value: " + isCubic);
            chart.getLineChartData().getLines().get(0).setCubic(isCubic);
            chart.invalidate();

            return true;
        }
        if (id == R.id.action_toggleArea) {
            isFilled = chart.getLineChartData().getLines().get(0).isFilled();
            isFilled = !isFilled;
            Log.i(TAG, "Toggle Area - Value: " + isFilled);
            chart.getLineChartData().getLines().get(0).setFilled(isFilled);
            chart.invalidate();
            return true;
        }
        if (id == R.id.action_zoomX) {
            chart.setZoomType(ZoomType.HORIZONTAL);
            Log.i(TAG, "Zoom/Scaling along X-Axis");
            return true;
        }
        if (id == R.id.action_zoomY) {
            chart.setZoomType(ZoomType.VERTICAL);
            Log.i(TAG, "Zoom/Scaling along Y-Axis");
            return true;
        }
        if (id == R.id.action_zoomXY) {
            chart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
            Log.i(TAG, "Zoom/Scaling along X-Axis and Y-Axis");
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {
        String xType,yType;


        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            String message = "Y-Axis: \t";
            if(yType.equalsIgnoreCase("temperature")){
                message = message.concat("Temperature = " + value.getY() + " C");
            }
            else if(yType.equalsIgnoreCase("pressure")){
                message = message.concat("Pressure = " + value.getY() + " hPa");
            }
            else if(yType.equalsIgnoreCase("date")){
                message = message.concat("Time = ");
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss z",Locale.US);
                dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));

                message = message.concat(
                        dateFormatter.format(new Date(Float.valueOf(value.getY()).longValue())));

            }
            else if(yType.equalsIgnoreCase("humidity")){
                message = message.concat("Humidity = " + value.getY() + " %");
            }
            else{// count
                message = message.concat("Count = " + value.getY());
            }

            message = message.concat("\nX-Axis: \t");

            if(xType.equalsIgnoreCase("temperature")){
                message = message.concat("Temperature = " + value.getX() + " C");
            }
            else if(xType.equalsIgnoreCase("pressure")){
                message = message.concat("Pressure = " + value.getX() + " hPa");
            }
            else if(xType.equalsIgnoreCase("date")){
                message = message.concat("Time = ");
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss z",Locale.US);
                dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));

                message = message.concat(
                        dateFormatter.format(new Date(Float.valueOf(value.getX()).longValue())));
            }
            else if(xType.equalsIgnoreCase("humidity")){
                message = message.concat("Humidity = " + value.getX() + " %");
            }
            else{// count
                message = message.concat("Count = " + value.getX());
            }


            Toast.makeText(HelloChartActivity.this, message, Toast.LENGTH_LONG).show();

        }

        @Override
        public void onValueDeselected() {


        }

    }


}
