package com.example.balinator.androidprojekt.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by czimbortibor on 08/12/16.
 */

public class WidgetService extends RemoteViewsService {

    private final String tag = "WidgetService";
    public static Context context;

    public WidgetService() {
        Log.v(tag, "service start");
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        if(context == null){
            context = getApplicationContext();
        }
        Log.v(tag, "get factory");
        int widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        return (new WidgetViewFactory(this.getApplicationContext(), intent));
    }
}
