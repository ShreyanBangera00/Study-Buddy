package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    EditText edtTaskName;
    TextView btnAddTask;
    ListView lvTasks;
    LinearLayout navHome, navTasks, navReminders, navProfile;

    TaskViewModel taskViewModel;
    ArrayList<Task> taskObjects;
    ArrayAdapter<String> adapter;
    ArrayList<String> taskTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        edtTaskName = findViewById(R.id.edtTaskName);
        btnAddTask  = findViewById(R.id.btnAddTask);
        lvTasks     = findViewById(R.id.lvTasks);

        navHome      = findViewById(R.id.navHome);
        navTasks     = findViewById(R.id.navTasks);
        navReminders = findViewById(R.id.navReminders);
        navProfile   = findViewById(R.id.navProfile);

        taskTitles  = new ArrayList<>();
        taskObjects = new ArrayList<>();
        adapter     = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskTitles);
        lvTasks.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // LiveData observer - updates list automatically when DB changes
        taskViewModel.getAllTasks().observe(this, tasks -> {
            taskTitles.clear();
            taskObjects.clear();
            for (Task t : tasks) {
                taskTitles.add(t.isCompleted() ? "[Done] " + t.getTitle() : t.getTitle());
                taskObjects.add(t);
            }
            adapter.notifyDataSetChanged();
        });

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTaskName.getText().toString().trim();
                if (title.isEmpty()) {
                    edtTaskName.setError("Enter a task name");
                    return;
                }
                taskViewModel.insert(new Task(title));
                edtTaskName.setText("");
                Toast.makeText(TasksActivity.this, "Task added!", Toast.LENGTH_SHORT).show();
            }
        });

        // Tap to mark complete/incomplete
        lvTasks.setOnItemClickListener((parent, view, position, id) -> {
            Task task = taskObjects.get(position);
            task.setCompleted(!task.isCompleted());
            taskViewModel.update(task);
        });

        // Long press to delete
        lvTasks.setOnItemLongClickListener((parent, view, position, id) -> {
            taskViewModel.delete(taskObjects.get(position));
            Toast.makeText(TasksActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
            return true;
        });

        navHome.setOnClickListener(v -> { startActivity(new Intent(TasksActivity.this, MainActivity.class)); finish(); });
        navTasks.setOnClickListener(v -> { /* Already here */ });
        navReminders.setOnClickListener(v -> { startActivity(new Intent(TasksActivity.this, RemindersActivity.class)); finish(); });
        navProfile.setOnClickListener(v -> { startActivity(new Intent(TasksActivity.this, ProfileActivity.class)); finish(); });
    }
}