package com.dailyexpense.tracker;

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
        try {
            correctDate = sdf1.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static String nextPaymentDate(String loanOpenDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nextPaymentDate = "";
        Date selectedDate = null;
        try {
            selectedDate = sdf.parse(loanOpenDate);
            Calendar calendar = Calendar.getInstance();
            //Current month details
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONDAY);
            int currentDate = calendar.get(Calendar.DATE);

            //Loan date details
            calendar.setTime(selectedDate);
            int loanYear = calendar.get(Calendar.YEAR);
            int loanMonth = calendar.get(Calendar.MONDAY);
            int loanDate = calendar.get(Calendar.DATE);

            if(currentDate > loanDate){
                //If month is December
                if(currentMonth == 11){
                    nextPaymentDate = String.valueOf(currentYear+1) +"-"+ String.valueOf(1)+"-"+ String.valueOf(loanDate);
                }else{
                    nextPaymentDate = String.valueOf(currentYear) +"-"+ String.valueOf(currentMonth+2)+"-"+ String.valueOf(loanDate);
                }

            }else{
                /*
                //If month is December
                if(currentMonth == 11){
                    nextPaymentDate = String.valueOf(currentYear) +"-"+ String.valueOf(currentMonth+1)+"-"+ String.valueOf(loanDate);
                }else{
                    nextPaymentDate = String.valueOf(currentYear) +"-"+ String.valueOf(currentMonth+1)+"-"+ String.valueOf(loanDate);
                }
                */
                nextPaymentDate = String.valueOf(currentYear) +"-"+ String.valueOf(currentMonth+1)+"-"+ String.valueOf(loanDate);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nextPaymentDate;
    }

    public static int getOnlyDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date selectedDate = null;
        int loanDate = 0;
        try {
            selectedDate = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            //Loan date details
            calendar.setTime(selectedDate);
            loanDate = calendar.get(Calendar.DATE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return loanDate;
    }
}
