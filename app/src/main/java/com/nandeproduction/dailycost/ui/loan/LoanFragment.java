package com.nandeproduction.dailycost.ui.loan;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.nandeproduction.dailycost.DateConverter;
import com.nandeproduction.dailycost.Income;
import com.nandeproduction.dailycost.IncomeListviewAdapter;
import com.nandeproduction.dailycost.Loan;
import com.nandeproduction.dailycost.LoanListviewAdapter;
import com.nandeproduction.dailycost.R;
import com.nandeproduction.dailycost.db.DBHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

import static com.nandeproduction.dailycost.DateConverter.DateConvertToString;

public class LoanFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private LoanViewModel loanViewModel;
    public static EditText txtLoanAccount;
    public static EditText txtLoanAccountName;
    public static EditText txtLoanAmount;
    public static EditText txtLoanMonthlyPayment;
    public static EditText txtLoanInterestRate;
    public static EditText txtLoanOpenDate;
    public static EditText txtLoanMonths;
    public static TextView lblNumberOfLoans;
    public static TextView lblTotalLoans;
    DBHelper DB;
    public static Button btnSave,btnClear, btnUpdate;
    public static ArrayList<Loan> loanList;
    public static ListView listView;
    int itemID = 0;
    public static LoanListviewAdapter adapter = null;

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

        //Initialized Database
        DB = new DBHelper(getContext());
        //Get Current User ID
        final int userID = DB.getCurrentUserID();

        //Click item in the list Start
        loanList = new ArrayList<Loan>();
        listView = (ListView) root.findViewById(R.id.listView);
        getAllLoan();
        adapter = new LoanListviewAdapter(getActivity(), loanList);
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
                txtLoanAccount.setText(accountNumber);
                txtLoanMonthlyPayment.setText(payment);
                txtLoanOpenDate.setText(DateConverter.setCalanderDate(nextPaymentDate));
                btnUpdate.setVisibility(View.VISIBLE);
                btnClear.setVisibility(View.VISIBLE);

                Toast.makeText(getContext(),
                        "Account : " + accountNumber +"\n"
                                +"Payment : " +payment +"\n"
                                +"Next Payment Date : " +nextPaymentDate, Toast.LENGTH_SHORT).show();

            }
        });
        //Click item in the list End

        //Loan Start
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
                if(inputValidate()){
                    String accountNumber = String.valueOf(txtLoanAccount.getText());
                    String accountName = String.valueOf(txtLoanAccountName.getText());
                    long loanAmmount = Long.valueOf(txtLoanAmount.getText().toString());
                    int loanMonthlyPayment = Integer.valueOf(txtLoanMonthlyPayment.getText().toString());
                    String loanRate = String.valueOf(txtLoanInterestRate.getText());
                    String loanOpenDate = String.valueOf(txtLoanOpenDate.getText());
                    int loanNumberOfMonths = Integer.valueOf(txtLoanMonths.getText().toString());
                    int currentUserID = DB.getCurrentUserID();
                    Boolean insertLoan = DB.insertLoan(currentUserID,accountName,accountNumber,loanAmmount,loanMonthlyPayment,loanRate, DateConvertToString(loanOpenDate),loanNumberOfMonths,1);
                    if(insertLoan == true){
                        Toast.makeText(getContext(),"Loan Insert Successfully", Toast.LENGTH_SHORT).show();
                        defaultUI();
                    }else{
                        Toast.makeText(getContext(),"Loan Insert Unsuccessfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //Loan End

        //Update Start
        btnUpdate = root.findViewById(R.id.btnUpdate);
        btnUpdate.setVisibility(View.INVISIBLE);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputValidate()){
                    String accountNumber = String.valueOf(txtLoanAccount.getText());
                    String accountName = String.valueOf(txtLoanAccountName.getText());
                    long loanAmmount = Long.valueOf(txtLoanAmount.getText().toString());
                    long loanMonthlyPayment = Long.valueOf(txtLoanMonthlyPayment.getText().toString());
                    String loanRate = String.valueOf(txtLoanInterestRate.getText());
                    String loanOpenDate = String.valueOf(txtLoanOpenDate.getText());
                    int loanNumberOfMonths = Integer.valueOf(txtLoanMonths.getText().toString());
                    int currentUserID = DB.getCurrentUserID();
                    Boolean update = DB.updateLoan(itemID,accountName,accountNumber,loanAmmount, loanMonthlyPayment, loanRate,DateConvertToString(loanOpenDate), loanNumberOfMonths, 0,userID);
                    DB.close();
                    if(update){
                        defaultUI();
                        refreshList();
                        Toast.makeText(getContext(),"Income Update Successfully", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(),"Income Update Unsuccessfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //Update End

        //Clear Start
        btnClear = root.findViewById(R.id.btnClear);
        btnClear.setVisibility(View.INVISIBLE);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultUI();
            }
        });
        //Clear End

        //Validate only float and integer input to amount Start
        txtLoanAmount.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 10, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = txtLoanAmount.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        }
                        else if (temp.toString().indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });
        //Validate only float and integer input to amount End

        //Validate only float and integer input to amount Start
        txtLoanMonthlyPayment.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 10, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = txtLoanMonthlyPayment.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        }
                        else if (temp.toString().indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });
        //Validate only float and integer input to amount End

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

    private void getAllLoan(){
        loanList = DB.getAllLoans();
    }

    public void refreshList(){
        lblNumberOfLoans.setText("Loans("+DB.numberOfLoansRows()+")");
        lblTotalLoans.setText("Total Loans : " + DB.numberOfLoansRows());
        getAllLoan();
        LoanListviewAdapter refreshAdapter = new LoanListviewAdapter(getActivity(), loanList);
        listView.setAdapter(refreshAdapter);
        refreshAdapter.notifyDataSetChanged();
    }

    private void defaultUI(){
        DB = new DBHelper(getContext());
        txtLoanAccount.setText("");
        txtLoanAccountName.setText("");
        txtLoanAmount.setText("");
        txtLoanMonthlyPayment.setText("");
        txtLoanInterestRate.setText("");
        txtLoanOpenDate.setText("");
        txtLoanMonths.setText("");
        lblNumberOfLoans.setText("Loans("+DB.numberOfLoansRows()+")");
        lblTotalLoans.setText("Total Loans : " + DB.numberOfLoansRows());
    }

    private Boolean inputValidate(){
        Boolean isformValid = true;
        if(txtLoanAccount.getText().toString().length() == 0){
            isformValid = false;
            txtLoanAccount.setFocusable(true);
            txtLoanAccount.requestFocus();
            Toast.makeText(getContext(), "Please enter account number", Toast.LENGTH_SHORT).show();
        }else if(txtLoanAccountName.getText().toString().length() == 0){
            isformValid = false;
            txtLoanAccountName.setFocusable(true);
            txtLoanAccountName.requestFocus();
            Toast.makeText(getContext(),"Please enter account name", Toast.LENGTH_SHORT).show();
        }else if(txtLoanAmount.getText().toString().length() == 0){
            isformValid = false;
            txtLoanAmount.setFocusable(true);
            txtLoanAmount.requestFocus();
            Toast.makeText(getContext(),"Please enter loan amount", Toast.LENGTH_SHORT).show();
        }else if(txtLoanMonthlyPayment.getText().toString().length() == 0){
            isformValid = false;
            txtLoanMonthlyPayment.setFocusable(true);
            txtLoanMonthlyPayment.requestFocus();
            Toast.makeText(getContext(),"Please enter monthly payment", Toast.LENGTH_SHORT).show();
        }else if(txtLoanInterestRate.getText().toString().length() == 0){
            isformValid = false;
            txtLoanInterestRate.setFocusable(true);
            txtLoanInterestRate.requestFocus();
            Toast.makeText(getContext(),"Please enter interest rate", Toast.LENGTH_SHORT).show();
        }else if(txtLoanOpenDate.getText().toString().length() == 0){
            isformValid = false;
            txtLoanOpenDate.setFocusable(true);
            txtLoanOpenDate.requestFocus();
            Toast.makeText(getContext(),"Please enter open date", Toast.LENGTH_SHORT).show();
        }else if(txtLoanMonths.getText().toString().length() == 0){
            isformValid = false;
            txtLoanMonths.setFocusable(true);
            txtLoanMonths.requestFocus();
            Toast.makeText(getContext(),"Please enter number of period(months)", Toast.LENGTH_SHORT).show();
        }else{
            isformValid = true;
        }
        return isformValid;
    }

}