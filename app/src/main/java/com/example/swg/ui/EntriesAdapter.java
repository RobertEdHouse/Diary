package com.example.swg.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.swg.R;
import com.example.swg.model.Entry;

import java.util.List;

public class EntriesAdapter extends BaseAdapter {
    private final Context context;
    private final List<Entry> entries;

    public EntriesAdapter(Context context, List<Entry> entries) {
        this.context = context;
        this.entries = entries;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Entry getItem(int i) {
        return entries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.diary_entry, parent, false);
            holder = new ViewHolder();
            holder.dateTextView = convertView
                    .findViewById(R.id.dateTextView);
            holder.timeTextView = convertView
                    .findViewById(R.id.timeTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Entry entry = getItem(i);
        String time = entry.time;
        String date = entry.date;
        holder.timeTextView.setText(time);
        holder.dateTextView.setText(date);
        return convertView;
    }
    static class ViewHolder {
        TextView dateTextView;
        TextView timeTextView;
    }
}
