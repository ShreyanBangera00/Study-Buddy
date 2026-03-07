package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studybuddy.viewmodel.ReminderViewModel;
import com.example.studybuddy.viewmodel.TaskViewModel;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName, tvEmail, tvAvatarLetter, btnLogout;
    TextView tvTaskCount, tvReminderCount;
    Switch switchDarkMode;
    LinearLayout navHome, navTasks, navReminders, navProfile;

    TaskViewModel taskViewModel;
    ReminderViewModel reminderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName          = findViewById(R.id.tvName);
        tvEmail         = findViewById(R.id.tvEmail);
        tvAvatarLetter  = findViewById(R.id.tvAvatarLetter);
        btnLogout       = findViewById(R.id.btnLogout);
        tvTaskCount     = findViewById(R.id.tvTaskCount);
        tvReminderCount = findViewById(R.id.tvReminderCount);
        switchDarkMode  = findViewById(R.id.switchDarkMode);
        navHome         = findViewById(R.id.navHome);
        navTasks        = findViewById(R.id.navTasks);
        navReminders    = findViewById(R.id.navReminders);
        navProfile      = findViewById(R.id.navProfile);

        tvName.setText(Session.getName());
        tvEmail.setText(Session.getEmail());
        tvAvatarLetter.setText(Session.getName().isEmpty() ? "?" : String.valueOf(Session.getName().charAt(0)).toUpperCase());

        // Set switch to current theme state
        switchDarkMode.setChecked(ThemeManager.isDarkMode(this));

        // Toggle dark mode and restart activity to apply
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ThemeManager.setDarkMode(this, isChecked);
            recreate();
        });

        taskViewModel     = new ViewModelProvider(this).get(TaskViewModel.class);
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        taskViewModel.getTasksForUser(Session.getEmail()).observe(this, tasks ->
                tvTaskCount.setText(String.valueOf(tasks.size())));

        reminderViewModel.getRemindersForUser(Session.getEmail()).observe(this, reminders ->
                tvReminderCount.setText(String.valueOf(reminders.size())));

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.logout();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });

        navHome.setOnClickListener(v -> { startActivity(new Intent(ProfileActivity.this, MainActivity.class)); finish(); });
        navTasks.setOnClickListener(v -> { startActivity(new Intent(ProfileActivity.this, TasksActivity.class)); finish(); });
        navReminders.setOnClickListener(v -> { startActivity(new Intent(ProfileActivity.this, RemindersActivity.class)); finish(); });
        navProfile.setOnClickListener(v -> { });
    }
}