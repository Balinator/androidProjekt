package com.example.balinator.androidprojekt.struct;

/**
 * Created by Balinator on 2016. 12. 15..
 */
public class MyScreenOnService extends MyService {
    public static  final String sName = "MyScreenOnService";

    public MyScreenOnService(long mId, String mName, String mDescription) {
        super(mId, mName, mDescription);
    }

    @Override
    public void setStatistic() {
        mStatistic = "MyScreenOnService statistic";
    }

    @Override
    public String getStatistic() {
        return mStatistic;
    }
}
