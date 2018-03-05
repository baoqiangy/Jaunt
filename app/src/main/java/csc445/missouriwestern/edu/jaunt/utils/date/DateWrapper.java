package csc445.missouriwestern.edu.jaunt.utils.date;

import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import csc445.missouriwestern.edu.jaunt.Jaunt;

/**
 * Created by byan on 3/4/2018.
 */

public class DateWrapper {
    public static Date stringToDate(String str){
        if(str == null || str.equalsIgnoreCase("null")) {return null;}
        try{
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date date = formatter.parse(str);
            return date;
        }catch (ParseException e){
            Toast.makeText(Jaunt.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public static String dateToString(Date date){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String dateString = df.format(date);
        return dateString;
    }

    public static String dateToSqlString(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String sqlDateString = df.format(date);
        return sqlDateString;
    }

    public static Date sqlStringToDate(String str){
        if(str == null || str.equalsIgnoreCase("null")) {return null;}
        try{
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(str);
            return date;
        }catch (ParseException e){
            Toast.makeText(Jaunt.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
