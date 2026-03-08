package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.List;

public class RemindersActivity extends AppCompatActivity {

    EditText edtReminderTitle, edtReminderTime, edtSearch;
    TextView btnAddReminder, tvResultCount;
    ListView lvReminders;
    LinearLayout navHome, navTasks, navReminders, navProfile;

    ReminderViewModel reminderViewModel;
    List<Reminder> allReminders = new ArrayList<>();
    ArrayList<Reminder> displayedReminders = new ArrayList<>();
    ReminderAdapter adapter;

    String searchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        edtReminderTitle = findViewById(R.id.edtReminderTitle);
        edtReminderTime  = findViewById(R.id.edtReminderTime);
        edtSearch        = findViewById(R.id.edtSearch);
        btnAddReminder   = findViewById(R.id.btnAddReminder);
        tvResultCount    = findViewById(R.id.tvResultCount);
        lvReminders      = findViewById(R.id.lvReminders);
        navHome          = findViewById(R.id.navHome);
        navTasks         = findViewById(R.id.navTasks);
        navReminders     = findViewById(R.id.navReminders);
        navProfile       = findViewById(R.id.navProfile);

        adapter = new ReminderAdapter(this, displayedReminders);
        lvReminders.setAdapter(adapter);

        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        reminderViewModel.getRemindersForUser(Session.getEmail(this)).observe(this, reminders -> {
            allReminders = reminders;
            applySearch();
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString().trim().toLowerCase();
                applySearch();
            }
            public void afterTextChanged(Editable s) {}
        });

        btnAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtReminderTitle.getText().toString().trim();
                String time  = edtReminderTime.getText().toString().trim();
                if (title.isEmpty()) { edtReminderTitle.setError("Enter a reminder title"); return; }
                if (time.isEmpty())  { edtReminderTime.setError("Enter a time"); return; }
                reminderViewModel.insert(new Reminder(title, time, Session.getEmail(RemindersActivity.this)));
                edtReminderTitle.setText("");
                edtReminderTime.setText("");
                Toast.makeText(RemindersActivity.this, "Reminder added!", Toast.LENGTH_SHORT).show();
            }
        });

        lvReminders.setOnItemLongClickListener((parent, view, position, id) -> {
            reminderViewModel.delete(displayedReminders.get(position));
            Toast.makeText(RemindersActivity.this, "Reminder deleted", Toast.LENGTH_SHORT).show();
            return true;
        });

        navHome.setOnClickListener(v -> { startActivity(new Intent(RemindersActivity.this, MainActivity.class)); finish(); });
        navTasks.setOnClickListener(v -> { startActivity(new Intent(RemindersActivity.this, TasksActivity.class)); finish(); });
        navReminders.setOnClickListener(v -> { });
        navProfile.setOnClickListener(v -> { startActivity(new Intent(RemindersActivity.this, ProfileActivity.class)); finish(); });
    }

    private void applySearch() {
        displayedReminders.clear();
        for (Reminder r : allReminders) {
            if (!searchQuery.isEmpty() && !r.getTitle().toLowerCase().contains(searchQuery)) continue;
            displayedReminders.add(r);
        }
        adapter.notifyDataSetChanged();
        tvResultCount.setText(displayedReminders.size() + " reminder" + (displayedReminders.size() == 1 ? "" : "s"));
    }
}