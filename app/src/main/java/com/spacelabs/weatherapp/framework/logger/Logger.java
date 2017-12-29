package com.spacelabs.weatherapp.framework.logger;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.spacelabs.weatherapp.framework.util.AppUtils;

/**
 * Created by Gurpreet on 15-12-2017.
 */

public class Logger {

    private static final String TAG = Logger.class.getSimpleName();
    private static Boolean flag = !AppUtils.isProductionBuild();
//    private static Boolean flag = true;

    /**
     * To avoid printing stacktraces in debug builds and report caught exceptions in production/release build
     *
     * @param throwable
     */
    public static void handleCaughtException(Throwable throwable) {
        if (flag) {
            throwable.printStackTrace();
        } else {
//            Report crash
        }
    }


    public static void d(String statement) {
        if (flag) {
            Log.d(getTag(), getMessage(statement));
        }
    }

    public static void d(String tag, String statement) {
        if (flag) {
            Log.d(getTag(), getMessage(statement));
        }
    }


    public static void e(String statement) {
        if (flag) {
            Log.e(getTag(), getMessage(statement));
        }
    }

    public static void e(String tag, String statement, Exception e) {
        if (flag) {
            Log.e(tag, statement);
        }
    }

    public static void i(String statement) {
        if (flag) {
            Log.i(getTag(), getMessage(statement));
        }
    }

    private static String getTag() {
        StackTraceElement element = getElement();
        return new StringBuilder(element.getClassName()).substring(element.getClassName().lastIndexOf(".") + 1);
    }

    private static String getMessage(String msg) {
        StackTraceElement element = getElement();
        int lineNumber = element.getLineNumber();
        String methodName = element.getMethodName();
        return "line : " + lineNumber + ",  method : " + methodName + "(), MSG : " + msg;
    }

    private static StackTraceElement getElement() {
        return Thread.currentThread().getStackTrace()[5];
    }

    public static void showToast(Context context, String message) {
        if (flag) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
