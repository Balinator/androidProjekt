package com.example.balinator.androidprojekt.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.balinator.androidprojekt.R;
import com.example.balinator.androidprojekt.database.Database;
import com.example.balinator.androidprojekt.services.struct.MyService;

import java.util.ArrayList;


/**
 * Created by czimbortibor on 08/12/16.
 */

public class WidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private final String tag = "ViewFactory";
    private ArrayList<MyService> mData;
    private Context context;
    private int widgetID;

    private Database db;


    public WidgetViewFactory(Context context, Intent intent) {
        Log.v(tag, "constructor");
        this.context = context;
        this.widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        db = new Database(context);
        if (mData == null) {
            db.open();
            mData = db.getAllFavoriteService();
            db.close();
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        db.open();
        mData = db.getAllFavoriteService();
        db.close();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.statistics_row);
        MyService d = mData.get(i);
        row.setTextViewText(R.id.widget_statistics_row_title, d.getName());
        row.setTextColor(R.id.widget_statistics_row_title, Color.BLACK);

        d.setStatistic();

        row.setTextViewText(R.id.widget_statistics_row_text_view, d.getStatistic());
        row.setTextColor(R.id.widget_statistics_row_text_view, Color.BLACK);
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
        return mData.get(i).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
