package com.spacelabs.weatherapp.service.api;


import com.spacelabs.weatherapp.BuildConfig;
import com.spacelabs.weatherapp.service.api.dto.WeatherDataResponse;
import com.spacelabs.weatherapp.service.api.dto.WeatherForecastResponse;
import com.spacelabs.weatherapp.ui.base.BaseMvpView;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Gurpreet on 15-12-2017.
 */

public interface WeatherService {


    /**
     * Current weather data model
     */
    void getWeatherData(BaseMvpView baseMvpView, String latitude, String longitude, GetWeatherDataCallback getWeatherDataCallback);

    /**
     * Weather forecast model
     */

    void getWeatherForecast(BaseMvpView baseMvpView, String latitude, String longitude, GetWeatherForecastCallback getWeatherForecastCallback);

    interface WeatherResource {
        @GET("data/2.5/weather?appid=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY)
        Call<WeatherDataResponse> getLocationBasedWeatherData(@Query("lat") String latitude, @Query("lon") String longitude);

        @GET("data/2.5/forecast?appid=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY)
        Call<WeatherForecastResponse> getWeatherForecast(@Query("lat") String latitude, @Query("lon") String longitude);

    }


    interface GetWeatherDataCallback {
        void onSuccess(WeatherDataResponse weatherDataResponse);

        void onFailure(Throwable throwable);
    }

    interface GetWeatherForecastCallback {
        void onSuccess(WeatherForecastResponse weatherForecastResponse);

        void onFailure(Throwable throwable);
    }


}
