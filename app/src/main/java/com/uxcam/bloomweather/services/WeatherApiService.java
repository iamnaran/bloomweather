package com.uxcam.bloomweather.services;

import com.uxcam.bloomweather.constants.AppConstants;
import com.uxcam.bloomweather.model.WeatherData;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


/*
    Weather API Service URL
*/

public interface WeatherApiService {

    @Headers("x-api-key: " + AppConstants.WEATHER_API_KEY)
    @GET("data/2.5/forecast")
    Observable<WeatherData> requestWeather(
            @Query("lat") String lat, @Query("lon") String lon, @Query("units") String units, @Query("cnt") String count
    );

}
