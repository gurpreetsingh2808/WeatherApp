package com.spacelabs.weatherapp.framework.util;

/**
 * Created by Gurpreet on 15/12/2016.
 */

public class StringUtil {

    /**
     * Demonstrate checking for String that is not null, not empty, and not white
     * space only using standard Java classes.
     *
     * @param string String to be checked for not null, not empty, and not white
     *               space only.
     * @return {@code true} if provided String is not null, is not empty, and
     * has at least one character that is not considered white space.
     */
    public static boolean isNotBlank(final String string) {
        return string != null && !string.isEmpty() && !string.trim().isEmpty();
    }

}
