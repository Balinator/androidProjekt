package com.example.balinator.androidprojekt.services.screenonservice;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.balinator.androidprojekt.database.Database;
import com.example.balinator.androidprojekt.services.struct.MyService;
import com.example.balinator.androidprojekt.services.struct.ServiceLog;

import java.util.ArrayList;

/**
 * Created by Balinator on 2016. 12. 15..
 */
public class MyScreenOnService extends MyService {
    public static final String sName = "MyScreenOnService";

    private MyScreenOnOffReciver mScreenStateReceiver;
    private Database db;

    public MyScreenOnService(long mId, String mName, String mDescription, Context context) {
        super(mId, mName, mDescription, context);
        mScreenStateReceiver = new MyScreenOnOffReciver();
    }

    public MyScreenOnService(Context context) {
        super(context);
        mScreenStateReceiver = new MyScreenOnOffReciver();
    }

    @Override
    public void setStatistic() {
        if(db == null){
            db =new Database(context);
        }
        db.open();
        ArrayList<ServiceLog> array = db.getAllServiceLog(db.getService(sName).getId());
        db.close();

        long time = 0;

        mStatistic = "Your phones screen was on " + time + " hours on, in the last 24 hour!";
    }

    @Override
    public String getStatistic() {
        return mStatistic;
    }

    @Override
    public void setFavorite(boolean mFavorite) {
        super.setFavorite(mFavorite);
        if(this.mFavorite) {
            try {
                IntentFilter screenStateFilter = new IntentFilter();
                screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
                screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
                context.registerReceiver(mScreenStateReceiver, screenStateFilter);
            }catch (Exception e){}
            Log.d(sName,"on");
        }else {
            try {
                context.unregisterReceiver(mScreenStateReceiver);
            }catch (Exception e){}
            Log.d(sName,"off");
        }
    }
}
