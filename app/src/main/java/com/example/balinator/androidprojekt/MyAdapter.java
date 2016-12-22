package com.example.balinator.androidprojekt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.balinator.androidprojekt.database.Database;
import com.example.balinator.androidprojekt.services.screenonservice.MyScreenOnOffReciver;
import com.example.balinator.androidprojekt.services.screenonservice.MyScreenOnService;
import com.example.balinator.androidprojekt.services.struct.MyService;

import java.util.ArrayList;

/**
 * Created by Balinator on 2016. 12. 12..
 */
public class MyAdapter extends BaseAdapter {
    private static final String tag = "BaseAdapter";

    private ArrayList<MyService> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private Database db;
    private Context context;

    public MyAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        db = new Database(context);
        refreshItems();
    }

    public void refreshItems() {
        mData = new ArrayList<>();
        db.open();
        for(MyService s: db.getAllService()){
            mData.add(s);
        }
        db.close();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public MyService getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final MyService service = mData.get(position);
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.main_list_adapter, null);
            holder.title = (TextView)convertView.findViewById(R.id.main_list_view_item_title);
            holder.description = (TextView)convertView.findViewById(R.id.main_list_view_item_description);
            holder.aSwitch = (Switch)convertView.findViewById(R.id.main_list_view_item_switch);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.title.setText(service.getName());
        holder.description.setText(service.getDescription());
        holder.aSwitch.setChecked(service.isFavorite());
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db.open();
                db.updateService(service.getId(),service.getName(),service.getDescription(),toInt(isChecked));
                db.close();
                notifyDataset();
                MainActivity.updateMyWidgets(context);
            }

            private int toInt(boolean isChecked) {
                if (!isChecked) {
                    return 0;
                }
                return 1;
            }
        });

        holder.description.setTextColor(Color.BLACK);
        holder.title.setTextColor(Color.BLACK);
        return convertView;
    }

    private void notifyDataset() {
        Intent intent = new Intent("UPDATE_DATASET");

        context.sendBroadcast(intent);
    }

    private static class ViewHolder {
        private TextView title;
        private TextView description;
        private Switch aSwitch;
    }
}