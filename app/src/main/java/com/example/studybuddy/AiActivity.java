package com.example.studybuddy;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
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

    private static final String TAG = "AiActivity";
    private static final String API_KEY = "";
    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    EditText edtMessage;
    TextView btnSend, btnBack, btnClearChat;
    ListView lvChat;
    LinearLayout layoutTyping;

    List<ChatMessage> messages = new ArrayList<>();
    JSONArray conversationHistory = new JSONArray();
    ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
            // Prime conversation on first message
            if (conversationHistory.length() == 0) {
                JSONObject systemMsg = new JSONObject();
                systemMsg.put("role", "user");
                JSONArray systemParts = new JSONArray();
                systemParts.put(new JSONObject().put("text",
                        "You are a helpful AI Study Assistant inside a student app called StudyBuddy. " +
                                "Help students understand concepts, summarize topics, create practice questions, " +
                                "give study tips, and explain things clearly. Keep responses concise and friendly. " +
                                "Use plain text only, no markdown symbols like ** or ##."));
                systemMsg.put("parts", systemParts);
                conversationHistory.put(systemMsg);

                JSONObject ackMsg = new JSONObject();
                ackMsg.put("role", "model");
                JSONArray ackParts = new JSONArray();
                ackParts.put(new JSONObject().put("text", "Got it! I'm ready to help you study."));
                ackMsg.put("parts", ackParts);
                conversationHistory.put(ackMsg);
            }

            // Add user message
            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            JSONArray userParts = new JSONArray();
            userParts.put(new JSONObject().put("text", userText));
            userMsg.put("parts", userParts);
            conversationHistory.put(userMsg);

            // Build request
            JSONObject requestBody = new JSONObject();
            requestBody.put("contents", conversationHistory);

            JSONObject genConfig = new JSONObject();
            genConfig.put("temperature", 0.7);
            genConfig.put("maxOutputTokens", 800);
            requestBody.put("generationConfig", genConfig);

            String bodyStr = requestBody.toString();
            Log.d(TAG, "Sending request...");

            // HTTP call — API key in header
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("x-goog-api-key", API_KEY);
            conn.setDoOutput(true);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);

            OutputStream os = conn.getOutputStream();
            os.write(bodyStr.getBytes("UTF-8"));
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Response code: " + responseCode);

            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();

            if (responseCode != 200) {
                Log.e(TAG, "API error " + responseCode + ": " + sb.toString());
                return null;
            }

            // Parse response
            JSONObject jsonResponse = new JSONObject(sb.toString());
            String aiText = jsonResponse
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

            // Save to history
            JSONObject aiMsg = new JSONObject();
            aiMsg.put("role", "model");
            JSONArray aiParts = new JSONArray();
            aiParts.put(new JSONObject().put("text", aiText));
            aiMsg.put("parts", aiParts);
            conversationHistory.put(aiMsg);

            return aiText.trim();

        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage(), e);
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