package com.example.balinator.androidprojekt.services.struct;

/**
 * Created by Balinator on 2016. 12. 08..
 */
public class ServiceLog {
    private long mId;
    private long mTime;
    private String mData;

    public ServiceLog(long mId, long mTime, String mData) {
        this.mId = mId;
        this.mTime = mTime;
        this.mData = mData;
    }

    public long getId() {
        return mId;
    }

    public long getTime() {
        return mTime;
    }

    public String getData() {
        return mData;
    }
}
