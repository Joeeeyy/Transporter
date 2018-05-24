package com.jjoey.transportr.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoey.transportr.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView backIV, editIV;
    private CircleImageView profileImg;
    private TextView profileNameTV, phoneTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        initViews();
        setSupportActionBar(toolbar);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileSettingsActivity.this, ClientHomeActivity.class));
            }
        });

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        backIV = findViewById(R.id.backIV);
        editIV = findViewById(R.id.editIV);
        profileImg = findViewById(R.id.profileImg);
        profileNameTV = findViewById(R.id.profileNameTV);
        phoneTV = findViewById(R.id.phoneTV);
    }

}
