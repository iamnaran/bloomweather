package com.template.bloomweather.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.template.bloomweather.ui.main.views.MainActivity;

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