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
import java.util.Calendar;

import static com.nandeproduction.dailycost.ui.cost.CostFragment.adapter;
import static com.nandeproduction.dailycost.ui.cost.CostFragment.btnClear;
import static com.nandeproduction.dailycost.ui.cost.CostFragment.btnUpdate;
import static com.nandeproduction.dailycost.ui.cost.CostFragment.lblTotalCostThisMonthSum;
import static com.nandeproduction.dailycost.ui.cost.CostFragment.listView;
import static com.nandeproduction.dailycost.ui.cost.CostFragment.txtCostAmount;
import static com.nandeproduction.dailycost.ui.cost.CostFragment.txtCostDate;
import static com.nandeproduction.dailycost.ui.cost.CostFragment.txtCostTitle;

public class CostListviewAdapter extends BaseAdapter {

    public ArrayList<Cost> costList;
    Activity activity;
    DBHelper DB;

    public CostListviewAdapter(Activity activity, ArrayList<Cost> itemList) {
        super();
        this.activity = activity;
        this.costList = itemList;
    }

    @Override
    public int getCount() {
        return costList.size();
    }

    @Override
    public Object getItem(int position) {
        return costList.get(position);
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

        Cost item = costList.get(position);

        holder.mTitle.setText(item.getTitle().toString());
        //holder.mId.setText(Integer.valueOf(item.getId()).toString());
        holder.mAmount.setText(Integer.valueOf(item.getAmount()).toString());
        holder.mDate.setText(DateConverter.setCalanderDate(item.getDate().toString()));

        final View finalConvertView = convertView;
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println(position +" Deleted Position Item");
                Cost item = costList.get(position);
                final int itemId = item.getId();
                DB = new DBHelper(activity.getApplicationContext());
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                DB.hideCost(itemId,DB.getCurrentUserID());
                                costList.remove(position);
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

    private void getAllCurrentMonthCost(){
        DB = new DBHelper(activity.getApplicationContext());
        lblTotalCostThisMonthSum.setText(Long.valueOf(DB.CurrentMonthCost()).toString());
        //costList.clear();
        costList = DB.getAllCurrentMonthCosts();
        DB.close();
    }

    private void setDefaultUI(){
        Calendar calendar = Calendar.getInstance();
        //Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        String date = thisYear + "-" + (thisMonth+1) + "-" + thisDay;
        txtCostDate.setText(date);
        txtCostTitle.setText("");
        txtCostAmount.setText("");
        btnClear.setVisibility(View.INVISIBLE);
        btnUpdate.setVisibility(View.INVISIBLE);
        getAllCurrentMonthCost();
        adapter.costList = costList;
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
