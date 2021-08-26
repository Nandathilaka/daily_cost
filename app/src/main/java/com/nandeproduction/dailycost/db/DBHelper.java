package com.nandeproduction.dailycost.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.nandeproduction.dailycost.Cost;
import com.nandeproduction.dailycost.DateConverter;
import com.nandeproduction.dailycost.Income;
import com.nandeproduction.dailycost.IncomeChartModel;
import com.nandeproduction.dailycost.IncomeListviewAdapter;
import com.nandeproduction.dailycost.Loan;
import com.nandeproduction.dailycost.LoanPeriod;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DBHelper extends SQLiteOpenHelper {

    //Database
    public static final String DATABASE_NAME = "DailyCost.db";

    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_EMAIL = "email";
    public static final String CONTACTS_COLUMN_STREET = "street";
    public static final String CONTACTS_COLUMN_CITY = "place";
    public static final String CONTACTS_COLUMN_PHONE = "phone";

    //Users Table
    public static final String USERS_TABLE_NAME = "users";
    public static final String USERS_COLUMN_ID = "user_id";
    public static final String USERS_COLUMN_FIRST_NAME = "first_name";
    public static final String USERS_COLUMN_LAST_NAME = "last_name";
    public static final String USERS_COLUMN_EMAIL = "email";
    public static final String USERS_COLUMN_STREET = "street";
    public static final String USERS_COLUMN_COUNTRY = "country";

    //Income Table
    public static final String INCOMES_TABLE_NAME = "incomes";
    public static final String INCOMES_COLUMN_ID = "id";
    public static final String INCOMES_COLUMN_USER_ID = "user_id";
    public static final String INCOMES_COLUMN_TITLE = "title";
    public static final String INCOMES_COLUMN_AMOUNT = "amount";
    public static final String INCOMES_COLUMN_DATE = "date";

    //Cost Table
    public static final String COSTS_TABLE_NAME = "costs";
    public static final String COSTS_COLUMN_ID = "id";
    public static final String COSTS_COLUMN_USER_ID = "user_id";
    public static final String COSTS_COLUMN_TITLE = "title";
    public static final String COSTS_COLUMN_AMOUNT = "amount";
    public static final String COSTS_COLUMN_DATE = "date";

    //Loan Table
    public static final String LOANS_TABLE_NAME = "loans";
    public static final String LOANS_COLUMN_ID = "id";
    public static final String LOANS_COLUMN_USER_ID = "user_id";
    public static final String LOANS_COLUMN_ACCOUNT_NUMBER = "account_number";
    public static final String LOANS_COLUMN_ACCOUNT_NAME = "account_name";
    public static final String LOANS_COLUMN_LOAN_AMOUNT = "loan_amount";
    public static final String LOANS_COLUMN_MONTHLY_PAYMENT = "monthly_payment";
    public static final String LOANS_COLUMN_RATE = "rate";
    public static final String LOANS_COLUMN_OPEN_DATE = "open_date";
    public static final String LOANS_COLUMN_MONTHS = "months";
    public static final String LOANS_COLUMN_NUMBER_OF_PAID_MONTHS = "number_of_paid_months";

    //LoanPayment Table
    public static final String LOAN_INSTALLMENT = "loan_installment";
    public static final String LOAN_INSTALLMENT_COLUMN_ID = "id"; //1,2,3,.... Primary Key
    public static final String LOAN_INSTALLMENT_COLUMN_LOAN_ID = "loan_account_number"; //80088000066, Foreign Key
    public static final String LOAN_INSTALLMENT_COLUMN_INSTALLMENT_ID = "loan_installment_id"; // 1,2,3,4,5,...,60 for the 5 years Foreign Key
    public static final String LOAN_INSTALLMENT_COLUMN_INSTALLMENT = "loan_installment"; // 23000 /-
    public static final String LOAN_INSTALLMENT_COLUMN_USER_ID = "user_id"; // Foreign Key
    public static final String LOAN_INSTALLMENT_COLUMN_STATUS = "status"; // YES or NO
    public static final String LOAN_INSTALLMENT_COLUMN_CREATE_DATE = "date_created";
    public static final String LOAN_INSTALLMENT_COLUMN_UPDATE_DATE = "date_updated";



    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table contacts " +
                        "(id integer primary key , name text,phone text,email text, street text,place text, date_created text, date_updated text, deleted integer default 0);"
        );

        // Create Users Table
        db.execSQL(
                "create table users " +
                        "(user_id integer primary key , first_name text, last_name text, email text UNIQUE, street text, country text, date_created text, date_updated text, deleted integer default 0);"
        );

        // Create Income Table
        db.execSQL(
                "create table incomes " +
                        "(id integer primary key , user_id interger ,title text, amount interger, date text, date_created text, date_updated text, deleted integer default 0, foreign key (user_id) references users (user_id));"
        );

        // Create Cost Table
        db.execSQL(
                "create table costs " +
                        "(id integer primary key , user_id interger ,title text, amount interger, date text, date_created text, date_updated text, deleted integer default 0, foreign key (user_id) references users (user_id));"
        );

        // Create Loan Table
        db.execSQL(
                "create table loans " +
                        "(id integer primary key , user_id interger , account_name text, account_number text UNIQUE, loan_amount interger, monthly_payment interger,rate text, open_date text,months interger, number_of_paid_months interger, date_created text, date_updated text, deleted integer default 0, foreign key (user_id) references users (user_id));"
        );

        //create Loan Installment Table
        db.execSQL(
                "create table loan_installment " +
                        "(id integer primary key, loan_account_number text,  loan_installment_id integer, loan_installment integer, user_id integer, status text default 'NOT PAID', date_created text, date_updated text, foreign key (loan_account_number) references loans (account_number), foreign key (user_id) references users (user_id));"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS incomes");
        db.execSQL("DROP TABLE IF EXISTS costs");
        db.execSQL("DROP TABLE IF EXISTS loans");
        onCreate(db);
    }

    public boolean insertContact (String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        contentValues.put("date_created", DateConverter.DateConvert(new Date()));
        contentValues.put("date_updated", DateConverter.DateConvert(new Date()));
        long result = db.insert("contacts", null, contentValues);
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Insert User
    public boolean insertUser (String fname, String lname, String email, String street,String country) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", fname);
        contentValues.put("last_name", lname);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("country", country);
        contentValues.put("date_created", DateConverter.DateConvert(new Date()));
        contentValues.put("date_updated", DateConverter.DateConvert(new Date()));
        long result = db.insert("users", null, contentValues);
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Insert daily income
    public boolean insertIncome (int user_id, String title, long amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", user_id);
        contentValues.put("title", title);
        contentValues.put("amount", amount);
        contentValues.put("date", date);
        contentValues.put("date_created", DateConverter.DateConvert(new Date()));
        contentValues.put("date_updated", DateConverter.DateConvert(new Date()));
        long result = db.insert("incomes", null, contentValues);
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Insert Daily Cost
    public boolean insertCost (int user_id, String title, long amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", user_id);
        contentValues.put("title", title);
        contentValues.put("amount", amount);
        contentValues.put("date", date);
        contentValues.put("date_created", DateConverter.DateConvert(new Date()));
        contentValues.put("date_updated", DateConverter.DateConvert(new Date()));
        long result = db.insert("costs", null, contentValues);
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Insert Personal Loan details
    public boolean insertLoan (int user_id, String account_name, String account_number, long loan_amount, double monthly_payment, String rate, String open_date, int months, int number_of_paid_months) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", user_id);
        contentValues.put("account_name", account_name);
        contentValues.put("account_number", account_number);
        contentValues.put("loan_amount", loan_amount);
        contentValues.put("monthly_payment", monthly_payment);
        contentValues.put("rate", rate);
        contentValues.put("open_date", open_date);
        contentValues.put("months",months);
        contentValues.put("number_of_paid_months",number_of_paid_months);
        contentValues.put("date_created", DateConverter.DateConvert(new Date()));
        contentValues.put("date_updated", DateConverter.DateConvert(new Date()));
        long result = db.insert("loans", null, contentValues);
        db.close();
        if(result == -1){
            return false;
        }else{
            boolean installment_all_success = true;
            for(int install=1 ; install<=months ; install++){
                installment_all_success = insertLoanInstallment(install, user_id,account_number,monthly_payment);
                if(!installment_all_success){
                    break;
                }
            }
            return installment_all_success;
        }
    }

    //Insert Personal Loan details
    public boolean insertLoanInstallment (int installment_id, int user_id, String account_number, double monthly_payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("loan_account_number", account_number);
        contentValues.put("loan_installment_id", installment_id);
        contentValues.put("loan_installment", monthly_payment);
        contentValues.put("user_id", user_id);
        contentValues.put("status", "NOT PAID");
        contentValues.put("date_created", DateConverter.DateConvert(new Date()));
        contentValues.put("date_updated", DateConverter.DateConvert(new Date()));
        long result = db.insert("loan_installment", null, contentValues);
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }


    //Get All Contact details
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        db.close();
        return res;
    }

    //Get number of rows in the Contact table
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        db.close();
        return numRows;
    }

    //Get number of rows in the Loan table
    public int numberOfActiveLoansRows(){
        int numOfLoansRows = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String getamountdata = "SELECT count(*) FROM "+ LOANS_TABLE_NAME + " WHERE deleted = 0";
        Cursor c = db.rawQuery(getamountdata, null);
        if(c.moveToFirst()){
            numOfLoansRows = (int) c.getLong(0);
        }
        db.close();
        return numOfLoansRows;
    }

    //Get number of rows in the Loan table
    public int numberOfLoansRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numOfLoansRows = (int) DatabaseUtils.queryNumEntries(db, LOANS_TABLE_NAME);
        db.close();
        return numOfLoansRows;
    }

    //Check user available in the User Table
    public int checkUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numOfUserRows = (int) DatabaseUtils.queryNumEntries(db, USERS_TABLE_NAME);
        db.close();
        return numOfUserRows;
    }

    //Get current user ID
    public int getCurrentUserID(){
        int x=0;
        SQLiteDatabase db = this.getReadableDatabase();
        String getamountdata = "SELECT user_id AS id FROM "+ USERS_TABLE_NAME + " ORDER BY ROWID ASC LIMIT 1";
        Cursor c = db.rawQuery(getamountdata, null);
        if(c.moveToFirst()){
            x = c.getInt(0);
        }
        db.close();
        return x;
    }

    //Update Contact Table data given by the ID
    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        contentValues.put("date_updated", DateConverter.DateConvert(new Date()));
        long result = db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Update user table data given by the User ID
    public boolean updateUser (Integer user_id, String fname, String lname, String email, String street,String country) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", fname);
        contentValues.put("last_name", lname);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("country", country);
        contentValues.put("date_updated", DateConverter.DateConvert(new Date()));
        long result = db.update("users", contentValues, "user_id = ? ", new String[] { Integer.toString(user_id) } );
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Update income table data given by the ID
    public boolean updateIncome (Integer id, String title, long amount, String date, Integer user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("amount", amount);
        contentValues.put("date", date);
        contentValues.put("date_updated", DateConverter.DateConvert(new Date()));
        long result = db.update("incomes", contentValues, "id = ? AND user_id = ? ", new String[] { Integer.toString(id), Integer.toString(user_id) } );
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Update Cost table data given by the ID
    public boolean updateCost (Integer id, String title, long amount, String date, Integer user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("amount", amount);
        contentValues.put("date", date);
        contentValues.put("date_updated", DateConverter.DateConvert(new Date()));
        long result = db.update("costs", contentValues, "id = ? AND user_id = ? ", new String[] { Integer.toString(id), Integer.toString(user_id) } );
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Update Cost table data given by the ID
    public boolean updateLoan (Integer id, String account_name, String account_number, long loan_amount, double monthly_payment, String rate, String open_date, int months, int number_of_paid_months, Integer user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_name", account_name);
        contentValues.put("account_number", account_number);
        contentValues.put("loan_amount", loan_amount);
        contentValues.put("monthly_payment", monthly_payment);
        contentValues.put("rate", rate);
        contentValues.put("open_date", open_date);
        contentValues.put("months",months);
        contentValues.put("number_of_paid_months",number_of_paid_months);
        contentValues.put("date_updated", DateConverter.DateConvert(new Date()));
        long result = db.update("loans", contentValues, "id = ? AND user_id = ? ", new String[] { Integer.toString(id), Integer.toString(user_id) } );
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Update loan installment table data given by the installment_id and loan account number
    public boolean updateLoanInstallment (Integer loan_installment_id, String installment, String account_number, Integer user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("loan_installment", installment);
        contentValues.put("date_updated", DateConverter.DateConvert(new Date()));
        long result = db.update("loan_installment", contentValues, "loan_installment_id = ? AND user_id = ? AND loan_account_number = ?", new String[] { Integer.toString(loan_installment_id), Integer.toString(user_id), account_number} );
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Update loan installment table data given by the installment_id and loan account number
    public boolean payInstallment (Integer loan_installment_id, String account_number, Integer user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", "PAID");
        contentValues.put("date_updated", DateConverter.DateConvert(new Date()));
        long result = db.update("loan_installment", contentValues, "loan_installment_id = ? AND user_id = ? AND loan_account_number = ?", new String[] { Integer.toString(loan_installment_id), Integer.toString(user_id), account_number} );
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Hide income table data given by the ID, To do that update the "deleted" column as 1 (1 mean deleted raw item/ 0 mean non-deleted raw item)
    public boolean hideIncome (Integer id, Integer user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("deleted", 1);
        contentValues.put("date_updated", DateConverter.DateConvert(new Date()));
        long result = db.update("incomes", contentValues, "id = ? AND user_id = ? ", new String[] { Integer.toString(id), Integer.toString(user_id) } );
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Delete data in the Contact table given by the ID
    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    //Delete data in the Income table given by the ID
    public Integer deleteIncome (Integer id, Integer user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("incomes",
                "id = ? AND user_id = ? ",
                new String[] { Integer.toString(id), Integer.toString(user_id) });
    }

    //Hide costs table data given by the ID, To do that update the "deleted" column as 1 (1 mean deleted raw item/ 0 mean non-deleted raw item)
    public boolean hideCost (Integer id, Integer user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("deleted", 1);
        contentValues.put("date_updated", DateConverter.DateConvert(new Date()));
        long result = db.update("costs", contentValues, "id = ? AND user_id = ? ", new String[] { Integer.toString(id), Integer.toString(user_id) } );
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Delete data in the Cost table given by the ID
    public Integer deleteCost (Integer id, Integer user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("costs",
                "id = ? AND user_id = ? ",
                new String[] { Integer.toString(id), Integer.toString(user_id) });
    }

    //Hide loans table data given by the ID, To do that update the "deleted" column as 1 (1 mean deleted raw item/ 0 mean non-deleted raw item)
    public boolean hideLoan (Integer id, Integer user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("deleted", 1);
        contentValues.put("date_updated", DateConverter.DateConvert(new Date()));
        long result = db.update("loans", contentValues, "id = ? AND user_id = ? ", new String[] { Integer.toString(id), Integer.toString(user_id) } );
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Delete data in the Loan table given by the ID
    public Integer deleteLoan (Integer id, Integer user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("loans",
                "id = ? AND user_id = ? ",
                new String[] { Integer.toString(id), Integer.toString(user_id) });
    }

    //Get all contact data
    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        db.close();
        return array_list;
    }

    //Get All Income data
    public ArrayList<String> getAllIncomes() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from incomes WHERE deleted = 0", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(INCOMES_COLUMN_TITLE)));
            res.moveToNext();
        }
        db.close();
        return array_list;
    }

    //Get all current month income data list
    public ArrayList<Income> getAllCurrentMonthIncomes() {
        ArrayList<Income> array_list = new ArrayList<Income>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from incomes"+ " WHERE strftime('%Y',date) = strftime('%Y',date('now')) AND  strftime('%m',date) = strftime('%m',date('now')) AND deleted = 0 ORDER BY date DESC", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Income income = new Income();
            income.setId(res.getInt(res.getColumnIndex(INCOMES_COLUMN_ID)));
            income.setTitle(res.getString(res.getColumnIndex(INCOMES_COLUMN_TITLE)));
            income.setAmount(res.getLong(res.getColumnIndex(INCOMES_COLUMN_AMOUNT)));
            income.setDate(res.getString(res.getColumnIndex(INCOMES_COLUMN_DATE)));
            array_list.add(income);
            res.moveToNext();
        }
        db.close();
        return array_list;
    }

    //Get all current month income data list
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ArrayList<IncomeChartModel> getAllCurrentMonthIncomesOrderByDateASE() {
        ArrayList<Income> array_list = new ArrayList<Income>();
        ArrayList<IncomeChartModel> chartModelList = new ArrayList<IncomeChartModel>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from incomes"+ " WHERE strftime('%Y',date) = strftime('%Y',date('now')) AND  strftime('%m',date) = strftime('%m',date('now')) AND deleted = 0 ORDER BY date ASC", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Income income = new Income();
            income.setId(res.getInt(res.getColumnIndex(INCOMES_COLUMN_ID)));
            income.setTitle(res.getString(res.getColumnIndex(INCOMES_COLUMN_TITLE)));
            income.setAmount(res.getLong(res.getColumnIndex(INCOMES_COLUMN_AMOUNT)));
            income.setDate(res.getString(res.getColumnIndex(INCOMES_COLUMN_DATE)));
            array_list.add(income);
            res.moveToNext();
        }
        db.close();
        if(array_list.size() > 0){
            int firstDate = DateConverter.getOnlyDate(array_list.get(0).getDate());
            int lastDate = DateConverter.getOnlyDate(array_list.get(array_list.size()-1).getDate());
            int todayDate = DateConverter.getOnlyDate(DateConverter.DateConvert(new Date()));
            if(lastDate < todayDate)
                lastDate = todayDate;
            for (int i=0; i <= lastDate;i++){
                IncomeChartModel incomeChartModel = new IncomeChartModel();
                incomeChartModel.setId(i);
                if(i==0){
                    incomeChartModel.setAmmount(0);
                }else {
                    //incomeChartModel.setAmmount(ThreadLocalRandom.current().nextFloat());

                    for (Income income: array_list) {
                        if(DateConverter.getOnlyDate(income.getDate()) == i){
                            incomeChartModel.setAmmount(income.getAmount());
                        }
                    }
                }
                chartModelList.add(incomeChartModel);
            }
        }

        return chartModelList;
    }

    //Get all cost data list
    public ArrayList<String> getAllCosts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from costs WHERE deleted = 0", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COSTS_COLUMN_TITLE)));
            res.moveToNext();
        }
        db.close();
        return array_list;
    }

    //Get all current month cost data list
    public ArrayList<Cost> getAllCurrentMonthCosts() {
        ArrayList<Cost> array_list = new ArrayList<Cost>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from costs"+ " WHERE strftime('%Y',date) = strftime('%Y',date('now')) AND  strftime('%m',date) = strftime('%m',date('now')) AND deleted = 0 ORDER BY date DESC", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Cost cost = new Cost();
            cost.setId(res.getInt(res.getColumnIndex(INCOMES_COLUMN_ID)));
            cost.setTitle(res.getString(res.getColumnIndex(INCOMES_COLUMN_TITLE)));
            cost.setAmount(res.getLong(res.getColumnIndex(INCOMES_COLUMN_AMOUNT)));
            cost.setDate(res.getString(res.getColumnIndex(INCOMES_COLUMN_DATE)));
            array_list.add(cost);
            res.moveToNext();
        }
        db.close();
        return array_list;
    }

    //Get all loans
    public ArrayList<Loan> getAllLoans() {
        ArrayList<Loan> array_list = new ArrayList<Loan>();
        Loan loan ;
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from loans WHERE deleted = 0", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            loan = new Loan();
            loan.setId(Integer.parseInt(res.getString(res.getColumnIndex("id"))));
            loan.setAccountName(res.getString(res.getColumnIndex("account_name")));
            loan.setAccountNumber(res.getString(res.getColumnIndex("account_number")));
            loan.setLoanAmount(Long.parseLong(res.getString(res.getColumnIndex("loan_amount"))));
            loan.setMonthlyPayment(res.getString(res.getColumnIndex("monthly_payment")));
            //String a = (res.getString(res.getColumnIndex("monthly_payment")));
            loan.setInterestRate(res.getString(res.getColumnIndex("rate")));
            loan.setOpenDate(res.getString(res.getColumnIndexOrThrow("open_date")));
            loan.setNumberOfMonth(Integer.parseInt(res.getString(res.getColumnIndex("months"))));
            loan.setNumberOfPaidMonths(Integer.parseInt(res.getString(res.getColumnIndex("number_of_paid_months"))));
            array_list.add(loan);
            res.moveToNext();
        }
        db.close();
        return array_list;
    }

    //Get all loans
    public ArrayList<LoanPeriod> getAllLoanInstallment(String account_number, int user_id ) {
        ArrayList<LoanPeriod> array_list = new ArrayList<LoanPeriod>();
        Loan loan ;
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from loan_installment WHERE loan_account_number = "+ account_number + " AND user_id = "+user_id, null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            LoanPeriod loanPeriod= new LoanPeriod();
            loanPeriod.setId(Integer.parseInt(res.getString(res.getColumnIndex("id"))));
            loanPeriod.setLoan_account_number(res.getString(res.getColumnIndex("loan_account_number")));
            loanPeriod.setLoan_installment_id(Integer.parseInt(res.getString(res.getColumnIndex("loan_installment_id"))));
            loanPeriod.setLoan_installment(Double.parseDouble(res.getString(res.getColumnIndex("loan_installment"))));
            loanPeriod.setUser_id(Integer.parseInt(res.getString(res.getColumnIndex("user_id"))));
            loanPeriod.setStatus(res.getString(res.getColumnIndex("status")));
            array_list.add(loanPeriod);
            res.moveToNext();
        }
        db.close();
        return array_list;
    }

    // Count Current Month Income
    public long CurrentMonthIncome(){
        long x = 0;
        SQLiteDatabase db = getReadableDatabase();
        String getamountdata = "SELECT SUM(amount) AS totalIncome FROM "+ INCOMES_TABLE_NAME + " WHERE strftime('%Y',date) = strftime('%Y',date('now')) AND  strftime('%m',date) = strftime('%m',date('now')) and deleted = 0";
        Cursor c = db.rawQuery(getamountdata, null);
        if(c.moveToFirst()){
            x= c.getLong(0);
        }
        db.close();
        return x;
    }

    // Count Current Month Cost
    public long CurrentMonthCost(){
        long x = 0;
        SQLiteDatabase db = getReadableDatabase();
        String getamountdata = "SELECT SUM(amount) AS totalCost FROM "+ COSTS_TABLE_NAME + " WHERE strftime('%Y',date) = strftime('%Y',date('now')) AND  strftime('%m',date) = strftime('%m',date('now')) AND deleted = 0";
        Cursor c = db.rawQuery(getamountdata, null);
        if(c.moveToFirst()){
            x = c.getLong(0);
        }
        db.close();
        return x;
    }

    // Count Current Year Income
    public long CurrentYearIncome(){
        long x = 0;
        float y = 0;
        SQLiteDatabase db = getReadableDatabase();
        String getamountdata = "SELECT SUM(amount) AS totalIncome FROM "+ INCOMES_TABLE_NAME + " WHERE strftime('%Y',date) = strftime('%Y',date('now')) AND deleted = 0";
        Cursor c = db.rawQuery(getamountdata, null);
        if(c.moveToFirst()){
            x = c.getLong(0);
        }
        db.close();
        return x;
    }

    // Count Current Year Cost
    public long CurrentYearCost(){
        long x = 0;
        SQLiteDatabase db = getReadableDatabase();
        String getamountdata = "SELECT SUM(amount) AS totalCost FROM "+ COSTS_TABLE_NAME + " WHERE strftime('%Y',date) = strftime('%Y',date('now')) AND deleted = 0";
        Cursor c = db.rawQuery(getamountdata, null);
        if(c.moveToFirst()){
            x = c.getLong(0);
        }
        db.close();
        return x;
    }
}
