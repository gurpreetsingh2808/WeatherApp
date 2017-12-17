package com.spacelabs.weatherapp.framework.util;

/**
 * Created by Gurpreet on 17-12-2017.
 */

public class DateUtil {

    public String getDayName(int i) {
        if (i == 2) {
            return "Monday";
        } else if (i == 3) {
            return "Tuesday";
        } else if (i == 4) {
            return "Wednesday";
        } else if (i == 5) {
            return "Thursday";
        } else if (i == 6) {
            return "Friday";
        } else if (i == 7) {
            return "Saturday";
        } else if (i == 1) {
            return "Sunday";
        } else {
            return null;
        }
    }
}
