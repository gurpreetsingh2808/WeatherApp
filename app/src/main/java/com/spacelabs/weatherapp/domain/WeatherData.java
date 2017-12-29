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
    private String description;
    private String latitude;
    private String longitude;
    private String locality;
    private String temperature;
    private int weatherId;
    private String weatherIcon;

}
