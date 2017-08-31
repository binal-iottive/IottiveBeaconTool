package com.iottivebeacontool.iottivebeacontool;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;


import com.iottivebeacontool.iottivebeacontool.model.AddPropertyModel;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by User on 26-Oct-15.
 */
public class SharedPrefsUtils {


    public static String getStringPreference(Context context, String key) {
        String value = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            value = preferences.getString(key, null);
        }
        return value;
    }

    /**
     * Helper method to write a String value to {@link SharedPreferences}.
     *
     * @param context a {@link Context} object.
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public static boolean setStringPreference(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            return editor.commit();
        }
        return false;
    }

    public static long getLongPreference(Context context, String key) {
        long value = 0;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            value = preferences.getLong(key, 0);
        }
        return value;
    }


    public static boolean setLongPreference(Context context, String key, long value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(key, value);
            return editor.commit();
        }
        return false;
    }

    public static boolean getBooleanPreference(Context context, String key) {
        boolean value = false;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            value = preferences.getBoolean(key, true);
        }
        return value;
    }

    public static boolean setBooleanPreference(Context context, String key, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            return editor.commit();
        }
        return false;
    }


    public static ArrayList<AddPropertyModel> getArraylist(Context context, String key) {
        ArrayList<AddPropertyModel> value = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            try {
                value = (ArrayList<AddPropertyModel>) ObjectSerializer.deserialize(preferences.getString(key, ObjectSerializer.serialize(new ArrayList<AddPropertyModel>())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static boolean setArraylist(Context context, String key, ArrayList<AddPropertyModel> value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            try {
                editor.putString(key, ObjectSerializer.serialize(value));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return editor.commit();
        }
        return false;
    }



    public static ArrayList<AddPropertyModel> getInventoryPreference(Context context, String key) {
        ArrayList<AddPropertyModel> value = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            try {
                value = (ArrayList<AddPropertyModel>) ObjectSerializer.deserialize(preferences.getString(key, ObjectSerializer.serialize(new ArrayList<AddPropertyModel>())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }



    public static boolean setInventoryPreference(Context context, String key, ArrayList<AddPropertyModel> value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            try {
                editor.putString(key, ObjectSerializer.serialize(value));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return editor.commit();
        }
        return false;
    }



    ///////////////////////////////////////hashmap/////////////////////////////////////////////////////

    public static boolean saveMap(Context context,String key,Map<String,Boolean> inputMap){
        SharedPreferences pSharedPref = context.getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove(key).commit();
            editor.putString(key, jsonString);
            return editor.commit();
        }
        return false;
    }

    public static Map<String,Boolean> loadMap(Context context,String key1){
        Map<String,Boolean> outputMap = new HashMap<String,Boolean>();
        SharedPreferences pSharedPref = context.getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString(key1, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    Boolean value = (Boolean) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }

}
