package com.nandeproduction.dailycost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nandeproduction.dailycost.db.DBHelper;

public class UserRegistration extends AppCompatActivity {

    TextView txtError;
    EditText txtFirstName, txtLastName, txtEmail, txtCurrency;
    Button btnSignUp;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtEmail);
        txtCurrency = findViewById(R.id.txtCurrency);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtError = findViewById(R.id.txtError);
        DB = new DBHelper(getApplicationContext());
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fname = txtFirstName.getText().toString();
                final String lname = txtLastName.getText().toString();
                final String email = txtEmail.getText().toString();
                final String currency = txtCurrency.getText().toString();
                boolean validation = true;
                if(fname.length()==0){
                    txtError.setText("Please Enter First Name");
                    validation = false;
                    txtFirstName.setFocusable(true);
                    txtFirstName.requestFocus();
                }else if(lname.length()==0){
                    txtError.setText("Please Enter Last Name");
                    validation = false;
                    txtLastName.setFocusable(true);
                    txtLastName.requestFocus();
                }else if(email.length()==0){
                    txtError.setText("Please Enter Email or Phone Number");
                    validation = false;
                    txtEmail.setFocusable(true);
                    txtEmail.requestFocus();
                }else if(currency.length() == 0){
                    txtError.setText("Please Enter Currency");
                    validation = false;
                    txtCurrency.setFocusable(true);
                    txtCurrency.requestFocus();
                }
                if(validation){
                    try {
                        User user = new User();
                        user.setFirstName(fname);
                        user.setLastName(lname);
                        user.setEmailOrPhonenumber(email);
                        user.setStreet("Street");
                        user.setCountry("Country");
                        user.setCurrency(currency);
                        Boolean userInsert = DB.insertUser(user);
                    if(userInsert){
                        Intent mainIntent = new Intent(UserRegistration.this, MainActivity.class);
                        startActivity(mainIntent);
                    }else{
                        txtError.setText("User Registration Fail.! Try Again");
                        validation = false;
                    }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}