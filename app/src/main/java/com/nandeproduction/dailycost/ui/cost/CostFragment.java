package com.nandeproduction.dailycost.ui.cost;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.analytics.Tracker;
import com.nandeproduction.dailycost.AnalyticsApplication;
import com.nandeproduction.dailycost.Cost;
import com.nandeproduction.dailycost.CostListviewAdapter;
import com.nandeproduction.dailycost.DateConverter;
import com.nandeproduction.dailycost.R;
import com.nandeproduction.dailycost.db.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CostFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private CostViewModel costViewModel;
    public static EditText txtCostDate;
    public static EditText txtCostTitle;
    public static EditText txtCostAmount;
    public static TextView lblTotalCostThisMonthSum;
    DBHelper DB;
    public static Button btnSave,btnUpdate,btnClear;
    public static ListView listView;
    ImageView icon;
    public static ArrayList<Cost> costList;
    int itemID = 0;
    public static CostListviewAdapter adapter = null;
    private Tracker mTracker;//Google Analytics

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        costViewModel =
                ViewModelProviders.of(this).get(CostViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cost, container, false);
        final TextView textView = root.findViewById(R.id.text_cost);
        costViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //Google Analytics Start
        // Obtain the shared Tracker instance.
        //AnalyticsApplication application = (AnalyticsApplication) getContext();
        //mTracker = application.getDefaultTracker();
        //Google Analytics End

        //Date Start
        txtCostDate=(EditText) root.findViewById(R.id.txtCostDate);
        txtCostDate.setInputType(InputType.TYPE_NULL);
        txtCostDate.setFocusableInTouchMode(true);
        txtCostDate.setFocusable(false);
        //Set current date default
        setCurrentDate();
        txtCostDate.setOnClickListener(new View.OnClickListener() {
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
        costList = new ArrayList<Cost>();
        listView = (ListView) root.findViewById(R.id.listView);
        getAllCurrentMonthCost();
        adapter = new CostListviewAdapter(getActivity(), costList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cost selecteedCost = costList.get(position);
                itemID = selecteedCost.getId();
                //String incomeId = ((TextView)view.findViewById(R.id.id)).getText().toString();
                String costTitle = ((TextView)view.findViewById(R.id.title)).getText().toString();
                String costAmount = ((TextView)view.findViewById(R.id.amount)).getText().toString();
                String costDate = ((TextView)view.findViewById(R.id.date)).getText().toString();
                txtCostTitle.setText(costTitle);
                txtCostAmount.setText(costAmount);
                txtCostDate.setText(DateConverter.setCalanderDate(costDate));
                btnUpdate.setVisibility(View.VISIBLE);
                btnClear.setVisibility(View.VISIBLE);

                Toast.makeText(getContext(),
                        "Title : " + costTitle +"\n"
                                +"Amount : " + costAmount +"\n"
                                +"Date : " + costDate, Toast.LENGTH_SHORT).show();

            }
        });
        //Click item in the list End

        //Cost Start
        lblTotalCostThisMonthSum = root.findViewById(R.id.lblTotalCostThisMonthSum);
        lblTotalCostThisMonthSum.setText(getCurrentMonthCost());
        txtCostTitle = root.findViewById(R.id.txtCostTitle);
        txtCostAmount = root.findViewById(R.id.txtCostAmount);
        txtCostDate = root.findViewById(R.id.txtCostDate);
        btnSave = root.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputValidate()){
                    String title = String.valueOf(txtCostTitle.getText());
                    double ammount = Double.parseDouble(txtCostAmount.getText().toString());
                    String date = String.valueOf(txtCostDate.getText());
                    int currentUserID = DB.getCurrentUserID();
                    Boolean insertCost = DB.insertCost(currentUserID,title,ammount, DateConverter.DateConvertToString(date));
                    DB.close();
                    if(insertCost == true){
                        defualtUI();
                        refreshList();
                        Toast.makeText(getContext(),"Cost Insert Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //Cost End

        //Update Start
        btnUpdate = root.findViewById(R.id.btnUpdate);
        btnUpdate.setVisibility(View.INVISIBLE);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputValidate()){
                    String updatedTitle = txtCostTitle.getText().toString();
                    double updatedAmount = Double.valueOf(txtCostAmount.getText().toString());
                    String updatedDate = txtCostDate.getText().toString();
                    Boolean update = DB.updateCost(itemID,updatedTitle,updatedAmount,DateConverter.DateConvertToString(updatedDate),userID);
                    DB.close();
                    if(update){
                        defualtUI();
                        refreshList();
                        Toast.makeText(getContext(),"Cost Update Successfully", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(),"Cost Update Unsuccessfully", Toast.LENGTH_SHORT).show();
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
        txtCostAmount.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 10, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = txtCostAmount.getText() + source.toString();

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
        txtCostDate.setText(date);
    }

    private void setCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        //Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        String date = thisYear + "-" + (thisMonth+1) + "-" + thisDay;
        txtCostDate.setText(date);
    }

    private String getCurrentMonthCost(){
        DB = new DBHelper(getContext());
        //long currentMonthCost = DB.CurrentMonthCost();
        double currentMonthCost = DB.CurrentMonthCost();
        DB.close();
        return String.format("%.2f",currentMonthCost) ;
    }

    private void getAllCurrentMonthCost(){
        //ArrayList<Income> arrayList = new ArrayList<Income>();
        DB = new DBHelper(getContext());
        costList = DB.getAllCurrentMonthCosts();
        DB.close();
        //return arrayList;
    }

    private void defualtUI(){
        txtCostTitle.setText("");
        txtCostAmount.setText("");
        setCurrentDate();
        btnUpdate.setVisibility(View.INVISIBLE);
        btnClear.setVisibility(View.INVISIBLE);
        txtCostTitle.requestFocus();
    }

    public void refreshList(){
        lblTotalCostThisMonthSum.setText(String.format("%.2f",DB.CurrentMonthCost()));
        getAllCurrentMonthCost();
        CostListviewAdapter refreshAdapter = new CostListviewAdapter(getActivity(), costList);
        listView.setAdapter(refreshAdapter);
        refreshAdapter.notifyDataSetChanged();
    }

    private Boolean inputValidate(){
        Boolean isformValid = false;
        if(txtCostTitle.getText().toString().length() > 0){
            isformValid = true;
            if(txtCostAmount.getText().toString().length() > 0){
                isformValid = true;
            }else {
                isformValid = false;
                txtCostAmount.setFocusable(true);
                txtCostAmount.requestFocus();
                Toast.makeText(getContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
            }
        }else{
            isformValid = false;
            txtCostTitle.setFocusable(true);
            txtCostTitle.requestFocus();
            Toast.makeText(getContext(),"Please enter title", Toast.LENGTH_SHORT).show();
        }
        return isformValid;
    }
}