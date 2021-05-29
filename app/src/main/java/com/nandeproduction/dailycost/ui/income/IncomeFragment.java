package com.nandeproduction.dailycost.ui.income;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.nandeproduction.dailycost.MainActivity;
import com.nandeproduction.dailycost.R;
import com.nandeproduction.dailycost.db.DBHelper;

import java.util.Calendar;
import java.util.TimeZone;

public class IncomeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private IncomeViewModel incomeViewModel;

    EditText txtIncomeDate;
    EditText txtIncomeTitle;
    EditText txtIncomeAmount;
    DBHelper DB;
    Button btnSave;

    public View onCreateView(@NonNull LayoutInflater inflater,
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

        //Income Start
        DB = new DBHelper(getContext());
        txtIncomeTitle = root.findViewById(R.id.txtIncomeTitle);
        txtIncomeAmount = root.findViewById(R.id.txtIncomeAmount);
        txtIncomeDate = root.findViewById(R.id.txtIncomeDate);
        btnSave = root.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = String.valueOf(txtIncomeTitle.getText());
                int ammount = Integer.valueOf(txtIncomeAmount.getText().toString());
                String date = String.valueOf(txtIncomeDate.getText());
                int currentUserID = DB.getCurrentUserID();
                Boolean insertIncome = DB.insertIncome(currentUserID,title,ammount,date);
                if(insertIncome == true){
                    Toast.makeText(getContext(),"Income Insert Successfully", Toast.LENGTH_SHORT).show();
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
        String date = year + "/" + month + "/" + dayOfMonth;
        txtIncomeDate.setText(date);
    }

    public void setCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        //Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        String date = thisYear + "/" + thisMonth + "/" + thisDay;
        txtIncomeDate.setText(date);
    }
}