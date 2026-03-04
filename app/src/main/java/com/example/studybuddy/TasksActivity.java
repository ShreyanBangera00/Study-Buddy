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

public class TasksActivity extends AppCompatActivity {

    EditText edtTaskName;
    Button btnAddTask;
    ListView lvTasks;
    LinearLayout navHome, navTasks, navReminders, navProfile;

    ArrayList<String> taskList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        edtTaskName = findViewById(R.id.edtTaskName);
        btnAddTask = findViewById(R.id.btnAddTask);
        lvTasks    = findViewById(R.id.lvTasks);

        navHome      = findViewById(R.id.navHome);
        navTasks     = findViewById(R.id.navTasks);
        navReminders = findViewById(R.id.navReminders);
        navProfile   = findViewById(R.id.navProfile);

        taskList = new ArrayList<>();
        adapter  = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        lvTasks.setAdapter(adapter);

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = edtTaskName.getText().toString().trim();
                if (task.isEmpty()) {
                    edtTaskName.setError("Enter a task name");
                    return;
                }
                taskList.add(task);
                adapter.notifyDataSetChanged();
                edtTaskName.setText("");
                Toast.makeText(TasksActivity.this, "Task added!", Toast.LENGTH_SHORT).show();
            }
        });


        lvTasks.setOnItemLongClickListener((parent, view, position, id) -> {
            taskList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(TasksActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
            return true;
        });

        // Bottom navigation
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TasksActivity.this, MainActivity.class));
                finish();
            }
        });

        navTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already here
            }
        });

        navReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TasksActivity.this, RemindersActivity.class));
                finish();
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TasksActivity.this, ProfileActivity.class));
                finish();
            }
        });
    }
}