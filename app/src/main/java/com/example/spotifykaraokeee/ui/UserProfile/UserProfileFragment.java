package com.example.spotifykaraokeee.ui.UserProfile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.spotifykaraokeee.R;

public class UserProfileFragment extends Fragment {

    private UserProfileViewModel userProfileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userProfileViewModel =
                new ViewModelProvider(this).get(UserProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);
        //originally a notifications fragment. changed to user profile fragment
        final TextView textView = root.findViewById(R.id.text_user_profile);
        userProfileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}