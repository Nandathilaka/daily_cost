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

import java.lang.annotation.Repeatable;
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

        final ViewHolder holder;
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
        holder.icon.setImageResource(R.drawable.delete);

        final View finalConvertView = convertView;
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println(holder.mTitle.getText() +"Icon click in listview already works!!");
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                System.out.println(holder.mTitle.getText() +" Deleted successfully!!");
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                System.out.println(holder.mTitle.getText() +" Deleted unsuccessfully!!");
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
}

