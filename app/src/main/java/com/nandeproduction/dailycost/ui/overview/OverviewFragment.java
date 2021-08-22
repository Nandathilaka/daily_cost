package com.nandeproduction.dailycost.ui.overview;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nandeproduction.dailycost.AppLoading;
import com.nandeproduction.dailycost.BottomLoanListviewAdapter;
import com.nandeproduction.dailycost.DateConverter;
import com.nandeproduction.dailycost.Income;
import com.nandeproduction.dailycost.IncomeChartModel;
import com.nandeproduction.dailycost.Loan;
import com.nandeproduction.dailycost.LoanListviewAdapter;
import com.nandeproduction.dailycost.LoanOverviewListviewAdapter;
import com.nandeproduction.dailycost.MainActivity;
import com.nandeproduction.dailycost.R;
import com.nandeproduction.dailycost.db.DBHelper;
import com.nandeproduction.dailycost.ui.cost.CostFragment;
import com.nandeproduction.dailycost.ui.income.IncomeFragment;
import com.nandeproduction.dailycost.ui.loan.LoanFragment;

import org.w3c.dom.Entity;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
    TextView txtTime;
    DBHelper DB;
    Button btnIncome,btnCost,btnLoan;
    CardView cardViewChart;

    Fragment fragment = null;

    public static ArrayList<Loan> loanList;
    public static ArrayList<IncomeChartModel> incomeList;
    public static ListView listView;
    int itemID = 0;
    public static LoanOverviewListviewAdapter adapter = null;

    private static final String TAG = "OverviewFragment";
    private LineChart lineChart;
    private Object Income;

    //Bottom Details
    public static ListView bottomListView;
    public static BottomLoanListviewAdapter adapterBottom = null;


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
        txtTime = root.findViewById(R.id.txtTime);
        Date today=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(today);
        txtTime.setText("Here's info as at "+ currentDateTimeString + ", Today. Pull to refresh");
        lblAssetThisMonth = root.findViewById(R.id.lblAssetThisMonth);
        lblAssetThisYear = root.findViewById(R.id.lblAssetThisYear);
        DB = new DBHelper(getContext());
        lblIncomeThisMonth = root.findViewById(R.id.lblIncomeThisMonth);
        lblCostThisMonth = root.findViewById(R.id.lblCostThisMonth);
        long currentMonthIncome = DB.CurrentMonthIncome();
        long currentMonthCost = DB.CurrentMonthCost();
        lblIncomeThisMonth.setText(Long.toString(currentMonthIncome));
        lblCostThisMonth.setText(Long.toString(currentMonthCost));
        lblAssetThisMonth.setText(Long.toString(currentMonthIncome-currentMonthCost));
        lblIncomeThisYear = root.findViewById(R.id.lblIncomeThisYear);
        lblCostThisYear = root.findViewById(R.id.lblCostThisYear);
        long currentYearIncome = DB.CurrentYearIncome();
        long currentYearCost = DB.CurrentYearCost();
        lblIncomeThisYear.setText(Long.toString(currentYearIncome));
        lblCostThisYear.setText(Long.toString(currentYearCost));
        lblAssetThisYear.setText(Long.toString(currentYearIncome-currentYearCost));
        lblNumberOfLoans = root.findViewById(R.id.lblNumberOfLoans);
        int numberofLoans = DB.numberOfActiveLoansRows();
        lblNumberOfLoans.setText("Loans("+numberofLoans+")");
        cardViewChart = root.findViewById(R.id.cardViewChart);
        if(currentMonthIncome == 0){
            cardViewChart.setVisibility(View.INVISIBLE);
        }else{
            cardViewChart.setVisibility(View.VISIBLE);
        }
        cardViewChart.setVisibility(View.VISIBLE);

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
                //String incomeId = ((TextView)view.findViewById(R.id.id)).getText().toString();
                String accountNumber = ((TextView)view.findViewById(R.id.accNumber)).getText().toString();
                String payment = ((TextView)view.findViewById(R.id.payment)).getText().toString();
                String nextPaymentDate = ((TextView)view.findViewById(R.id.nextPaymentDate)).getText().toString();

                Toast.makeText(getContext(),
                        "Account : " + accountNumber +"\n"
                                +"Payment : " +payment +"\n"
                                +"Next Payment Date : " +nextPaymentDate, Toast.LENGTH_SHORT).show();

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(), R.style.BottomSheetDialogTheme
                );
                View bottomSheetView  = LayoutInflater.from(getActivity()).inflate(
                        R.layout.layout_bottom_sheet,
                        (LinearLayout)root.findViewById(R.id.bottomSheetContainer)
                );

                //
                bottomListView =   bottomSheetView.findViewById(R.id.bottomLoanListView);
                adapterBottom = new BottomLoanListviewAdapter(getActivity(), loanList);
                bottomListView.setAdapter(adapterBottom);
                adapterBottom.notifyDataSetChanged();
                //

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();



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

        //LineChard Start
        lineChart = (LineChart) root.findViewById(R.id.lineChar);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);

        ArrayList<Entry> income= new ArrayList<>();
        incomeList = DB.getAllCurrentMonthIncomesOrderByDateASE();
        for (IncomeChartModel data : incomeList) {
            // turn your data into Entry objects
            income.add(new Entry(data.getId(), data.getAmmount()));
        }

        LineDataSet set1 = new LineDataSet(income, "Income");
        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextColor(Color.parseColor("#3090C7"));
        set1.setValueTextSize(10f);

        ArrayList<ILineDataSet> datasets1 = new ArrayList<>();
        datasets1.add(set1);
        LineData data1 = new LineData(datasets1);
        lineChart.setData(data1);

        //Line Chart End

        DB.close();
        return root;
    }

    private void getAllLoan(){
        loanList = DB.getAllLoans();
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}