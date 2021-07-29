package com.example.spotifykaraokeee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class MainActivity extends AppCompatActivity {

    //our sliding top down animation object
    Animation topAnim;
    //our logo image
    ImageView logoImage;

    //allows us to transition to next screen
    private Thread mSplashThread;

    // Set up Spotify Credentials
    private static final int REQUEST_CODE = 1337;
    //our clientID from our developer dashboard
    private static final String CLIENT_ID = "b6cb228259164dabab54198bf1537a75";
    //also had to set this as our redirect URI in our developer dashboard
    private static final String REDIRECT_URI = "http://com.yourdomain.yourapp/callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    public static String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //authenticates through spotify client
        LogIn();

        //sets no title and makes splash screen into full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();//hides the action bar with title

        setContentView(R.layout.activity_main);

        //sliding top down animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        //logo image instantiated from xml by id
        logoImage = findViewById(R.id.ellipse_1);
        //sets our image to display with top down animation
        logoImage.setAnimation(topAnim);

        //instance of the splash screen
        final MainActivity splashScreen = this;
        //try catch method to allow for smooth transition between splash screen and entry page
        mSplashThread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        wait(3000);
                    }
                }
                catch(InterruptedException ex){
                }
                finish();

                Intent intent = new Intent(splashScreen, EntryPage.class);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(splashScreen, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                startActivity(intent, bundle);
            }
        };
        mSplashThread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    // returns activity result from request and intent
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            access_token = response.getAccessToken();

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
    // Set up Spotify Auth Logging in
    private void LogIn() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID,
                        AuthenticationResponse.Type.TOKEN,
                        REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }
    // Set Up Spotify Auth Logging Out
    private void LogOut() {

    }
}