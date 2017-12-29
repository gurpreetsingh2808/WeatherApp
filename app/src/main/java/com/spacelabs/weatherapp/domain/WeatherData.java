package com.spacelabs.weatherapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Gurpreet on 18-12-2017.
 */

@Getter
@Setter
@AllArgsConstructor
public class WeatherData {

    private String day;
    private long timestamp;
    private String description;
    private String latitude;
    private String longitude;
    private String locality;
    private String temperature;
    private int weatherId;
    private String weatherIcon;

    public WeatherData(String day, String description, String latitude, String longitude, String locality, String temperature, int weatherId, String weatherIcon) {
        this.day = day;
        this.timestamp = System.currentTimeMillis();
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locality = locality;
        this.temperature = temperature;
        this.weatherId = weatherId;
        this.weatherIcon = weatherIcon;
    }
}
