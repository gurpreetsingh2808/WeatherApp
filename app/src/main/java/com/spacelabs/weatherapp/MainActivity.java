package com.spacelabs.weatherapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.spacelabs.weatherapp.domain.WeatherDataResponse;
import com.spacelabs.weatherapp.framework.logger.Logger;
import com.spacelabs.weatherapp.ui.base.BaseActivity;
import com.spacelabs.weatherapp.ui.main.MainPresenter;
import com.spacelabs.weatherapp.ui.main.MainPresenterImpl;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, MainPresenter.View {

    private final int PERMISSION_REQUEST_CODE = 1;
    private final LocationRequest REQUEST = LocationRequest.create()
            .setFastestInterval(60000)   // in milliseconds
            .setInterval(180000)         // in milliseconds
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    protected GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private String subLocality = null;
    private Boolean isLocationPopupVisible = false;
    private MainPresenterImpl mainPresenterImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp();
    }

    private Boolean isGPSEnabled() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //  wait for some time so that whether gps is enabled or not can be recognized
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mGoogleApiClient.isConnected()) {
                    getLocation();
                }
            }
        }, 1000);
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
    }

    /**
     * Method to fetch the location when google api client is connected
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Logger.d("onConnected: ");
        Logger.d("check permissions");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {

            return;

        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    REQUEST,
                    this);  // LocationListener
        }
    }


    /**
     * Method to connect the client again if connection is suspended
     */
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }


    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Logger.e("onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        getSnackbar("Could not connect to Google API Client: Error " + connectionResult.getErrorCode()).show();
    }


    /**
     * Method to fetch the location
     */
    public void getLocation() {
        if (!isGPSEnabled()) {
            if (!isLocationPopupVisible) {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.need_location))
                        .setMessage("\n" + getString(R.string.need_location_desc))
                        .setPositiveButton("TURN ON", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                isLocationPopupVisible = false;
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create().show();
                isLocationPopupVisible = true;
            }
        }
        //  check for run time permissions if gps is enabled

        else {
            Logger.d("check permissions");

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]
                                {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_CODE);
                Logger.d("onClick: should allow permission");

            } else {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);

                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();

                    Logger.d("getLocation: lat " + latitude);
                    Logger.d("getLocation: lng " + longitude);

                    mainPresenterImpl.getWeatherInfo(String.valueOf(latitude), String.valueOf(longitude));

                    Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (addresses.size() > 0) {
                            Address returnedAddress = addresses.get(0);
                            subLocality = returnedAddress.getSubLocality();
                        }
                    } catch (IOException e) {
                        Logger.handleCaughtException(e);
                    }


                } else {
                    getSnackbar("Couldn't get your location.").setAction("TRY AGAIN", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mGoogleApiClient.connect();
                            getLocation();
                        }
                    }).show();
                    Logger.d("getLocation: Couldn't get the location. Make sure location is enabled on the device");
                }
            }
        }
    }

    @Override
    protected void setUp() {
        buildGoogleApiClient();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        mainPresenterImpl = new MainPresenterImpl(this);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onWeatherDataRetreivalSuccess(WeatherDataResponse weatherDataResponse) {

    }

    @Override
    public void onWeatherDataRetreivalFailure(Throwable throwable) {

    }
}
