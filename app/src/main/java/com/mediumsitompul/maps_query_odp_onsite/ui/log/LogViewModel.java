package com.mediumsitompul.maps_query_odp_onsite.ui.log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mText2;
    private MutableLiveData<String> mText3;

    public LogViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("No");
        mText2 = new MutableLiveData<>();
        mText2.setValue("Timestamp");
        mText3 = new MutableLiveData<>();
        mText3.setValue("UserID");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getText2(){ return mText2;}
    public LiveData<String> getText3(){ return mText3;}
}