package com.example.spotifykaraokeee.ui.SearchSong;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.spotifykaraokeee.MainActivity;
import com.example.spotifykaraokeee.R;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchSongFragment extends Fragment {

    //access token taken from main activity after spotify authentication
    public String access_token1;



    private SearchSongViewModel searchSongViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        searchSongViewModel = new ViewModelProvider(this).get(SearchSongViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search_song, container, false);

        //song title
        String song_title = "All we need";

        //initialize access token string
        access_token1 = (MainActivity.access_token);

        //spotify web api
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(access_token1);
        SpotifyService spotify = api.getService();

        //search song by song title
        spotify.searchTracks(song_title, new Callback<TracksPager>() {
            @Override
            public void success(TracksPager tracks, Response response) {

                Log.d("sucesss", ("size: " + tracks.tracks.items.size()));

                for(int i = 0; i<tracks.tracks.items.size();i++){
                    Log.d("song:", tracks.tracks.items.get(i).name);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("u failed homie", error.toString());
            }
        });




        return root;
    }
}