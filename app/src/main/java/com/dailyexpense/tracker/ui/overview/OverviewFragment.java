package com.dailyexpense.tracker.ui.overview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dailyexpense.tracker.AppLoading;
import com.dailyexpense.tracker.MainActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.dailyexpense.tracker.BottomLoanListviewAdapter;
import com.dailyexpense.tracker.CostChartModel;
import com.dailyexpense.tracker.IncomeChartModel;
import com.dailyexpense.tracker.Loan;
import com.dailyexpense.tracker.LoanOverviewListviewAdapter;
import com.dailyexpense.tracker.LoanPeriod;
import com.dailyexpense.tracker.R;
import com.dailyexpense.tracker.User;
import com.dailyexpense.tracker.db.DBHelper;
import com.dailyexpense.tracker.ui.cost.CostFragment;
import com.dailyexpense.tracker.ui.income.IncomeFragment;
import com.dailyexpense.tracker.ui.loan.LoanFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OverviewFragment extends Fragment {

    private OverviewViewModel overviewViewModel;
    TextView lblIncomeThisMonth;
    TextView lblCostThisMonth;
    TextView lblAssetThisMonth;
    TextView lblIncomeThisYear;
    TextView lblCostThisYear;
    TextView lblAssetThisYear;
    TextView lblNumberOfLoans;
    TextView lblIncomeCurrency;
    TextView lblCostCurrency, lblSaveCurrency;
    TextView lblSavePart1, lblSavePart2, lblSavePart3;
    TextView txtTime;
    DBHelper DB;
    Button btnIncome,btnCost,btnLoan;
    CardView cardViewIncomeChart, cardViewCostChart;

    Fragment fragment = null;

    public static ArrayList<Loan> loanList;
    public static ArrayList<LoanPeriod> loanInstallmentList;
    public static ArrayList<IncomeChartModel> incomeList;
    public static ArrayList<CostChartModel> costList;
    public static ListView listView;
    int itemID = 0;
    public static LoanOverviewListviewAdapter adapter = null;

    private static final String TAG = "OverviewFragment";
    private LineChart lineIncomeChart, lineCostChart;
    private Object Income;

    //Bottom Details
    public static ListView bottomListView;
    public static BottomLoanListviewAdapter adapterBottom = null;
    //Get Current User ID
    private int userID = 0;

    private AdView mAdView; //AdMob

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        overviewViewModel =
                ViewModelProviders.of(this).get(OverviewViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_overview, container, false);
        final TextView textView = root.findViewById(R.id.text_overview);
        overviewViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        //AdMob Start
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Intent mainIntent = new Intent(getContext(), AppLoading.class);
                startActivity(mainIntent);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        });
        //AdMob End

        DB = new DBHelper(getContext());
        User user = new User();
        user = DB.getUserDetails();
        final String currency = user.getCurrency();
        lblIncomeCurrency = root.findViewById(R.id.lblIncomeCurrency);
        lblIncomeCurrency.setText(user.getCurrency());
        lblCostCurrency = root.findViewById(R.id.lblCostCurrency);
        lblCostCurrency.setText(user.getCurrency());
        lblSaveCurrency = root.findViewById(R.id.lblSaveCurrency);
        lblSaveCurrency.setText(user.getCurrency());
        txtTime = root.findViewById(R.id.txtTime);
        Date today=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(today);
        txtTime.setText("Here's info as at "+ currentDateTimeString + ", Today. Pull to refresh");
        lblAssetThisMonth = root.findViewById(R.id.lblAssetThisMonth);
        lblAssetThisYear = root.findViewById(R.id.lblAssetThisYear);
        lblIncomeThisMonth = root.findViewById(R.id.lblIncomeThisMonth);
        lblCostThisMonth = root.findViewById(R.id.lblCostThisMonth);
        double currentMonthIncome = DB.CurrentMonthIncome();
        double currentMonthCost = DB.CurrentMonthCost();
        //lblIncomeThisMonth.setText(Long.toString(currentMonthIncome));
        lblIncomeThisMonth.setText((String.format("%.2f",currentMonthIncome)));
        //lblCostThisMonth.setText(Long.toString(currentMonthCost));
        lblCostThisMonth.setText((String.format("%.2f",currentMonthCost)));
        //lblAssetThisMonth.setText(Long.toString(currentMonthIncome-currentMonthCost));
        lblAssetThisMonth.setText((String.format("%.2f",currentMonthIncome-currentMonthCost)));
        lblIncomeThisYear = root.findViewById(R.id.lblIncomeThisYear);
        lblCostThisYear = root.findViewById(R.id.lblCostThisYear);
        double currentYearIncome = DB.CurrentYearIncome();
        double currentYearCost = DB.CurrentYearCost();
        lblIncomeThisYear.setText((String.format("%.2f",currentYearIncome)));
        lblCostThisYear.setText((String.format("%.2f",currentYearCost)));
        lblAssetThisYear.setText((String.format("%.2f",currentYearIncome-currentYearCost)));
        lblNumberOfLoans = root.findViewById(R.id.lblNumberOfLoans);
        int numberofLoans = DB.numberOfActiveLoansRows();
        lblNumberOfLoans.setText("Loans("+numberofLoans+")");
        cardViewIncomeChart = root.findViewById(R.id.cardViewIncomeChart);
        /*
        if(currentMonthIncome == 0){
            cardViewIncomeChart.setVisibility(View.INVISIBLE);
        }else{
            cardViewIncomeChart.setVisibility(View.VISIBLE);
        }
        //cardViewIncomeChart.setVisibility(View.VISIBLE);
        cardViewCostChart = root.findViewById(R.id.cardViewCostChart);
        if(currentMonthCost == 0){
            cardViewCostChart.setVisibility(View.INVISIBLE);
        }else{
            cardViewCostChart.setVisibility(View.VISIBLE);
        }
         */

        //Save plan start
        String asset25 = String.format("%.2f",(Double.parseDouble(lblIncomeThisMonth.getText().toString()) * 25/100));
        String asset50 = String.format("%.2f",(Double.parseDouble(lblIncomeThisMonth.getText().toString()) * 50/100));

        lblSavePart1 = root.findViewById(R.id.lblSavePart1);
        lblSavePart2 = root.findViewById(R.id.lblSavePart2);
        lblSavePart3 = root.findViewById(R.id.lblSavePart3);
        lblSavePart1.setText(asset25);
        lblSavePart2.setText(asset50);
        lblSavePart3.setText(asset25);
        //Save plan end

        //Click item in the list Start
        loanList = new ArrayList<Loan>();
        listView = (ListView) root.findViewById(R.id.listView);
        getAllLoan();
        adapter = new LoanOverviewListviewAdapter(getActivity(), loanList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Loan selecteedLoan = loanList.get(position);
                itemID = selecteedLoan.getId();
                //String loanId = ((TextView)view.findViewById(R.id.id)).getText().toString();
                String accountNumber = ((TextView)view.findViewById(R.id.accNumber)).getText().toString();
                String payment = ((TextView)view.findViewById(R.id.payment)).getText().toString();
                String nextPaymentDate = ((TextView)view.findViewById(R.id.nextPaymentDate)).getText().toString();

                Toast.makeText(getContext(),
                        "Account : " + accountNumber +"\n"
                                +"Payment : " +payment +"\n"
                                +"Next Payment Date : " +nextPaymentDate, Toast.LENGTH_SHORT).show();

                //Bottom Sheet View Start
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(), R.style.BottomSheetDialogTheme
                );
                View bottomSheetView  = LayoutInflater.from(getContext()).inflate(
                        R.layout.layout_bottom_sheet,
                        (LinearLayout)root.findViewById(R.id.bottomSheetContainer)
                );

                //Set Loan Properties Start
                TextView accountName = (TextView) bottomSheetView.findViewById(R.id.AccName);
                accountName.setText(selecteedLoan.getAccountName());
                TextView accountNum = bottomSheetView.findViewById(R.id.accountNumber);
                accountNum.setText(selecteedLoan.getAccountNumber());
                TextView nextPayment = bottomSheetView.findViewById(R.id.nextPayment);
                nextPayment.setText(selecteedLoan.getMonthlyPayment());
                TextView nextPaytDate = bottomSheetView.findViewById(R.id.nextPaymentDate);
                nextPaytDate.setText(selecteedLoan.getNextPaymentDate());
                TextView txtcurrency = bottomSheetView.findViewById(R.id.currency);
                txtcurrency.setText(currency);// Please update using this from DB - User table

                TextView totalLoanAmount = bottomSheetView.findViewById(R.id.totalLoanAmount);
                totalLoanAmount.setText(selecteedLoan.getLoanAmount());
                TextView paid = bottomSheetView.findViewById(R.id.totalPaidAmount);
                paid.setText(selecteedLoan.getCurrentlyPaid());
                TextView amountInArrears = bottomSheetView.findViewById(R.id.totalRestAmount);
                amountInArrears.setText(selecteedLoan.getAmountArreas());
                TextView interestRateCost = bottomSheetView.findViewById(R.id.rateAmount);
                interestRateCost.setText(selecteedLoan.getInterestAmount());
                TextView finalToBeComplatedPayment = bottomSheetView.findViewById(R.id.finalToBeComplatedPayment);
                finalToBeComplatedPayment.setText(selecteedLoan.getToBeCompletedTotalPaid());
                TextView installments = bottomSheetView.findViewById(R.id.installments);
                installments.setText(selecteedLoan.getNumberOfPaidMonths());
                //Set Loan Properties End

                //Set Loan Installment Plan Start
                getAllLoanInstalments(accountNumber);
                bottomListView =   bottomSheetView.findViewById(R.id.bottomLoanListView);
                adapterBottom = new BottomLoanListviewAdapter(getActivity(), loanInstallmentList);
                bottomListView.setAdapter(adapterBottom);
                adapterBottom.notifyDataSetChanged();
                //Set Loan Installment Plan End

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                //Bottom Sheet View End

            }
        });
        //Click item in the list End
        btnIncome = (Button) root.findViewById(R.id.btnIncome);
        btnCost = (Button) root.findViewById(R.id.btnCost);
        btnLoan = (Button) root.findViewById(R.id.btnLoan);

        btnIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new IncomeFragment();
                replaceFragment(fragment);
            }
        });

        btnCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new CostFragment();
                replaceFragment(fragment);
            }
        });

        btnLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new LoanFragment();
                replaceFragment(fragment);
            }
        });
        //Tab Income, Cost and Loan

        //Line Income Chart Start
        lineIncomeChart = (LineChart) root.findViewById(R.id.lineIncomeChart);
        lineIncomeChart.setDragEnabled(true);
        lineIncomeChart.setScaleEnabled(false);

        ArrayList<Entry> income= new ArrayList<>();
        incomeList = DB.getAllCurrentMonthIncomesOrderByDateASE();
        for (IncomeChartModel data : incomeList) {
            // turn your data into Entry objects
            income.add(new Entry(data.getId(), data.getAmmount()));
        }

        LineDataSet setIncome = new LineDataSet(income, "Income");
        setIncome.setFillAlpha(110);
        setIncome.setColor(Color.parseColor("#1B9A55"));
        setIncome.setLineWidth(3f);
        setIncome.setValueTextColor(Color.parseColor("#3090C7"));
        setIncome.setValueTextSize(10f);

        ArrayList<ILineDataSet> datasetsIncome = new ArrayList<>();
        datasetsIncome.add(setIncome);
        LineData dataIncome = new LineData(datasetsIncome);
        lineIncomeChart.setData(dataIncome);
        //Line Income Chart End

        //Line Cost Chart Start
        lineCostChart = (LineChart) root.findViewById(R.id.lineCostChart);
        lineCostChart.setDragEnabled(true);
        lineCostChart.setScaleEnabled(false);

        ArrayList<Entry> cost= new ArrayList<>();
        costList = DB.getAllCurrentMonthCostsOrderByDateASE();
        for (CostChartModel data : costList) {
            // turn your data into Entry objects
            cost.add(new Entry(data.getId(), data.getAmmount()));
        }

        LineDataSet setCost = new LineDataSet(cost, "Cost");
        setCost.setFillAlpha(110);
        setCost.setColor(Color.RED);
        setCost.setLineWidth(3f);
        setCost.setValueTextColor(Color.parseColor("#3090C7"));
        setCost.setValueTextSize(10f);

        ArrayList<ILineDataSet> datasetCost = new ArrayList<>();
        datasetCost.add(setCost);
        LineData dataCost = new LineData(datasetCost);
        lineCostChart.setData(dataCost);
        //Line Cost Chart End

        DB.close();
        return root;
    }

    private void getAllLoan(){
        loanList = DB.getAllLoans();
    }

    private void getAllLoanInstalments(String account_nu){
        userID = DB.getCurrentUserID();
        loanInstallmentList = DB.getAllLoanInstallment(account_nu,userID);
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}