package com.jjoey.transportr.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.jjoey.transportr.fragments.OnGoingTripsFragment;
import com.jjoey.transportr.fragments.PastTripsFragment;
import com.jjoey.transportr.R;
import com.jjoey.transportr.adapters.HistoryTabsPagerAdapter;
import com.jjoey.transportr.utils.FirebaseUtils;
import com.jjoey.transportr.utils.SharedPrefsHelper;

public class BookingHistoryActivity extends FirebaseUtils {

    private Toolbar toolbar;
    private ImageView backIV;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private HistoryTabsPagerAdapter tabsPagerAdapter;
    private SharedPrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        initViews();
        setSupportActionBar(toolbar);
        prefsHelper = new SharedPrefsHelper(this);
        prefsHelper.setLoggedOut(false);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookingHistoryActivity.this, ClientHomeActivity.class));
            }
        });

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.tabsContainer, new OnGoingTripsFragment()).commit();

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        backIV = findViewById(R.id.backIV);
        tabLayout = findViewById(R.id.bookingTabs);
        viewPager = findViewById(R.id.viewPager);

        setUpViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setUpViewPager(ViewPager viewPager) {
        tabsPagerAdapter = new HistoryTabsPagerAdapter(getSupportFragmentManager());
        tabsPagerAdapter.addFragments(new OnGoingTripsFragment(), "Ongoing Trips");
        tabsPagerAdapter.addFragments(new PastTripsFragment(), "Past Trips");
        viewPager.setAdapter(tabsPagerAdapter);
    }

}
