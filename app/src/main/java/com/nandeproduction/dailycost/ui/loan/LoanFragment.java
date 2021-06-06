package com.nandeproduction.dailycost.ui.loan;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.nandeproduction.dailycost.DateConverter;
import com.nandeproduction.dailycost.R;
import com.nandeproduction.dailycost.db.DBHelper;

import org.w3c.dom.Text;

import java.util.Calendar;

public class LoanFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private LoanViewModel loanViewModel;

    EditText txtLoanAccount;
    EditText txtLoanAccountName;
    EditText txtLoanAmount;
    EditText txtLoanMonthlyPayment;
    EditText txtLoanInterestRate;
    EditText txtLoanOpenDate;
    EditText txtLoanMonths;
    TextView lblNumberOfLoans;
    TextView lblTotalLoans;
    DBHelper DB;
    Button btnSave;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loanViewModel =
                ViewModelProviders.of(this).get(LoanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_loan, container, false);
        final TextView textView = root.findViewById(R.id.text_loan);
        loanViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //Date Start
        txtLoanOpenDate=(EditText) root.findViewById(R.id.txtLoanOpenDate);
        txtLoanOpenDate.setInputType(InputType.TYPE_NULL);
        txtLoanOpenDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        //Date End

        //Income Start
        DB = new DBHelper(getContext());
        txtLoanAccount = root.findViewById(R.id.txtLoanAccount);
        txtLoanAccountName = root.findViewById(R.id.txtLoanAccountName);
        txtLoanAmount = root.findViewById(R.id.txtLoanAmount);
        txtLoanMonthlyPayment = root.findViewById(R.id.txtLoanMonthlyPayment);
        txtLoanInterestRate = root.findViewById(R.id.txtLoanInterestRate);
        txtLoanOpenDate = root.findViewById(R.id.txtLoanOpenDate);
        txtLoanMonths = root.findViewById(R.id.txtLoanMonths);
        btnSave = root.findViewById(R.id.btnSave);
        lblNumberOfLoans = root.findViewById(R.id.lblNumberOfLoans);
        lblTotalLoans = root.findViewById(R.id.lblTotalLoans);
        lblNumberOfLoans.setText("Loans("+DB.numberOfLoansRows()+")");
        lblTotalLoans.setText("Total Loans : " + DB.numberOfLoansRows());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String accountNumber = String.valueOf(txtLoanAccount.getText());
                String accountName = String.valueOf(txtLoanAccountName.getText());
                long loanAmmount = Long.valueOf(txtLoanAmount.getText().toString());
                int loanMonthlyPayment = Integer.valueOf(txtLoanMonthlyPayment.getText().toString());
                String loanRate = String.valueOf(txtLoanInterestRate.getText());
                String loanOpenDate = String.valueOf(txtLoanOpenDate.getText());
                int loanNumberOfMonths = Integer.valueOf(txtLoanMonths.getText().toString());
                int currentUserID = DB.getCurrentUserID();
                Boolean insertLoan = DB.insertLoan(currentUserID,accountName,accountNumber,loanAmmount,loanMonthlyPayment, DateConverter.DateConvertToString(loanRate),loanOpenDate,loanNumberOfMonths,1);
                if(insertLoan == true){
                    Toast.makeText(getContext(),"Loan Insert Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Income End
        return root;
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = year + "-" + (month+1) + "-" + dayOfMonth;
        txtLoanOpenDate.setText(date);
    }

}