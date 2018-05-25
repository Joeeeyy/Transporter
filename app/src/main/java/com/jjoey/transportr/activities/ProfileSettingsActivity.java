package com.jjoey.transportr.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoey.transportr.R;
import com.jjoey.transportr.utils.EmptyRecyclerView;
import com.jjoey.transportr.utils.FirebaseUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView backIV, editIV;
    private CircleImageView profileImg;
    private TextView profileNameTV, phoneTV, emailTV, addPlacesTV;
    private EditText et_change_pwd;
    private EmptyRecyclerView savedPlacesRV;
    private LinearLayout emptyPlacesLayout, logOutLayout;

    private boolean isInEditPwdMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        initViews();
        setSupportActionBar(toolbar);
        disableInputPassword();

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileSettingsActivity.this, ClientHomeActivity.class));
            }
        });

        editIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        addPlacesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        et_change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableInputPassword();
            }
        });

        logOutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUtils.signOut(ProfileSettingsActivity.this);
            }
        });

    }

    private void enableInputPassword() {
        et_change_pwd.setEnabled(true);
        et_change_pwd.requestFocus();
        isInEditPwdMode = true;
    }

    private void disableInputPassword() {
        et_change_pwd.setEnabled(false);
        isInEditPwdMode = false;
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        backIV = findViewById(R.id.backIV);
        editIV = findViewById(R.id.editIV);
        profileImg = findViewById(R.id.profileImg);
        profileNameTV = findViewById(R.id.profileNameTV);
        phoneTV = findViewById(R.id.phoneTV);
        emailTV = findViewById(R.id.emailTV);
        addPlacesTV = findViewById(R.id.addPlacesTV);
        emptyPlacesLayout = findViewById(R.id.emptyPlacesLayout);
        et_change_pwd = findViewById(R.id.et_change_pwd);
        savedPlacesRV = findViewById(R.id.savedPlacesRV);
        logOutLayout = findViewById(R.id.logOutLayout);

        addPlacesTV.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        savedPlacesRV.setEmptyView(emptyPlacesLayout);
    }

}
