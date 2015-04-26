package seniordesign.cosmicraydetector;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Day {
    ///CONSTANTS///
    static final int DATA_TYPE_COUNT = 6;

    static final int DATE_INDEX = 0;
    static final int TIME_INDEX = 1;
    static final int COUNT_INDEX = 2;
    static final int TEMPERATURE_INDEX = 3;
    static final int HUMIDITY_INDEX = 4;
    static final int PRESSURE_INDEX = 5;

    ///DATA MEMBERS///
    private String date;
    private ArrayList<Number> epochTime = new ArrayList<Number>();
    private ArrayList<Number> count = new ArrayList<Number>();
    private ArrayList<Number> temperature = new ArrayList<Number>();
    private ArrayList<Number> humidity = new ArrayList<Number>();
    private ArrayList<Number> pressure = new ArrayList<Number>();

    //TODO: LOGGING

    ///CONSTRUCTOR///
    public Day(String fileContent){
        parseData(fileContent);
    }

    ///GETTERS/SETTERS///
    public String getDate() {
        return date;
    }

    ///METHODS///
    private void parseData(String fileContent){
        //Temporary variables used to convert time from String to long (epoch time)
        String currentTime; DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss"); Date formattedDate;

        String[] rows = fileContent.split("\n");
        for (String row : rows){
            String[] rowContent = row.split(",\\s*");
            if (rowContent.length != DATA_TYPE_COUNT) continue; //Skip invalid row

            //TODO: test and document
            //Convert time to epoch time and store in ArrayList
            try {
                currentTime = rowContent[TIME_INDEX].trim();
                formattedDate = dateFormat.parse(currentTime);
                epochTime.add(formattedDate.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                continue;   //We do not want to add a row without a time
            }


            date = rowContent[DATE_INDEX];
            count.add(Integer.parseInt(rowContent[COUNT_INDEX]));
            temperature.add(Integer.parseInt(rowContent[TEMPERATURE_INDEX]));
            humidity.add(Integer.parseInt(rowContent[HUMIDITY_INDEX]));
            pressure.add(Double.parseDouble(rowContent[PRESSURE_INDEX]));

        }
    }
}
