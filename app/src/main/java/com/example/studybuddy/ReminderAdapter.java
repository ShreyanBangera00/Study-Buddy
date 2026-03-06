package com.example.studybuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.studybuddy.database.entities.Reminder;

import java.util.List;

public class ReminderAdapter extends ArrayAdapter<Reminder> {

    private Context context;
    private List<Reminder> reminders;

    public ReminderAdapter(Context context, List<Reminder> reminders) {
        super(context, 0, reminders);
        this.context   = context;
        this.reminders = reminders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_reminder, parent, false);
        }

        Reminder reminder = reminders.get(position);

        TextView tvTitle = convertView.findViewById(R.id.tvReminderTitle);
        TextView tvTime  = convertView.findViewById(R.id.tvReminderTime);

        tvTitle.setText(reminder.getTitle());
        tvTime.setText("⏰  " + reminder.getTime());

        return convertView;
    }
}