package com.dailyexpense.tracker;

public class LoanPeriod {
    private int id;
    private int loan_installment_id;
    private String loan_account_number;
    private double loan_installment;
    private int user_id;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLoan_installment_id() {
        return loan_installment_id;
    }

    public void setLoan_installment_id(int loan_installment_id) {
        this.loan_installment_id = loan_installment_id;
    }

    public String getLoan_account_number() {
        return loan_account_number;
    }

    public void setLoan_account_number(String loan_account_number) {
        this.loan_account_number = loan_account_number;
    }

    public String getLoan_installment() {
        return String.format("%.2f",loan_installment);
    }

    public void setLoan_installment(double loan_installment) {
        this.loan_installment = loan_installment;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
