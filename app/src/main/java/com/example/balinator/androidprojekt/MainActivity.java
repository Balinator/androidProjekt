package com.example.balinator.androidprojekt;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.example.balinator.androidprojekt.database.Database;
import com.example.balinator.androidprojekt.widget.StatisticsWidgetProvider;
import com.example.balinator.androidprojekt.widget.StatisticsWidgetService;

public class MainActivity extends AppCompatActivity {
    private static final String tag = "MainActivity";

    private String inputText = "";

    private ListView mListView;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddServiceDialog();
            }
        });

        mListView = (ListView) findViewById(R.id.main_list_view);
        mAdapter = new MyAdapter(getApplicationContext());

        mListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.action_add_new_service:
                showAddServiceDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showAddServiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New service");

        /** the dialog's view is going to be an EditText */
        final EditText input1 = new EditText(this);
        input1.setInputType(InputType.TYPE_CLASS_TEXT);
        //builder.setView(input1);

        final EditText input2 = new EditText(this);
        input2.setInputType(InputType.TYPE_CLASS_TEXT);
        //builder.setView(input2);

        LinearLayout ll = new LinearLayout(getApplicationContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(input1);
        ll.addView(input2);

        builder.setView(ll);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Database db = new Database(getApplicationContext());
                db.open();
                db.createService(input1.getText().toString(),input2.getText().toString());
                db.close();

                mAdapter.refresshItems();

                updateMyWidgets(getApplicationContext());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name the new entry");

        /** the dialog's view is going to be an EditText */
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                inputText = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void updateMyWidgets(Context context) {
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context,StatisticsWidgetProvider.class));

        for (int currentWidgetId: ids) {
            ComponentName thisWidget = new ComponentName(context, StatisticsWidgetProvider.class);
            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_provider);

            Intent intentService = new Intent(context, StatisticsWidgetProvider.class);

            intentService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, currentWidgetId);
            intentService.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentService, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setRemoteAdapter(R.id.widgetListView, intentService);
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

            AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, remoteView);
        }//*/

        /*int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context,StatisticsWidgetProvider.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(StatisticsWidgetProvider.WIDGET_IDS_KEY, ids);
        context.sendBroadcast(updateIntent);//*/
    }
}
