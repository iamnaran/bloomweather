package com.template.bloomweather.services;

import com.template.bloomweather.constants.AppConstants;
import com.template.bloomweather.model.WeatherData;

import io.reactivex.Observable;
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
