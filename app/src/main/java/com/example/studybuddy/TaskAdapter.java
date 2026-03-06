package com.example.studybuddy;

import android.content.Context;
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

        TextView tvTitle  = convertView.findViewById(R.id.tvTaskTitle);
        TextView tvStatus = convertView.findViewById(R.id.tvTaskStatus);
        TextView tvCheck  = convertView.findViewById(R.id.tvTaskCheck);
        TextView tvDot    = convertView.findViewById(R.id.tvTaskDot);

        tvTitle.setText(task.getTitle());

        if (task.isCompleted()) {
            tvTitle.setPaintFlags(tvTitle.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            tvTitle.setTextColor(0xFF9CA3AF);
            tvStatus.setText("Completed");
            tvStatus.setTextColor(0xFF10B981);
            tvCheck.setBackground(context.getResources().getDrawable(R.drawable.check_bg_active));
            tvCheck.setTextColor(0xFFFFFFFF);
            tvDot.setBackground(context.getResources().getDrawable(R.drawable.dot_green));
            convertView.setBackground(context.getResources().getDrawable(R.drawable.task_item_done_bg));
        } else {
            tvTitle.setPaintFlags(tvTitle.getPaintFlags() & (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));
            tvTitle.setTextColor(0xFF1E1B4B);
            tvStatus.setText("Tap to complete  ·  Long press to delete");
            tvStatus.setTextColor(0xFF9CA3AF);
            tvCheck.setBackground(context.getResources().getDrawable(R.drawable.check_bg_inactive));
            tvCheck.setTextColor(0xFF9CA3AF);
            tvDot.setBackground(context.getResources().getDrawable(R.drawable.dot_purple));
            convertView.setBackground(context.getResources().getDrawable(R.drawable.task_item_bg));
        }

        return convertView;
    }
}