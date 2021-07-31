package com.example.spotifykaraokeee;

import androidx.annotation.NonNull;
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

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.example.spotifykaraokeee.PlayedSongs1;
import java.util.*;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firestore.v1.WriteResult;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.ImageUri;
import com.spotify.protocol.types.Track;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.type.DateTime;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.ImageUri;
import com.spotify.protocol.types.Track;


import org.jetbrains.annotations.NotNull;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


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
    private static final String TAG = "Doc Snapshot?";
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
    int secondsFromStart;
    String trackName;
    String trackArtist;

    ImageView albumCover;

    boolean isRecording=false;
    boolean isSynced=false;
    boolean isPaused;


    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference newsong = db.collection("PlayedSongs1");


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
                        trackName = track.name;
                        trackArtist = track.artist.name;
                        secondsFromStart = (int)playerState.playbackPosition;
                        //passes in track name and artist name into displayLyrics method
                        displayLyrics(track.name, track.artist.name);
                        //sets song title, album title, and artist name to our text views
                        setSongTitle(track.name);
                        setAlbumTitle(track.album.name);
                        setArtistName(track.artist.name);
                        setAlbumCover(track.imageUri);
                        setTrackDuration((int)playerState.track.duration);
                        updateSeekBar((int)playerState.playbackPosition);
                        handler.postDelayed(this, 1000);
                        long totaltracktime = (track.duration)/1000;
                        long halfwaythrough = totaltracktime/2;
                        long now = ourSeekBar.getProgress();
                        if(now==halfwaythrough){
                            String currAlbumName = track.album.name;
                            String currArtistName = track.artist.name;
                            int numplays = 0;
                            String currSongetitle = track.name;
                            Date currdate = Calendar.getInstance().getTime();

                            onHalfpoint(currAlbumName, currArtistName, numplays,currSongetitle, currdate);
                        }


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
    }
    private void onHalfpoint(String currAlbumName, String currArtistName, int Numplays, String currSondtitle, Date currDate){
        System.out.println("MADE IT HALF WAY");

        DocumentReference referencetodocument = db.collection("PlayedSongs1").document(currSondtitle);
        referencetodocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot docsnap = task.getResult();
                    if(docsnap.exists()){
                        Log.d(TAG, "Document SnapShot Data: "+docsnap.getData());
                        //System.out.println(docsnap.getData());
                        //doc exisits, so increment numplays
                        //referencetodocument.update("Numplays", FieldValue.increment(1));
                        referencetodocument.update("Numplays", FieldValue.increment(1));
                       // referencetodocument.update("Numplays", FieldValue.increment(-0.5));

                        referencetodocument.update("currDate", currDate);



                    }
                    else{
                        Log.d(TAG,"No such docment exists, so creating now");
                        Map<String, Object> songsplayed = new HashMap<>();
                        songsplayed.put("AlbumName", currAlbumName);
                        songsplayed.put("ArtistName", currArtistName);
                        songsplayed.put("Numplays", Numplays);
                        songsplayed.put("SongTitle", currSondtitle);
                        songsplayed.put("currDate", currDate);
                        db.collection("PlayedSongs1").document(currSondtitle).set(songsplayed).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "NEW Document Successfully Written");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG,"Error writing document",e);
                            }
                        });
                    }
                }
                else{
                    Log.d(TAG, "Get failed with ", task.getException());
                }


            }
        });

        //CollectionReference songsthatwereplayed = db.collection("PlayedSongs1");






        /*db.collection("PlayedSongs1").whereEqualTo("SongTitle",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document:task.getResult()){
                        Log.d(TAG,document.getId()+"=>"+document.getData());
                    }
                }
                else{
                    Log.d(TAG, "Error getting Docs: ", task.getException());
                }
            }
        });*/
    }
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
        trackName = song_title;
        //sets song title for textView
        songTitle.setText(trackName);
    }
    private void setAlbumTitle(String album_title){
        //sets album title for textView
        albumTitle.setText(album_title);
    }
    private void setArtistName(String artist_name){
        trackArtist = artist_name;
        //sets artist name for textView
        artistName.setText(trackArtist.toUpperCase());
    }
    private void setAlbumCover(ImageUri uri){
        mSpotifyAppRemote.getImagesApi().getImage(uri).setResultCallback(artwork1 -> {
            albumCover.setImageBitmap(artwork1);
        });
    }
    //alert dialog box shown when time edit button is clicked
    public void showAlertDialogButtonClicked(View view) {
        //pauses song when dialog box is shown
        pauseSong();

        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter lyrics");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.time_suggestion_dialog_box, null);
        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("Send",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // send data from the
                        // AlertDialog to the Activity
                        EditText editText = customLayout.findViewById(R.id.lyric_suggestion);
                        //sendToFireStore(editText.getText().toString());
                        resumeSong();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // If user click no
                        // then dialog box is canceled.
                        dialog.cancel();
                        resumeSong();
                    }
                });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //sends timeStamp suggestions given by users to cloud firestore database
   /* private void sendToFireStore(String lyrics) {
        //sends data to cloud firestore database
        Log.d("tyvm :)", lyrics + "seconds:" + secondsFromStart);

        Map<String, Object> timeStamps = new HashMap<>();
        timeStamps.put("Artist", trackArtist);
        timeStamps.put("SongTitle", trackName);
        timeStamps.put("LyricalContent", lyrics);
        timeStamps.put("SecondsFromStart", secondsFromStart/1000);

        //adds Timestamp info to collection 'TimeStamps'
        firestore.collection("TimeStamps").document(trackName)
                .collection("Suggestions").document(convertFormat(secondsFromStart))
                .set(timeStamps, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("O YAH", "YERR");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("O NO", "sorry my g");
            }
        });
    }*/

    private void resumeSong(){
        mSpotifyAppRemote.getPlayerApi().resume();
        //when song is resumed, play button will be the pause button drawable
        playButton.setBackgroundResource(R.drawable.ic_baseline_pause_circle);
    }
    private void pauseSong(){
        mSpotifyAppRemote.getPlayerApi().pause();
        //when song is paused, play button will be the play button drawable
        playButton.setBackgroundResource(R.drawable.ic_baseline_play_circle);
    }

    //overridden onClick method to give function to playback buttons
    @Override
    public void onClick(View v) {
        //PLAY BUTTON
        if(v.getId()==R.id.play_button){
            //gets the current player state
            mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(playerState ->{
                isPaused = playerState.isPaused;
                //if song is already paused, resume the song
                if(isPaused == true){
                    resumeSong();
                }
                //if song is already playing, pause the song
                else if(isPaused == false){
                    pauseSong();
                }
            });
        }
        //NEXT BUTTON
        //when skip next button is clicked
        if(v.getId()==R.id.next_button){
            mSpotifyAppRemote.getPlayerApi().skipNext();
            playButton.setBackgroundResource(R.drawable.ic_baseline_pause_circle);
        }
        //PREVIOUS BUTTON
        //when skip previous button is clicked
        if(v.getId()==R.id.previous_button){
            mSpotifyAppRemote.getPlayerApi().skipPrevious();
            playButton.setBackgroundResource(R.drawable.ic_baseline_pause_circle);
        }
        //RETURN BUTTON
        if(v.getId()==R.id.return_button){
            mSpotifyAppRemote.getPlayerApi().pause();
            //when song is paused, play button will be the play button drawable
            playButton.setBackgroundResource(R.drawable.ic_baseline_play_circle);
            Intent intent = new Intent(LyricDisplayPage.this, NavigationHomePage.class);//the user enterd the correct credentials and are authorized to enter the navigation page
            startActivity(intent);
            finish();
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