package com.nandeproduction.dailycost;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nandeproduction.dailycost.db.DBHelper;

import java.util.ArrayList;

public class BottomLoanListviewAdapter extends BaseAdapter {
    public ArrayList<LoanPeriod> loanList;
    Activity activity;
    DBHelper DB;

    public BottomLoanListviewAdapter(Activity activity, ArrayList<LoanPeriod> itemList) {
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
        TextView id;
        TextView payment;
        Button paid;
        Button delete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final BottomLoanListviewAdapter.ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            //convertView = inflater.inflate(R.layout.listview_row, null);
            convertView = inflater.inflate(R.layout.listview_bottom_loan, parent,false);
            holder = new BottomLoanListviewAdapter.ViewHolder();
            //holder.mId = (TextView) convertView.findViewById(R.id.id);
            holder.id = (TextView) convertView.findViewById(R.id.id);
            holder.payment = (TextView) convertView.findViewById(R.id.payment);
            holder.paid = (Button) convertView.findViewById(R.id.paid);
            holder.delete = (Button) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (BottomLoanListviewAdapter.ViewHolder) convertView.getTag();
        }

        LoanPeriod item = loanList.get(position);
        holder.id.setText(String.valueOf(item.getLoan_installment_id()));
        holder.payment.setText(String.valueOf((int) item.getLoan_installment()));
        holder.paid.setText(item.getStatus());

        return convertView;
    }
}
