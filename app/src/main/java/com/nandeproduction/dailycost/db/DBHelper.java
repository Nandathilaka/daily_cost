package com.nandeproduction.dailycost.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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


    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table contacts " +
                        "(id integer primary key , name text,phone text,email text, street text,place text)"
        );

        // Create Users Table
        db.execSQL(
                "create table users " +
                        "(user_id interger primary key AUTOINCREMENT, first_name text, last_name text, email text UNIQUE, street text, country text)"
        );

        // Create Income Table
        db.execSQL(
                "create table incomes " +
                        "(id interger primary key AUTOINCREMENT, user_id interger ,title text, amount interger, date text, foreign key (user_id) references users (user_id))"
        );

        // Create Cost Table
        db.execSQL(
                "create table costs " +
                        "(id interger primary key AUTOINCREMENT, user_id interger ,title text, amount interger, date text, foreign key (user_id) references users (user_id))"
        );

        // Create Loan Table
        db.execSQL(
                "create table loans " +
                        "(id interger primary key AUTOINCREMENT, user_id interger , account_name text, account_number text, loan_amount interger, monthly_payment interger,rate text, open_date text,months interger, foreign key (user_id) references users (user_id))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
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
        db.insert("contacts", null, contentValues);
        return true;
    }

    public boolean insertUser (String fname, String lname, String email, String street,String country) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", fname);
        contentValues.put("last_name", lname);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", country);
        db.insert("users", null, contentValues);
        return true;
    }

    public boolean insertIncome (String user_id, String title, String amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", user_id);
        contentValues.put("title", title);
        contentValues.put("amount", amount);
        contentValues.put("date", date);
        db.insert("income", null, contentValues);
        return true;
    }

    public boolean insertCost (String user_id, String title, String amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", user_id);
        contentValues.put("title", title);
        contentValues.put("amount", amount);
        contentValues.put("date", date);
        db.insert("costs", null, contentValues);
        return true;
    }

    public boolean insertLoan (String user_id, String account_name, String account_number, String loan_amount, String monthly_payment, String rate, String open_date, String months) {
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
        db.insert("loans", null, contentValues);
        return true;
    }


    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public int numberOfLoansRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numOfLoansRows = (int) DatabaseUtils.queryNumEntries(db, LOANS_TABLE_NAME);
        return numOfLoansRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateUser (Integer user_id, String fname, String lname, String email, String street,String country) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", fname);
        contentValues.put("last_name", lname);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("country", country);
        db.update("users", contentValues, "user_id = ? ", new String[] { Integer.toString(user_id) } );
        return true;
    }

    public boolean updateIncome (Integer id, String title, String amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("amount", amount);
        contentValues.put("date", date);
        db.update("incomes", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateCost (Integer id, String title, String amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("amount", amount);
        contentValues.put("date", date);
        db.update("costs", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateLoan (Integer id, String account_name, String account_number, String loan_amount, String monthly_payment, String rate, String open_date, String months) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_name", account_name);
        contentValues.put("account_number", account_number);
        contentValues.put("loan_amount", loan_amount);
        contentValues.put("monthly_payment", monthly_payment);
        contentValues.put("rate", rate);
        contentValues.put("open_date", open_date);
        contentValues.put("months",months);
        db.update("loans", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteIncome (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("incomes",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteCost (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("costs",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteLoan (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("loans",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }


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
        return array_list;
    }

    public ArrayList<String> getAllIncomes() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from incomes", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(INCOMES_COLUMN_TITLE)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllCosts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from costs", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COSTS_COLUMN_TITLE)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllLoans() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from loans", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(LOANS_COLUMN_ACCOUNT_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}
