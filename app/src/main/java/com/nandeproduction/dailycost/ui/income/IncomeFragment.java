package com.nandeproduction.dailycost.ui.income;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.nandeproduction.dailycost.R;
import com.nandeproduction.dailycost.db.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.R.*;
import static android.R.layout.simple_list_item_1;

public class IncomeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private IncomeViewModel incomeViewModel;

    EditText txtIncomeDate;
    EditText txtIncomeTitle;
    EditText txtIncomeAmount;
    TextView lblTotalIncomeThisMonthSum;
    DBHelper DB;
    Button btnSave,btnUpdate,btnClear;
    ListView listView;
    private ArrayList<Income> incomeList;
    int itemID = 0;

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
        getAllCurrentMonthIncime();
        final IncomeListviewAdapter adapter = new IncomeListviewAdapter(getActivity(), incomeList);
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
                /*
                Toast.makeText(getContext(),
                        "Income Id : " + incomeId +"\n"
                                +"Title : " + incomeTitle +"\n"
                                +"Amount : " +incomeAmount +"\n"
                                +"Date : " +incomeDate, Toast.LENGTH_SHORT).show();
                */

                Toast.makeText(getContext(),
                        "Title : " + incomeTitle +"\n"
                                +"Amount : " +incomeAmount +"\n"
                                +"Date : " +incomeDate, Toast.LENGTH_SHORT).show();

            }
        });
        //Click item in the list End

        //Income Start
        lblTotalIncomeThisMonthSum = root.findViewById(R.id.lblTotalIncomeThisMonthSum);
        lblTotalIncomeThisMonthSum.setText(Long.valueOf(DB.CurrentMonthIncome()).toString());
        txtIncomeTitle = root.findViewById(R.id.txtIncomeTitle);
        txtIncomeAmount = root.findViewById(R.id.txtIncomeAmount);
        txtIncomeDate = root.findViewById(R.id.txtIncomeDate);
        btnSave = root.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = String.valueOf(txtIncomeTitle.getText());
                long ammount = (long)Double.parseDouble(txtIncomeAmount.getText().toString());
                String date = String.valueOf(txtIncomeDate.getText());
                int currentUserID = DB.getCurrentUserID();
                Boolean insertIncome = DB.insertIncome(currentUserID,title,ammount, DateConverter.DateConvertToString(date));
                DB.close();
                if(insertIncome == true){
                    refreshList();
                    Toast.makeText(getContext(),"Income Insert Successfully", Toast.LENGTH_SHORT).show();
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
                String updatedTitle = txtIncomeTitle.getText().toString();
                Long updatedAmount = Long.valueOf(txtIncomeAmount.getText().toString());
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


        /*
        //List Income Start
        listView = (ListView)root.findViewById(R.id.listView);
        ArrayList<Income> incomeList = new ArrayList<Income>();
        incomeList = getAllCurrentMonthIncime();
        List<String> your_array_list = new ArrayList<String>();
        your_array_list.add("2021-06-05"+"     "+"5000"+"       "+"View");
        your_array_list.add("bar");
        //IncomeAdapter adapter = new IncomeAdapter((Activity) getContext(),incomeList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item_1, your_array_list);
        listView.setAdapter(adapter);
        //List Income End
        */



        /*
        //Click item in the list Start
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
            }
        });
        //Click item in the list End
        */
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

    private void getAllCurrentMonthIncime(){
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

    }

    private void refreshList(){
        lblTotalIncomeThisMonthSum.setText(Long.valueOf(DB.CurrentMonthIncome()).toString());
        getAllCurrentMonthIncime();
        IncomeListviewAdapter refreshAdapter = new IncomeListviewAdapter(getActivity(), incomeList);
        listView.setAdapter(refreshAdapter);
        refreshAdapter.notifyDataSetChanged();
    }
}