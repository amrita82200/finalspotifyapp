package com.example.spotifykaraokeee;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.spotifykaraokeee.PlayedSongs1;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class NavigationHomePage extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView songTitle;
    TextView artistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_home_page);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search_song, R.id.navigation_user_profile, R.id.monthly_top_requested, R.id.weekly_top_requested, R.id.daily_top_requested, R.id.nav_view,R.id.artist_name_this_hour,R.id.song_title_this_hour)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        songTitle = (TextView) findViewById(R.id.song_title_this_hour);
        artistName = (TextView) findViewById(R.id.artist_name_this_hour);
        songTitle.setText("Odeza");
        CollectionReference collectionRef = db.collection("PlayedSongs1");
        Query query = collectionRef.orderBy("Numplays", Query.Direction.DESCENDING).limit(1);
        //String mostlistened = (collectionRef.orderBy("Numplays", Query.Direction.DESCENDING).limit(1)).get("PlayedSongs1");

               // System.out.println("HERE BITCH: "+ mostlistened);

    }


}