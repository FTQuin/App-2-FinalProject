/*==================================================================================================
* File: MapsFragment.java
* Description: Java Class for fragment_maps.xml, used when user wants to view their location
* Authors: Shea Holden, Quin Adam
* Date: November 03, 2021
* Project: Anon
==================================================================================================*/
package com.anon.anon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anon.anon.R;
import com.anon.anon.databinding.FragmentMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Java Class for fragment_maps.xml, used when user wants to view their location
 */
public class MapsFragment extends Fragment {

    private FragmentMapsBinding binding;
    private static final int DEFAULT_ZOOM = 15;

    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";
    private double latitude, longitude;

    public MapsFragment(){}

    public static MapsFragment newInstance(String latitude, String longitude) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LATITUDE, latitude);
        args.putString(ARG_LONGITUDE, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_LATITUDE);
            longitude = getArguments().getDouble(ARG_LONGITUDE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            binding.menuPopup.setVisibility(View.INVISIBLE);
            binding.background.setVisibility(View.INVISIBLE);

            LatLng currentLocation = new LatLng(latitude, longitude);

            Marker marker = googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Current Location"));
            if (marker != null)
                marker.showInfoWindow();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM));
            googleMap.getUiSettings().setAllGesturesEnabled(false);

            googleMap.setBuildingsEnabled(true);
            googleMap.setOnMapClickListener(latLng -> {
                String title = "Upgrade Required";
                String upgrade = "Upgrade";
                binding.menuPopupTitleText.setText(title);
                binding.popupUpgradeBtn.setText(upgrade);
                binding.menuPopup.setVisibility(View.VISIBLE);
                binding.background.setVisibility(View.VISIBLE);
                binding.background.animate().alpha(1.0f).setDuration(200);
                binding.menuPopup.animate().alpha(1.0f).setDuration(200);
            });
        }


    };
}