package seniordesign.cosmicraydetector;

import android.util.Log;

import java.util.ArrayList;

public class Day {
    ///CONSTANTS///
    static final int DATA_TYPE_COUNT = 6;
    static final int DATE = 0;
    static final int TIME = 1;
    static final int COUNT = 2;
    static final int TEMPERATURE = 3;
    static final int HUMIDITY = 4;
    static final int PRESSURE = 5;

    ///DATA MEMBERS///
    private String date;
    //todo TIME?
    private ArrayList<Number> count;
    private ArrayList<Number> temperature;
    private ArrayList<Number> humidity;
    private ArrayList<Number> pressure;

    ///CONSTRUCTOR///
    public Day(String fileContent){
        count = new ArrayList<Number>();
        temperature = new ArrayList<Number>();
        humidity = new ArrayList<Number>();
        pressure = new ArrayList<Number>();

        parseData(fileContent);

    }

    ///GETTERS/SETTERS///
    public String getDate() {
        return date;
    }

    ///METHODS///
    private void parseData(String fileContent){
        String[] rows = fileContent.split("\n");
        for (String row : rows){
            String[] rowContent = row.split(",\\s*");
            if (rowContent.length != DATA_TYPE_COUNT) continue; //Skip invalid row

            date = rowContent[DATE];
            //TODO: what about timestamp??
            count.add(Integer.parseInt(rowContent[COUNT]));
            temperature.add(Integer.parseInt(rowContent[TEMPERATURE]));
            humidity.add(Integer.parseInt(rowContent[HUMIDITY]));
            pressure.add(Double.parseDouble(rowContent[PRESSURE]));

        }
    }
}
