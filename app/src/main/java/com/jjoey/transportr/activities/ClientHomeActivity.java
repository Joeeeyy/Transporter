package com.jjoey.transportr.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jjoey.transportr.R;
import com.jjoey.transportr.adapters.DrawerMenuAdapter;
import com.jjoey.transportr.adapters.VehiclesAdapter;
import com.jjoey.transportr.interfaces.RecyclerClickListener;
import com.jjoey.transportr.models.ClientUser;
import com.jjoey.transportr.models.DrawerHeader;
import com.jjoey.transportr.models.DrawerItems;
import com.jjoey.transportr.models.Vehicle;
import com.jjoey.transportr.utils.AppConstants;
import com.jjoey.transportr.utils.FirebaseUtils;
import com.jjoey.transportr.utils.RecyclerItemTouchListener;
import com.jjoey.transportr.utils.SharedPrefsHelper;
import com.jjoey.transportr.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.jjoey.transportr.utils.AppConstants.DROP_PLACE_AUTOCOMPLETE_REQUEST_CODE;
import static com.jjoey.transportr.utils.AppConstants.START_PLACE_AUTOCOMPLETE_REQUEST_CODE;

public class ClientHomeActivity extends FirebaseUtils implements OnMapReadyCallback,  GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private static final String TAG = ClientHomeActivity.class.getSimpleName();

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private RecyclerView drawerRV;

    private List<Object> objectList = new ArrayList<>();
    private DrawerMenuAdapter menuAdapter;

    private EditText startLocationET, dropLocationET;
    private LinearLayout rideContinueLayout;
    private RecyclerView vehiclesRV;

    private String vehicleType = null;
    private String startAddr, destAddr;
    private boolean isValid = false;

    private SharedPrefsHelper prefsHelper;

    private List<Vehicle> vehicleList = new ArrayList<>();
    private VehiclesAdapter vehiclesAdapter;

    public GoogleMap mGmap;
    public SupportMapFragment mapFragment;
    public Location mLastLocation;
    public GoogleApiClient apiClient;
    public Marker mUserMarker;
    public LocationRequest locationRequest;

    public static double latitude = 0f, longitude = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        prefsHelper = new SharedPrefsHelper(this);
        initViews();
        setSupportActionBar(toolbar);

        setUpDrawer();
        prefsHelper.setLoggedOut(false);

        getVehicles();

        handlePlacesInput();

        rideContinueLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate() && Utils.isNetwork(ClientHomeActivity.this)) {
                    Intent intent = new Intent(ClientHomeActivity.this, BookRideActivity.class);
                    intent.putExtra("start_addr", startAddr);
                    intent.putExtra("dest_addr", destAddr);
                    startActivity(intent);
                    clearInputs();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Enter Start and Drop Locations", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void clearInputs() {
        startLocationET.setText("");
        dropLocationET.setText("");
    }

    // TODO: 5/28/2018 Add Places Restriction
    private void handlePlacesInput() {
        startLocationET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(ClientHomeActivity.this);
                    startActivityForResult(intent, START_PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.d(TAG, "Play-Services-Repairable Err:\t" + e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.d(TAG, "Play-Services-Unavailable Err:\t" + e.getMessage());
                }
            }
        });

        dropLocationET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(ClientHomeActivity.this);
                    startActivityForResult(intent, DROP_PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.d(TAG, "Play-Services-Repairable Err:\t" + e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.d(TAG, "Play-Services-Unavailable Err:\t" + e.getMessage());
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case START_PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    String txt = place.getAddress().toString();
                    startLocationET.setText(txt);
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    Log.d(TAG, status.getStatusMessage());

                } else if (resultCode == RESULT_CANCELED) {  }
                break;
            case DROP_PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    String txt = place.getAddress().toString();
                    dropLocationET.setText(txt);
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    Log.d(TAG, status.getStatusMessage());

                } else if (resultCode == RESULT_CANCELED) {  }
                break;
        }
    }

    // TODO: 5/27/2018 Finish Vehicles List
    private void getVehicles() {

        vehiclesRV.setHasFixedSize(true);
        LinearLayoutManager hllm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vehiclesRV.setLayoutManager(hllm);

        vehiclesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Vehicle v = new Vehicle();

                        String image = ds.child("vehicleImg").getValue().toString();
                        String type = ds.child("vehicleType").getValue().toString();
                        String eta = ds.child("estimatedTime").getValue().toString();

                        v.setVehicleIcon(image);
                        v.setVehicleType(type);
                        v.setEstimatedTime(eta);

                        vehicleList.add(v);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        vehiclesAdapter = new VehiclesAdapter(this, vehicleList);
        vehiclesRV.setAdapter(vehiclesAdapter);

    }

    private boolean validate() {
        startAddr = startLocationET.getText().toString();
        destAddr = dropLocationET.getText().toString();

        if (TextUtils.isEmpty(startAddr) && TextUtils.isEmpty(destAddr)) {
            isValid = false;
        } else if (!TextUtils.isEmpty(startAddr) && !TextUtils.isEmpty(destAddr)) {
            isValid = true;
        }
        return isValid;
    }

    private void setUpDrawer() {
        drawerRV.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        drawerRV.setLayoutManager(llm);
        drawerRV.addItemDecoration(new DividerItemDecoration(this, llm.getOrientation()));

        final DrawerHeader header = new DrawerHeader();

        usersRef.child(FirebaseUtils.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ClientUser user = dataSnapshot.getValue(ClientUser.class);

                        header.setProfileName(user.fullName);
                        header.setPhoneNum("+917326996784");

                        String imgURL = user.imgURL;
                        header.setProfileIcon(imgURL);

//                        phoneTV.setText(clientUser.phoneNumber);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        objectList.add(header);

        DrawerItems items = new DrawerItems();
        items.setItemTitle("Book a Ride");
        items.setIconImg(R.drawable.book_ride);

        DrawerItems items1 = new DrawerItems();
        items1.setItemTitle("Your Bookings");
        items1.setIconImg(R.drawable.bookings);

        DrawerItems items2 = new DrawerItems();
        items2.setItemTitle("Payment");
        items2.setIconImg(R.drawable.payment);

        DrawerItems items3 = new DrawerItems();
        items3.setItemTitle("Notifications");
        items3.setIconImg(R.drawable.bell);

        DrawerItems items4 = new DrawerItems();
        items4.setItemTitle("Refer & Earn");
        items4.setIconImg(R.drawable.gift);

        DrawerItems items5 = new DrawerItems();
        items5.setItemTitle("Help");
        items5.setIconImg(R.drawable.help);

        DrawerItems items6 = new DrawerItems();
        items6.setItemTitle("Privacy Policy");
        items6.setIconImg(R.drawable.privacy_policy);

        DrawerItems items7 = new DrawerItems();
        items7.setItemTitle("Profile Settings");
        items7.setIconImg(R.drawable.settings);

        objectList.add(items);
        objectList.add(items1);
        objectList.add(items2);
        objectList.add(items3);
        objectList.add(items4);
        objectList.add(items5);
        objectList.add(items6);
        objectList.add(items7);

        menuAdapter = new DrawerMenuAdapter(this, objectList);
        drawerRV.setAdapter(menuAdapter);

        handleDrawerEvents();

    }

    private void handleDrawerEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    setDrawerClicks();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        setDrawerClicks();
    }

    private void setDrawerClicks() {
        drawerRV.addOnItemTouchListener(new RecyclerItemTouchListener(this, drawerRV, new RecyclerClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position) {
                    case 1:
                        startActivity(new Intent(ClientHomeActivity.this, BookRideActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(ClientHomeActivity.this, BookingHistoryActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(ClientHomeActivity.this, PaymentsActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(ClientHomeActivity.this, NotificationsActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(ClientHomeActivity.this, ReferActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(ClientHomeActivity.this, HelpActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(ClientHomeActivity.this, PrivacyPolicyActivity.class));
                        break;
                    case 8:
                        startActivity(new Intent(ClientHomeActivity.this, ProfileSettingsActivity.class));
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        drawerRV = findViewById(R.id.drawerRV);
        drawerLayout = findViewById(R.id.drawerLayout);
        startLocationET = findViewById(R.id.startLocationET);
        dropLocationET = findViewById(R.id.dropLocationET);
        vehiclesRV = findViewById(R.id.vehiclesRV);
        rideContinueLayout = findViewById(R.id.rideContinueLayout);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        getLocation();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.ACTION_DOWN) {
            super.onKeyDown(keyCode, event);
            return true;
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGmap = googleMap;
    }

    public void getLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, AppConstants.LOCATION_PERMS_REQ_CODE);
        } else {
            if (checkPlayServices()) {
                buildApiClient();
                makeLocationRequest();
                displayLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConstants.LOCATION_PERMS_REQ_CODE:
                for (int gr : grantResults) {
                    if (gr == PackageManager.PERMISSION_GRANTED) {
                        if (checkPlayServices()) {
                            buildApiClient();
                            makeLocationRequest();
                            displayLocation();
                        }
                    } else {
                        finish();
                        moveTaskToBack(true);
                    }
                }
                break;
        }
    }

    public void displayLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: 5/29/2018 Send user to settings to grant permission
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        if (mLastLocation != null){
            latitude = mLastLocation.getLatitude();
            Log.d(TAG, "Your Latitude:\t" + latitude);
            longitude = mLastLocation.getLongitude();
            Log.d(TAG, "Your Longitude:\t" + longitude);

            geoFire.setLocation(getUid(), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (error != null){
                        Toast.makeText(ClientHomeActivity.this, "Could Not Get Your Location", Toast.LENGTH_SHORT).show();
                    } else {
                        if (mUserMarker != null){
                            mUserMarker.remove();
                            mUserMarker = mGmap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitude, longitude)).title("Your Location"));
                        }
                    }
                }
            });

        }
    }

    public void makeLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(AppConstants.FASTEST_INTERVAL);
        locationRequest.setSmallestDisplacement(AppConstants.DISPLACEMENT);
        locationRequest.setInterval(AppConstants.UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void buildApiClient() {
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        apiClient.connect();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "API Client Connection Failed:\t" + connectionResult.getErrorMessage().toString());
        apiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        apiClient.connect();
    }
}
