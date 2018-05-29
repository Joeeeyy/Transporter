package com.jjoey.transportr.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.firebase.geofire.GeoFire;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jjoey.transportr.activities.LoginActivity;

//public class FirebaseUtils extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

public class FirebaseUtils extends AppCompatActivity  {

    private static final String TAG = FirebaseUtils.class.getSimpleName();

    public static FirebaseAuth mAuth;
    public static DatabaseReference usersRef, vehiclesRef, driverLocationRef;
    public static FirebaseUser user;
    public static GeoFire geoFire;
    public static StorageReference sRef, imageRef;
    public static UploadTask uploadTask;
    public static String downloadURL = null;

    public static GoogleMap mGmap;
    public static SupportMapFragment mapFragment;
    public static Location mLastLocation;
    public static GoogleApiClient apiClient;
    public static Marker mUserMarker;
    public static LocationRequest locationRequest;

    public static double latitude = 0f, longitude = 0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        usersRef = FirebaseDatabase.getInstance().getReference(AppConstants.USERS_REF);
        usersRef.keepSynced(true);

        driverLocationRef = FirebaseDatabase.getInstance().getReference(AppConstants.DRIVER_LOCATION_REF);
        driverLocationRef.keepSynced(true);
        geoFire = new GeoFire(driverLocationRef);

        vehiclesRef = FirebaseDatabase.getInstance().getReference(AppConstants.VEHICLES);
        vehiclesRef.keepSynced(true);

        sRef = FirebaseStorage.getInstance().getReference(AppConstants.STORAGE_REF);

        //getLocation();

    }

//    public void getLocation() {
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, AppConstants.LOCATION_PERMS_REQ_CODE);
//        } else {
//            if (checkPlayServices()) {
//                buildApiClient();
//                makeLocationRequest();
//                displayLocation();
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case AppConstants.LOCATION_PERMS_REQ_CODE:
//                for (int gr : grantResults) {
//                    if (gr == PackageManager.PERMISSION_GRANTED) {
//                        if (checkPlayServices()) {
//                            buildApiClient();
//                            makeLocationRequest();
//                            displayLocation();
//                        }
//                    } else {
//                        finish();
//                        moveTaskToBack(true);
//                    }
//                }
//                break;
//        }
//    }
//
//    public void displayLocation() {
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: 5/29/2018 Send user to settings to grant permission
//        }
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
//        if (mLastLocation != null) {
//            latitude = mLastLocation.getLatitude();
//            Log.d(TAG, "Your Latitude:\t" + latitude);
//            longitude = mLastLocation.getLongitude();
//            Log.d(TAG, "Your Longitude:\t" + longitude);
//
//            geoFire.setLocation(getUid(), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
//                @Override
//                public void onComplete(String key, DatabaseError error) {
//                    if (error != null) {
//                        Toast.makeText(FirebaseUtils.this, "Could Not Get Your Location", Toast.LENGTH_SHORT).show();
//                    } else {
//                        if (mUserMarker != null) {
//                            mUserMarker.remove();
//                            mUserMarker = mGmap.addMarker(new MarkerOptions()
//                                    .position(new LatLng(latitude, longitude)).title("Your Location"));
//                        }
//                    }
//                }
//            });
//
//        }
//    }

//    public void makeLocationRequest() {
//        locationRequest = LocationRequest.create();
//        locationRequest.setFastestInterval(AppConstants.FASTEST_INTERVAL);
//        locationRequest.setSmallestDisplacement(AppConstants.DISPLACEMENT);
//        locationRequest.setInterval(AppConstants.UPDATE_INTERVAL);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
//
//    public void buildApiClient() {
//        apiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//        apiClient.connect();
//    }
//
//    public boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this, AppConstants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Snackbar.make(findViewById(android.R.id.content), "Play Services NOT Supported on Your Device", Snackbar.LENGTH_LONG).show();
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }


    public static FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public static String getUid() {
        return mAuth.getCurrentUser().getUid();
    }

    public static void signOut(Context context) {
        if (mAuth != null) {
            mAuth.signOut();
            mAuth = null;
        }
        Intent signOutIntent = new Intent(context, LoginActivity.class);
        signOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(signOutIntent);
    }

//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Log.d(TAG, "API Client Connection Failed:\t" + connectionResult.getErrorMessage().toString());
//        if (apiClient != null) {
//            apiClient.disconnect();
//        }
//        apiClient.connect();
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        apiClient.connect();
//    }

}
