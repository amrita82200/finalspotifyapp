package com.example.spotifykaraokeee.ui.SearchSong;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchSongViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SearchSongViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is search song fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}