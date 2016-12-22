package com.example.balinator.androidprojekt;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.example.balinator.androidprojekt.database.Database;
import com.example.balinator.androidprojekt.services.struct.MyService;
import com.example.balinator.androidprojekt.widget.WidgetProvider;
import com.example.balinator.androidprojekt.widget.WidgetRefresher;

public class MainActivity extends AppCompatActivity {
    private static final String tag = "MainActivity";

    private Context context;
    private ListView mListView;
    private MyAdapter mAdapter;
    private BroadcastReceiver mReceiver_deleteService;

    private Database db;
    private WidgetRefresher widgetRefresher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        if(db == null){
            db = new Database(getApplicationContext());
        }

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

        db.open();
        db.checkServices();
        db.close();

        mReceiver_deleteService = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int viewID = intent.getIntExtra("viewID", 0);
                long serviceID = intent.getLongExtra("serviceID", 0);
                db.open();
                MyService serviceToDelete = db.getService(serviceID);
                db.close();
                View viewToDelete = findViewById(viewID);
                showDeleteServiceDialog(serviceToDelete, viewToDelete);
            }
        };
        registerReceiver(mReceiver_deleteService, new IntentFilter("REMOVE_SERVICE"));

        int updateRate = 60;
        String startState = Intent.ACTION_SCREEN_ON;
        String endState = Intent.ACTION_SCREEN_OFF;
        widgetRefresher = new WidgetRefresher(context, updateRate, startState, endState);
        registerReceiver(widgetRefresher, new IntentFilter(Intent.ACTION_SCREEN_ON));
        //registerReceiver(widgetRefresher, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mReceiver_deleteService != null) {
            unregisterReceiver(mReceiver_deleteService);
            mReceiver_deleteService = null;
        }
        if (widgetRefresher != null) {
            unregisterReceiver(widgetRefresher);
            widgetRefresher = null;
        }
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
                //showAddServiceDialog();
                return true;
            case R.id.action_check_services:
                db.open();
                db.checkServices();
                db.close();
                mAdapter.refreshItems();
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

                mAdapter.refreshItems();

                updateMyWidgets(context);
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
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetListView);
    }

    private void showDeleteServiceDialog(final MyService service, final View viewToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("Do you wish to delete: " + service.getName() + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // remove service
                db.open();
                db.deleteService(service);
                db.close();

                // remove service's view
                mListView.removeViewInLayout(viewToDelete);
                mAdapter.notifyDataSetChanged();
                MainActivity.updateMyWidgets(context);
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
}
