package com.nandeproduction.dailycost;

import java.util.List;

public class Loan {
    private int id;
    private String accountNumber; // bla bla bla..
    private String accountName; // commercial bank
    private long loanAmount; // 1,500,000
    private double monthlyPayment; // 31,000
    private double interestRate; // 9.75
    private String openDate;
    private String nextPaymentDate;
    private int numberOfMonth;
    private List<LoanPeriod> loanPeriod;
    private int numberOfPaidMonths;
    private double totalPaid; // (monthlyPayment * numberOfMonth)
    private double interestAmount;//  totalPaid - loanAmount

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
        if(accountNumber != null && accountNumber.length() > 0){
            this.accountNumber = accountNumber;
        }else{
            this.accountNumber = "No Account Number";
        }

    }
    public String getAccountName(){
        return accountName;
    }
    public void setAccountName(String accountName){
        if(accountName != null && accountName.length() > 0){
            this.accountName = accountName;
        }else{
            this.accountName = "No Account Name";
        }
    }
    public long getLoanAmount(){
        return loanAmount;
    }
    public void setLoanAmount(long loanAmount){
        if(loanAmount != 0 && loanAmount > 0){
            this.loanAmount = loanAmount;
        }else{
            this.loanAmount = 0;
        }
    }
    public double getMonthlyPayment(){
        return monthlyPayment;
    }
    public void setMonthlyPayment(String monthlyPayment){
        if(monthlyPayment != "" && monthlyPayment.length() > 0){
            this.monthlyPayment = Double.parseDouble(monthlyPayment);
        }else{
            this.monthlyPayment = 0.00;
        }
    }
    public double getInterestRate(){
        return interestRate;
    }
    public void setInterestRate(String interestRate){
        if (interestRate != "" && interestRate.length() > 0) {
            try {
                this.interestRate = Float.parseFloat(interestRate);;
            } catch(Exception e) {
                this.interestRate = (float) 0.00;;   // or some value to mark this field is wrong. or make a function validates field first ...
            }
        }
        else this.interestRate = (float) 0.00;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
        this.nextPaymentDate = DateConverter.nextPaymentDate(openDate);
    }

    public String getNextPaymentDate(){
        return nextPaymentDate;
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
        if(numberOfPaidMonths != 0 && numberOfPaidMonths > 0){
            this.numberOfPaidMonths = numberOfPaidMonths;
        }else {
            this.numberOfPaidMonths = 1;
        }
    }

    public double getTotalPaid() {
        return totalPaid = monthlyPayment * numberOfMonth;
    }

    public double getInterestAmount() {
        if(totalPaid > 0){
            return interestAmount = totalPaid - loanAmount;
        }else{
            return 0;
        }
    }

}

class LoanPeriod{
    private int premiumId; // Number of period (Premium)
    private float monthlyPayment; // monthly payment
    private Boolean paidOrNot=false; // paid or not. this is boolean. True mean paid, False mean not paid for the specific month
}
