package com.spacelabs.weatherapp.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewTreeObserver;

import com.spacelabs.weatherapp.framework.logger.Logger;
import com.spacelabs.weatherapp.framework.util.ConnectivityUtil;
import com.spacelabs.weatherapp.service.ApiErrorResponse;

import butterknife.Unbinder;

/**
 * Created by Gurpreet on 15-11-2017.
 */

public abstract class BaseFragment extends Fragment implements BaseMvpView {

    private BaseActivity mActivity;
    private Unbinder mUnBinder;
    private ProgressDialog mProgressDialog;
    private KeyboardListenerCallback keyboardListenerCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = com.spacelabs.weatherapp.components.ProgressDialog.init(this.getContext());
        mProgressDialog.show();
    }

    @Override
    public void showLoading(String message) {
        hideLoading();
        mProgressDialog = com.spacelabs.weatherapp.components.ProgressDialog.init(this.getContext(), message);
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void onApiError(ApiErrorResponse apiErrorResponse) {
        if (mActivity != null) {
            mActivity.onApiError(apiErrorResponse);
        }
    }

    @Override
    public Snackbar getSnackbar(String message) {
        if (mActivity != null) {
            return mActivity.getSnackbar(message);
        }
        return null;
    }

    @Override
    public Snackbar getSnackbar(@StringRes int resId) {
        if (mActivity != null) {
            return mActivity.getSnackbar(resId);
        }
        return null;
    }

    @Override
    public void onError(String message) {
        getSnackbar(message);
    }

    @Override
    public void onError(@StringRes int resId) {
        getSnackbar(resId);
    }

    @Override
    public boolean isNetworkConnected() {
        return ConnectivityUtil.isConnected();
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    protected abstract void setUp(View view);

    @Override
    public void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }


    public void setKeyboardVisibilityListener(final View contentView, final KeyboardListenerCallback keyboardListenerCallback) {
        this.keyboardListenerCallback = keyboardListenerCallback;
        if (contentView != null) {
            contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    Rect r = new Rect();
                    contentView.getWindowVisibleDisplayFrame(r);
                    int screenHeight = contentView.getRootView().getHeight();

                    // r.bottom is the position above soft keypad or device button.
                    // if keypad is shown, the r.bottom is smaller than that before.
                    int keypadHeight = screenHeight - r.bottom;

                    Logger.d("keypadHeight = " + keypadHeight);

                    if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                        // keyboard is opened
                        Logger.d("Keyboard visible");
                        if (keyboardListenerCallback != null) {
                            keyboardListenerCallback.onKeyboardVisible();
                        }
                    } else {
                        // keyboard is closed
                        Logger.d("Keyboard gone");
                        if (keyboardListenerCallback != null) {
                            keyboardListenerCallback.onKeyboardGone();
                        }
                    }
                }
            });
        }
    }


    public interface KeyboardListenerCallback {
        void onKeyboardVisible();

        void onKeyboardGone();
    }


    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }
}
