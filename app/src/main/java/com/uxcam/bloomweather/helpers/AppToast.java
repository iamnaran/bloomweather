package com.uxcam.bloomweather.helpers;

import android.widget.Toast;

import com.uxcam.bloomweather.App;


public class AppToast {
    public static Toast showToastWithMessage(String messageResourceId) {
        Toast toast = Toast.makeText(App.getAppContext(), messageResourceId, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }
}
