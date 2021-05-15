package com.template.bloomweather.ui.main.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.template.bloomweather.R;
import com.template.bloomweather.helpers.EmptyDataObserver;
import com.template.bloomweather.ui.main.controller.WeatherController;
import com.template.bloomweather.model.WeatherData;
import com.template.bloomweather.ui.custom.LocationPickerDialog;
import com.template.bloomweather.ui.main.adapter.WeatherAdapter;

public class MainActivity extends AppCompatActivity implements WeatherController.View, View.OnClickListener , LocationPickerDialog.OnLocationSelectedListener{


    private WeatherController weatherController;
    private WeatherAdapter weatherAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewWeather;
    private Button btnSelectLocation;
    private ConstraintLayout emptyParent;
    private final String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    final int RC_LOCATION = 0034;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        prepareRecyclerView();
        initListeners();
    }

    private void initViews() {
        recyclerViewWeather = findViewById(R.id.weather_recycler_view);
        btnSelectLocation = findViewById(R.id.btn_select_location);
        emptyParent = findViewById(R.id.empty_view_parent);
        progressBar = findViewById(R.id.progress_bar);

    }

    private void initListeners() {

        weatherController = new WeatherController(this);
        btnSelectLocation.setOnClickListener(this);

    }


    private void showProgressBar(){

        if (progressBar != null){
            progressBar.setVisibility(View.VISIBLE);
        }

    }

    private void hideProgressBar(){
        if (progressBar != null){
            progressBar.setVisibility(View.GONE);
        }
    }

    private void prepareRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayout.VERTICAL, false);
        recyclerViewWeather.setLayoutManager(linearLayoutManager);
        weatherAdapter = new WeatherAdapter();
        recyclerViewWeather.setAdapter(weatherAdapter);
        weatherAdapter.registerAdapterDataObserver(new EmptyDataObserver(recyclerViewWeather,emptyParent));
    }

    @Override
    public void onWeatherApiSuccess(WeatherData weatherData) {

        setUpWeatherDetails(weatherData);

    }

    @Override
    public void onWeatherApiFailure(String message) {

    }

    private void setUpWeatherDetails(WeatherData weatherData) {

        hideProgressBar();
        if (weatherAdapter != null){
            weatherAdapter.add(weatherData);
            weatherAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (weatherController != null){
            weatherController.onActivityStop();
            hideProgressBar();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_select_location:

                if (Build.VERSION.SDK_INT >= 23) {
                    if (!checkPermissions()){
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, RC_LOCATION);

                    }else{
                        showMapDialog();
                    }
                } else {
                    showMapDialog();

                }

                break;
        }
    }

    private void showMapDialog() {

        new LocationPickerDialog().show(
                getSupportFragmentManager(), "TAG_LOCATION");

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showMapDialog();
                } else {
                    Toast.makeText(MainActivity.this, "Location permission required", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    private boolean checkPermissions() {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onLocationSelected(Double lat, Double lng) {

        if (weatherController != null){
            weatherController.getWeatherData(lat,lng);
            showProgressBar();

        }

    }
}