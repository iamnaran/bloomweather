package com.template.bloomweather.utils;

import com.google.gson.JsonSyntaxException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.HttpException;

public class Utils {

    public static String handleApiError(Throwable error) {

        if (error instanceof HttpException) {

            switch (((HttpException) error).code()) {


                //422, 401,404
                case HttpsURLConnection.HTTP_NOT_FOUND:

                    return "Error code 404 not found,  Please try again later.";

                case HttpsURLConnection.HTTP_UNAUTHORIZED:
                    return "HTTP Unauthorised user access error, Please contact our support.";
                case HttpsURLConnection.HTTP_FORBIDDEN:
                    return "Forbidden error . Logout & Re-login if you are facing some error.";
                case HttpsURLConnection.HTTP_INTERNAL_ERROR:
                    return "Internal Server Error. Please try again later.";

                case HttpsURLConnection.HTTP_BAD_REQUEST:
                    return "Sorry! Bad Request. Please try again later.";
                case 0:
                    return "No Internet Connection.  Please try again later.";
                default:
                    return error.getLocalizedMessage();

            }
        } else if (error instanceof JsonSyntaxException) {
            return "Something went wrong while receiving data. PLease contact our support!";
        } else {
            return error.getMessage();
        }
    }


    public static String formatToClientDate(String unformattedDate) {
        String currentTimeZoneDate = "";
        try {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
            Date date = sdf.parse(unformattedDate);
            sdf.setTimeZone(TimeZone.getDefault());
            currentTimeZoneDate = sdf.format(date);
            date = sdf.parse(currentTimeZoneDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
            currentTimeZoneDate = simpleDateFormat.format(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return currentTimeZoneDate;
    }

    public static String formatToClientTime(String unformattedDate) {
        String currentTimeZoneDate = "";
        try {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
            Date date = sdf.parse(unformattedDate);
            sdf.setTimeZone(TimeZone.getDefault());
            currentTimeZoneDate = sdf.format(date);
            date = sdf.parse(currentTimeZoneDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            currentTimeZoneDate = simpleDateFormat.format(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return currentTimeZoneDate;
    }

}
