package com.example.spotifykaraokeee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPage extends AppCompatActivity implements View.OnClickListener{
//the register page is done very similar to the login page with certain key differences, but mcuh of the logic and methods are similar
    Button signUpButton;
    private FirebaseAuth auth;
    private EditText inputEmail, inputPassword;
    //initialize the variables and their object origins. these varibles will be used across different methods which is why they are global.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sets no title and makes splash screen into full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();//hides the action bar with title
        setContentView(R.layout.activity_register_page);

        //our instance of our signUp button
        signUpButton = (Button)findViewById(R.id.registerPageButton);
        signUpButton.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        inputEmail=(EditText)findViewById(R.id.editTextTextEmailAddress);
        inputPassword=(EditText)findViewById(R.id.editTextTextPassword2);
        //all of this follows the same logic as the login page
        //the variables are initialized globally and must be given values/instances here

    }
    //overridden onClick method to allow transition to HomePage
    @Override
    public void onClick(View v) {
        RegisterPage.this.registerUser();
    }
    private void registerUser(){
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        //cant have empty inputs!!!
        if (TextUtils.isEmpty(email)) {
            showToast("Enter email address!");
            return;
        }
        if(TextUtils.isEmpty(password)){
            showToast("Enter Password!");
            return;
        }
        if(password.length() < 6){ //in order to have a password accepted, it must be 6 characters long at least. it is one way to have some security measures.
            //inthe future i a going to use this similar logic and add more requirements to check for a special character, numbers, and capitals
            showToast("Password too short, enter minimum 6 characters");
            return;
        }
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterPage.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    RegisterPage.this.showToast("Authentication failed. " + task.getException());
                }
                else {
                    //RegisterPage.this.startActivity(new Intent(RegisterPage.this, LogInPage.class));
                    //RegisterPage.this.finish();
                    Intent intent = new Intent(RegisterPage.this, LyricDisplayPage.class);//the user enterd the correct credentials and are authorized to enter the navigation page
                    startActivity(intent);
                    finish();
                }
            }
        });
    }//register user
    @Override
    protected void onResume() {
        super.onResume();
    }
    public void showToast(String toastText) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }
}
