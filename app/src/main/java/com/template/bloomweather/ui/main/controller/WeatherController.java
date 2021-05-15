package com.template.bloomweather.ui.main.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.template.bloomweather.constants.AppConstants;
import com.template.bloomweather.model.WeatherData;
import com.template.bloomweather.services.ApiClient;
import com.template.bloomweather.services.WeatherApiService;
import com.template.bloomweather.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class WeatherController {


    private static final String TAG = WeatherController.class.getSimpleName();
    CompositeDisposable compositeDisposable;
    WeakReference<View> view;
    Map<String, String> options = new HashMap();

    public WeatherController(View view) {
        this.view = new WeakReference<>(view);
        this.compositeDisposable = new CompositeDisposable();
    }

    private View getView() throws NullPointerException {
        if (view != null)
            return view.get();
        else
            throw new NullPointerException("View is unavailable");
    }


    public interface View {

        void onWeatherApiSuccess(WeatherData weatherData);

        void onWeatherApiFailure(String message);

//        void onLocationReceived(double latitude, double longitude);

    }

    public void getWeatherData(double latitude, double longitude) {

        Observable<WeatherData> attendanceObservable = ApiClient.getClient().create(WeatherApiService.class)
                .requestWeather(String.valueOf(latitude), String.valueOf(longitude), "metric", "10")
                .subscribeOn(Schedulers.io())
                .retry(AppConstants.API_RETRY_COUNT)
                .observeOn(AndroidSchedulers.mainThread());

        DisposableObserver<WeatherData> weatherDisposableObserver = new DisposableObserver<WeatherData>() {

            @Override
            public void onNext(@NonNull WeatherData weatherData) {

                if (getView() != null) {
                    Log.e(TAG, new GsonBuilder().create().toJson(weatherData));
                    getView().onWeatherApiSuccess(weatherData);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getView().onWeatherApiFailure(Utils.handleApiError(e));

                Log.e(TAG, "error");

            }

            @Override
            public void onComplete() {

            }
        };
        compositeDisposable.add(attendanceObservable.subscribeWith(weatherDisposableObserver));
    }

    public void onActivityStop() {
        if (getView() != null) {
            compositeDisposable.clear();
        }
    }


}
