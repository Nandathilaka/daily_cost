package com.nandeproduction.dailycost;

public class Cost {
    private int id;
    private String title;
    private long amount;
    private String date;

    public Cost(){

    }
    public Cost(int incomeId, String incomeTitle, long incomeAmount, String incomeDate){
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
    public int getAmount(){
        return (int) amount;
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
    public void setAmount(long incomeAmount){
        this.amount = incomeAmount;
    }
    public void setDate(String incomeDate){
        this.date = incomeDate;
    }
}
