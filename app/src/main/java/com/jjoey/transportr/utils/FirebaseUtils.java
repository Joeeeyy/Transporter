package com.jjoey.transportr.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jjoey.transportr.activities.LandingActivity;

public class FirebaseUtils extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void signOut(Context context) {
        Intent signOutIntent = new Intent(context, LandingActivity.class);
        signOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(signOutIntent);
    }

}
