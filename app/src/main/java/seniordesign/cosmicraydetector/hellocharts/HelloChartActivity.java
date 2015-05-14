package seniordesign.cosmicraydetector.hellocharts;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
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
        line.setShape(ValueShape.CIRCLE);
        line.setHasLabelsOnlyForSelected(true);
        line.setHasPoints(true);
        line.setHasLines(true);

        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis xAxis = new Axis().setHasLines(true);
        Axis yAxis = new Axis().setHasLines(true);

        //X AXIS SETUP
        String xAxisLabel, yAxisLabel;
        if(xType.equalsIgnoreCase("temperature")){
            xAxisLabel = "Temperature (C)";
        }
        else if(xType.equalsIgnoreCase("pressure")){
            xAxisLabel = "Pressure (C)";
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
        xAxis.setHasTiltedLabels(true);


        //Y AXIS SETUP
        if(yType.equalsIgnoreCase("temperature")){
            yAxisLabel = "Temperature (C)";
        }
        else if(yType.equalsIgnoreCase("pressure")){
            yAxisLabel = "Pressure (C)";
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

        //Setup Chart Data
        data.setAxisXBottom(xAxis);
        data.setAxisYLeft(yAxis);
        data.setBaseValue(Float.NEGATIVE_INFINITY);

        //Get ChartView
        LineChartView chart = (LineChartView) findViewById(R.id.chart);
        chart.setLineChartData(data);
        //Default zoom will be scrolling left to right only
        chart.setZoomType(ZoomType.HORIZONTAL);

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
