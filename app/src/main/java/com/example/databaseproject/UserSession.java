package com.example.databaseproject;

public class UserSession {
    private static User currentUser;

    public static User getUser() {
        return currentUser;
    }

    public static void setCurrentUser(User u) {
        currentUser = u;
    }

    public static void clearCurrentUser() {
        currentUser = null;
    }
}
