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


public class LogInPage extends AppCompatActivity implements View.OnClickListener {

    Button loginButton;
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
        setContentView(R.layout.activity_log_in_page);

        //our instance of logInPage button
        loginButton = (Button)findViewById(R.id.logInPageButton);
        //sets onClickListener to this button. this is the only button on the page, so it is the only object that needs the listener
        loginButton.setOnClickListener(this);
        //initalize the other global varibales. auth is an instance of firebase authentication. and the input email and passords need to go to the activity login page xml to get the id's so that when there is a get Text, the right object is being accessed
        auth = FirebaseAuth.getInstance();
        inputEmail=(EditText)findViewById(R.id.editTextTextEmailAddress);
        inputPassword=(EditText)findViewById(R.id.editTextTextPassword2);

    }
    //overridden onClick method to allow transition to Navigation
    @Override
    public void onClick(View v) {
        //when the login button is clicked, that means the information in the email and password fields should be authenticated to what is in firebase, so authenitcateuser method is called here
        LogInPage.this.authenticateUser();
    }
    public void authenticateUser(){
        //input email and password are private global varibales, so to access and get what their current values are, we need to use a getter and store the strings in local varibles
        //password has to be a final varible as it should not be changed as if someone enters the wrong password then that password needs to be checked in refernce to the firebase information for the user
        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {//the email shouldnt be empty other wise a toast message will pop up to let the user know that they have an invalid input
            //toast is a great tool i learned about. it has messages popup on screen and then go away like a piece of toast coming out a toaster. it was really convenient to use rather than implement some extra ui
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();//the text for the toast message is to remind the user to enter their email because the field was empty
            return;
        }
        if (TextUtils.isEmpty(password)) {//same as email empty but for if password is empty
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LogInPage.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {//This is the most important sectoin of the login authentication. this is where the email and password information is passed to auth to go to firebase auth to do the authentication
                if (!task.isSuccessful()) {//if the result of the authentication is not successful, why?
                    if (password.length() < 6) {//the authenitcation failed because the password entered did not meet the requrirements set in register page
                        showToast("Password too short, enter minimum 6 characters");
                    }
                    else {//if the authentication failed but it wasnt because the password was too short, then there was some other issue like the password entered does not match what is in firebase wuth and therefore failed
                        LogInPage.this.showToast("Authentication failed. " + task.getException());
                    }
                }
                else {//if the opposite of the task is not successful happened then the task must have been succesful, so that is why there successful auth is in the else
                    Intent intent = new Intent(LogInPage.this, LyricDisplayPage.class);//the user enterd the correct credentials and are authorized to enter the navigation page
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    public void showToast(String toastText) {//helper method for toast messages.
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }
}