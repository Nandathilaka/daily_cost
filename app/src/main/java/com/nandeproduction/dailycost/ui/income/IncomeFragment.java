package com.nandeproduction.dailycost.ui.income;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.nandeproduction.dailycost.R;
import com.nandeproduction.dailycost.db.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.R.*;
import static android.R.layout.simple_list_item_1;

public class IncomeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private IncomeViewModel incomeViewModel;
    public static EditText txtIncomeDate;
    public static EditText txtIncomeTitle;
    public static EditText txtIncomeAmount;
    public static TextView lblTotalIncomeThisMonthSum;
    DBHelper DB;
    public static Button btnSave,btnUpdate,btnClear;
    public static ListView listView;
    ImageView icon;
    public static ArrayList<Income> incomeList;
    int itemID = 0;
    public static IncomeListviewAdapter adapter = null;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        incomeViewModel =
                ViewModelProviders.of(this).get(IncomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_income, container, false);
        final TextView textView = root.findViewById(R.id.text_income);
        incomeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //Date Start
        txtIncomeDate=(EditText) root.findViewById(R.id.txtIncomeDate);
        txtIncomeDate.setInputType(InputType.TYPE_NULL);
        txtIncomeDate.setFocusableInTouchMode(true);
        txtIncomeDate.setFocusable(false);
        //Set current date default
        setCurrentDate();
        txtIncomeDate.setOnClickListener(new View.OnClickListener() {
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
        incomeList = new ArrayList<Income>();
        listView = (ListView) root.findViewById(R.id.listView);
        getAllCurrentMonthIncome();
        adapter = new IncomeListviewAdapter(getActivity(), incomeList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Income selecteedIncome = incomeList.get(position);
                itemID = selecteedIncome.getId();
                //String incomeId = ((TextView)view.findViewById(R.id.id)).getText().toString();
                String incomeTitle = ((TextView)view.findViewById(R.id.title)).getText().toString();
                String incomeAmount = ((TextView)view.findViewById(R.id.amount)).getText().toString();
                String incomeDate = ((TextView)view.findViewById(R.id.date)).getText().toString();
                txtIncomeTitle.setText(incomeTitle);
                txtIncomeAmount.setText(incomeAmount);
                txtIncomeDate.setText(DateConverter.setCalanderDate(incomeDate));
                btnUpdate.setVisibility(View.VISIBLE);
                btnClear.setVisibility(View.VISIBLE);

                Toast.makeText(getContext(),
                        "Title : " + incomeTitle +"\n"
                                +"Amount : " +incomeAmount +"\n"
                                +"Date : " +incomeDate, Toast.LENGTH_SHORT).show();

            }
        });
        //Click item in the list End

        //Income Start
        lblTotalIncomeThisMonthSum = root.findViewById(R.id.lblTotalIncomeThisMonthSum);
        lblTotalIncomeThisMonthSum.setText(getCurrentMonthIncome());
        txtIncomeTitle = root.findViewById(R.id.txtIncomeTitle);
        txtIncomeAmount = root.findViewById(R.id.txtIncomeAmount);
        txtIncomeDate = root.findViewById(R.id.txtIncomeDate);
        btnSave = root.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputValidate()){
                    String title = String.valueOf(txtIncomeTitle.getText());
                    double ammount = Double.parseDouble(txtIncomeAmount.getText().toString());
                    String date = String.valueOf(txtIncomeDate.getText());
                    int currentUserID = DB.getCurrentUserID();
                    Boolean insertIncome = DB.insertIncome(currentUserID,title,ammount, DateConverter.DateConvertToString(date));
                    DB.close();
                    if(insertIncome == true){
                        defualtUI();
                        refreshList();
                        Toast.makeText(getContext(),"Income Insert Successfully", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        //Income End

        //Update Start
        btnUpdate = root.findViewById(R.id.btnUpdate);
        btnUpdate.setVisibility(View.INVISIBLE);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputValidate()){
                    String updatedTitle = txtIncomeTitle.getText().toString();
                    double updatedAmount = Double.valueOf(txtIncomeAmount.getText().toString());
                    String updatedDate = txtIncomeDate.getText().toString();
                    Boolean update = DB.updateIncome(itemID,updatedTitle,updatedAmount,DateConverter.DateConvertToString(updatedDate),userID);
                    DB.close();
                    if(update){
                        defualtUI();
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
                defualtUI();
            }
        });
        //Clear End

        //Validate only float and integer input to amount Start
        txtIncomeAmount.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 10, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = txtIncomeAmount.getText() + source.toString();

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
        txtIncomeDate.setText(date);
    }

    private void setCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        //Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        String date = thisYear + "-" + (thisMonth+1) + "-" + thisDay;
        txtIncomeDate.setText(date);
    }

    private String getCurrentMonthIncome(){
        DB = new DBHelper(getContext());
        //long currentMonthIncome = DB.CurrentMonthIncome();
        double currentMonthIncome = DB.CurrentMonthIncome();
        DB.close();
        return String.format("%.2f",currentMonthIncome);
    }

    private void getAllCurrentMonthIncome(){
        //ArrayList<Income> arrayList = new ArrayList<Income>();
        DB = new DBHelper(getContext());
        incomeList = DB.getAllCurrentMonthIncomes();
        DB.close();
        //return arrayList;
    }

    private void defualtUI(){
        txtIncomeTitle.setText("");
        txtIncomeAmount.setText("");
        setCurrentDate();
        btnUpdate.setVisibility(View.INVISIBLE);
        btnClear.setVisibility(View.INVISIBLE);
        txtIncomeTitle.requestFocus();
    }

    public void refreshList(){
        //lblTotalIncomeThisMonthSum.setText(Long.valueOf(DB.CurrentMonthIncome()).toString());
        lblTotalIncomeThisMonthSum.setText(String.format("%.2f",DB.CurrentMonthIncome()));
        getAllCurrentMonthIncome();
        IncomeListviewAdapter refreshAdapter = new IncomeListviewAdapter(getActivity(), incomeList);
        listView.setAdapter(refreshAdapter);
        refreshAdapter.notifyDataSetChanged();
    }

    private Boolean inputValidate(){
        Boolean isformValid = false;
        if(txtIncomeTitle.getText().toString().length() > 0){
            isformValid = true;
            if(txtIncomeAmount.getText().toString().length() > 0){
                isformValid = true;
            }else {
                isformValid = false;
                txtIncomeAmount.setFocusable(true);
                txtIncomeAmount.requestFocus();
                Toast.makeText(getContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
            }
        }else{
            isformValid = false;
            txtIncomeTitle.setFocusable(true);
            txtIncomeTitle.requestFocus();
            Toast.makeText(getContext(),"Please enter title", Toast.LENGTH_SHORT).show();
        }
        return isformValid;
    }
}