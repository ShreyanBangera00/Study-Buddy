package com.example.studybuddy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
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
import java.util.Calendar;

public class TasksActivity extends AppCompatActivity {

    EditText edtTaskName;
    TextView btnAddTask, btnPickDate;
    TextView btnPriorityHigh, btnPriorityMedium, btnPriorityLow;
    ListView lvTasks;
    LinearLayout navHome, navTasks, navReminders, navProfile;

    TaskViewModel taskViewModel;
    ArrayList<Task> taskObjects;
    TaskAdapter adapter;

    String selectedPriority = "Medium";
    String selectedDate     = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        edtTaskName      = findViewById(R.id.edtTaskName);
        btnAddTask       = findViewById(R.id.btnAddTask);
        btnPickDate      = findViewById(R.id.btnPickDate);
        btnPriorityHigh  = findViewById(R.id.btnPriorityHigh);
        btnPriorityMedium= findViewById(R.id.btnPriorityMedium);
        btnPriorityLow   = findViewById(R.id.btnPriorityLow);
        lvTasks          = findViewById(R.id.lvTasks);
        navHome          = findViewById(R.id.navHome);
        navTasks         = findViewById(R.id.navTasks);
        navReminders     = findViewById(R.id.navReminders);
        navProfile       = findViewById(R.id.navProfile);

        taskObjects = new ArrayList<>();
        adapter     = new TaskAdapter(this, taskObjects);
        lvTasks.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getTasksForUser(Session.getEmail(this)).observe(this, tasks -> {
            taskObjects.clear();
            taskObjects.addAll(tasks);
            adapter.notifyDataSetChanged();
        });

        // Priority buttons
        btnPriorityHigh.setOnClickListener(v -> selectPriority("High"));
        btnPriorityMedium.setOnClickListener(v -> selectPriority("Medium"));
        btnPriorityLow.setOnClickListener(v -> selectPriority("Low"));

        // Date picker
        btnPickDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(
                    TasksActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        btnPickDate.setText("📅  " + selectedDate);
                        btnPickDate.setTextColor(Color.parseColor("#7C3AED"));
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            );
            dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
            dialog.show();
        });

        // Add task
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTaskName.getText().toString().trim();
                if (title.isEmpty()) { edtTaskName.setError("Enter a task name"); return; }

                Task task = new Task(title, Session.getEmail(TasksActivity.this));
                task.setPriority(selectedPriority);
                task.setDueDate(selectedDate);
                taskViewModel.insert(task);

                // Reset form
                edtTaskName.setText("");
                selectedDate = "";
                btnPickDate.setText("📅  Pick Due Date");
                selectedPriority = "Medium";
                selectPriority("Medium");

                Toast.makeText(TasksActivity.this, "Task added!", Toast.LENGTH_SHORT).show();
            }
        });

        // Tap to complete, long press to delete
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

    private void selectPriority(String priority) {
        selectedPriority = priority;

        // Reset all to unselected style
        btnPriorityHigh.setBackgroundResource(R.drawable.priority_btn_unselected);
        btnPriorityHigh.setTextColor(Color.parseColor("#EF4444"));

        btnPriorityMedium.setBackgroundResource(R.drawable.priority_btn_unselected);
        btnPriorityMedium.setTextColor(Color.parseColor("#F59E0B"));

        btnPriorityLow.setBackgroundResource(R.drawable.priority_btn_unselected);
        btnPriorityLow.setTextColor(Color.parseColor("#10B981"));

        // Highlight selected
        switch (priority) {
            case "High":
                btnPriorityHigh.setBackgroundResource(R.drawable.priority_btn_selected_red);
                btnPriorityHigh.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case "Medium":
                btnPriorityMedium.setBackgroundResource(R.drawable.priority_btn_selected_amber);
                btnPriorityMedium.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case "Low":
                btnPriorityLow.setBackgroundResource(R.drawable.priority_btn_selected_green);
                btnPriorityLow.setTextColor(Color.parseColor("#FFFFFF"));
                break;
        }
    }
}