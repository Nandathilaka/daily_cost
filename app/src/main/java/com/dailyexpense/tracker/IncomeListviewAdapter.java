package com.dailyexpense.tracker;

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

import com.dailyexpense.tracker.db.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;

import static com.dailyexpense.tracker.ui.income.IncomeFragment.adapter;
import static com.dailyexpense.tracker.ui.income.IncomeFragment.btnClear;
import static com.dailyexpense.tracker.ui.income.IncomeFragment.btnUpdate;
import static com.dailyexpense.tracker.ui.income.IncomeFragment.lblTotalIncomeThisMonthSum;
import static com.dailyexpense.tracker.ui.income.IncomeFragment.listView;
import static com.dailyexpense.tracker.ui.income.IncomeFragment.txtIncomeAmount;
import static com.dailyexpense.tracker.ui.income.IncomeFragment.txtIncomeDate;
import static com.dailyexpense.tracker.ui.income.IncomeFragment.txtIncomeTitle;

public class IncomeListviewAdapter extends BaseAdapter {

    public ArrayList<Income> incomeList;
    Activity activity;
    DBHelper DB;

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
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            //convertView = inflater.inflate(R.layout.listview_row, null);
            convertView = inflater.inflate(R.layout.listview_row, parent,false);
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
        holder.mAmount.setText(item.getAmount());
        holder.mDate.setText(DateConverter.setCalanderDate(item.getDate().toString()));

        final View finalConvertView = convertView;
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println(position +" Deleted Position Item");
                Income item = incomeList.get(position);
                final int itemId = item.getId();
                DB = new DBHelper(activity.getApplicationContext());
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                DB.hideIncome(itemId,DB.getCurrentUserID());
                                incomeList.remove(position);
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

    private void getAllCurrentMonthIncome(){
        DB = new DBHelper(activity.getApplicationContext());
        //lblTotalIncomeThisMonthSum.setText(Long.valueOf(DB.CurrentMonthIncome()).toString());
        lblTotalIncomeThisMonthSum.setText(String.format("%.2f",DB.CurrentMonthIncome()));
        //incomeList.clear();
        incomeList = DB.getAllCurrentMonthIncomes();
        DB.close();
    }

    private void setDefaultUI(){
        Calendar calendar = Calendar.getInstance();
        //Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        String date = thisYear + "-" + (thisMonth+1) + "-" + thisDay;
        txtIncomeDate.setText(date);
        txtIncomeTitle.setText("");
        txtIncomeAmount.setText("");
        btnClear.setVisibility(View.INVISIBLE);
        btnUpdate.setVisibility(View.INVISIBLE);
        getAllCurrentMonthIncome();
        adapter.incomeList = incomeList;
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

