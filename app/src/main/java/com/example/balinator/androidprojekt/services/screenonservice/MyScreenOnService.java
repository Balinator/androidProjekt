package com.example.balinator.androidprojekt.services.screenonservice;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.balinator.androidprojekt.database.Database;
import com.example.balinator.androidprojekt.services.struct.MyService;
import com.example.balinator.androidprojekt.services.struct.ServiceLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Balinator on 2016. 12. 15..
 */
public class MyScreenOnService extends MyService {
    public static final String sName = "MyScreenOnService";
    public static final long SECS_IN_MILLS = 10000;
    public static final long MINS_IN_MILLS = 60 * SECS_IN_MILLS;
    public static final long HOUR_IN_MILLS = 60 * MINS_IN_MILLS;
    public static final long DAY_IN_MILLS = 24 * HOUR_IN_MILLS;


    private static MyScreenOnOffReciver mScreenStateReceiver;
    private Database db;

    public MyScreenOnService(long mId, String mName, String mDescription, Context context) {
        super(mId, mName, mDescription, context);
        if(mScreenStateReceiver == null) {
            mScreenStateReceiver = new MyScreenOnOffReciver();
        }
    }

    public MyScreenOnService(Context context) {
        super(context);
        if(mScreenStateReceiver == null) {
            mScreenStateReceiver = new MyScreenOnOffReciver();
        }
    }

    @Override
    public void setStatistic() {
        if(db == null){
            db =new Database(context);
        }
        long time = System.currentTimeMillis() - DAY_IN_MILLS;

        db.open();
        ArrayList<ServiceLog> array = db.getAllServiceLog(db.getService(sName).getId());

        ArrayList<ServiceLog> deleted = new ArrayList<>();
        for(int i = 0; i < array.size(); ++i){
            if(array.get(i).getTime() < time) {
                ServiceLog l = array.get(i);
                db.deleteServiceLog(l);
                deleted.add(l);
            }
        }
        array.removeAll(deleted);
        db.close();

        Collections.sort(array, new Comparator<ServiceLog>() {
            @Override
            public int compare(ServiceLog o1, ServiceLog o2) {
                long a = o1.getTime() - o2.getTime();
                if(a < 0){
                    return -1;
                }
                else if(a > 0){
                    return 1;
                }
                return 0;
            }
        });

        long t = 0;
        if(array.size() > 0) {
            int starti = 0;
            if (!array.get(0).getData().equals("on")) {
                starti = 1;
                //t = array.get(0).getTime() - time;
            }

            for (int i = starti; i < array.size(); i += 2) {
                if (array.size() < (i + 1)) {
                    t += array.get(i + 1).getTime() - array.get(i).getTime();
                } else {
                    t += System.currentTimeMillis() - array.get(i).getTime();
                }
            }
        }

        long hours = t / HOUR_IN_MILLS;
        long mins = (t % HOUR_IN_MILLS) / MINS_IN_MILLS;
        long sec = (t % MINS_IN_MILLS) / SECS_IN_MILLS;

        mStatistic = "Your phones screen was on " + hours + " hours and " + mins +" minutes on, in the last 24 hour!";
    }

    @Override
    public String getStatistic() {
        return mStatistic;
    }

    @Override
    public void setFavorite(boolean mFavorite) {
        super.setFavorite(mFavorite);
        if(this.mFavorite) {
            if(mScreenStateReceiver.isOn()) {
                try {
                    IntentFilter screenStateFilter = new IntentFilter();
                    screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
                    screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
                    context.registerReceiver(mScreenStateReceiver, screenStateFilter);
                } catch (Exception e) {
                }
                Log.d(sName, "on");
                mScreenStateReceiver.switchState();
            }
        }else {
            if(!mScreenStateReceiver.isOn()) {
                try {
                    context.unregisterReceiver(mScreenStateReceiver);
                } catch (Exception e) {
                }
                Log.d(sName, "off");
                mScreenStateReceiver.switchState();
            }
        }
    }
}
