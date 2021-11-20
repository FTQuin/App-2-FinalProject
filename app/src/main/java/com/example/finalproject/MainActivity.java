package com.example.finalproject;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject.databinding.ActivityMainBinding;
import com.example.finalproject.feed.FeedFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FeedFragment feedFragment;
    private NewPostFragment newPostFragment;
    private MenuFragment menuFragment;

    FragmentManager fragmentManager = getSupportFragmentManager();

    //Variables for location
    int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Boolean permissionDenied = false;
    private boolean locationPermissionGranted = false;
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        feedFragment = new FeedFragment();
        newPostFragment = new NewPostFragment();
        menuFragment = new MenuFragment();

        //To remove top app title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        //Implementing View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        enableMyLocation();
        getDeviceLocation();

        //Setting feed fragment to open when app launches.
        fragmentManager.beginTransaction().add(binding.fragmentContainerView.getId(),
                feedFragment).commit();

        binding.chatBtn.setEnabled(false);

        binding.locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: **If version == upgraded** display map fragment on button click.
            }
        });

        binding.newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Fragment fragmentInFrame = getSupportFragmentManager()
                        .findFragmentById(R.id.fragmentContainerView);

                if (fragmentInFrame instanceof FeedFragment){
                    fragmentManager.beginTransaction().setTransition(FragmentTransaction
                            .TRANSIT_FRAGMENT_OPEN).add(binding.fragmentContainerView.getId(),
                            newPostFragment).addToBackStack("feed_frag").commit();
                    //binding.newPostBtn.setImageResource(R.drawable.ic_down_40);
                    ObjectAnimator.ofFloat(binding.newPostBtn, "rotation",
                            0, 135).setDuration(250).start();

                }else if (fragmentInFrame instanceof NewPostFragment){
                    fragmentManager.popBackStackImmediate();
                    //binding.newPostBtn.setImageResource(R.drawable.ic_add);
                    ObjectAnimator.ofFloat(binding.newPostBtn, "rotation",
                            135, 0).setDuration(250).start();

                }

                // TODO: animate appearance of fragment
            }
        });

        binding.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

                if (fragmentInFrame instanceof FeedFragment){
                    fragmentManager.beginTransaction().setTransition(FragmentTransaction
                            .TRANSIT_FRAGMENT_FADE).add(binding.fragmentContainerView.getId(),
                            menuFragment).addToBackStack("feed_frag").commit();
                    binding.menuBtn.setImageResource(R.drawable.ic_down_40);
                }else if (fragmentInFrame instanceof MenuFragment){
                    fragmentManager.popBackStackImmediate();
                    binding.menuBtn.setImageResource(R.drawable.ic_menu);
                }
            }
        });

        /*binding.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: display chat fragment on button click.
                final Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

                if (fragmentInFrame instanceof FeedFragment){
                    fragmentManager.beginTransaction().add(binding.fragmentContainerView.getId(),
                            chatFragment).addToBackStack("chat_frag").commit();
                    binding.newPostBtn.setImageResource(R.drawable.ic_down_40);
                }else if (fragmentInFrame instanceof ChatFragment){
                    fragmentManager.popBackStackImmediate();
                    binding.newPostBtn.setImageResource(R.drawable.ic_chat);
                }
            }
        });*/
        getDeviceLocation();
    }

    public void onBackPressed(){
        super.onBackPressed();
        if(binding.newPostBtn.getRotation() == 135){
            ObjectAnimator.ofFloat(binding.newPostBtn, "rotation", 135, 0).setDuration(250).start();
        }

        //binding.newPostBtn.setImageResource(R.drawable.ic_add);
        //binding.chatBtn.setImageResource(R.drawable.ic_chat);
        binding.menuBtn.setImageResource(R.drawable.ic_menu);
    }

    /*==============================================================================================
    * Location enabling/ permission
    ==============================================================================================*/

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful())
                            lastKnownLocation = task.getResult();
                        // Set the map's camera position to the current location of the device.
                        if (lastKnownLocation != null) {
                            /*mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            */
                            Log.d("cloc","===== LOCATION: Lat = " + lastKnownLocation.getLatitude() + ", Long = " + lastKnownLocation.getLongitude());
                        } else {
                            Log.d("loc_app", "Current location is null. Using defaults.");
                            Log.e("loc_app", "Exception: %s", task.getException());
                            /*mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            */
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void enableMyLocation() {
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //if (mMap != null) {
            //    mMap.setMyLocationEnabled(true);
                locationPermissionGranted = true;

            //}
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        // [END maps_check_location_permission]
    }

    // [START maps_check_location_permission_result]
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
            locationPermissionGranted = true;

        } else {
            // Permission was denied. Display an error message
            // [START_EXCLUDE]
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
            // [END_EXCLUDE]
        }
    }
    // [END maps_check_location_permission_result]

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


}