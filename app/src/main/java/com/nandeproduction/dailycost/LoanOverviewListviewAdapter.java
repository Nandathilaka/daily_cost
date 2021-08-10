package com.nandeproduction.dailycost;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nandeproduction.dailycost.db.DBHelper;

import java.util.ArrayList;

public class LoanOverviewListviewAdapter extends BaseAdapter {
    public ArrayList<Loan> loanList;
    Activity activity;
    DBHelper DB;

    public LoanOverviewListviewAdapter(Activity activity, ArrayList<Loan> itemList) {
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
        TextView accName;
        TextView nextPayment;
        TextView nextPaymentDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final LoanOverviewListviewAdapter.ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            //convertView = inflater.inflate(R.layout.listview_row, null);
            convertView = inflater.inflate(R.layout.listview_overview_loan, parent,false);
            holder = new LoanOverviewListviewAdapter.ViewHolder();
            //holder.mId = (TextView) convertView.findViewById(R.id.id);
            holder.accNumber = (TextView) convertView.findViewById(R.id.accNumber);
            holder.accName = (TextView) convertView.findViewById(R.id.accName);
            holder.nextPayment = (TextView) convertView.findViewById(R.id.payment);
            holder.nextPaymentDate = (TextView) convertView.findViewById(R.id.nextPaymentDate);
            convertView.setTag(holder);
        } else {
            holder = (LoanOverviewListviewAdapter.ViewHolder) convertView.getTag();
        }

        Loan item = loanList.get(position);
        holder.accNumber.setText(item.getAccountNumber());
        holder.accName.setText(item.getAccountName());
        holder.nextPayment.setText((String.valueOf(item.getMonthlyPayment())));
        holder.nextPaymentDate.setText(DateConverter.setCalanderDate(item.getNextPaymentDate()));

        return convertView;
    }
}
