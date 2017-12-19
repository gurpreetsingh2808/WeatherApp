package com.spacelabs.weatherapp.framework;


import com.spacelabs.weatherapp.BuildConfig;

import java.util.Locale;

/**
 * <code>ApplicationConstants</code> this class contains the base constants which will be used across the application.
 *
 * @author Gurpreet Singh
 * @since 12 Dec 2017
 */
public final class ApplicationConstants {

    // The default Locale of the app
    public static final String DEFAULT_LOCALE_KEY = Locale.getDefault().toString();
    // The name of the application platform
    public static final String APP_PLATFORM_NAME = "android";
    // The type of device
    public static final String DEVICE_TYPE = "mobile";
    // The heading font
    public static final String HEADING_FONT = BuildConfig.HEADING_FONT;
    // The body font
    public static final String BODY_FONT = BuildConfig.BODY_FONT;

    // Private constructor so that this class cannot be initiated as this is a singleton
    private ApplicationConstants() {
    }

    // The body font italic
//    public static final String BODY_FONT_ITALIC = BuildConfig.BODY_FONT_ITALIC;
}
