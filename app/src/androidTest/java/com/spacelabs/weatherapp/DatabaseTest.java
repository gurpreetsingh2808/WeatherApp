package com.spacelabs.weatherapp;


import android.support.test.InstrumentationRegistry;

import com.spacelabs.weatherapp.database.WeatherDataSource;
import com.spacelabs.weatherapp.domain.WeatherData;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Gurpreet on 18-12-2017.
 */
public class DatabaseTest {

    private WeatherDataSource weatherDataSource;

    @Before
    public void init() {
        weatherDataSource = new WeatherDataSource(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testInsertWeatherData() throws Exception {
        WeatherData weatherData = new WeatherData("Sunday", "Clear sky", "28.73", "77.13",
                "Delhi", "15", 800, "01d");
        weatherDataSource.insertWeatherData(weatherData);
    }

    @Test
    public void testGetWeatherData() throws Exception {
        weatherDataSource.getLatestWeatherData();
    }

    @Test
    public void testUpdateWeatherData() throws Exception {
        WeatherData weatherData = new WeatherData("Monday", "Foggy", "28.73", "77.13",
                "Rohini", "15", 800, "01n");
        weatherDataSource.updateWeatherData(weatherData, 0);
    }

    @Test
    public void testDeleteWeatherData() throws Exception {
        weatherDataSource.deleteWeatherData(0);
    }
}
