package com.template.bloomweather.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.template.bloomweather.R;
import com.template.bloomweather.helpers.AppRecyclerView;
import com.template.bloomweather.helpers.DefineType;
import com.template.bloomweather.model.WeatherData;
import com.template.bloomweather.utils.Utils;

public class WeatherAdapter extends AppRecyclerView {


    private WeatherData weatherData;


    @Override
    public void add(Object object) {

        this.weatherData = DefineType.getType(object, WeatherData.class);

    }

    @Override
    protected void clear() {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_forecast, parent, false);
        return new VHWeather(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof VHWeather) {
            VHWeather vhHeader = (VHWeather) holder;
            WeatherData.WeatherList weatherList = weatherData.getList().get(position);

            vhHeader.nameTV.setText(weatherData.getCity().getName());
            vhHeader.dateTv.setText(Utils.formatToClientDate(weatherList.getDtTxt()));
            vhHeader.timeTv.setText(Utils.formatToClientTime(weatherList.getDtTxt()));
            vhHeader.humidityTV.setText(String.valueOf(Math.round(weatherList.getMain().getHumidity())));
            vhHeader.pressureTV.setText(String.valueOf(Math.round(weatherList.getMain().getPressure())));
            vhHeader.temperatureDegreeTV.setText(String.valueOf(Math.round(weatherList.getMain().getTemp())) + "°C");

            getCurrentWeatherStatusIcon(vhHeader.weatherStatusIcon, weatherList.getWeather().get(0).getIcon());


        }

    }


    private void getCurrentWeatherStatusIcon(ImageView weatherStatusIcon, String icon) {
        switch (icon) {

            case "01d":
            case "01n":

                Glide.with(weatherStatusIcon.getContext()).load(R.mipmap.ic_weather_sunny).into(weatherStatusIcon);

                break;
            case "02d":
            case "02n":
            case "04d":
            case "03d":

                Glide.with(weatherStatusIcon.getContext()).load(R.mipmap.ic_weather_sunny_with_cloud).into(weatherStatusIcon);

                break;

            case "09d":
            case "09n":
            case "10d":
            case "10n":
                Glide.with(weatherStatusIcon.getContext()).load(R.mipmap.ic_weather_light_rain_with_sun).into(weatherStatusIcon);
                break;

            case "11d":
            case "11n":
                Glide.with(weatherStatusIcon.getContext()).load(R.mipmap.ic_weather_thunder_strom).into(weatherStatusIcon);

                break;

            case "13d":
            case "13n":
            case "50d":
            case "50n":
                Glide.with(weatherStatusIcon.getContext()).load(R.mipmap.ic_weather_snow).into(weatherStatusIcon);

                break;


        }
    }


    @Override
    public int getItemCount() {
        if (weatherData != null) {
            return weatherData.getList().size();
        }
        return 0;
    }

    private class VHWeather extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView humidityTV;
        TextView nameTV;
        TextView dateTv;
        TextView timeTv;
        TextView temperatureDegreeTV;
        TextView pressureTV;
        ImageView weatherStatusIcon;


        public VHWeather(View view) {
            super(view);

            humidityTV = view.findViewById(R.id.humidity_value);
            nameTV = view.findViewById(R.id.weather_location);
            dateTv = view.findViewById(R.id.weather_date);
            timeTv = view.findViewById(R.id.weather_time);
            weatherStatusIcon = view.findViewById(R.id.imageView);
            temperatureDegreeTV = view.findViewById(R.id.temperature_value);
            pressureTV = view.findViewById(R.id.atm_pressure_value);


        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {


            }
        }


    }
}
