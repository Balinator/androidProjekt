package com.example.balinator.androidprojekt;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class StatisticsWidgetProvider extends AppWidgetProvider {

    private final String tag = "WidgetProvider";
    private Context context;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.context = context;
        for (int i = 0; i< appWidgetIds.length; ++i){
            int currentWidgetId = appWidgetIds[i];
            /** creating an Intent to the WidgetService to add another row to the widget's ListView */
            Intent intentService = new Intent(context, StatisticsWidgetService.class);
            /** pass the widget's ID through the Intent */
            intentService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, currentWidgetId);

            /** set the layout for the widget */
            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_provider);
            /** attach the adapter to the ListView */
            remoteView.setRemoteAdapter(R.id.widgetListView, intentService);

            // TODO: update the widget with a new row
            Intent intentClick = new Intent(context, MainActivity.class);
            intentClick.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            /** retrieve a PendingIntent that will get the new row's name? */
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentClick, PendingIntent.FLAG_UPDATE_CURRENT);
            //views.setPendingIntentTemplate(R.id.widgetListView, pendingIntent);
            remoteView.setOnClickPendingIntent(R.id.btnAddRow, pendingIntent);

            /** perform an update on the current widget */
            appWidgetManager.updateAppWidget(currentWidgetId, remoteView);

            Toast.makeText(context, "widget added", Toast.LENGTH_SHORT).show();
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);
    }
}
