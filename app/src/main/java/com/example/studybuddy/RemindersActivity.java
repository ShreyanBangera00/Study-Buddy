package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studybuddy.database.entities.Reminder;
import com.example.studybuddy.viewmodel.ReminderViewModel;

import java.util.ArrayList;

public class RemindersActivity extends AppCompatActivity {

    EditText edtReminderTitle, edtReminderTime;
    TextView btnAddReminder;
    ListView lvReminders;
    LinearLayout navHome, navTasks, navReminders, navProfile;

    ReminderViewModel reminderViewModel;
    ArrayList<Reminder> reminderObjects;
    ReminderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        edtReminderTitle = findViewById(R.id.edtReminderTitle);
        edtReminderTime  = findViewById(R.id.edtReminderTime);
        btnAddReminder   = findViewById(R.id.btnAddReminder);
        lvReminders      = findViewById(R.id.lvReminders);
        navHome          = findViewById(R.id.navHome);
        navTasks         = findViewById(R.id.navTasks);
        navReminders     = findViewById(R.id.navReminders);
        navProfile       = findViewById(R.id.navProfile);

        reminderObjects = new ArrayList<>();
        adapter         = new ReminderAdapter(this, reminderObjects);
        lvReminders.setAdapter(adapter);

        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        // Only load reminders for the logged-in user
        reminderViewModel.getRemindersForUser(Session.getEmail()).observe(this, reminders -> {
            reminderObjects.clear();
            reminderObjects.addAll(reminders);
            adapter.notifyDataSetChanged();
        });

        btnAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtReminderTitle.getText().toString().trim();
                String time  = edtReminderTime.getText().toString().trim();
                if (title.isEmpty()) { edtReminderTitle.setError("Enter a reminder title"); return; }
                if (time.isEmpty())  { edtReminderTime.setError("Enter a time"); return; }
                reminderViewModel.insert(new Reminder(title, time, Session.getEmail()));
                edtReminderTitle.setText("");
                edtReminderTime.setText("");
                Toast.makeText(RemindersActivity.this, "Reminder added!", Toast.LENGTH_SHORT).show();
            }
        });

        lvReminders.setOnItemLongClickListener((parent, view, position, id) -> {
            reminderViewModel.delete(reminderObjects.get(position));
            Toast.makeText(RemindersActivity.this, "Reminder deleted", Toast.LENGTH_SHORT).show();
            return true;
        });

        navHome.setOnClickListener(v -> { startActivity(new Intent(RemindersActivity.this, MainActivity.class)); finish(); });
        navTasks.setOnClickListener(v -> { startActivity(new Intent(RemindersActivity.this, TasksActivity.class)); finish(); });
        navReminders.setOnClickListener(v -> { });
        navProfile.setOnClickListener(v -> { startActivity(new Intent(RemindersActivity.this, ProfileActivity.class)); finish(); });
    }
}