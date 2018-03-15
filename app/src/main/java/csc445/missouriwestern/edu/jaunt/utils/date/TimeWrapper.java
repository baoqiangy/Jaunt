package csc445.missouriwestern.edu.jaunt.utils.date;

import org.joda.time.LocalTime;

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
}
