package com.spacelabs.weatherapp.ui.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import com.spacelabs.weatherapp.framework.util.ConnectivityUtil;
import com.spacelabs.weatherapp.service.ApiErrorResponse;

import butterknife.Unbinder;

/**
 * Created by Gurpreet on 15-12-2017.
 */

public abstract class BaseFragment extends Fragment implements BaseMvpView {

    private BaseActivity mActivity;
    private Unbinder mUnBinder;
    private ProgressDialog mProgressDialog;

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

}
