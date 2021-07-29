package com.example.spotifykaraokeee.ui.UserProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is user profile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}