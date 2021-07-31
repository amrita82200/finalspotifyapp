package com.example.spotifykaraokeee;
import android.content.Context;
import android.text.TextUtils;
import com.example.spotifykaraokeee.LyricDisplayPage;
import com.google.firebase.firestore.Query;
import com.example.spotifykaraokeee.PlayedSongs1.*;
import java.util.*;

public class DBFilters {
    private String albumName=null;
    private String artistName=null;
    private int numPLays=-1;
    private String songTitle=null;
    private Date datelist=null;
    private String sortBy=null;
    private String category = null;
    private Query.Direction sortDirection = null;


    public DBFilters(){}

    public static DBFilters getDefault(){
        DBFilters filter = new DBFilters();
        //filter.set
        return filter;
    }
    public  boolean hasAlbumName(){
        return !(TextUtils.isEmpty(albumName));
    }
    public boolean hasArtistName(){
        return !(TextUtils.isEmpty(artistName));
    }
    public boolean hasPlays(){
        return(numPLays>0);
    }
    public boolean hasSortBy(){
        return !(TextUtils.isEmpty(sortBy));
    }
    public String getCategory(){
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getAlbumName(){
        return albumName;
    }
    public void setAlbumName(){
        this.albumName=albumName;
    }
    public String getArtistName(){
        return artistName;
    }
    public void setArtistName(){
        this.artistName=artistName;
    }
    public int getNumPLays(){
        return numPLays;
    }
    public void setNumPLays(){
        this.numPLays=numPLays;
    }
    public String getSongTitle(){
        return songTitle;
    }
    public void setSongTitle(){
        this.songTitle=songTitle;
    }
    public Date getDatelist(){
        return datelist;
    }
    public void setDatelist(){
        this.datelist=datelist;
    }
    public String getSortBy(){
        return sortBy;
    }
    public void setSortBy(){
        this.sortBy=sortBy;
    }
    public Query.Direction getSortDirection(){
        return sortDirection;
    }
    public String getSearchDescription(Context context) {
        StringBuilder desc = new StringBuilder();
        if (category == null && datelist == null) {
            desc.append("<b>");
            //desc.append(context.getString(R.string.));
            desc.append("</b>");
        }

        if (category != null) {
            desc.append("<b>");
            desc.append(category);
            desc.append("</b>");
        }

        if (category != null && datelist != null) {
            desc.append(" in ");
        }

        if (datelist != null) {
            desc.append("<b>");
            desc.append(datelist);
            desc.append("</b>");
        }

        if (numPLays > 0) {
            desc.append(" for ");
            desc.append("<b>");
            desc.append(PlayedSongs1.PLAYS);
            desc.append("</b>");
        }

        return desc.toString();

    }


    }
