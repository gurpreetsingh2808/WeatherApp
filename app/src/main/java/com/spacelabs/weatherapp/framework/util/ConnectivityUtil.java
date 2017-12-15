package com.spacelabs.weatherapp.framework.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.spacelabs.weatherapp.WeatherApplication;


/**
 * <code>ConnectivityUtil</code> provides various utility methods on checking internet connectivity
 *
 * @author Gurpreet Singh
 * @since 15-12-2017
 */
public class ConnectivityUtil {

    public static boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) WeatherApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }

}
