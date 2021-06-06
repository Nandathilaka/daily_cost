package com.nandeproduction.dailycost.ui.overview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.nandeproduction.dailycost.R;
import com.nandeproduction.dailycost.db.DBHelper;

import org.w3c.dom.Text;

public class OverviewFragment extends Fragment {

    private OverviewViewModel overviewViewModel;
    TextView lblIncomeThisMonth;
    TextView lblCostThisMonth;
    TextView lblAssetThisMonth;
    TextView lblIncomeThisYear;
    TextView lblCostThisYear;
    TextView lblAssetThisYear;
    TextView lblNumberOfLoans;
    DBHelper DB;

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
        int numberofLoans = DB.numberOfLoansRows();
        lblNumberOfLoans.setText("Loans("+numberofLoans+")");

        return root;
    }
}