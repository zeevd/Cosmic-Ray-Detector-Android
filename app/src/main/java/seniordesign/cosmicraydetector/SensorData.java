package seniordesign.cosmicraydetector;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SensorData {
    ///CONSTANTS///
    static final int DATA_TYPE_COUNT = 5;

    static final int DATE_INDEX = 0;
    static final int COUNT_INDEX = 1;
    static final int TEMPERATURE_INDEX = 2;
    static final int HUMIDITY_INDEX = 3;
    static final int PRESSURE_INDEX = 4;

    ///DATA MEMBERS///
    private Date date;
    private Number count;
    private Number temperature;
    private Number humidity;
    private Number pressure;
    //TODO: LOGGING

    ///CONSTRUCTOR///
    public SensorData(Date date, Number count, Number temperature, Number humidity, Number pressure){
        this.date = date;
        this.count = count;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    ///GETTERS/SETTERS///
    public Number getPressure() {
        return pressure;
    }
    public Number getHumidity() {
        return humidity;
    }
    public Number getTemperature() {
        return temperature;
    }
    public Number getCount() {
        return count;
    }
    public Date getDate() {
        return date;
    }

    ///METHODS///
    //Takes String fileContent (content of entire text file) and returns a List of SensorData objects
    public static List<SensorData> parseData(String fileContent){
        List<SensorData> dataFromFile = new ArrayList<SensorData>();    //To be returned

        DateFormat dateFormat = new SimpleDateFormat("MM_dd_yy HH:mm:ss");

        String[] rows = fileContent.split("\n");
        for (String row : rows){
            Date currentDate; Number currentCount; Number currentTemperature; Number currentHumidity; Number currentPressure;

            String[] rowContent = row.split(",\\s*");
            if (rowContent.length != DATA_TYPE_COUNT) continue; //Skip invalid row

            //TODO: test and document
            try {
                currentDate = dateFormat.parse(rowContent[DATE_INDEX].trim());
                currentCount = Integer.parseInt(rowContent[COUNT_INDEX]);
                currentTemperature = Integer.parseInt(rowContent[TEMPERATURE_INDEX]);
                currentHumidity = Integer.parseInt(rowContent[HUMIDITY_INDEX]);
                currentPressure = Double.parseDouble(rowContent[PRESSURE_INDEX]);

            } catch (ParseException e) {
                e.printStackTrace();
                continue;   //We do not want to add a row without a time
            }

            SensorData currentRow = new SensorData(currentDate,currentCount,currentTemperature,currentHumidity,currentPressure);
            dataFromFile.add(currentRow);
        }
        return dataFromFile;
    }
}