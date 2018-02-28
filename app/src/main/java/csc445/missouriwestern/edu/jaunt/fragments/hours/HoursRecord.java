package csc445.missouriwestern.edu.jaunt.fragments.hours;

import java.util.List;

/**
 * Created by byan on 2/17/2018.
 */

public class HoursRecord {
    private int day;
    private List<TimeRange> availability;

    public HoursRecord(int day, List<TimeRange> availability) {
        this.day = day;
        this.availability = availability;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<TimeRange> getAvailability() {
        return availability;
    }

    public void setAvailability(List<TimeRange> availability) {
        this.availability = availability;
    }

    public String getDayStr(){
        String result = "";
        switch (day) {
            case 1:
                result = "Monday";
                break;
            case 2:
                result = "Tuesday";
                break;
            case 3:
                result = "Wednesday";
                break;
            case 4:
                result = "Thursday";
                break;
            case 5:
                result = "Friday";
                break;
            case 6:
                result = "Saturday";
                break;
            case 0:
                result = "Sunday";
                break;
        }
        return result;
    }

    public String getRangesStr(){
        String result = "";
        for(TimeRange r: availability) {
            result += r + "  ";
        }
        return result;
    }
}

