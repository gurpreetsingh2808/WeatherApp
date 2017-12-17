package com.spacelabs.weatherapp.ui.main;

import com.spacelabs.weatherapp.domain.WeatherDataResponse;
import com.spacelabs.weatherapp.service.api.WeatherService;
import com.spacelabs.weatherapp.service.api.WeatherServiceImpl;

/**
 * Created by Gurpreet on 17-12-2017.
 */

public class MainPresenterImpl implements MainPresenter.Presenter {

    private final MainPresenter.View view;
    private WeatherService weatherService;

    public MainPresenterImpl(MainPresenter.View view) {
        this.view = view;
        this.weatherService = new WeatherServiceImpl();
    }

    @Override
    public void getWeatherInfo(String latitude, String longitude) {
        weatherService.getWeatherData(view, latitude, longitude, new WeatherService.GetWeatherDataCallback() {
            @Override
            public void onSuccess(WeatherDataResponse weatherDataResponse) {
                view.onWeatherDataRetreivalSuccess(weatherDataResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                view.onWeatherDataRetreivalFailure(throwable);
            }
        });
    }
}
