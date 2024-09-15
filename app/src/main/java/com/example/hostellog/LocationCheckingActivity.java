package com.example.hostellog;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.rejowan.cutetoast.CuteToast;

public class LocationCheckingActivity extends AppCompatActivity {
    TextView textView  ;
    private LocationRequest locationRequest;
    double latNorth, latSouth, lonEast, lonWest;
    double latitude1,longitude1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location_checking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        double latitude1 = 23.181467;
        double longitude1 = 79.986404;
        double distanceKm = 2.0;  // 2 km radius
        // Calculate offsets
        double latOffset = LocationUtils.calculateLatitudeOffset(distanceKm);
        double lonOffset = LocationUtils.calculateLongitudeOffset(latitude1, distanceKm);
        latNorth = latitude1 + latOffset;
        latSouth = latitude1 - latOffset;
        // East and West boundaries
        lonEast = longitude1 + lonOffset;
        lonWest = longitude1 - lonOffset;

        latNorth = Math.round(latNorth * 1000000.0) / 1000000.0;
        latSouth = Math.round(latSouth * 1000000.0) / 1000000.0;
        lonEast = Math.round(lonEast * 1000000.0) / 1000000.0;
        lonWest = Math.round(lonWest * 1000000.0) / 1000000.0;

        textView = findViewById(R.id.tv);

        getCurrentLocation();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);


        boolean b = longitude1 >= lonEast || longitude1 >= lonWest;
     //   boolean b = 77.102490 >= lonEast || 77.102490 >= lonWest;
        boolean b1 = latitude1 >= latNorth || latitude1 >= latSouth;
      //  boolean b1 = 28.704059 >= latNorth || 28.704059 >= latSouth;

       // textView.setText(latitude1 + " " + longitude1 + " " + latNorth + " " + latSouth + " " + lonEast + " " + lonWest);
       // Toast.makeText(LocationCheckingActivity.this, b + " " + b1, Toast.LENGTH_SHORT).show();
        if (b && b1) {
            SharedPreferences sharedPreferences1 = getSharedPreferences("logoutes", MODE_PRIVATE);
            boolean check = sharedPreferences1.getBoolean("isLoggedIn", true);
           // Toast.makeText(LocationCheckingActivity.this, check + "", Toast.LENGTH_SHORT).show();
            if (check){
                Intent intent = new Intent(LocationCheckingActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }else {
               Intent intent = new Intent(LocationCheckingActivity.this, MainActivity.class);
               startActivity(intent);
               finish();
            }

        }else {
            getCurrentLocation();
            Toast.makeText(LocationCheckingActivity.this, "Not in location", Toast.LENGTH_SHORT).show();
            textView.setText("Not in location");
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }


    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(LocationCheckingActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(LocationCheckingActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(LocationCheckingActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                         latitude1 = locationResult.getLocations().get(index).getLatitude();
                                         longitude1 = locationResult.getLocations().get(index).getLongitude();



                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Log.d("TAG", "onComplete: ", task.getException());
                  //  Toast.makeText(LocationCheckingActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(LocationCheckingActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }


    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

}