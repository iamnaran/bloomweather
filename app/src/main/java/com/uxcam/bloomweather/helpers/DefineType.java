package com.uxcam.bloomweather.helpers;


public class DefineType {

    public static <T> T getType(Object type, Class<T> classes) {
        try {
            return classes.cast(type);
        } catch (ClassCastException ex) {
            return null;
        }
    }
}
