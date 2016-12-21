    package com.example.balinator.androidprojekt.services.screenonservice;

    import android.content.BroadcastReceiver;
    import android.content.Context;
    import android.content.Intent;
    import android.hardware.display.DisplayManager;
    import android.os.Bundle;
    import android.os.PowerManager;
    import android.telephony.TelephonyManager;
    import android.util.Log;
    import android.view.Display;

    import com.example.balinator.androidprojekt.database.Database;

    import java.util.Set;

    /**
     * Created by Balinator on 2016. 12. 15..
     */
    public class MyScreenOnOffReciver extends BroadcastReceiver {
        private static final String tag = "MyScreenOnOffReciver";

        private Database db;

        @Override
        public void onReceive(Context context, Intent intent) {
            String state = intent.getAction();

            if (state.equals(Intent.ACTION_SCREEN_ON)) {
                if(db == null){
                    db = new Database(context);
                }
                db.open();
                long id = db.getService(MyScreenOnService.sName).getId();
                db.createServiceLog(id,System.currentTimeMillis() + ":on");
                db.close();
                Log.d(tag,"screen on log");
            }else if(state.equals(Intent.ACTION_SCREEN_OFF)) {
                if (db == null) {
                    db = new Database(context);
                }
                db.open();
                long id = db.getService(MyScreenOnService.sName).getId();
                db.createServiceLog(id, System.currentTimeMillis() + ":off");
                db.close();
                Log.d(tag, "screen off log");
            }
        }
    }
