package com.example.readyachieve.ui.main.progress;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProgressViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProgressViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is progress fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
