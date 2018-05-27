package com.jjoey.transportr.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoey.transportr.R;
import com.jjoey.transportr.utils.EmptyRecyclerView;
import com.jjoey.transportr.utils.FirebaseUtils;
import com.jjoey.transportr.utils.SharedPrefsHelper;

public class NotificationsActivity extends FirebaseUtils {

    private Toolbar toolbar;
    private ImageView backIV;
    private EmptyRecyclerView notifsRV;
    private TextView emptyState;
    private SharedPrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        initViews();
        setSupportActionBar(toolbar);
        prefsHelper = new SharedPrefsHelper(this);
        prefsHelper.setLoggedOut(false);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotificationsActivity.this, ClientHomeActivity.class));
            }
        });

        setUpList();

    }

    private void setUpList() {
        notifsRV.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        notifsRV.setLayoutManager(llm);


        getNotifications();
    }

    private void getNotifications() {

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        backIV = findViewById(R.id.backIV);
        emptyState = findViewById(R.id.emptyState);
        notifsRV = findViewById(R.id.notifsRV);

        notifsRV.setEmptyView(emptyState);

    }

}
