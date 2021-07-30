package com.example.spotifykaraokeee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.ImageUri;
import com.spotify.protocol.types.Track;


import org.jetbrains.annotations.NotNull;
import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LyricDisplayPage extends AppCompatActivity implements View.OnClickListener {

    // Set up Spotify Credentials
    private static final int REQUEST_CODE = 1337;
    //our clientID from our developer dashboard
    private static final String CLIENT_ID = "b6cb228259164dabab54198bf1537a75";
    //also had to set this as our redirect URI in our developer dashboard
    private static final String REDIRECT_URI = "http://com.yourdomain.yourapp/callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    private final OkHttpClient client = new OkHttpClient();

    //our playback buttons
    ImageButton playButton;
    ImageButton nextButton;
    ImageButton previousButton;
    //return to homepage button
    ImageButton returnButton;
    //more playback buttons
    ImageButton recordButton;
    ImageButton syncedButton;
    ImageButton commentButton;

    SeekBar ourSeekBar;
    Handler handler = new Handler();

    TextView lyrics_display;
    TextView currentTime;
    TextView totalTime;
    TextView songTitle;
    TextView albumTitle;
    TextView artistName;

    String currentPlaybackTime;
    String totalPlaybackTime;

    ImageView albumCover;

    boolean isRecording=false;
    boolean isSynced=false;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sets no title and makes splash screen into full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();//hides the action bar with title
        setContentView(R.layout.activity_lyric_display_page);

        //initializing our buttons
        playButton = (ImageButton)findViewById(R.id.play_button);
        nextButton = (ImageButton)findViewById(R.id.next_button);
        previousButton = (ImageButton)findViewById(R.id.previous_button);
        returnButton = (ImageButton)findViewById(R.id.return_button);
        recordButton = (ImageButton)findViewById(R.id.recording_button);
        syncedButton = (ImageButton)findViewById(R.id.synced_button);
        commentButton = (ImageButton)findViewById(R.id.comment_button);

        //our music progress bar
        ourSeekBar = (SeekBar)findViewById(R.id.music_bar);

        //our textView objects
        currentTime = (TextView)findViewById(R.id.current_time);
        totalTime = (TextView)findViewById(R.id.total_time);
        songTitle = (TextView) findViewById(R.id.song_title);
        albumTitle = (TextView) findViewById(R.id.album_title);
        artistName = (TextView) findViewById(R.id.artist_name);

        //ImageView object holding album artwork
        albumCover = (ImageView)findViewById(R.id.album_art_cover);

        //our lyrics textView object
        lyrics_display = (TextView) findViewById(R.id.lyrics_text);

        //sets onClickListener to our buttons
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        recordButton.setOnClickListener(this);
        syncedButton.setOnClickListener(this);
        commentButton.setOnClickListener(this);

        //string that holds song uri
        String song_uri = "spotify:playlist:37i9dQZF1DX2sUQwD7tbmL";

        //connects to spotify app remote and plays song
        //also calls displayLyrics() by passing in track name and track artist name
        connect(song_uri);
    }
    //this method makes the http GET request to our api endpoint using OkHTTP library
    private void displayLyrics(String song_title, String artist){

        Request request = new Request.Builder().url("https://36rls94ll3.execute-api.us-east-2.amazonaws.com/test/getlyrics?"+"song_title="+song_title+"&artist="+artist).build();

        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    //saves lyrics in string format
                    final String lyrics_string = response.body().string();

                    //runs method on this activity's thread
                    LyricDisplayPage.this.runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            //allow for scrolling movement
                            lyrics_display.setMovementMethod(new ScrollingMovementMethod());
                            //set desired text for our textView object
                            lyrics_display.setText(lyrics_string);
                        }
                    });
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }
    //allows us to connect to spotify app remote
    private void connect(String song_uri){
        //sets connection parameters
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();
        //connects to app remote
        SpotifyAppRemote.connect(this, connectionParams, new Connector.ConnectionListener() {
            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                mSpotifyAppRemote = spotifyAppRemote;
                Log.d("MainActivity", "Connected! Yay!");

                // Now you can start interacting with App Remote
                //in our case, we want it to play a song
                startSong(song_uri);
            }
            @Override
            public void onFailure(Throwable error) {
                // Something went wrong if you go here!
                Log.e("MyActivity", error.getMessage(), error); // edited from throwable.getMessage()
            }
        });
    }
    // Connected functionality for remote play
    private void startSong(String song_uri) {
        //plays default song from playlist in quick start guide
        mSpotifyAppRemote.getPlayerApi().play(song_uri);

        //when song is played, set the play button to be the pause button drawable
        playButton.setBackgroundResource(R.drawable.ic_baseline_pause_circle);

        LyricDisplayPage.this.runOnUiThread (new Runnable(){
            @Override
            public void run(){
                mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(playerState ->{
                    Track track = playerState.track;
                    if (track != null) {
                        //passes in track name and artist name into displayLyrics method
                        displayLyrics(track.name, track.artist.name);
                        //sets song title, album title, and artist name to our text views
                        setSongTitle(track.name);
                        setAlbumTitle(track.album.name);
                        setArtistName(track.artist.name);
                        setAlbumCover(track.imageUri);
                        setTrackDuration((int)playerState.track.duration);
                        long totaltracktime = (track.duration)/1000;
                        long halfwaythrough = totaltracktime/2;
                        long now = ourSeekBar.getProgress();


                        //System.out.println("Total Time: "+ totaltracktime);
                        //System.out.println("Half Time: "+ halfwaythrough);
                        //System.out.println("Curr Time: "+ now);


                        updateSeekBar((int)playerState.playbackPosition);
                        handler.postDelayed(this, 1000);
                    }
                });
                //when seekbar position is changed
                ourSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        //when user changes position in seek bar, move to that position in the current playing song
                        if(fromUser){
                            mSpotifyAppRemote.getPlayerApi().seekTo(progress*1000);
                        }
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
            }


        });
    }//start song

    private void setTrackDuration(int duration){
        //sets max duration of the seek bar
        ourSeekBar.setMax(duration/1000);
        //converts duration to string format
        totalPlaybackTime = convertFormat(duration);
        //sets duration to our text view object
        totalTime.setText(totalPlaybackTime);
    }
    private void updateSeekBar(int playbackPosition){
        //updates the current playback position on the seek bar
        ourSeekBar.setProgress(playbackPosition/1000);
        //converts curent playback position to string format
        currentPlaybackTime = convertFormat(playbackPosition);
        //sets the current playback time to our text view object
        currentTime.setText(currentPlaybackTime);
    }
    private String convertFormat(int duration){
        String timerLabel = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;
        timerLabel += min + ":";
        if (sec < 10){
            timerLabel += "0";
        }
        timerLabel +=sec;
        return timerLabel;
    }
    private void setSongTitle(String song_title){
        //sets song title for textView
        songTitle.setText(song_title);
    }
    private void setAlbumTitle(String album_title){
        //sets album title for textView
        albumTitle.setText(album_title);
    }
    private void setArtistName(String artist_name){
        //sets artist name for textView
        artistName.setText(artist_name.toUpperCase());
    }
    private void setAlbumCover(ImageUri uri){
        mSpotifyAppRemote.getImagesApi().getImage(uri).setResultCallback(artwork1 -> {
            albumCover.setImageBitmap(artwork1);
        });
    }
    //overridden onClick method to give function to playback buttons
    @Override
    public void onClick(View v) {
        //PLAY BUTTON
        if(v.getId()==R.id.play_button){
            //gets the current player state
            mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(playerState ->{
                boolean paused = playerState.isPaused;
                //if song is already paused, resume the song
                if(paused == true){
                    mSpotifyAppRemote.getPlayerApi().resume();
                    //when song is resumed, play button will be the pause button drawable
                    playButton.setBackgroundResource(R.drawable.ic_baseline_pause_circle);
                }
                //if song is already playing, pause the song
                else if(paused == false){
                    mSpotifyAppRemote.getPlayerApi().pause();
                    //when song is paused, play button will be the play button drawable
                    playButton.setBackgroundResource(R.drawable.ic_baseline_play_circle);
                }
            });
        }
        //NEXT BUTTON
        //when skip next button is clicked
        if(v.getId()==R.id.next_button){
            mSpotifyAppRemote.getPlayerApi().skipNext();
        }
        //PREVIOUS BUTTON
        //when skip previous button is clicked
        if(v.getId()==R.id.previous_button){
            mSpotifyAppRemote.getPlayerApi().skipPrevious();
        }
        //RETURN BUTTON
        if(v.getId()==R.id.return_button){
            mSpotifyAppRemote.getPlayerApi().pause();
            //when song is paused, play button will be the play button drawable
            playButton.setBackgroundResource(R.drawable.ic_baseline_play_circle);
            LyricDisplayPage LDP = this;
            Intent intent = new Intent(LDP, NavigationHomePage.class);
            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(LDP, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
            startActivity(intent, bundle);
        }
        //RECORDING BUTTON
        if(v.getId()==R.id.recording_button){
            if(isRecording==false) {
                recordButton.setBackgroundResource(R.drawable.ic_baseline_radio_button_checked_24);
                isRecording = true;
            }
            else if(isRecording==true) {
                recordButton.setBackgroundResource(R.drawable.ic_baseline_radio_button_unchecked_24);
                isRecording = false;
            }
        }
        //LYRICS SYNCED BUTTON
        if(v.getId()==R.id.synced_button){
            if(isSynced==false) {
                syncedButton.setBackgroundResource(R.drawable.ic_baseline_check_circle_24);
                isSynced = true;
            }
            else if(isSynced==true) {
                syncedButton.setBackgroundResource(R.drawable.ic_baseline_check_circle_outline_24);
                isSynced = false;
            }
        }
        //COMMENT BUTTON
        if(v.getId()==R.id.comment_button){

        }
    }
}


