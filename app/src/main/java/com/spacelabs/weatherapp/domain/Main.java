
package com.spacelabs.weatherapp.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Main {

    private float temp;
    private float pressure;
    private int humidity;
    private float tempMin;
    private float tempMax;
    private float seaLevel;
    private float grndLevel;

}
