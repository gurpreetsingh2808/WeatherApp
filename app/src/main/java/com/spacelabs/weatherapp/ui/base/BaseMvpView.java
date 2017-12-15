package com.spacelabs.weatherapp.ui.base;

/**
 * Created by Gurpreet on 15/11/17.
 */

import android.support.annotation.StringRes;

import com.spacelabs.weatherapp.service.ApiErrorResponse;


/**
 * Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
 * pattern must implement. Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 */
public interface BaseMvpView {

    void showLoading();

    void showLoading(String message);

    void hideLoading();

    void onApiError(ApiErrorResponse apiErrorResponse);

    void onError(String message);

    void onError(@StringRes int resId);

    void showSnackbar(String message);

    void showSnackbar(@StringRes int resId);

    boolean isNetworkConnected();

    void hideKeyboard();

}
