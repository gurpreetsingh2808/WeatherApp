package com.spacelabs.weatherapp.ui.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.spacelabs.weatherapp.components.ProgressDialog;
import com.spacelabs.weatherapp.framework.util.AppUtils;
import com.spacelabs.weatherapp.framework.util.ConnectivityUtil;
import com.spacelabs.weatherapp.service.ApiErrorResponse;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Gurpreet on 15-12-2017.
 */

public abstract class BaseActivity extends AppCompatActivity
        implements BaseMvpView, BaseFragment.Callback {

    private android.app.ProgressDialog progressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void init() {
        ButterKnife.bind(this);
        AppUtils.initializeCalligraphy();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void setStatusBarColor(int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(statusBarColor);
        }
    }

    public void setStatusBarColorToWhite() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    public void makeStatusBarTransparent() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            // Set the status bar to dark-semi-transparentish
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void invertStatusBarIconColor(boolean shouldChangeStatusBarTintToDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            if (shouldChangeStatusBarTintToDark) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                decor.setSystemUiVisibility(0);
            }
        }
    }

    @Override
    public void showLoading() {
        hideLoading();
        progressDialog = ProgressDialog.init(this);
        progressDialog.show();
    }

    @Override
    public void showLoading(String message) {
        hideLoading();
        progressDialog = ProgressDialog.init(this, message);
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void onApiError(final ApiErrorResponse apiErrorResponse) {
        //  Handle any unknown error
                if (!BaseActivity.this.isFinishing()) {
                    new AlertDialog.Builder(this)
                            .setTitle("Error " + apiErrorResponse.getStatus_code())
                            .setMessage("\n" + apiErrorResponse.getMessage())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .create().show();
//                    new MicroInteraction.Builder(BaseActivity.this)
//                            .headerImage(R.drawable.failure)
//                            .title(getString(R.string.popup_failure))
//                            .content(apiErrorResponse.getMessage())
//                            .cancelable(false, false)
//                            .animation(AnimUtils.AnimDown)
//                            .typefaceTitle(ApplicationConstants.HEADING_FONT)
//                            .typefaceContent(ApplicationConstants.BODY_FONT)
//                            .typefacePositiveButton(ApplicationConstants.HEADING_FONT)
//                            .positiveButton("OKAY", new MicroInteraction.onPositiveListener() {
//                                @Override
//                                public void onPositive(Dialog dialog) {
//                                    dialog.cancel();
//                                }
//                            })
//                            .show();
                }

    }

    @Override
    public void onError(String message) {
        getSnackbar(message).show();
    }

    @Override
    public void onError(@StringRes int resId) {
        getSnackbar(resId).show();
    }

    @Override
    public Snackbar getSnackbar(String message) {
        return Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
    }

    @Override
    public Snackbar getSnackbar(@StringRes int resId) {
        return Snackbar.make(findViewById(android.R.id.content), getString(resId), Snackbar.LENGTH_LONG);
    }

    @Override
    public boolean isNetworkConnected() {
        return ConnectivityUtil.isConnected();
    }

    @Override
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    protected abstract void setUp();


//    @Override
//    public void onBackPressed() {
//        if (this.isTaskRoot()) {
//            FlowManager.moveToHomeActivity(this);
//        } else {
//            super.onBackPressed();
//        }
//    }


}
