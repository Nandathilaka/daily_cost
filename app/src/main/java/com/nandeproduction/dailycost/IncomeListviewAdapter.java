package com.nandeproduction.dailycost;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class IncomeListviewAdapter extends BaseAdapter {

    public ArrayList<Income> incomeList;
    Activity activity;

    public IncomeListviewAdapter(Activity activity, ArrayList<Income> itemList) {
        super();
        this.activity = activity;
        this.incomeList = itemList;
    }

    @Override
    public int getCount() {
        return incomeList.size();
    }

    @Override
    public Object getItem(int position) {
        return incomeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder {
        //TextView mId;
        TextView mDate;
        TextView mTitle;
        TextView mAmount;
        ImageView icon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_row, null);
            holder = new ViewHolder();
            //holder.mId = (TextView) convertView.findViewById(R.id.id);
            holder.mTitle = (TextView) convertView.findViewById(R.id.title);
            holder.mAmount = (TextView) convertView.findViewById(R.id.amount);
            holder.mDate = (TextView) convertView.findViewById(R.id.date);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Income item = incomeList.get(position);

        holder.mTitle.setText(item.getTitle().toString());
        //holder.mId.setText(Integer.valueOf(item.getId()).toString());
        holder.mAmount.setText(Integer.valueOf(item.getAmount()).toString());
        holder.mDate.setText(DateConverter.setCalanderDate(item.getDate().toString()));
        holder.icon.setImageResource(R.drawable.ic_menu_send);

        return convertView;
    }
}

