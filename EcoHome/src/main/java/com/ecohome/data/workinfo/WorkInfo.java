package com.ecohome.data.workinfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WorkInfo {

    private final String timeTurnedOn;
    private final String timeTurnedOff;

    private SimpleDateFormat sdf = null;
    private final Calendar calendar = Calendar.getInstance();

    public WorkInfo(String timeTurnedOn, String timeTurnedOff) {
        this.timeTurnedOn = timeTurnedOn;
        this.timeTurnedOff = timeTurnedOff;
    }

    public String getTimeTurnedOn(){
        return timeTurnedOn.substring(11,16);
    }

    public String getTimeTurnedOff(){
        if(timeTurnedOff == null) return null;

        return timeTurnedOff.substring(11,16);

    }

    public int getHourTurnedOn(){
        return Integer.parseInt(timeTurnedOn.substring(11,13));
    }

    public int getHourTurnedOff(){
        if(timeTurnedOff == null) return -1;
        return Integer.parseInt(timeTurnedOff.substring(11,13));
    }

    public int getDayOfWeekTurnedOn(){
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            calendar.setTime(sdf.parse(timeTurnedOn));
            return calendar.get(Calendar.DAY_OF_WEEK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public double getConsumedEnergy(double wattHour){
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        double timeTurnedOn = 0;
        double timeTurnedOff = 0;

        try {
            timeTurnedOn = sdf.parse(this.timeTurnedOn).getTime();
            if(this.timeTurnedOff != null)
                timeTurnedOff = sdf.parse(this.timeTurnedOff).getTime();
            else
                timeTurnedOff = System.currentTimeMillis();
        }catch (Exception e){
            e.printStackTrace();
        }

        return (timeTurnedOff - timeTurnedOn) * wattHour / 3600000;
    }

    public double getConsumedEnergy(String maxDate,double wattHour){
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        double timeTurnedOn = 0;
        double timeTurnedOff = 0;

        try {
            timeTurnedOn = sdf.parse(this.timeTurnedOn).getTime();
            if(this.timeTurnedOff != null)
                timeTurnedOff = sdf.parse(this.timeTurnedOff).getTime();
            else
                timeTurnedOff = sdf.parse(maxDate).getTime();
        }catch (Exception e){
            e.printStackTrace();
        }

        return (timeTurnedOff - timeTurnedOn) * wattHour / 3600000;
    }

}


