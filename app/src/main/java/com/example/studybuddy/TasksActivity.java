package com.example.studybuddy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.studybuddy.database.entities.Task;
import com.example.studybuddy.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    EditText edtTaskName, edtSearch;
    TextView btnAddTask, btnPickDate;
    TextView btnPriorityHigh, btnPriorityMedium, btnPriorityLow;
    TextView chipAll, chipPending, chipDone, chipHigh, chipMedium, chipLow;
    TextView tvResultCount;
    ListView lvTasks;
    LinearLayout navHome, navTasks, navReminders, navProfile;

    TaskViewModel taskViewModel;
    List<Task> allTasks = new ArrayList<>();
    ArrayList<Task> displayedTasks = new ArrayList<>();
    TaskAdapter adapter;

    String selectedPriority = "Medium";
    String selectedDate     = "";
    String activeFilter     = "All";
    String searchQuery      = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        edtTaskName       = findViewById(R.id.edtTaskName);
        edtSearch         = findViewById(R.id.edtSearch);
        btnAddTask        = findViewById(R.id.btnAddTask);
        btnPickDate       = findViewById(R.id.btnPickDate);
        btnPriorityHigh   = findViewById(R.id.btnPriorityHigh);
        btnPriorityMedium = findViewById(R.id.btnPriorityMedium);
        btnPriorityLow    = findViewById(R.id.btnPriorityLow);
        chipAll           = findViewById(R.id.chipAll);
        chipPending       = findViewById(R.id.chipPending);
        chipDone          = findViewById(R.id.chipDone);
        chipHigh          = findViewById(R.id.chipHigh);
        chipMedium        = findViewById(R.id.chipMedium);
        chipLow           = findViewById(R.id.chipLow);
        tvResultCount     = findViewById(R.id.tvResultCount);
        lvTasks           = findViewById(R.id.lvTasks);
        navHome           = findViewById(R.id.navHome);
        navTasks          = findViewById(R.id.navTasks);
        navReminders      = findViewById(R.id.navReminders);
        navProfile        = findViewById(R.id.navProfile);

        adapter = new TaskAdapter(this, displayedTasks);
        lvTasks.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getTasksForUser(Session.getEmail(this)).observe(this, tasks -> {
            allTasks = tasks;
            applyFilters();
        });

        // Search
        edtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString().trim().toLowerCase();
                applyFilters();
            }
            public void afterTextChanged(Editable s) {}
        });

        // Filter chips
        chipAll.setOnClickListener(v -> setFilter("All"));
        chipPending.setOnClickListener(v -> setFilter("Pending"));
        chipDone.setOnClickListener(v -> setFilter("Done"));
        chipHigh.setOnClickListener(v -> setFilter("High"));
        chipMedium.setOnClickListener(v -> setFilter("Medium"));
        chipLow.setOnClickListener(v -> setFilter("Low"));

        // Priority buttons for new task
        btnPriorityHigh.setOnClickListener(v -> selectPriority("High"));
        btnPriorityMedium.setOnClickListener(v -> selectPriority("Medium"));
        btnPriorityLow.setOnClickListener(v -> selectPriority("Low"));

        // Date picker
        btnPickDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                selectedDate = day + "/" + (month + 1) + "/" + year;
                btnPickDate.setText("📅  " + selectedDate);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            {{ getDatePicker().setMinDate(cal.getTimeInMillis()); }}
                    .show();
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
                edtTaskName.setText("");
                selectedDate = "";
                btnPickDate.setText("📅  Pick Due Date");
                selectedPriority = "Medium";
                selectPriority("Medium");
                Toast.makeText(TasksActivity.this, "Task added!", Toast.LENGTH_SHORT).show();
            }
        });

        // Tap = toggle complete, long press = delete
        lvTasks.setOnItemClickListener((parent, view, position, id) -> {
            Task task = displayedTasks.get(position);
            task.setCompleted(!task.isCompleted());
            taskViewModel.update(task);
        });
        lvTasks.setOnItemLongClickListener((parent, view, position, id) -> {
            taskViewModel.delete(displayedTasks.get(position));
            Toast.makeText(TasksActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
            return true;
        });

        navHome.setOnClickListener(v -> { startActivity(new Intent(TasksActivity.this, MainActivity.class)); finish(); });
        navTasks.setOnClickListener(v -> { });
        navReminders.setOnClickListener(v -> { startActivity(new Intent(TasksActivity.this, RemindersActivity.class)); finish(); });
        navProfile.setOnClickListener(v -> { startActivity(new Intent(TasksActivity.this, ProfileActivity.class)); finish(); });
    }

    private void applyFilters() {
        displayedTasks.clear();
        for (Task task : allTasks) {
            // Search filter
            if (!searchQuery.isEmpty() && !task.getTitle().toLowerCase().contains(searchQuery)) continue;

            // Chip filter
            switch (activeFilter) {
                case "Pending": if (task.isCompleted()) continue; break;
                case "Done":    if (!task.isCompleted()) continue; break;
                case "High":    if (!"High".equals(task.getPriority())) continue; break;
                case "Medium":  if (!"Medium".equals(task.getPriority())) continue; break;
                case "Low":     if (!"Low".equals(task.getPriority())) continue; break;
            }
            displayedTasks.add(task);
        }
        adapter.notifyDataSetChanged();
        tvResultCount.setText(displayedTasks.size() + " task" + (displayedTasks.size() == 1 ? "" : "s"));
    }

    private void setFilter(String filter) {
        activeFilter = filter;

        // Reset all chips
        int white = Color.parseColor("#FFFFFF");
        int purple = Color.parseColor("#7C3AED");
        chipAll.setBackgroundResource(R.drawable.chip_unselected);    chipAll.setTextColor(purple);
        chipPending.setBackgroundResource(R.drawable.chip_unselected); chipPending.setTextColor(purple);
        chipDone.setBackgroundResource(R.drawable.chip_unselected);    chipDone.setTextColor(purple);
        chipHigh.setBackgroundResource(R.drawable.chip_unselected);    chipHigh.setTextColor(Color.parseColor("#EF4444"));
        chipMedium.setBackgroundResource(R.drawable.chip_unselected);  chipMedium.setTextColor(Color.parseColor("#F59E0B"));
        chipLow.setBackgroundResource(R.drawable.chip_unselected);     chipLow.setTextColor(Color.parseColor("#10B981"));

        // Highlight active chip
        switch (filter) {
            case "All":     chipAll.setBackgroundResource(R.drawable.chip_selected);     chipAll.setTextColor(white); break;
            case "Pending": chipPending.setBackgroundResource(R.drawable.chip_selected); chipPending.setTextColor(white); break;
            case "Done":    chipDone.setBackgroundResource(R.drawable.chip_selected);    chipDone.setTextColor(white); break;
            case "High":    chipHigh.setBackgroundResource(R.drawable.chip_selected);    chipHigh.setTextColor(white); break;
            case "Medium":  chipMedium.setBackgroundResource(R.drawable.chip_selected);  chipMedium.setTextColor(white); break;
            case "Low":     chipLow.setBackgroundResource(R.drawable.chip_selected);     chipLow.setTextColor(white); break;
        }
        applyFilters();
    }

    private void selectPriority(String priority) {
        selectedPriority = priority;
        btnPriorityHigh.setBackgroundResource(R.drawable.priority_btn_unselected);   btnPriorityHigh.setTextColor(Color.parseColor("#EF4444"));
        btnPriorityMedium.setBackgroundResource(R.drawable.priority_btn_unselected); btnPriorityMedium.setTextColor(Color.parseColor("#F59E0B"));
        btnPriorityLow.setBackgroundResource(R.drawable.priority_btn_unselected);    btnPriorityLow.setTextColor(Color.parseColor("#10B981"));
        switch (priority) {
            case "High":   btnPriorityHigh.setBackgroundResource(R.drawable.priority_btn_selected_red);   btnPriorityHigh.setTextColor(Color.parseColor("#FFFFFF")); break;
            case "Medium": btnPriorityMedium.setBackgroundResource(R.drawable.priority_btn_selected_amber); btnPriorityMedium.setTextColor(Color.parseColor("#FFFFFF")); break;
            case "Low":    btnPriorityLow.setBackgroundResource(R.drawable.priority_btn_selected_green);  btnPriorityLow.setTextColor(Color.parseColor("#FFFFFF")); break;
        }
    }
}