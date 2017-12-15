package com.spacelabs.weatherapp.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spacelabs.weatherapp.BuildConfig;
import com.spacelabs.weatherapp.service.interceptor.ErrorInterceptor;
import com.spacelabs.weatherapp.ui.base.BaseMvpView;

import java.util.concurrent.TimeUnit;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.OkHttpClient.Builder;

/**
 * <code>ResourceBuilder</code> builds the backend service api using retrofit and okHTTP.
 * It adds the necessary interceptors and settings and returns a fully executable service
 * instance on which api's can be invoked
 *
 * @author Gurpreet Singh
 * @since 15-Dec-2017
 */
public class ResourceBuilder {

    private static final String BASE_URL = BuildConfig.BASE_URL;

    private static <T> T buildResource(final Class<T> service, String url, BaseMvpView baseMvpView) {
        Builder okHttpClient = new Builder();

        setTimeSettings(okHttpClient);
        addInterceptors(okHttpClient, baseMvpView);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient.build())
                .build();

        return retrofit.create(service);
    }

    public static <T> T buildResource(final Class<T> service, BaseMvpView baseMvpView) {
        return buildResource(service, BASE_URL, baseMvpView);
    }


    // Private helper method to set timeout values on the backend calls
    private static void setTimeSettings(Builder okHttpClient) {

        //  setting up timeouts
        okHttpClient.connectTimeout(20, TimeUnit.SECONDS);
        okHttpClient.readTimeout(20, TimeUnit.SECONDS); //  socket timeout
        okHttpClient.writeTimeout(15, TimeUnit.SECONDS);
    }

    // Private Helper method to set various interceptors on the request
    private static void addInterceptors(Builder okHttpClient, BaseMvpView baseMvpView) {

        // Add error processing interceptor
        okHttpClient.addInterceptor(new ErrorInterceptor(baseMvpView));

        // Add  logging interceptor as the last interceptor, because this will also log the information
        // which you added with previous interceptors to your request.
        if (BuildConfig.DEBUG) {

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT);
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient.addInterceptor(httpLoggingInterceptor);
        }
    }
}