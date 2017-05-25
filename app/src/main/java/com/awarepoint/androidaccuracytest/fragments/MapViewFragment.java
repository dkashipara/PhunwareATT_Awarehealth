package com.awarepoint.androidaccuracytest.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awarepoint.androidaccuracytest.MainActivity;
import com.awarepoint.androidaccuracytest.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by dkashipara on 5/9/2017.
 * LE-103
 */

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap googleMap;
    public View mfragmentView;
    MainActivity mainActivity;
    double latitude = 32.716226;
    double longitude = -117.1687974;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mfragmentView = inflater.inflate(R.layout.location_fragment, null);


        mMapView = (MapView) mfragmentView.findViewById(R.id.mapView);
        mainActivity = (MainActivity) getActivity();
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(this);


        return mfragmentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;

        // For showing a move to my location button
        googleMap.setMyLocationEnabled(true);


    }

    public void updateLatNLon(double lat, double lon) {
        latitude = lat;
        longitude = lon;
    }

    public void displayMap() {
        googleMap.clear();
        // For dropping a marker at a point on the Map
        LatLng awpLocation = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(awpLocation).title("AWP location").snippet("Lat and lon from  awarepoint location engine"));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(awpLocation).zoom(20).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
