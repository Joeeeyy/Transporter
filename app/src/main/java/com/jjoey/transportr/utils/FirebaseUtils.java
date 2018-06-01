package com.jjoey.transportr.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.firebase.geofire.GeoFire;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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

    }

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

    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, AppConstants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Play Services NOT Supported on Your Device", Snackbar.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

}
