package com.nandeproduction.dailycost;

public class IncomeChartModel {
    private int id;
    private float ammount;
    private String date;

    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return this.id;
    }
    public String getDate(){
        return this.date;
    }
    public void setDate(String date){
        this.date = date;
    }
    public float getAmmount(){
        return this.ammount;
    }
    public void setAmmount(float ammount){
        this.ammount += ammount;
    }
}
