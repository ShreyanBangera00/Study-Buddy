package com.example.studybuddy;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AiActivity extends AppCompatActivity {

    private static final String API_KEY = "AIzaSyDpO4s187fhS3tHja-xJxkGSYUODlFm4Ts";
    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    EditText edtMessage;
    TextView btnSend, btnBack, btnClearChat;
    ListView lvChat;
    LinearLayout layoutTyping;

    List<ChatMessage> messages = new ArrayList<>();
    JSONArray conversationHistory = new JSONArray();
    ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai);

        edtMessage   = findViewById(R.id.edtMessage);
        btnSend      = findViewById(R.id.btnSend);
        btnBack      = findViewById(R.id.btnBack);
        btnClearChat = findViewById(R.id.btnClearChat);
        lvChat       = findViewById(R.id.lvChat);
        layoutTyping = findViewById(R.id.layoutTyping);

        adapter = new ChatAdapter(this, messages);
        lvChat.setAdapter(adapter);

        String name = Session.getName(this);
        addAiMessage("Hi " + name + "! I'm your AI Study Assistant. Ask me anything — explanations, summaries, practice questions, study tips, or help with any subject!");

        btnSend.setOnClickListener(v -> sendMessage());

        btnBack.setOnClickListener(v -> finish());

        btnClearChat.setOnClickListener(v -> {
            messages.clear();
            conversationHistory = new JSONArray();
            addAiMessage("Chat cleared! Ask me anything.");
            adapter.notifyDataSetChanged();
        });
    }

    private void sendMessage() {
        String text = edtMessage.getText().toString().trim();
        if (text.isEmpty()) return;

        edtMessage.setText("");
        addUserMessage(text);
        layoutTyping.setVisibility(View.VISIBLE);
        btnSend.setEnabled(false);

        new Thread(() -> {
            String response = callGeminiApi(text);
            runOnUiThread(() -> {
                layoutTyping.setVisibility(View.GONE);
                btnSend.setEnabled(true);
                if (response != null) {
                    addAiMessage(response);
                } else {
                    addAiMessage("Sorry, I could not get a response. Please check your internet and try again.");
                }
            });
        }).start();
    }

    private String callGeminiApi(String userText) {
        try {
            // Add user message to history
            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            JSONArray userParts = new JSONArray();
            userParts.put(new JSONObject().put("text", userText));
            userMsg.put("parts", userParts);
            conversationHistory.put(userMsg);

            // Build request body
            JSONObject requestBody = new JSONObject();

            // System instruction
            JSONObject systemInstruction = new JSONObject();
            JSONArray sysParts = new JSONArray();
            sysParts.put(new JSONObject().put("text",
                    "You are a helpful AI Study Assistant inside a student productivity app called StudyBuddy. " +
                            "Help students understand concepts, summarize topics, create practice questions, give study tips, " +
                            "and explain things clearly. Keep responses concise and friendly. Use simple formatting — " +
                            "avoid markdown symbols like ** or ## since this is a mobile app. Use plain text only."));
            systemInstruction.put("parts", sysParts);
            requestBody.put("system_instruction", systemInstruction);

            requestBody.put("contents", conversationHistory);

            JSONObject genConfig = new JSONObject();
            genConfig.put("temperature", 0.7);
            genConfig.put("maxOutputTokens", 800);
            requestBody.put("generationConfig", genConfig);

            // HTTP call
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);

            OutputStream os = conn.getOutputStream();
            os.write(requestBody.toString().getBytes("UTF-8"));
            os.close();

            int responseCode = conn.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();

            if (responseCode != 200) return null;

            // Parse response
            JSONObject jsonResponse = new JSONObject(sb.toString());
            String aiText = jsonResponse
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

            // Add AI response to history
            JSONObject aiMsg = new JSONObject();
            aiMsg.put("role", "model");
            JSONArray aiParts = new JSONArray();
            aiParts.put(new JSONObject().put("text", aiText));
            aiMsg.put("parts", aiParts);
            conversationHistory.put(aiMsg);

            return aiText.trim();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addUserMessage(String text) {
        messages.add(new ChatMessage(text, ChatMessage.TYPE_USER));
        adapter.notifyDataSetChanged();
        lvChat.setSelection(messages.size() - 1);
    }

    private void addAiMessage(String text) {
        messages.add(new ChatMessage(text, ChatMessage.TYPE_AI));
        adapter.notifyDataSetChanged();
        lvChat.setSelection(messages.size() - 1);
    }
}