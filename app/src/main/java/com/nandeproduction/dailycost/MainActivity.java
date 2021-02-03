package com.nandeproduction.dailycost;

import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private EditText edtName, edtAge, edtPosition, edtAddress;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_overview, R.id.nav_income, R.id.nav_cost, R.id.nav_loan)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        /*
        edtName = (EditText)findViewById(R.id.edtName);
        edtAddress = (EditText)findViewById(R.id.edtAddress);
        edtAge = (EditText)findViewById(R.id.edtAge);
        edtPosition = (EditText)findViewById(R.id.edtPosition);

        // get our button by its ID
        btnSave = (Button) findViewById(R.id.BtnSave);

        // set its click listener
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //addEmployee();

            }
        });
        */
    }
/*
    private void addEmployee()  {
    }

    private void addEmployee(View v) {

        String employeeName = edtName.getText().toString();
        String employeeAge = edtAge.getText().toString();
        String employeePosition = edtPosition.getText().toString();
        String employeeAddress = edtAddress.getText().toString();

        // return if the input fields are blank
        if (TextUtils.isEmpty(employeeName) && TextUtils.isEmpty(employeeAge) &&
                TextUtils.isEmpty(employeePosition) &&
                TextUtils.isEmpty(employeeAddress)) {
            return;
        }

        RequestParams params = new RequestParams();
        // set our JSON object
        params.put("name", employeeName);
        params.put("age", employeeAge);
        params.put("position", employeePosition);
        params.put("address", employeeAddress);

        // create our HTTP client
        AsyncHttpClient client = new AsyncHttpClient();
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}