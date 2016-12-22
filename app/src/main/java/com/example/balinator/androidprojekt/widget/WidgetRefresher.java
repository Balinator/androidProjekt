package com.example.balinator.androidprojekt.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;


/**
 * Created by czimbortibor on 22/12/16.
 */

public class WidgetRefresher extends BroadcastReceiver {
    public static final String tag = "WidgetRefresher";

    private Context context;
    private Handler timerHandler;
    Runnable worker;
    private int updateRate;
    private String startAction;
    private String endAction;

    public WidgetRefresher(final Context context, int updateRate, String startAction, String endAction) {
        this.updateRate = updateRate;
        this.startAction = startAction;
        this.endAction = endAction;
        this.context = context;

        Log.v(tag, "constructor");
        timerHandler = new Handler();
        final Intent refreshIntent = new Intent();
        refreshIntent.setAction("REFRESH_WIDGET");
        worker = new Runnable() {
            @Override
            public void run() {
                Log.v(tag, "run");
                context.sendBroadcast(refreshIntent);
            }
        };

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        Log.v(tag, "received");

        if (action.equals(startAction)) {
            Log.v(tag, "starting time");
            timerHandler.postDelayed(worker, updateRate);
        } else if (action.equals(endAction)) {
            Log.v(tag, "ending timer");
            timerHandler.removeCallbacks(worker);
        }
    }
}
