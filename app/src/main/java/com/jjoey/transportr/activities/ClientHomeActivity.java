package com.jjoey.transportr.activities;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jjoey.transportr.R;
import com.jjoey.transportr.adapters.DrawerMenuAdapter;
import com.jjoey.transportr.interfaces.RecyclerClickListener;
import com.jjoey.transportr.models.ClientUser;
import com.jjoey.transportr.models.DrawerHeader;
import com.jjoey.transportr.models.DrawerItems;
import com.jjoey.transportr.utils.FirebaseUtils;
import com.jjoey.transportr.utils.RecyclerItemTouchListener;
import com.jjoey.transportr.utils.SharedPrefsHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ClientHomeActivity extends FirebaseUtils {

    private static final String TAG = ClientHomeActivity.class.getSimpleName();

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private RecyclerView drawerRV;

    private List<Object> objectList = new ArrayList<>();
    private DrawerMenuAdapter menuAdapter;

    private SharedPrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        prefsHelper = new SharedPrefsHelper(this);
        initViews();
        setSupportActionBar(toolbar);

        setUpDrawer();
        prefsHelper.setLoggedOut(false);

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
                        Log.d(TAG, user.fullName);
                        Log.d(TAG, user.emailAddr);

                        header.setProfileName(user.fullName);
                        header.setPhoneNum("+917326996784");

                        String imgURL = user.imgURL;
                        Log.d(TAG, "Image URL:\t" + imgURL);
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
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
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
                switch (position){
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        drawerRV = findViewById(R.id.drawerRV);
        drawerLayout = findViewById(R.id.drawerLayout);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.ACTION_DOWN){
            super.onKeyDown(keyCode, event);
            return true;
        }
        return false;
    }

}
