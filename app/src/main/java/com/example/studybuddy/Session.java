package com.example.studybuddy;

public class Session {
    private static String currentUserEmail = "";
    private static String currentUserName  = "";

    public static void login(String email, String name) {
        currentUserEmail = email;
        currentUserName  = name;
    }

    public static void logout() {
        currentUserEmail = "";
        currentUserName  = "";
    }

    public static String getEmail() { return currentUserEmail; }
    public static String getName()  { return currentUserName; }
}