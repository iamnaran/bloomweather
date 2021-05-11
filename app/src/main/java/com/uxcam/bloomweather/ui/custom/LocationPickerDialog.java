package com.uxcam.bloomweather.ui.custom;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.uxcam.bloomweather.R;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class LocationPickerDialog extends DialogFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    private static final String TAG = LocationPickerDialog.class.getSimpleName();

    private FrameLayout addLocation;
    private Double mapLocationLatitude, mapLocationLongitude;
    private String mapLocationPlace;
    LocationManager locationManager;
    private GoogleMap mMap;
    private static final int DEFAULT_ZOOM = 15;
    private Location lastKnownLocation;
    private Marker marker;

    private final String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private FusedLocationProviderClient fusedLocationProviderClient;

    public LocationPickerDialog() {


    }

    public interface OnLocationSelectedListener {
        void onLocationSelected(Double lat, Double lng);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map_dialog, container, false);

        addLocation = rootView.findViewById(R.id.btn_add_location);

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnLocationSelectedListener listener = (OnLocationSelectedListener) getActivity();
                assert listener != null;
                listener.onLocationSelected(mapLocationLatitude , mapLocationLongitude);
                dismiss();

            }
        });


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");

        if (mapFragment == null) {
            mapFragment = new SupportMapFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.map, mapFragment, "mapFragment");
            ft.commit();
            fm.executePendingTransactions();
        }

        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        getDeviceLocation();
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(@NonNull Marker marker1) {
                View v = Objects.requireNonNull(getDialog()).getLayoutInflater().inflate(R.layout.item_map_info_window, null);
                TextView tvLocality = (TextView) v.findViewById(R.id.tv_locality);
                LatLng latLng1 = marker.getPosition();
                tvLocality.setText(marker.getTitle());

                mapLocationLatitude = latLng1.latitude;
                mapLocationLongitude = latLng1.longitude;
                LatLng latLng = new LatLng(latLng1.latitude, latLng1.longitude);
                Geocoder geocoder = new Geocoder(getActivity());
                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng1.latitude, latLng.longitude, 1);
                    if (addressList.size() > 0) {
                        String str = addressList.get(0).getLocality() + ",";
                        if (!str.equals("null")) {
                            str += addressList.get(0).getFeatureName();

                        } else {
                            str = addressList.get(0).getFeatureName();
                        }
                        tvLocality.setText(str);
                        mapLocationPlace = str;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return v;
            }

            @Override
            public View getInfoContents(Marker marker1) {
                return null;
            }
        });


    }


    @Override
    public void onMapClick(@NonNull LatLng latLng) {


        if (mMap != null) {
            try {

                addLocation.setVisibility(View.VISIBLE);

                if (marker != null) {
                    marker.remove();
                }

                MarkerOptions options = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude));
                marker = mMap.addMarker(options);
                marker.showInfoWindow();

            } catch (Exception e) {

            }
        }


    }


    private boolean checkPermissions() {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (checkPermissions()) {

                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.getResult();

                        if (lastKnownLocation != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        }else {

                        }
                    } else {
                        Log.e(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }



}
