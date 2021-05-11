package com.uxcam.bloomweather.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.uxcam.bloomweather.ui.main.views.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doFirstRunCheckup();
    }

    private void doFirstRunCheckup() {

        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }


}