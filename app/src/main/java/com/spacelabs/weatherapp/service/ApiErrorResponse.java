package com.spacelabs.weatherapp.service;

import lombok.Getter;
import lombok.Setter;

/**
 * <code>ApiErrorResponse</code> represents an error which is sent by the backend api
 * as a result of an API invocation
 */
@Getter
@Setter
public class ApiErrorResponse {

    // The status_message of error from the backend
    private String message;

    // The backend stacktrace ( only for development)
    private Integer status_code;

}

