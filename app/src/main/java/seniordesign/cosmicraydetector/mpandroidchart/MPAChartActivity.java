package seniordesign.cosmicraydetector.mpandroidchart;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;


import java.util.ArrayList;

import seniordesign.cosmicraydetector.R;

public class MPAChartActivity extends ActionBarActivity {
    ///LOGGING TAG///
    private static final String TAG = "MPAChartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpachart);

        LineChart chart = (LineChart) findViewById(R.id.chart);

        //Setup Y-Axis
        /**
         * Each Data "Entry" is floating point value, with an intent on the X access.
         * Entry(FLOATING POINT NUMBER, POSITION ON THE X-AXIS) -> (Y,X-offset)
         */

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(12f, 2));
        entries.add(new Entry(16f, 3));
        entries.add(new Entry(6f, 4));
        entries.add(new Entry(12f, 5));
        entries.add(new Entry(18f, 6));
        entries.add(new Entry(9f, 7));

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("0");
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");
        labels.add("6");
        labels.add("7");

        LineDataSet dataset = new LineDataSet(entries, "# of Calls");
        LineData data = new LineData(labels, dataset);

        chart.setData(data);
        chart.setDescription("Counts Vs Pressure");
        chart.setDescriptionTextSize(20);

        //Setup Axis

        //XAXIS
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawAxisLine(true);


        //YAXIS-LEFT



        //YAXIS-RIGHT



        //Setup Legend
        chart.getLegend().setEnabled(false);

        chart.animateXY(2000, 2000);





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mpachart, menu);
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
