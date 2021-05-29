package com.nandeproduction.dailycost.ui.cost;

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

import com.nandeproduction.dailycost.R;
import com.nandeproduction.dailycost.db.DBHelper;

import java.util.Calendar;

public class CostFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private CostViewModel costViewModel;
    EditText txtCostDate;
    EditText txtCostTitle;
    EditText txtCostAmount;
    DBHelper DB;
    Button btnSave;

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

        //Date Start
        txtCostDate=(EditText) root.findViewById(R.id.txtCostDate);
        txtCostDate.setInputType(InputType.TYPE_NULL);
        //Set current date default
        setCurrentDate();
        txtCostDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        //Date End

        //Cost Start
        DB = new DBHelper(getContext());
        txtCostTitle = root.findViewById(R.id.txtCostTitle);
        txtCostAmount = root.findViewById(R.id.txtCostAmount);
        txtCostDate = root.findViewById(R.id.txtCostDate);
        btnSave = root.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = String.valueOf(txtCostTitle.getText());
                int ammount = Integer.valueOf(txtCostAmount.getText().toString());
                String date = String.valueOf(txtCostDate.getText());
                int currentUserID = DB.getCurrentUserID();
                Boolean insertCost = DB.insertCost(currentUserID,title,ammount,date);
                if(insertCost == true){
                    Toast.makeText(getContext(),"Cost Insert Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Cost End
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
        String date = year + "/" + month + "/" + dayOfMonth;
        txtCostDate.setText(date);
    }

    public void setCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        //Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        String date = thisYear + "/" + thisMonth + "/" + thisDay;
        txtCostDate.setText(date);
    }
}