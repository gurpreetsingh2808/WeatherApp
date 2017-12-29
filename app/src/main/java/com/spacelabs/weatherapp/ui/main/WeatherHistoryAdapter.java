package com.spacelabs.weatherapp.ui.main;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spacelabs.weatherapp.R;
import com.spacelabs.weatherapp.domain.WeatherData;
import com.spacelabs.weatherapp.framework.ImageLoader;
import com.spacelabs.weatherapp.framework.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gurpreet on 29/12/2017.
 */
public class WeatherHistoryAdapter extends RecyclerView.Adapter<WeatherHistoryAdapter.ViewHolder> {

    private List<WeatherData> listWeatherHistory;
    private LayoutInflater inflater;
    private Context context;


    public WeatherHistoryAdapter(Context context, List<WeatherData> listWeatherHistory) {
        if (context != null) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.listWeatherHistory = listWeatherHistory;
        } else {
            Logger.e("WeatherDataAdapter: context is null");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_weather_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final WeatherData WeatherData = listWeatherHistory.get(position);
        holder.setData(WeatherData, position);
    }

    @Override
    public int getItemCount() {
        return listWeatherHistory.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvDay)
        TextView tvDay;
        @BindView(R.id.tvTemperature)
        TextView tvTemperature;
        @BindView(R.id.tvTemperatureDescription)
        TextView tvTemperatureDescription;
        @BindView(R.id.ivWeatherIcon)
        AppCompatImageView ivWeatherIcon;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(final WeatherData weatherHistory, int position) {
            Logger.d("DAYYYYY " + weatherHistory.getDay());
            tvDay.setText(weatherHistory.getDay());
            tvTemperature.setText(weatherHistory.getTemperature() + context.getString(R.string.degree));
            tvTemperatureDescription.setText(weatherHistory.getDescription());
            ImageLoader.loadImage(context, weatherHistory.getWeatherIcon(), ivWeatherIcon);
        }
    }

}