package com.example.studybuddy;

import android.content.Context;
import android.content.SharedPreferences;

public class ThemeManager {

    private static final String PREFS_NAME = "ThemePrefs";
    private static final String KEY_DARK   = "isDarkMode";

    public static boolean isDarkMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_DARK, false);
    }

    public static void setDarkMode(Context context, boolean isDark) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_DARK, isDark);
        editor.apply();
    }

    public static void applyTheme(Context context) {
        if (isDarkMode(context)) {
            ((android.app.Activity) context).setTheme(R.style.Theme_StudyBuddy_Dark);
        } else {
            ((android.app.Activity) context).setTheme(R.style.Theme_StudyBuddy_Light);
        }
    }
}