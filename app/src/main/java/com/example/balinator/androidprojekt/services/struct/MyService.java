package com.example.balinator.androidprojekt.services.struct;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Created by Balinator on 2016. 12. 08..
 */
public class MyService {
    private static final String tag = "MyService";
    protected Context context;

    protected long mId;
    protected String mName;
    protected String mDescription;
    protected boolean mFavorite;

    protected String mStatistic;

    public MyService(Context context) {
        this.context = context;
    }

    public MyService(long mId, String mName, String mDescription, Context context) {
        this.mId = mId;
        this.mName = mName;
        this.mDescription = mDescription;
        this.context = context;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean mFavorite) {
        this.mFavorite = mFavorite;
        Log.d(tag,"setfavorite to: " + mFavorite);
    }

    public String getStatistic() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    public void setStatistic() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
