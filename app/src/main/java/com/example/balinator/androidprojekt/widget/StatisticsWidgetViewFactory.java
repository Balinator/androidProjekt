package com.example.balinator.androidprojekt.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.balinator.androidprojekt.R;
import com.example.balinator.androidprojekt.database.Database;
import com.example.balinator.androidprojekt.struct.MyService;

import java.util.ArrayList;


/**
 * Created by czimbortibor on 08/12/16.
 */

public class StatisticsWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private final String tag = "ViewFactory";
    private ArrayList<MyService> data;
    private Context context;
    private int widgetID;

    private Database db;


    public StatisticsWidgetViewFactory(Context context, Intent intent) {
        Log.v(tag, "constructor");
        this.context = context;
        this.widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        db = new Database(context);
        if (data == null) {
            db.open();
            data = db.getAllFavoriteService();
            db.close();
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        db.open();
        data = db.getAllFavoriteService();
        db.close();
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
        row.setTextViewText(R.id.widget_statistics_row_title, data.get(i).getName());
        row.setTextViewText(R.id.widget_statistics_row_text_view, data.get(i).getStatistic());
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
        return data.get(i).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
