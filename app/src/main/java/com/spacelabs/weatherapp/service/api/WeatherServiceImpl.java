package com.spacelabs.weatherapp.service.api;

import com.spacelabs.weatherapp.service.ResourceBuilder;
import com.spacelabs.weatherapp.service.api.dto.WeatherDataResponse;
import com.spacelabs.weatherapp.service.api.dto.WeatherForecastResponse;
import com.spacelabs.weatherapp.ui.base.BaseMvpView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gurpreet on 22-11-2017.
 */

public class WeatherServiceImpl implements WeatherService {

    @Override
    public void getWeatherData(BaseMvpView baseMvpView, String latitude, String longitude, final GetWeatherDataCallback getWeatherDataCallback) {
        WeatherResource weatherResource = ResourceBuilder.buildResource(WeatherResource.class, baseMvpView);
        Call<WeatherDataResponse> call = weatherResource.getLocationBasedWeatherData(latitude, longitude);
        call.enqueue(new Callback<WeatherDataResponse>() {
            @Override
            public void onResponse(Call<WeatherDataResponse> call, Response<WeatherDataResponse> response) {
                if (response.body() != null && response.isSuccessful())
                    getWeatherDataCallback.onSuccess(response.body());
                else {
                    getWeatherDataCallback.onFailure(new Throwable("Error"));
                }
            }

            @Override
            public void onFailure(Call<WeatherDataResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    getWeatherDataCallback.onFailure(t);
                }
            }
        });
    }

    @Override
    public void getWeatherForecast(BaseMvpView baseMvpView, String latitude, String longitude, final GetWeatherForecastCallback getWeatherForecastCallback) {
        WeatherResource weatherResource = ResourceBuilder.buildResource(WeatherResource.class, baseMvpView);
        Call<WeatherForecastResponse> call = weatherResource.getWeatherForecast(latitude, longitude);
        call.enqueue(new Callback<WeatherForecastResponse>() {
            @Override
            public void onResponse(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
                if (response.body() != null && response.isSuccessful())
                    getWeatherForecastCallback.onSuccess(response.body());
                else {
                    getWeatherForecastCallback.onFailure(new Throwable("Error"));
                }
            }

            @Override
            public void onFailure(Call<WeatherForecastResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    getWeatherForecastCallback.onFailure(t);
                }
            }
        });
    }
}
