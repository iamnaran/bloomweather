package com.uxcam.bloomweather.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uxcam.bloomweather.BuildConfig;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static int cacheSize = 10 * 1024 * 1024; // 10 MiB

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        builder.interceptors().add(interceptor);
//        builder.authenticator(new ApiTokenAuthenticator());
        OkHttpClient httpClient = builder
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        RxJava2CallAdapterFactory rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.computation());


        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(rxJava2CallAdapterFactory)
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }



}
