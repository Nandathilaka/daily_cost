package com.nandeproduction.dailycost;

import java.util.List;

public class Loan {
    private int id;
    private String accountNumber; // bla bla bla..
    private String accountName; // commercial bank
    private long loanAmount; // 1,500,000
    private float monthlyPayment; // 31,000
    private float interestRate; // 9.75
    private String openDate;
    private int numberOfMonth;
    private List<LoanPeriod> loanPeriod;
    private int numberOfPaidMonths;
    private float totalPaid; // (monthlyPayment * numberOfMonth)
    private float interestAmount;//  totalPaid - loanAmount

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getAccountNumber(){
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public String getAccountName(){
        return accountName;
    }
    public void setAccountName(String accountName){
        this.accountName = accountName;
    }
    public long getLoanAmount(){
        return loanAmount;
    }
    public void setLoanAmount(long loanAmount){
        this.loanAmount = loanAmount;
    }
    public float getMonthlyPayment(){
        return monthlyPayment;
    }
    public void setMonthlyPayment(float monthlyPayment){
        this.monthlyPayment = monthlyPayment;
    }
    public float getInterestRate(){
        return interestRate;
    }
    public void setInterestRate(float interestRate){
        this.interestRate = interestRate;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public int getNumberOfMonth() {
        return numberOfMonth;
    }

    public void setNumberOfMonth(int numberOfMonth) {
        this.numberOfMonth = numberOfMonth;
    }

    public List<LoanPeriod> getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(List<LoanPeriod> loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public int getNumberOfPaidMonths() {
        return numberOfPaidMonths;
    }

    public void setNumberOfPaidMonths(int numberOfPaidMonths) {
        this.numberOfPaidMonths = numberOfPaidMonths;
    }

    public float getTotalPaid() {
        return totalPaid = monthlyPayment * numberOfMonth;
    }

    public float getInterestAmount() {
        return interestAmount = totalPaid - loanAmount;
    }

}

class LoanPeriod{
    private int premiumId; // Number of period (Premium)
    private float monthlyPayment; // monthly payment
    private Boolean paidOrNot=false; // paid or not. this is boolean. True mean paid, False mean not paid for the specific month
}
