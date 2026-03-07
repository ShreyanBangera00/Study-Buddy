package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studybuddy.viewmodel.TaskViewModel;

public class MainActivity extends AppCompatActivity {

    LinearLayout btnAddTask, btnReminders;
    LinearLayout navHome, navTasks, navReminders, navProfile;
    TextView tvPendingCount, tvDoneCount;

    TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddTask     = findViewById(R.id.btnAddTask);
        btnReminders   = findViewById(R.id.btnReminders);
        tvPendingCount = findViewById(R.id.tvPendingCount);
        tvDoneCount    = findViewById(R.id.tvDoneCount);
        navHome        = findViewById(R.id.navHome);
        navTasks       = findViewById(R.id.navTasks);
        navReminders   = findViewById(R.id.navReminders);
        navProfile     = findViewById(R.id.navProfile);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Only count tasks for the logged-in user
        taskViewModel.getTasksForUser(Session.getEmail()).observe(this, tasks -> {
            int pending = 0, done = 0;
            for (com.example.studybuddy.database.entities.Task t : tasks) {
                if (t.isCompleted()) done++; else pending++;
            }
            tvPendingCount.setText(String.valueOf(pending));
            tvDoneCount.setText(String.valueOf(done));
        });

        btnAddTask.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TasksActivity.class)));
        btnReminders.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RemindersActivity.class)));
        navHome.setOnClickListener(v -> { });
        navTasks.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TasksActivity.class)));
        navReminders.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RemindersActivity.class)));
        navProfile.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));
    }
}