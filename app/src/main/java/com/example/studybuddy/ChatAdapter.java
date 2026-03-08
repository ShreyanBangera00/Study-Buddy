package com.example.studybuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<ChatMessage> {

    private Context context;
    private List<ChatMessage> messages;

    public ChatAdapter(Context context, List<ChatMessage> messages) {
        super(context, 0, messages);
        this.context  = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage msg = messages.get(position);

        if (msg.getType() == ChatMessage.TYPE_USER) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_user, parent, false);
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_ai, parent, false);
        }

        TextView tvMessage = convertView.findViewById(R.id.tvMessage);
        tvMessage.setText(msg.getText());
        return convertView;
    }
}