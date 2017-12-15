package com.spacelabs.weatherapp.framework.util;


import android.provider.Settings;

import com.spacelabs.weatherapp.BuildConfig;
import com.spacelabs.weatherapp.R;
import com.spacelabs.weatherapp.WeatherApplication;
import com.spacelabs.weatherapp.framework.ApplicationConstants;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * <code>AppUtils</code> is a utility class which provides various re-usable and convenience
 * functions. Only generic and application wide functions should be included here
 *
 * @author Gurpreet Singh
 * @since 15-11-2017
 */
public class AppUtils {

    // The tag declaration
    private static final String TAG = AppUtils.class.getSimpleName();

    /**
     * This method returns the application version currently running
     *
     * @return
     */
    public static String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }

    /***
     * This method returns whether the build type is production
     *
     * @return
     */
    public static Boolean isProductionBuild() {
        return BuildConfig.BUILD_TYPE.equalsIgnoreCase("RELEASE");
    }

    /**
     * This method returns the unique device id on which the app is running
     *
     * @return
     */
    public static String getDeviceId() {
        return Settings.Secure.getString(WeatherApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * This method initializes calligraphy(fonts) in the system
     */
    public static void initializeCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(ApplicationConstants.BODY_FONT)
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
