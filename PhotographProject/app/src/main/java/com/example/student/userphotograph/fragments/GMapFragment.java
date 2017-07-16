package com.example.student.userphotograph.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.student.userphotograph.R;
import com.example.student.userphotograph.service.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

public class GMapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnPoiClickListener, View.OnClickListener {

    double lat;
    double lng;

    private GoogleMap mMap;

    public GMapFragment(){
    }

     public static GMapFragment newInstance() {
        return new GMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        ImageView imgMyLocation = (ImageView) rootView.findViewById(R.id.my_location_map);
        imgMyLocation.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        SharedPreferences shared = getActivity().getSharedPreferences("location", Context.MODE_PRIVATE);
        float lng = shared.getFloat("key_lng", 0);
        float lat = shared.getFloat("key_lat", 0);

        mMap = googleMap;
        mMap.setOnPoiClickListener(this);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LatLng l = new LatLng(lat, 21);
        mMap.addMarker(new MarkerOptions().position(l));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(l));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l, 15));
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setMyLocationButtonEnabled(false);

    }

    @Override
    public void onPoiClick(PointOfInterest poi) {
        Toast.makeText(getActivity(), poi.name, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        GPSTracker gpsTracker = new GPSTracker(getActivity().getApplicationContext());
        Location mlocation = gpsTracker.getLocation();

        if (mlocation == null) {
            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else {
            double mLatitude = mlocation.getLatitude();
            double mLongitude = mlocation.getLongitude();

            LatLng myLocation = new LatLng(mLatitude, mLongitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        }
    }
}
