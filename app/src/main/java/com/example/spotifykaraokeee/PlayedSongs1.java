package com.example.spotifykaraokeee;
import com.google.firebase.firestore.util.*;
import java.util.*;


public class PlayedSongs1 {
    public static final String ALBUMNAME = "album_name";
    public static final String ARTISTNAME = "artist_name";
    public static final int PLAYS = 0;
    public static final String SONGTITLE = "song_title";
    public static final Date TIME = Calendar.getInstance().getTime();


    private String album_name;
    private String artist_name;
    private int numplays;
    private String song_title;
    private Date dateplayed;

    public PlayedSongs1(){}
    public PlayedSongs1(String album_name, String artist_name, int numplays, String song_title, Date dateplayed){
        this.album_name=album_name;
        this.artist_name=artist_name;
        this.numplays=numplays;
        this.song_title=song_title;
        this.dateplayed = dateplayed;
    }
    public String getAlbumname(){
        return album_name;
    }
    public void setAlbum_name(){
        this.album_name = album_name;
    }
    public String getArtist_name(){
        return artist_name;
    }
    public void setArtist_name(){
        this.artist_name = artist_name;
    }
    public int getNumplays(){
        return numplays;
    }
    public void setNumplays(){
        this.numplays = numplays;
    }
    public String getSong_title(){
        return song_title;
    }
    public void setSong_title(){
        this.song_title = song_title;
    }
    public Date getDate(){
        return dateplayed;
    }
    public void setDate(){
        this.dateplayed = dateplayed;
    }

}
