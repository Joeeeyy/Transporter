package com.jjoey.transportr.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.jjoey.transportr.R;
import com.jjoey.transportr.utils.FirebaseUtils;
import com.jjoey.transportr.utils.SharedPrefsHelper;

public class PrivacyPolicyActivity extends FirebaseUtils {

    private Toolbar toolbar;
    private ImageView backIV;
    private SharedPrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        initViews();
        setSupportActionBar(toolbar);
        prefsHelper = new SharedPrefsHelper(this);
        prefsHelper.setLoggedOut(false);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrivacyPolicyActivity.this, ClientHomeActivity.class));
            }
        });

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        backIV = findViewById(R.id.backIV);
    }

}
