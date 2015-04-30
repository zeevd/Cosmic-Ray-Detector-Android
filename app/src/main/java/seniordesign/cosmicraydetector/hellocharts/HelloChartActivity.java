package seniordesign.cosmicraydetector.hellocharts;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_chart);


        // Get X and Y types to know what values to graph
        String xType = GraphActivity.xType;
        String yType = GraphActivity.yType;






        List<PointValue> values = new ArrayList<PointValue>();
        float xValue, yValue;
        SensorData sensorData;

        /*
        Set<Long> keySet = MainActivity.sensorDataMap.keySet();
        for (Long key : keySet){
            sensorData = MainActivity.sensorDataMap.get(key);
            values.add(new PointValue(sensorData.getPressure().floatValue(),
                    sensorData.getCount().floatValue()));
        }

        */


        Set<Long> keySet = MainActivity.sensorDataMap.keySet();
        for (Long key : keySet){
            sensorData = MainActivity.sensorDataMap.get(key);
            values.add(new PointValue(new Long(sensorData.getDate().getTime()).floatValue(),
                    sensorData.getCount().floatValue()));
        }



        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
        List<Line> lines = new ArrayList<Line>(1);

        line.setShape(ValueShape.CIRCLE);
        line.setCubic(false);
        line.setHasLabelsOnlyForSelected(true);
        line.setHasPoints(true);
        line.setHasLines(true);


        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis xAxis = new Axis().setHasLines(true);
        Axis yAxis = new Axis().setHasLines(true);

        xAxis.setName("X Axis");
        xAxis.setTextColor(Color.BLACK);
        xAxis.setHasTiltedLabels(true);

        yAxis.setName("Y Axis");
        yAxis.setTextColor(Color.BLACK);


        data.setAxisXBottom(xAxis);
        data.setAxisYLeft(yAxis);

        data.setBaseValue(Float.NEGATIVE_INFINITY);

        LineChartView chart = (LineChartView) findViewById(R.id.chart);

        chart.setLineChartData(data);
        chart.setZoomType(ZoomType.HORIZONTAL);






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
