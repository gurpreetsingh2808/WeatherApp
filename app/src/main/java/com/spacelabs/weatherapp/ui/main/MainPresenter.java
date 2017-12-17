package com.spacelabs.weatherapp.ui.main;


import com.spacelabs.weatherapp.domain.WeatherDataResponse;
import com.spacelabs.weatherapp.ui.base.BaseMvpView;

/**
 * Created by Gurpreet on 12-10-2017.
 */

public class MainPresenter {

    public interface View extends BaseMvpView {

        void onWeatherDataRetreivalSuccess(WeatherDataResponse weatherDataResponse);

        void onWeatherDataRetreivalFailure(Throwable throwable);
    }

    interface Presenter {
        void getWeatherInfo(String latitude, String longitude);
    }
}
