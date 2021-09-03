package com.dailyexpense.tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.dailyexpense.tracker.db.DBHelper;

public class AppLoading extends AppCompatActivity {
    DBHelper DB;
    private final String TAG = "AppLoading";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_loading);

        DB = new DBHelper(getApplicationContext());
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(4000);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    int userCouunt = DB.checkUser();
                    if(userCouunt>0){
                        Intent mainIntent = new Intent(AppLoading.this, MainActivity.class);
                        startActivity(mainIntent);
                    }else{
                        Intent userIntent = new Intent(AppLoading.this, UserRegistration.class);
                        startActivity(userIntent);
                    }
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}