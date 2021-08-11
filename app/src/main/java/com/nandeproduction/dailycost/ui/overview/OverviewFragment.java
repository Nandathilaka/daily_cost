package com.nandeproduction.dailycost.ui.overview;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.nandeproduction.dailycost.AppLoading;
import com.nandeproduction.dailycost.DateConverter;
import com.nandeproduction.dailycost.Loan;
import com.nandeproduction.dailycost.LoanListviewAdapter;
import com.nandeproduction.dailycost.LoanOverviewListviewAdapter;
import com.nandeproduction.dailycost.MainActivity;
import com.nandeproduction.dailycost.R;
import com.nandeproduction.dailycost.db.DBHelper;
import com.nandeproduction.dailycost.ui.income.IncomeFragment;

import org.w3c.dom.Text;

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
    TextView txtTime;
    DBHelper DB;
    Button btnIncome,btnCost,btnLoan;

    public static ArrayList<Loan> loanList;
    public static ListView listView;
    int itemID = 0;
    public static LoanOverviewListviewAdapter adapter = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        overviewViewModel =
                ViewModelProviders.of(this).get(OverviewViewModel.class);
        View root = inflater.inflate(R.layout.fragment_overview, container, false);
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

            }
        });
        //Click item in the list End
        btnIncome = (Button) root.findViewById(R.id.btnIncome);
        btnCost = (Button) root.findViewById(R.id.btnCost);
        btnLoan = (Button) root.findViewById(R.id.btnLoan);

        btnIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //Tab Income, Cost and Loan


        DB.close();
        return root;
    }

    private void getAllLoan(){
        loanList = DB.getAllLoans();
    }
}