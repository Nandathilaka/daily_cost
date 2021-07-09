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
    EditText txtFirstName, txtLastName, txtEmail;
    Button btnSignUp;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtEmail);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtError = findViewById(R.id.txtError);
        DB = new DBHelper(getApplicationContext());
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fname = txtFirstName.getText().toString();
                final String lname = txtLastName.getText().toString();
                final String email = txtEmail.getText().toString();
                boolean validation = true;
                if(fname.length()==0){
                    txtError.setText("Please Enter First Name");
                    validation = false;
                }else if(lname.length()==0){
                    txtError.setText("Please Enter Last Name");
                    validation = false;
                }else if(email.length()==0){
                    txtError.setText("Please Enter Email or Phone Number");
                    validation = false;
                }
                if(validation){
                    try {
                    Boolean user = DB.insertUser(fname,lname,email,"Bulutota","Sri Lanka");
                    if(user){
                        Intent mainIntent = new Intent(UserRegistration.this, MainActivity.class);
                        startActivity(mainIntent);
                    }else{
                        txtError.setText("User Registration Faild.! Try Again");
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