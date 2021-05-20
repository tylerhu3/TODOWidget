package com.hu.tyler.todowidget;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SavedPreferences {
    private static SavedPreferences single_instance = null;
    private Context mainContext;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // private constructor restricted to this class itself
    private SavedPreferences(Context context) {
        if (mainContext != null)
            return;
        mainContext = context;
        pref = mainContext.getSharedPreferences("TodoSavePreferences", MODE_PRIVATE);
        editor = pref.edit();
    }

    public void put(String key, String value) {
        if (mainContext == null)
            return;
        editor.putString(key, value);
        editor.commit();
    }

    public void put(String key, float value) {
        if (mainContext == null)
            return;
        editor.putFloat(key, value);
        editor.commit();
    }

    public void put(String key, int value) {
        if (mainContext == null)
            return;
        editor.putInt(key, value);
        editor.commit();
    }

    public void put(String key, boolean value) {
        if (mainContext == null)
            return;
        editor.putBoolean(key, value);
        editor.commit();
    }


    public String get(String key, String value) {
        if (mainContext == null)
            return "";
        return pref.getString(key, value);
    }

    public float get(String key, float value) {
        if (mainContext == null)
            return 0;
        return pref.getFloat(key, value);
    }

    public int get(String key, int value) {
        if (mainContext == null)
            return 0;
        return pref.getInt(key, value);
    }

    public boolean get(String key, boolean value) {
        if (mainContext == null)
            return true;
        return pref.getBoolean(key, value);
    }

    // static method to create instance of Singleton class
    public static SavedPreferences getInstance(Context context) {
        if (single_instance == null){
            single_instance = new SavedPreferences(context);
        }
        return single_instance;
    }
}
