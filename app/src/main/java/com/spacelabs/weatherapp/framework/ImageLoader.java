package com.spacelabs.weatherapp.framework;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.spacelabs.weatherapp.BuildConfig;

/**
 * Created by Gurpreet on 16-10-2017.
 */

public class ImageLoader {

    public static void loadImage(Context context, String url, AppCompatImageView imageView) {
        Glide.with(context)
                .load(BuildConfig.BASE_URL_WEATHER_ICON + url + ".png")
                .fitCenter()
                .crossFade()
                .into(imageView);
    }

}
