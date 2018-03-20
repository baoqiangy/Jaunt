package csc445.missouriwestern.edu.jaunt.utils.date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by byan on 3/13/2018.
 */

public class TimeWrapper {
    public static String jodaTimeToStr(LocalTime time){
        String str = time==null?"":time.toString("K:mm a");
        if(str.startsWith("0:")){
            str = str.replace("0:", "12:");
        }
        return str;
    }

    public static String jodaTimeToDateStr(DateTime jodatime){
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MM/dd/yyyy"); //"MM/dd/yyyy HH:mm:ss"
        String str = jodatime==null?"":dtfOut.print(jodatime);
        return str;
    }

    public static String jodaTimeToFullDisplayStr(DateTime jodatime){
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MM/dd/yyyy h:mm a"); //"MM/dd/yyyy HH:mm:ss"
        String str = jodatime==null?"":dtfOut.print(jodatime);
        return str;
    }

    public static DateTime utcToJodaTime(TimeZone timeZone, long unixSeconds){
        DateTimeZone jodaTimeZone = DateTimeZone.forTimeZone(timeZone);
        DateTime jodaDateTime = new DateTime(unixSeconds*1000L, jodaTimeZone);
        return jodaDateTime;
    }

    public static String utcToString(long unixSeconds, String format){
        // convert seconds to milliseconds
        Date date = new java.util.Date(unixSeconds*1000L);
        // the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
        // give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getDefault());
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public static DateTime sqlTimeToJodaTime(String sqlTime){
        return new DateTime();
    }

}
