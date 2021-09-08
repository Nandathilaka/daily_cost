package com.dailyexpense.tracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.makeramen.roundedimageview.RoundedImageView;
import com.dailyexpense.tracker.db.DBHelper;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private EditText edtName, edtAge, edtPosition, edtAddress;
    private Button btnSave;
    private static final String TAG = "MainActivity";
    private Tracker mTracker;//Google Analytics
    private FirebaseAnalytics firebaseAnalytics;
    DBHelper DB;
    private InterstitialAd mInterstitialAd;//Display Ad

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //AdmMbo Initialize Start
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //AdMob Initialize End

        //AdMob InterstitialAd ads Start - test ID = ca-app-pub-3940256099942544/1033173712 /
        //ca-app-pub-4566432493079281/2872583182
        AdRequest adRequestmInAd = new AdRequest.Builder().build();
        mInterstitialAd.load(this, "ca-app-pub-4566432493079281/2872583182", adRequestmInAd,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
        //AdMob InterstitialAd ads End

        //Google Analytics Start
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Daily Expense MainActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        //Google Analytics End
        //Firebase Start
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        //Firebase Cloud Messaging Token Start
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
        //Firebase  Cloud Messaging  Token End

        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.default_notification_channel_name))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        //Firebase End


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Developer : Thilina Chamika \nEmail: thilinachamikashtc@gmail.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //Profile Start
        //Initialized Database
        DB = new DBHelper(this);
        View headerView = navigationView.getHeaderView(0);
        TextView name = headerView.findViewById(R.id.name);
        TextView emaiOrPhone = headerView.findViewById(R.id.emailOrPhoneNumber);
        TextView country = headerView.findViewById(R.id.country);
        TextView currency = headerView.findViewById(R.id.currency);
        User user = new User();
        user = DB.getUserDetails();
        name.setText(user.getFirstName() + " " + user.getLastName());
        emaiOrPhone.setText(user.getEmailOrPhonenumber());
        country.setText(user.getCountry());
        currency.setText(user.getCurrency());

        RoundedImageView profile = headerView.findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        MainActivity.this, R.style.BottomSheetDialogTheme
                );
                final View bottomSheetView  = LayoutInflater.from(MainActivity.this).inflate(
                        R.layout.edit_profile,
                        (LinearLayout)findViewById(R.id.bottomSheetProfileContainer)
                );
                User newUser = new User();
                newUser = DB.getUserDetails();
                TextView fullName = bottomSheetView.findViewById(R.id.txtName);
                TextView bottomfname = bottomSheetView.findViewById(R.id.txtFirstName);
                TextView bottomlname = bottomSheetView.findViewById(R.id.txtLastName);
                TextView bottomemail = bottomSheetView.findViewById(R.id.txtEmail);
                TextView bottomcountry = bottomSheetView.findViewById(R.id.txtCountry);
                TextView bottomcurrency = bottomSheetView.findViewById(R.id.txtCurrency);
                fullName.setText(newUser.getFirstName() + " " +newUser.getLastName());
                bottomfname.setText(newUser.getFirstName());
                bottomlname.setText(newUser.getLastName());
                bottomemail.setText(newUser.getEmailOrPhonenumber());
                bottomcountry.setText(newUser.getCountry());
                bottomcurrency.setText(newUser.getCurrency());

                bottomSheetDialog.setContentView(bottomSheetView);
                if(!bottomSheetDialog.isShowing()){
                    bottomSheetDialog.show();
                }else{
                    bottomSheetDialog.dismiss();
                }

                //bottomSheetDialog.show();
                bottomSheetView.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        User updateUser = new User();
                        updateUser.setUserID(DB.getCurrentUserID());
                        updateUser.setFirstName(bottomfname.getText().toString());
                        updateUser.setLastName(bottomlname.getText().toString());
                        updateUser.setEmailOrPhonenumber(bottomemail.getText().toString());
                        updateUser.setCountry(bottomcountry.getText().toString());
                        updateUser.setCurrency(bottomcurrency.getText().toString());
                        if(validateUser(updateUser)){
                            if(DB.updateUser(updateUser)){
                                Toast.makeText(MainActivity.this,"User Update Successfully", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.hide();
                                User updatedUser = new User();
                                updatedUser = DB.getUserDetails();
                                name.setText(updatedUser.getFirstName() + " " + updatedUser.getLastName());
                                emaiOrPhone.setText(updatedUser.getEmailOrPhonenumber());
                                country.setText(updatedUser.getCountry());
                                currency.setText(updatedUser.getCurrency());
                                //Load Ad Stat
                                if(checkInternetOn()){
                                    //Load Adds when click Play Again button
                                    if (mInterstitialAd != null) {
                                        mInterstitialAd.show(MainActivity.this);
                                    } else {
                                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                                    }
                                }
                                //Load Ad End
                                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(mainIntent);
                            }
                        }

                    }

                    //Check Internet Connection Available at this time
                    private boolean checkInternetOn(){
                        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                            //we are connected to a network
                            return true;
                        }else{
                            return false;
                        }
                    }

                    private boolean validateUser(User updateUser) {
                        boolean validation = true;
                        if(updateUser.getFirstName().length()==0){
                            validation = false;
                            bottomfname.setFocusable(true);
                            bottomfname.requestFocus();
                        }else if(updateUser.getLastName().length()==0){
                            validation = false;
                            bottomlname.setFocusable(true);
                            bottomlname.requestFocus();
                        }else if(updateUser.getEmailOrPhonenumber().length()==0){
                            validation = false;
                            bottomemail.setFocusable(true);
                            bottomemail.requestFocus();
                        }else if(updateUser.getCountry().length() == 0){
                            validation = false;
                            bottomcountry.setFocusable(true);
                            bottomcountry.requestFocus();
                        }else if(updateUser.getCurrency().length() == 0){
                            validation = false;
                            bottomcurrency.setFocusable(true);
                            bottomcurrency.requestFocus();
                        }
                        return validation;
                    }
                });
            }
        });
        //Profile End

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_overview, R.id.nav_income, R.id.nav_cost, R.id.nav_loan)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

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



    // Here you will enable Multidex
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(getBaseContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

}