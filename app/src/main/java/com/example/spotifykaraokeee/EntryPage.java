package com.example.spotifykaraokeee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class EntryPage extends AppCompatActivity implements View.OnClickListener {

    Button logIn, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sets no title and makes splash screen into full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();//hides the action bar with title
        setContentView(R.layout.activity_entry_page);

        //our instances of login/signup buttons
        logIn = (Button)findViewById(R.id.logInButton);
        signUp = (Button)findViewById(R.id.signUpButton);

        //sets onClickListener to these buttons
        logIn.setOnClickListener(this);
        signUp.setOnClickListener(this);

    }

    //overridden onClick method to allow transition to sign in page and sign up page
    @Override
    public void onClick(View v) {
        final EntryPage entryPage = this;
        if(v.getId()==R.id.logInButton){
            Intent intent = new Intent(entryPage, LogInPage.class);
            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(entryPage, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
            startActivity(intent, bundle);
        }
        if(v.getId()==R.id.signUpButton){
            Intent intent = new Intent(entryPage, RegisterPage.class);
            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(entryPage, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
            startActivity(intent, bundle);
        }
    }
}