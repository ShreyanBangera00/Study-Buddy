package com.example.studybuddy;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.studybuddy.database.entities.Task;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    private Context context;
    private List<Task> tasks;

    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
        this.context = context;
        this.tasks   = tasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        }

        Task task = tasks.get(position);

        TextView tvTitle    = convertView.findViewById(R.id.tvTaskTitle);
        TextView tvStatus   = convertView.findViewById(R.id.tvTaskStatus);
        TextView tvCheck    = convertView.findViewById(R.id.tvTaskCheck);
        TextView tvDot      = convertView.findViewById(R.id.tvTaskDot);
        TextView tvPriority = convertView.findViewById(R.id.tvTaskPriority);
        TextView tvDueDate  = convertView.findViewById(R.id.tvTaskDueDate);

        tvTitle.setText(task.getTitle());

        // Priority badge
        String priority = task.getPriority() != null ? task.getPriority() : "Medium";
        tvPriority.setText(priority);
        switch (priority) {
            case "High":
                tvPriority.setBackgroundResource(R.drawable.badge_red);
                tvPriority.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case "Low":
                tvPriority.setBackgroundResource(R.drawable.badge_green);
                tvPriority.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            default:
                tvPriority.setBackgroundResource(R.drawable.badge_amber);
                tvPriority.setTextColor(Color.parseColor("#FFFFFF"));
                break;
        }

        // Due date
        String dueDate = task.getDueDate();
        if (dueDate != null && !dueDate.isEmpty()) {
            tvDueDate.setVisibility(View.VISIBLE);
            tvDueDate.setText("📅 " + dueDate);
        } else {
            tvDueDate.setVisibility(View.GONE);
        }

        if (task.isCompleted()) {
            tvTitle.setPaintFlags(tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvTitle.setTextColor(Color.parseColor("#9CA3AF"));
            tvStatus.setText("Completed ✓");
            tvStatus.setTextColor(Color.parseColor("#10B981"));
            tvCheck.setBackground(context.getResources().getDrawable(R.drawable.check_bg_active));
            tvDot.setBackground(context.getResources().getDrawable(R.drawable.dot_green));
            convertView.setBackground(context.getResources().getDrawable(R.drawable.task_item_done_bg));
        } else {
            tvTitle.setPaintFlags(tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            tvTitle.setTextColor(Color.parseColor("#1E1B4B"));
            tvStatus.setText("Tap to complete  ·  Long press to delete");
            tvStatus.setTextColor(Color.parseColor("#9CA3AF"));
            tvCheck.setBackground(context.getResources().getDrawable(R.drawable.check_bg_inactive));
            tvDot.setBackground(context.getResources().getDrawable(R.drawable.dot_purple));
            convertView.setBackground(context.getResources().getDrawable(R.drawable.task_item_bg));
        }

        return convertView;
    }
}