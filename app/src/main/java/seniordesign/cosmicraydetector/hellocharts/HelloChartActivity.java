package seniordesign.cosmicraydetector.hellocharts;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import seniordesign.cosmicraydetector.GraphActivity;
import seniordesign.cosmicraydetector.MainActivity;
import seniordesign.cosmicraydetector.R;
import seniordesign.cosmicraydetector.SensorData;

public class HelloChartActivity extends ActionBarActivity {

    ///LOGGING TAG///
    private static final String TAG = "HelloChartActivity";


    private LineChartView chart;
    private LineChartData data;

    private boolean hasLines = true;
    private boolean hasPoints = true;
    private boolean isFilled = false;
    private boolean isCubic = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_chart);


        // Get X and Y types to know what values to graph
        String xType = GraphActivity.xType;
        String yType = GraphActivity.yType;


        //List of vales to be graphed
        List<PointValue> values = new ArrayList<PointValue>();

        //Graph Generation
        float xValue, yValue;
        /*
        * TODO: Add retrieval of specified range of SensorData
        */
        //List of Axis Labels
        List<AxisValue> xAxisValue = new ArrayList<>();
        List<AxisValue> yAxisValue = new ArrayList<>();


        SensorData sensorData;
        Set<Long> keySet = MainActivity.sensorDataMap.keySet();

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
                xValue = new Long(sensorData.getDate().getTime()).floatValue();
                AxisValue xLabel = new AxisValue(xValue);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
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
                yValue = new Long(sensorData.getDate().getTime()).floatValue();
                AxisValue yLabel = new AxisValue(yValue);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
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

            //TODO: Add Label Generating for dates, and may append units


            //Create point and add to list
            PointValue point = new PointValue(xValue,yValue);
            values.add(point);
        }// end of for loop

        Line line = new Line(values).setColor(Color.BLUE).setCubic(false);
        List<Line> lines = new ArrayList<Line>(1);

        //Initial Line Setup
        line.setPointRadius(3);
        line.setStrokeWidth(2);
        line.setShape(ValueShape.CIRCLE);
        line.setHasLabelsOnlyForSelected(true);
        line.setHasPoints(true);
        line.setHasLines(true);
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
            xAxisLabel = "Pressure (C)";
            xAxis.setFormatter(new SimpleAxisValueFormatter(1));
        }
        else if(xType.equalsIgnoreCase("date")){
            xAxisLabel = "Date (MM-DD-YY)";
        }
        else if(xType.equalsIgnoreCase("humidity")){
            xAxisLabel = "Humidity (%)";
        }
        else{// count
            xAxisLabel = "Cosmic Ray Counts";
        }
        xAxis.setName(xAxisLabel);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setHasTiltedLabels(false);
        xAxis.setMaxLabelChars(10);
        xAxis.setInside(true);

        //Y AXIS SETUP
        if(yType.equalsIgnoreCase("temperature")){
            yAxisLabel = "Temperature (C)";
        }
        else if(yType.equalsIgnoreCase("pressure")){
            yAxisLabel = "Pressure (C)";
            yAxis.setFormatter(new SimpleAxisValueFormatter(1));
        }
        else if(yType.equalsIgnoreCase("date")){
            yAxisLabel = "Date (MM-DD-YY)";
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
        yAxis.setInside(true);

        //Setup Chart Data
        data.setAxisXBottom(xAxis);
        data.setAxisYLeft(yAxis);
        data.setValueLabelsTextColor(Color.RED);
        data.setValueLabelTextSize(2);
        data.setBaseValue(Float.NEGATIVE_INFINITY);

        if(yType.equalsIgnoreCase("temperature")){
            data.setBaseValue(0);
        }
        else if(yType.equalsIgnoreCase("pressure")){
            data.setBaseValue(0);;
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
        if(xType.equalsIgnoreCase("Humidity")){
            v.left = 0;
            v.right = 0;
        }
        else{
            v.top += 3;
            v.bottom -= 3;
        }
        //Setup Y Viewpoints
        if(yType.equalsIgnoreCase("Humidity")){
            v.top = 100;
            v.bottom = 0;
        }

        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
        chart.setViewportCalculationEnabled(true);


        //Default zoom will be scrolling left to right only
        //chart.setZoomType(ZoomType.HORIZONTAL);

        //chart.startDataAnimation();

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
            chart.getLineChartData().getLines().get(0).setHasPoints(hasPoints);
            chart.invalidate();

            return true;
        }
        if (id == R.id.action_toggleLine) {
            hasLines = chart.getLineChartData().getLines().get(0).hasLines();
            hasLines = !hasLines;
            chart.getLineChartData().getLines().get(0).setHasLines(hasLines);
            chart.invalidate();;
            return true;
        }
        if (id == R.id.action_toggleCubic) {
            isCubic = chart.getLineChartData().getLines().get(0).isCubic();
            isCubic = !isCubic;
            chart.getLineChartData().getLines().get(0).setCubic(isCubic);
            chart.invalidate();

            return true;
        }
        if (id == R.id.action_toggleArea) {
            isFilled = chart.getLineChartData().getLines().get(0).isFilled();
            isFilled = !isFilled;
            chart.getLineChartData().getLines().get(0).setFilled(isFilled);
            chart.invalidate();
            return true;
        }
        if (id == R.id.action_zoomX) {
            chart.setZoomType(ZoomType.HORIZONTAL);
            return true;
        }
        if (id == R.id.action_zoomY) {
            chart.setZoomType(ZoomType.VERTICAL);
            return true;
        }
        if (id == R.id.action_zoomXY) {
            chart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
