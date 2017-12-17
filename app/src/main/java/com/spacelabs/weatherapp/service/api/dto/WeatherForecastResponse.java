
package com.spacelabs.weatherapp.service.api.dto;


import com.spacelabs.weatherapp.domain.City;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherForecastResponse {
    private City city;
    private List<WeatherDataResponse> list;
    private int cnt;
    private float message;
    private int cod;

}
