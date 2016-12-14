package com.example.balinator.androidprojekt.struct;

/**
 * Created by Balinator on 2016. 12. 08..
 */
public class MyService {
    private long mId;
    private String mName;
    private String mDescription;
    private boolean mFavorite;

    public MyService() {
    }

    public MyService(long mId, String mName, String mDescription) {
        this.mId = mId;
        this.mName = mName;
        this.mDescription = mDescription;
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
    }
}
