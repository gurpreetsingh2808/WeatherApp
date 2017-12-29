package com.spacelabs.weatherapp;


import com.spacelabs.weatherapp.service.ResourceBuilder;
import com.spacelabs.weatherapp.service.api.WeatherService;
import com.spacelabs.weatherapp.service.api.dto.WeatherDataResponse;
import com.spacelabs.weatherapp.ui.main.MainPresenter;

import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Gurpreet on 18-12-2017.
 */

public class WeatherServiceTest {

    @Mock
    MainPresenter.View view;

    private String latitude = "28.73";
    private String longitude = "77.13";

    @Test
    public void testWeatherServiceResponse() throws Exception {
        WeatherService.WeatherResource weatherResource = ResourceBuilder.buildResource(WeatherService.WeatherResource.class, view);
        Call<WeatherDataResponse> call = weatherResource.getLocationBasedWeatherData(latitude, longitude);
        try {
            Response<WeatherDataResponse> response = call.execute();
            WeatherDataResponse weatherDataResponse = response.body();
            assertTrue(response.isSuccessful());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
