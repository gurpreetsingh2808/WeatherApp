package com.spacelabs.weatherapp;

import android.app.Application;
import android.content.Context;

/**
 * This is the main starting point of the application.
 *
 * @author Gurpreet Singh
 * @since 15-12-2017
 */

public class WeatherApplication extends Application {

    private static WeatherApplication mApplication;

    public static WeatherApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

}
