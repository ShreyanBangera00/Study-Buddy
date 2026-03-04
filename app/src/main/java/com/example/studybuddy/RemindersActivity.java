package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RemindersActivity extends AppCompatActivity {

    EditText edtReminderTitle, edtReminderTime;
    Button btnAddReminder;
    ListView lvReminders;
    LinearLayout navHome, navTasks, navReminders, navProfile;

    ArrayList<String> reminderList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        edtReminderTitle = findViewById(R.id.edtReminderTitle);
        edtReminderTime  = findViewById(R.id.edtReminderTime);
        btnAddReminder  = findViewById(R.id.btnAddReminder);
        lvReminders     = findViewById(R.id.lvReminders);

        navHome      = findViewById(R.id.navHome);
        navTasks     = findViewById(R.id.navTasks);
        navReminders = findViewById(R.id.navReminders);
        navProfile   = findViewById(R.id.navProfile);

        reminderList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reminderList);
        lvReminders.setAdapter(adapter);

        btnAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtReminderTitle.getText().toString().trim();
                String time  = edtReminderTime.getText().toString().trim();

                if (title.isEmpty()) {
                    edtReminderTitle.setError("Enter a reminder title");
                    return;
                }
                if (time.isEmpty()) {
                    edtReminderTime.setError("Enter a time");
                    return;
                }

                reminderList.add(title + " - " + time);
                adapter.notifyDataSetChanged();
                edtReminderTitle.setText("");
                edtReminderTime.setText("");
                Toast.makeText(RemindersActivity.this, "Reminder added!", Toast.LENGTH_SHORT).show();
            }
        });

        // Long press to delete
        lvReminders.setOnItemLongClickListener((parent, view, position, id) -> {
            reminderList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(RemindersActivity.this, "Reminder deleted", Toast.LENGTH_SHORT).show();
            return true;
        });

        // Bottom navigation
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RemindersActivity.this, MainActivity.class));
                finish();
            }
        });

        navTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RemindersActivity.this, TasksActivity.class));
                finish();
            }
        });

        navReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already here
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RemindersActivity.this, ProfileActivity.class));
                finish();
            }
        });
    }
}