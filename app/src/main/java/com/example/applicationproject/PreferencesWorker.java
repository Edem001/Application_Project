package com.example.applicationproject;

import android.content.SharedPreferences;
import android.util.Log;

public class PreferencesWorker {
    SharedPreferences sPref;

    public PreferencesWorker(SharedPreferences preferences){
        sPref = preferences;
    }

    public void save(String key, String value) {
        SharedPreferences.Editor edit = sPref.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public void save(String key, int value) {
        SharedPreferences.Editor edit = sPref.edit();
        edit.putString(key, Integer.toString(value));
        edit.commit();
    }

    public int loadPreferences(String key) {
        String resStr = sPref.getString(key, "");
        int a;
        if (resStr.equals(""))
            return 1;
        else
        {
            try {
                a = Integer.parseInt(resStr);
            }catch (Exception e){
                Log.e("PREF_PARSE", "ERROR OCCURRED DURING PREFERENCES PARSING");
                a = 1;
            }
        }
        return a;
    }
}
