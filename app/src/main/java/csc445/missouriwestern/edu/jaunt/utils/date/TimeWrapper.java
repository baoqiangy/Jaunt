package csc445.missouriwestern.edu.jaunt.utils.date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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

    public static DateTime utcToJodaTime(TimeZone timeZone, long unixSeconds){
        DateTimeZone jodaTimeZone = DateTimeZone.forTimeZone(timeZone);
        DateTime jodaDateTime = new DateTime(unixSeconds, jodaTimeZone);
        return jodaDateTime;
    }
}
