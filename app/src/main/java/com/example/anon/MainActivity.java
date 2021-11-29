/*==================================================================================================
* File: MainActivity.java
* Description: Java Class for activity_main.xml, holds all of the fragments that run in the app
* Authors: Shea Holden, Quin Adam
* Date: November 03, 2021
* Project: Anon
==================================================================================================*/
package com.example.anon;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.anon.database.DBViewModel;
import com.example.anon.databinding.ActivityMainBinding;
import com.example.anon.feed.FeedHolder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Java Class for activity_main.xml, holds all of the fragments that run in the app
 */
public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;
    private MenuFragment menuFragment;
    private FeedHolder feedHolder;
    private NewPostFragment newPostFragment;
    private MapsFragment mapsFragment;
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    //Variables for location
    int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Boolean permissionDenied = false;
    private boolean locationPermissionGranted = false;
    private Location lastKnownLocation;
    private Bundle mBundle, mapBundle;
    private FusedLocationProviderClient fusedLocationProviderClient;

    // vars for login
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //login
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //noinspection deprecation
        startActivityForResult(signInIntent, RC_SIGN_IN);
        //end login

        feedHolder = new FeedHolder();
        menuFragment = new MenuFragment();
        newPostFragment = new NewPostFragment();
        mapsFragment = new MapsFragment();
        mBundle = new Bundle();
        mapBundle = new Bundle();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Remove top app title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //noinspection ConstantConditions
        getSupportActionBar().hide();

        //Implementing View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Banner ad
        MobileAds.initialize(this, initializationStatus -> {
        });
        AdView mAdView = binding.adView;
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Goes through best practice location permission request procedure.
        enableMyLocation();

        //Prevents UI initialization until location permissions granted.
        if (locationPermissionGranted){
            initUI();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAuth.getCurrentUser();
        if(requestCode == RC_SIGN_IN){
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                task.getResult(ApiException.class);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Initialize the UI after location permissions granted.
     */
    public void initUI(){

        if(!isNetworkAvailable()){
            Toast.makeText(this, "Error: You need to be connected to the internet to use Anon.\n" +
                    "Please connect to a network and restart the app.", Toast.LENGTH_LONG).show();
        } else {
            getDeviceLocation();

            //Used to allow location text to scroll if too long for view
            binding.locationText.setSelected(true);

            //Bottom menu bar button onClick listeners
            binding.locationText.setOnClickListener(view -> {
                final Fragment fragmentInFrame = getSupportFragmentManager()
                        .findFragmentById(R.id.mainFragmentContainerView);

                fragmentManager.popBackStackImmediate();
                if (fragmentInFrame instanceof MapsFragment){
                    if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                        if (binding.mainFragmentContainerViewLeft != null)
                            binding.mainFragmentContainerViewLeft.setVisibility(View.VISIBLE);
                }else {
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.genie_up, R.anim.genie_down,
                                    R.anim.genie_up, R.anim.genie_down)
                            .add(binding.mainFragmentContainerView.getId(), mapsFragment)
                            .addToBackStack(null).commit();
                }
                if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                    if (binding.mainFragmentContainerViewLeft != null)
                        binding.mainFragmentContainerViewLeft.setVisibility(View.GONE);
            });

            binding.newPostBtn.setOnClickListener(view -> {
                final Fragment fragmentInFrame = getSupportFragmentManager()
                        .findFragmentById(R.id.mainFragmentContainerView);

                if (fragmentInFrame instanceof NewPostFragment){
                    fragmentManager.popBackStackImmediate();
                    ObjectAnimator.ofFloat(binding.newPostBtn, "rotation",
                            135, 0).setDuration(250).start();
                }else {
                    if (fragmentInFrame instanceof MenuFragment){
                        onBackPressed();
                    }
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.genie_up, R.anim.genie_down,
                                    R.anim.genie_up, R.anim.genie_down)
                            .add(binding.mainFragmentContainerView.getId(), newPostFragment)
                            .addToBackStack(null).commit();

                    ObjectAnimator.ofFloat(binding.newPostBtn, "rotation",
                            0, 135).setDuration(250).start();
                }
            });

            binding.menuBtn.setOnClickListener(view -> {
                final Fragment fragmentInFrame = getSupportFragmentManager()
                        .findFragmentById(R.id.mainFragmentContainerView);

                if (fragmentInFrame instanceof MenuFragment){
                    fragmentManager.popBackStackImmediate();
                    binding.menuBtn.setImageResource(R.drawable.ic_menu);
                } else {
                    if (fragmentInFrame instanceof NewPostFragment){
                        onBackPressed();
                    }
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                                    R.anim.slide_in_right, R.anim.slide_out_right)
                            .add(binding.mainFragmentContainerView.getId(), menuFragment)
                            .addToBackStack("feed_frag").commit();

                    Animation fadeOut = new AlphaAnimation(1, 0);
                    fadeOut.setInterpolator(new AccelerateInterpolator());
                    fadeOut.setDuration(300);

                    ImageView menuIcn = binding.menuBtn;

                    AnimatedVectorDrawable avd = (AnimatedVectorDrawable) AppCompatResources.getDrawable(this, R.drawable.avd_menu_to_down);
                    menuIcn.setImageDrawable(avd);
                    if (avd != null)
                        avd.start();
                }
            });
        }


    }

    public void onBackPressed(){
        super.onBackPressed();
        if(binding.newPostBtn.getRotation() == 135){
            ObjectAnimator.ofFloat(binding.newPostBtn, "rotation", 135, 0)
                    .setDuration(250).start();
        }
        binding.menuBtn.setImageResource(R.drawable.ic_menu);
    }

    /*==============================================================================================
    * Location/ network checking/ enabling/ permissions
    ==============================================================================================*/

    /**
     * Requests device location & sets main fragment content accordingly
     */
    @SuppressLint("MissingPermission")
    public void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                //Requests single location update
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    LocationListener listener = location -> { };
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);
                }
                //Task to get last device location, which is returned from above location request
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // get device location returned from task
                        lastKnownLocation = task.getResult();
                        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                        try {
                            //Get all available info of current location
                            List<Address> addresses = geocoder
                                    .getFromLocation(lastKnownLocation.getLatitude(),
                                    lastKnownLocation.getLongitude(), 1);

                            Address address = addresses.get(0);

                            Log.d("=====Location: ", "Full: " + address);

                            String locality = address.getLocality();
                            String subAdmin = address.getSubAdminArea();
                            double lat = address.getLatitude();
                            double lon = address.getLongitude();

                            binding.locationText.setText(locality);

                            mBundle.putString("locality", locality);
                            mBundle.putString("sub_admin_area", subAdmin);

                            newPostFragment.setArguments(mBundle);
                            feedHolder.setArguments(mBundle);

                            //Pass current location to view model to get proper feed from DB
                            DBViewModel viewModel = new ViewModelProvider(this).get(DBViewModel.class);
                            viewModel.passLocation(locality, subAdmin);

                            //set fragment to display location-specific feed
                            fragmentManager.beginTransaction()
                                    .replace(binding.mainFragmentContainerView.getId(),
                                            feedHolder).commit();

                            mapBundle.putDouble("latitude", lat);
                            mapBundle.putDouble("longitude", lon);
                            mapsFragment.setArguments(mapBundle);

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("loc_app", "Current location is null. Using defaults.");
                        Log.e("loc_app", "Exception: %s", task.getException());
                        Toast.makeText(getBaseContext(), "Error retrieving current location.", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Check if location permissions granted. Request them if not
     */
    private void enableMyLocation() {
        // Check location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    /**
     * Result from checking location permissions
     */
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
            initUI();

        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }

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

    /**
     * Checks if user has access to the internet
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission")
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}