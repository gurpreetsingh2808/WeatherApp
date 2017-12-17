
package com.spacelabs.weatherapp.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherDataResponse {

    private Coord coord;
    private List<Weather> weather = null;
    private String base;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private int dt;
    private Sys sys;
    private int id;
    private String name;
    private int cod;

}
