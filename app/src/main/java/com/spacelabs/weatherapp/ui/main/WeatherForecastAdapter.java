package com.spacelabs.weatherapp.ui.main;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spacelabs.weatherapp.R;
import com.spacelabs.weatherapp.framework.ImageLoader;
import com.spacelabs.weatherapp.framework.logger.Logger;
import com.spacelabs.weatherapp.framework.util.DateUtil;
import com.spacelabs.weatherapp.service.api.dto.WeatherDataResponse;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gurpreet on 1/17/2016.
 */
public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.ViewHolder> {

    private List<WeatherDataResponse> listWeatherForecasts;
    private LayoutInflater inflater;
    private Context context;
    private GregorianCalendar gregorianCalendar = new GregorianCalendar();
    private DateUtil dateUtil = new DateUtil();


    public WeatherForecastAdapter(Context context, List<WeatherDataResponse> listWeatherForecasts) {
        if (context != null) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.listWeatherForecasts = listWeatherForecasts;
        } else {
            Logger.e("WeatherDataResponseAdapter: context is null");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_weather_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final WeatherDataResponse WeatherDataResponse = listWeatherForecasts.get(position);
        holder.setData(WeatherDataResponse, position);
    }

    @Override
    public int getItemCount() {
        return listWeatherForecasts.size();
    }

//    public void setClickListener(CategoryItemClickListener clickListener) {
//        this.clickListener = clickListener;
//    }

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
//            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

//        @Override
//        public void onClick(View v) {
//            if (clickListener != null) {
//                clickListener.categoryClicked(v, getAdapterPosition(), listWeatherForecasts.get(getAdapterPosition()));
//            }
//
////            notifyItemChanged(selectedPosition);
////            selectedPosition= getLayoutPosition();
////            notifyItemChanged(selectedPosition);
//        }

        private void setData(final WeatherDataResponse weatherForecastResponse, int position) {
            gregorianCalendar.set(GregorianCalendar.DAY_OF_WEEK, gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) + 1);
            tvDay.setText(dateUtil.getDayName(gregorianCalendar.get(Calendar.DAY_OF_WEEK)));
            tvTemperature.setText((Math.round(weatherForecastResponse.getMain().getTemp() - 273.15)) + context.getString(R.string.degree));
            tvTemperatureDescription.setText(weatherForecastResponse.getWeather().get(0).getDescription());
            ImageLoader.loadImage(context, weatherForecastResponse.getWeather().get(0).getIcon(), ivWeatherIcon);
        }
    }

}