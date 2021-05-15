package com.template.bloomweather;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class App extends Application  {

    private static App app;
    private static SharedPreferences sharedPreferences;

    public void onCreate() {
        super.onCreate();
//        registerActivityLifecycleCallbacks(this);
        app = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getAppContext());

    }

    public static Context getAppContext() {
        return app.getApplicationContext();
    }

    public static SharedPreferences getSharedPreference() {
        return sharedPreferences;
    }


}