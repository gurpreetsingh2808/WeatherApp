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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.spacelabs.weatherapp.framework.logger.Logger;
import com.spacelabs.weatherapp.framework.util.StringUtil;
import com.spacelabs.weatherapp.service.api.dto.WeatherDataResponse;
import com.spacelabs.weatherapp.service.api.dto.WeatherForecastResponse;
import com.spacelabs.weatherapp.ui.base.BaseActivity;
import com.spacelabs.weatherapp.ui.main.MainPresenter;
import com.spacelabs.weatherapp.ui.main.MainPresenterImpl;
import com.spacelabs.weatherapp.ui.main.WeatherForecastAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, MainPresenter.View {

    private final int PERMISSION_REQUEST_CODE = 1;
    private final LocationRequest REQUEST = LocationRequest.create()
            .setFastestInterval(60000)   // in milliseconds
            .setInterval(180000)         // in milliseconds
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    protected GoogleApiClient mGoogleApiClient;
    @BindView(R.id.tvWeatherDescription)
    TextView tvWeatherDescription;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.tvTemperature)
    TextView tvTemperature;
    @BindView(R.id.ivWeather)
    ImageView ivWeather;
    @BindView(R.id.rvWeatherForecast)
    RecyclerView rvWeatherForecast;
    @BindView(R.id.pbWeatherCurrent)
    ProgressBar pbWeatherCurrent;
    @BindView(R.id.pbWeatherForecast)
    ProgressBar pbWeatherForecast;
    private Location mLastLocation;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private String subLocality = null;
    private Boolean isLocationPopupVisible = false;
    private MainPresenterImpl mainPresenterImpl;
    private WeatherForecastAdapter weatherForecastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
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
//        showLoading();
        pbWeatherCurrent.setVisibility(View.VISIBLE);
        pbWeatherForecast.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.fabLocation)
    void onLocationFabClick() {
        if (mGoogleApiClient.isConnected()) {
            getLocation();
            pbWeatherForecast.setVisibility(View.VISIBLE);
            pbWeatherCurrent.setVisibility(View.VISIBLE);
        }
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
//            hideLoading();
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
//            showLoading();
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
//                    hideLoading();
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();

                    Logger.d("getLocation: lat " + latitude);
                    Logger.d("getLocation: lng " + longitude);

                    //  call to api
                    mainPresenterImpl.getWeatherInfo(String.valueOf(latitude), String.valueOf(longitude));
                    mainPresenterImpl.getWeatherForecast(String.valueOf(latitude), String.valueOf(longitude));

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
        makeStatusBarTransparent();
        buildGoogleApiClient();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        mainPresenterImpl = new MainPresenterImpl(this);
        rvWeatherForecast.setLayoutManager(new LinearLayoutManager(this));
        rvWeatherForecast.setNestedScrollingEnabled(false);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onWeatherDataRetreivalSuccess(WeatherDataResponse weatherDataResponse) {
        pbWeatherCurrent.setVisibility(View.GONE);
        tvWeatherDescription.setText(weatherDataResponse.getWeather().get(0).getDescription());
        tvTemperature.setText(String.valueOf(Math.round(weatherDataResponse.getMain().getTemp() - 273.15)) + getString(R.string.degree));
        if (StringUtil.isNotBlank(weatherDataResponse.getName())) {
            tvLocation.setText(weatherDataResponse.getName());
        } else {
            tvLocation.setText(subLocality);
        }
        int weatherId = weatherDataResponse.getWeather().get(0).getId();
        //  thunderstorm
        if (weatherId >= 200 && weatherId < 300) {
//            ivWeather.setImageResource(R.id.);
        }
        //  drizzly
        else if (weatherId >= 300 && weatherId < 400) {
//            ivWeather.setImageResource(R.id.);
        }
        //  rain
        else if (weatherId >= 500 && weatherId < 600) {
            ivWeather.setImageResource(R.drawable.rain);
        }
        //  snow
        else if (weatherId >= 600 && weatherId < 700) {
            ivWeather.setImageResource(R.drawable.snow);
        }
        //  clear
        else if (weatherId == 800) {
            if (weatherDataResponse.getWeather().get(0).getIcon().equalsIgnoreCase("01n")) {
                ivWeather.setImageResource(R.drawable.night);
            } else {
                ivWeather.setImageResource(R.drawable.sunny);
            }
        }
        //  cloudy
        else if (weatherId >= 801 && weatherId <= 804) {
            ivWeather.setImageResource(R.drawable.cloudy);
        }
        // day or night
        else {
            if (weatherDataResponse.getWeather().get(0).getIcon().endsWith("n")) {
                ivWeather.setImageResource(R.drawable.night);
            } else {
                ivWeather.setImageResource(R.drawable.sunny);
            }
        }
        Logger.d("SUCCESS");
    }

    @Override
    public void onWeatherDataRetreivalFailure(Throwable throwable) {
        pbWeatherCurrent.setVisibility(View.GONE);
        Logger.e("ERROR " + throwable.fillInStackTrace());
        getSnackbar(getString(R.string.error)).show();
    }

    @Override
    public void onWeatherForecastRetreivalSuccess(WeatherForecastResponse weatherForecastResponse) {
        pbWeatherForecast.setVisibility(View.GONE);
        weatherForecastAdapter = new WeatherForecastAdapter(this, weatherForecastResponse.getList());
        rvWeatherForecast.setAdapter(weatherForecastAdapter);
    }


    @Override
    public void onWeatherForecastRetreivalFailure(Throwable throwable) {
        pbWeatherForecast.setVisibility(View.GONE);
        Logger.e("ERROR " + throwable.fillInStackTrace());
    }
}
