package com.nandeproduction.dailycost;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
// Creating a custom adapter by extending ArrayAdapter with Income data
public class TestAdapter extends ArrayAdapter<Income> {
    // Set a custom constructer
    public TestAdapter(Activity context, ArrayList<Income> incomeArray) {
        // The second parameter is 0 as we won't use constructer to inflate the views.
        super(context, 0, incomeArray);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.simple_list_item_1, parent, false
            );
        }
        // Get the Student object located at the given position
        Income currentIncome = getItem(position);

        /*
        // Set up one of the TextViews with the title text obtained from the Income object
        TextView incomeTitle = (TextView) listItemView.findViewById(R.id.txtIncomeTitle);
        incomeTitle.setText(currentIncome.getTitle());

        // Set up the other TextView with the amount text obtained from the Income object
        TextView incomeAmount = (TextView) listItemView.findViewById(R.id.txtIncomeAmount);
        incomeAmount.setText((int) currentIncome.getAmount());


        // Set up the ImageView with the date obtained from the Income object
        TextView incomeDate = (TextView) listItemView.findViewById(R.id.image);
        incomeDate.setText(currentIncome.getDate());
        */

        // Return the list item layout (containing 3 TextViews)
        return listItemView;
    }
}
