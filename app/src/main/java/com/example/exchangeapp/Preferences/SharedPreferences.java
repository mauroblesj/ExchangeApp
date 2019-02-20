package com.example.exchangeapp.Preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

public class SharedPreferences {

    private static final String TAG = "PreferencesConfig";
    private android.content.SharedPreferences myPreferences;
    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "exchangePreferences";
    private Context context;
    private android.content.SharedPreferences.Editor editor;

    // Constructor
    @SuppressLint("CommitPrefEdits")
    public SharedPreferences(Context context) {
        this.context = context;
        myPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = myPreferences.edit();
    }

    public void setBaseCountry(String country){
        editor.putString("baseCountry",country);
        editor.commit();
    }

    public String getBaseCountry() {
        return myPreferences.getString("baseCountry","EUR");
    }

}
