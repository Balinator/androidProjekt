package com.example.balinator.androidprojekt;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;


/**
 * Created by czimbortibor on 08/12/16.
 */

public class StatisticsWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private final String tag = "ViewFactory";
   // private static final String[] data = {"stuff", "stuff2", "stuff3"};
    private ArrayList<String> data;
    private Context context;
    private int widgetID;

    public StatisticsWidgetViewFactory(Context context, Intent intent) {
        Log.v(tag, "constructor");
        this.context = context;
        this.widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (data == null) {
            data = new ArrayList<String>();
            data.add("sttuff");
            data.add(" izee");
            data.add("sumthing");
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.statistics_row);
        row.setTextViewText(R.id.textView, data.get(i));
        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
