package com.nandeproduction.dailycost.ui.income;

import android.graphics.Typeface;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class IncomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public IncomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is income fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}