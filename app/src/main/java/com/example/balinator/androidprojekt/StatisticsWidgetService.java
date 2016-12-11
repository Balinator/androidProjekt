package com.example.balinator.androidprojekt;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by czimbortibor on 08/12/16.
 */

public class StatisticsWidgetService extends RemoteViewsService {

    private final String tag = "WidgetService";

    public StatisticsWidgetService() {
        Log.v(tag, "service start");
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.v(tag, "get factory");
        int widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        return (new StatisticsWidgetViewFactory(this.getApplicationContext(), intent));
    }
}
