package com.example.studybuddy;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private static final String PREFS_NAME = "SessionPrefs";
    private static final String KEY_EMAIL  = "email";
    private static final String KEY_NAME   = "name";

    public static void login(Context context, String email, String name) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.apply();
    }

    public static void logout(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        String email = getEmail(context);
        return email != null && !email.isEmpty();
    }

    public static String getEmail(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_EMAIL, "");
    }

    public static String getName(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_NAME, "");
    }
}