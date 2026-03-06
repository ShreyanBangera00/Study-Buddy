package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studybuddy.viewmodel.ReminderViewModel;
import com.example.studybuddy.viewmodel.TaskViewModel;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName, tvEmail, tvAvatarLetter, btnLogout;
    TextView tvTaskCount, tvReminderCount;
    LinearLayout navHome, navTasks, navReminders, navProfile;

    TaskViewModel taskViewModel;
    ReminderViewModel reminderViewModel;

    String userName  = "Student";
    String userEmail = "student@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName         = findViewById(R.id.tvName);
        tvEmail        = findViewById(R.id.tvEmail);
        tvAvatarLetter = findViewById(R.id.tvAvatarLetter);
        btnLogout      = findViewById(R.id.btnLogout);
        tvTaskCount    = findViewById(R.id.tvTaskCount);
        tvReminderCount = findViewById(R.id.tvReminderCount);

        navHome      = findViewById(R.id.navHome);
        navTasks     = findViewById(R.id.navTasks);
        navReminders = findViewById(R.id.navReminders);
        navProfile   = findViewById(R.id.navProfile);

        tvName.setText(userName);
        tvEmail.setText(userEmail);
        tvAvatarLetter.setText(String.valueOf(userName.charAt(0)).toUpperCase());

        taskViewModel     = new ViewModelProvider(this).get(TaskViewModel.class);
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        // Live task count
        taskViewModel.getAllTasks().observe(this, tasks -> {
            tvTaskCount.setText(String.valueOf(tasks.size()));
        });

        // Live reminder count
        reminderViewModel.getAllReminders().observe(this, reminders -> {
            tvReminderCount.setText(String.valueOf(reminders.size()));
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });

        navHome.setOnClickListener(v -> { startActivity(new Intent(ProfileActivity.this, MainActivity.class)); finish(); });
        navTasks.setOnClickListener(v -> { startActivity(new Intent(ProfileActivity.this, TasksActivity.class)); finish(); });
        navReminders.setOnClickListener(v -> { startActivity(new Intent(ProfileActivity.this, RemindersActivity.class)); finish(); });
        navProfile.setOnClickListener(v -> { /* Already here */ });
    }
}