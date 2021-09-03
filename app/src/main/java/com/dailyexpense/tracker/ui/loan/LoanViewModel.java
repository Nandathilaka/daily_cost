package com.dailyexpense.tracker.ui.loan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class LoanViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LoanViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is loan fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}