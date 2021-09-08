package com.dailyexpense.tracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dailyexpense.tracker.db.DBHelper;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;

public class BottomLoanListviewAdapter extends BaseAdapter {
    public ArrayList<LoanPeriod> loanList;
    Activity activity;
    DBHelper DB;
    private InterstitialAd mInterstitialAd;//Display Ad
    private static final String TAG = "BottomLoanListviewAdp";

    public BottomLoanListviewAdapter(Activity activity, ArrayList<LoanPeriod> itemList) {
        super();
        this.activity = activity;
        this.loanList = itemList;
    }

    @Override
    public int getCount() {
        return loanList.size();
    }

    @Override
    public Object getItem(int position) {
        return loanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder {
        TextView id;
        TextView payment;
        Button paid;
        //Button delete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        final BottomLoanListviewAdapter.ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            //convertView = inflater.inflate(R.layout.listview_row, null);
            convertView = inflater.inflate(R.layout.listview_bottom_loan, parent,false);
            holder = new BottomLoanListviewAdapter.ViewHolder();
            //holder.mId = (TextView) convertView.findViewById(R.id.id);
            holder.id = (TextView) convertView.findViewById(R.id.id);
            holder.payment = (TextView) convertView.findViewById(R.id.payment);
            holder.paid = (Button) convertView.findViewById(R.id.paid);
            //holder.delete = (Button) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (BottomLoanListviewAdapter.ViewHolder) convertView.getTag();
        }

        LoanPeriod item = loanList.get(position);
        holder.id.setText(String.valueOf(item.getLoan_installment_id()));
        holder.payment.setText(item.getLoan_installment());
        holder.paid.setText(item.getStatus());
        String paid = item.getStatus();

        holder.paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String paidOrNot = holder.paid.getText().toString();
                if(paidOrNot.contains("NOT")){
                    DB = new DBHelper(activity.getApplicationContext());
                    if(DB.payInstallment(item.getLoan_installment_id(),item.getLoan_account_number(),DB.getCurrentUserID())){
                        Toast.makeText(activity.getApplicationContext(),"Installment Paid Successfully", Toast.LENGTH_SHORT).show();
                        //Load Ad Stat
                        if(checkInternetOn()){
                            //AdmMbo Initialize Start
                            MobileAds.initialize(activity.getApplicationContext(), new OnInitializationCompleteListener() {
                                @Override
                                public void onInitializationComplete(InitializationStatus initializationStatus) {
                                }
                            });
                            //AdMob Initialize End

                            //AdMob InterstitialAd ads Start - test ID = ca-app-pub-3940256099942544/1033173712 /
                            //ca-app-pub-4566432493079281/7518953945
                            AdRequest adRequestmInAd = new AdRequest.Builder().build();
                            mInterstitialAd.load(activity.getApplicationContext(), "ca-app-pub-4566432493079281/7518953945", adRequestmInAd,
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
                            //Load Adds when click Play Again button
                            if (mInterstitialAd != null) {
                                mInterstitialAd.show(activity);
                            } else {
                                Log.d("TAG", "The interstitial ad wasn't ready yet.");
                            }
                        }
                        //Load Ad End
                        Intent mainIntent = new Intent(activity.getApplicationContext(), MainActivity.class);
                        activity.startActivity(mainIntent);
                    }
                }else{
                    Toast.makeText(activity.getApplicationContext(),"Installment Already Paid", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return convertView;
    }

    //Check Internet Connection Available at this time
    private boolean checkInternetOn(){
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }else{
            return false;
        }
    }
}
