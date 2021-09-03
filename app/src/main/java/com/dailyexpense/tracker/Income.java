package com.dailyexpense.tracker;

import java.text.DecimalFormat;

public class Income {
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private int id;
    private String title;
    private double amount;
    private String date;

    public Income(){

    }
    public Income(int incomeId, String incomeTitle, double incomeAmount, String incomeDate){
        id= incomeId;
        title=incomeTitle;
        amount=incomeAmount;
        date=incomeDate;
    }

    public int getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getAmount(){
        return String.format("%.2f",amount);
    }
    public String getDate(){
        return date;
    }
    public void setId(int incomeId){
        this.id = incomeId;
    }
    public void setTitle(String incomeTitle){
        this.title = incomeTitle;
    }
    public void setAmount(double  incomeAmount){
        this.amount = incomeAmount;
    }
    public void setDate(String incomeDate){
        this.date = incomeDate;
    }

}
