package com.nandeproduction.dailycost;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateConverter {
    // Taking date as "2021-05-31" and return string date value as "2021-05-31 00:00:00"
    public static String DateConvertToString(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date selectedDate = null;
        String correctDate = "";
        try {
            selectedDate = sdf.parse(date);
            correctDate = sdf1.format(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
       return correctDate;
    }

    // Taking date as "2021-05-31" and return string date value as "2021-05-31 00:00:00"
    public static String DateConvert(Date date){
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String correctDate = "";
        correctDate = sdf1.format(date);
        return correctDate;
    }

    public static String setCalanderDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String correctDate = "";
        Date selectedDate = null;
        try {
            selectedDate = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONDAY);
            int today = calendar.get(Calendar.DATE);
            correctDate = String.valueOf(year) +"-"+ String.valueOf(month+1)+"-"+ String.valueOf(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return correctDate;
    }
}
