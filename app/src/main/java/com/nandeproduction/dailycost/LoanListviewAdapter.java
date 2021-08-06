package com.nandeproduction.dailycost;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nandeproduction.dailycost.db.DBHelper;

import java.util.ArrayList;

import static com.nandeproduction.dailycost.ui.loan.LoanFragment.adapter;
import static com.nandeproduction.dailycost.ui.loan.LoanFragment.btnClear;
import static com.nandeproduction.dailycost.ui.loan.LoanFragment.btnUpdate;
import static com.nandeproduction.dailycost.ui.loan.LoanFragment.lblNumberOfLoans;
import static com.nandeproduction.dailycost.ui.loan.LoanFragment.lblTotalLoans;
import static com.nandeproduction.dailycost.ui.loan.LoanFragment.listView;
import static com.nandeproduction.dailycost.ui.loan.LoanFragment.txtLoanAccount;
import static com.nandeproduction.dailycost.ui.loan.LoanFragment.txtLoanAccountName;
import static com.nandeproduction.dailycost.ui.loan.LoanFragment.txtLoanAmount;
import static com.nandeproduction.dailycost.ui.loan.LoanFragment.txtLoanMonthlyPayment;
import static com.nandeproduction.dailycost.ui.loan.LoanFragment.txtLoanInterestRate;
import static com.nandeproduction.dailycost.ui.loan.LoanFragment.txtLoanOpenDate;
import static com.nandeproduction.dailycost.ui.loan.LoanFragment.txtLoanMonths;

public class LoanListviewAdapter extends BaseAdapter {
    public ArrayList<Loan> loanList;
    Activity activity;
    DBHelper DB;

    public LoanListviewAdapter(Activity activity, ArrayList<Loan> itemList) {
        super();
        this.activity = activity;
        this.loanList = itemList;
    }

    @Override
    public int getCount() {
        return loanList.size();
    }

    @Override
    public Object getItem(int position) {
        return loanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder {
        //TextView mId;
        TextView accNumber;
        TextView payment;
        TextView nextPaymentDate;
        ImageView icon;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final LoanListviewAdapter.ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            //convertView = inflater.inflate(R.layout.listview_row, null);
            convertView = inflater.inflate(R.layout.listview_loan, parent,false);
            holder = new LoanListviewAdapter.ViewHolder();
            //holder.mId = (TextView) convertView.findViewById(R.id.id);
            holder.accNumber = (TextView) convertView.findViewById(R.id.accNumber);
            holder.payment = (TextView) convertView.findViewById(R.id.payment);
            holder.nextPaymentDate = (TextView) convertView.findViewById(R.id.nextPaymentDate);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (LoanListviewAdapter.ViewHolder) convertView.getTag();
        }

        Loan item = loanList.get(position);

        holder.accNumber.setText(item.getAccountNumber().toString());
        //holder.mId.setText(Integer.valueOf(item.getId()).toString());
        holder.payment.setText(Double.valueOf(String.valueOf(item.getMonthlyPayment())).toString());
        holder.nextPaymentDate.setText(DateConverter.setCalanderDate(item.getOpenDate().toString()));


        final View finalConvertView = convertView;
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println(position +" Deleted Position Item");
                Loan item = loanList.get(position);
                final int itemId = item.getId();
                DB = new DBHelper(activity.getApplicationContext());
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                DB.hideLoan(itemId,DB.getCurrentUserID());
                                loanList.remove(position);
                                setDefaultUI();
                                Toast.makeText(activity.getApplicationContext(),"Delete Item Successfully", Toast.LENGTH_SHORT).show();
                                System.out.println(itemId +" Deleted successfully!!");
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                System.out.println(itemId +" Deleted unsuccessfully!!");
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(finalConvertView.getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        return convertView;
    }

    private void getAllLoan(){
        DB = new DBHelper(activity.getApplicationContext());
        int numberOfLoan = DB.numberOfActiveLoansRows();
        lblNumberOfLoans.setText("Loans("+numberOfLoan+")");
        lblTotalLoans.setText("Total Loans : " + numberOfLoan);
        //lblTotalLoans.setText(Long.valueOf(DB.numberOfLoansRows()).toString());
        //lblNumberOfLoans.setText(Long.valueOf(DB.numberOfRows()).toString());
        //incomeList.clear();
        loanList = DB.getAllLoans();
        DB.close();
    }

    private void setDefaultUI(){
        //Calendar calendar = Calendar.getInstance();
        ////Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        //int thisYear = calendar.get(Calendar.YEAR);
        //int thisMonth = calendar.get(Calendar.MONTH);
        //int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        //String date = thisYear + "-" + (thisMonth+1) + "-" + thisDay;
        txtLoanAccount.setText("");
        txtLoanAccountName.setText("");
        txtLoanAmount.setText("");
        txtLoanMonthlyPayment.setText("");
        txtLoanInterestRate.setText("");
        txtLoanOpenDate.setText("");
        txtLoanMonths.setText("");
        btnClear.setVisibility(View.INVISIBLE);
        btnUpdate.setVisibility(View.INVISIBLE);
        getAllLoan();
        adapter.loanList = loanList;
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
