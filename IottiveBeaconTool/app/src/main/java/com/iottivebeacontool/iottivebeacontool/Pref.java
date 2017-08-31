package com.iottivebeacontool.iottivebeacontool;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.iottivebeacontool.iottivebeacontool.GlobalApplication.PREF_FILE;

public class Pref {

    @Nullable
    private static SharedPreferences sharedPreferences = null;

    public static void openPref(@NonNull Context context) {

        sharedPreferences = context.getSharedPreferences(PREF_FILE,
                Context.MODE_PRIVATE);
    }

    @Nullable
    public static String getValue(@NonNull Context context, String key, String defaultValue) {
        Pref.openPref(context);
        String result = Pref.sharedPreferences.getString(key, defaultValue);
        Pref.sharedPreferences = null;
        return result;
    }

    @Nullable
    public static int getValue(@NonNull Context context, String key, int defaultValue) {
        Pref.openPref(context);
        int result = Pref.sharedPreferences.getInt(key, defaultValue);
        Pref.sharedPreferences = null;
        return result;
    }

    @Nullable
    public static Long getValue(@NonNull Context context, String key, Long defaultValue) {
        Pref.openPref(context);
        Long result = Pref.sharedPreferences.getLong(key, defaultValue);
        Pref.sharedPreferences = null;
        return result;
    }

    public static void setValue(@NonNull Context context, String key, String value) {
        Pref.openPref(context);
        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.putString(key, value);
        prefsPrivateEditor.commit();
        prefsPrivateEditor = null;
        Pref.sharedPreferences = null;
    }

    public static void setValue(@NonNull Context context, String key, int value) {
        Pref.openPref(context);
        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.putInt(key, value);
        prefsPrivateEditor.commit();
        prefsPrivateEditor = null;
        Pref.sharedPreferences = null;
    }

    public static void setValue(@NonNull Context context, String key, Long value) {
        Pref.openPref(context);
        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.putLong(key, value);
        prefsPrivateEditor.commit();
        prefsPrivateEditor = null;
        Pref.sharedPreferences = null;
    }
}
