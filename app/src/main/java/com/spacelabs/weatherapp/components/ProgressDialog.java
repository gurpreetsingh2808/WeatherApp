package com.spacelabs.weatherapp.components;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Created by Gurpreet on 15/12/2017.
 */
public class ProgressDialog {

    private static android.app.ProgressDialog progressDialog;

    public static android.app.ProgressDialog init(Context context) {
        return init(context, "Loading");
    }

    public static android.app.ProgressDialog init(Context context, String dialogText) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(context);
        progressDialog = new android.app.ProgressDialog(contextWeakReference.get());
        progressDialog.setCancelable(true);
        progressDialog.setMessage(dialogText);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        return progressDialog;
    }

}
