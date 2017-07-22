package com.example.student.userphotograph.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GMapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnPoiClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private DatabaseReference mLatRef;
    private DatabaseReference mLngRef;

    public GMapFragment() {
    }

    public static GMapFragment newInstance() {
        return new GMapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        final int phone = shared.getInt("key_phone", 0);

        mMap = googleMap;
        mMap.setOnPoiClickListener(this);
        updateLocation();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LatLng l = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(l).title(String.valueOf(phone)));
        if (lat != 0 && lng != 0) {
            mMap.addMarker(new MarkerOptions().position(l));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(l));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l, 15));
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));


                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                        return;
                    }
                    startActivity(callIntent);
                }
            });

        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @Override
    public void onPoiClick(PointOfInterest poi) {
        Toast.makeText(getActivity(), poi.name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        updateLocation();
    }

    public void updateLocation() {
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

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            final DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference()
                    .child("photographs").child(user.getUid());
            mLatRef = mDatabaseRef.child("latitude");
            mLngRef = mDatabaseRef.child("longitude");
            mLatRef.setValue(mLatitude);
            mLngRef.setValue(mLongitude);
        }
    }
}
