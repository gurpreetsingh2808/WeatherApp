package com.spacelabs.weatherapp.ui.main;


import com.spacelabs.weatherapp.service.api.dto.WeatherDataResponse;
import com.spacelabs.weatherapp.service.api.dto.WeatherForecastResponse;
import com.spacelabs.weatherapp.ui.base.BaseMvpView;

/**
 * Created by Gurpreet on 12-10-2017.
 */

public class MainPresenter {

    public interface View extends BaseMvpView {
        void onWeatherDataRetreivalSuccess(WeatherDataResponse weatherDataResponse);
        void onWeatherDataRetreivalFailure(Throwable throwable);

        void onWeatherForecastRetreivalSuccess(WeatherForecastResponse weatherForecastResponse);

        void onWeatherForecastRetreivalFailure(Throwable throwable);
    }

    interface Presenter {
        void getWeatherInfo(String latitude, String longitude);

        void getWeatherForecast(String latitude, String longitude);
    }
}
