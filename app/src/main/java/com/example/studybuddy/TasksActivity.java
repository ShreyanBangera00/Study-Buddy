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

import com.example.studybuddy.database.entities.Task;
import com.example.studybuddy.viewmodel.TaskViewModel;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity {

    EditText edtTaskName;
    TextView btnAddTask;
    ListView lvTasks;
    LinearLayout navHome, navTasks, navReminders, navProfile;

    TaskViewModel taskViewModel;
    ArrayList<Task> taskObjects;
    TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        edtTaskName  = findViewById(R.id.edtTaskName);
        btnAddTask   = findViewById(R.id.btnAddTask);
        lvTasks      = findViewById(R.id.lvTasks);
        navHome      = findViewById(R.id.navHome);
        navTasks     = findViewById(R.id.navTasks);
        navReminders = findViewById(R.id.navReminders);
        navProfile   = findViewById(R.id.navProfile);

        taskObjects = new ArrayList<>();
        adapter     = new TaskAdapter(this, taskObjects);
        lvTasks.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Only load tasks for the logged-in user
        taskViewModel.getTasksForUser(Session.getEmail()).observe(this, tasks -> {
            taskObjects.clear();
            taskObjects.addAll(tasks);
            adapter.notifyDataSetChanged();
        });

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTaskName.getText().toString().trim();
                if (title.isEmpty()) { edtTaskName.setError("Enter a task name"); return; }
                taskViewModel.insert(new Task(title, Session.getEmail()));
                edtTaskName.setText("");
                Toast.makeText(TasksActivity.this, "Task added!", Toast.LENGTH_SHORT).show();
            }
        });

        lvTasks.setOnItemClickListener((parent, view, position, id) -> {
            Task task = taskObjects.get(position);
            task.setCompleted(!task.isCompleted());
            taskViewModel.update(task);
        });

        lvTasks.setOnItemLongClickListener((parent, view, position, id) -> {
            taskViewModel.delete(taskObjects.get(position));
            Toast.makeText(TasksActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
            return true;
        });

        navHome.setOnClickListener(v -> { startActivity(new Intent(TasksActivity.this, MainActivity.class)); finish(); });
        navTasks.setOnClickListener(v -> { });
        navReminders.setOnClickListener(v -> { startActivity(new Intent(TasksActivity.this, RemindersActivity.class)); finish(); });
        navProfile.setOnClickListener(v -> { startActivity(new Intent(TasksActivity.this, ProfileActivity.class)); finish(); });
    }
}