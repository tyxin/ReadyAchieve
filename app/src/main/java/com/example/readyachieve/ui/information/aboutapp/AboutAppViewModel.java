package com.example.readyachieve.ui.information.aboutapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutAppViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public AboutAppViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is about app fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
