package com.mosis.treasurehunt.wrappers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesWrapper {
    private static SharedPreferencesWrapper instance;
    private SharedPreferences sharedPreferences;

    private static final String USER_DETAILS = "user_details";

    public static SharedPreferencesWrapper getInstance() {
        return instance;
    }

    public static void createInstance (Context context) {
        if (instance == null) {
            instance = new SharedPreferencesWrapper(context);
        }
    }


    private SharedPreferencesWrapper(Context context) {
        sharedPreferences = context.getSharedPreferences(USER_DETAILS, Context.MODE_PRIVATE);
    }

    public void putUsername(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("username", username);
        editor.apply();
    }

    public void putFullName(String fullName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("fullName", fullName);
        editor.apply();
    }

    public String getUsername() {
        return sharedPreferences.getString("username", "");
    }

    public String getFullName() {
        return sharedPreferences.getString("fullName", "");
    }

    public void clearUserDetails() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove("username");
        editor.remove("fullName");
        editor.apply();
    }

    public boolean containsUsername(String username) {
        return sharedPreferences.contains(username);
    }
}
