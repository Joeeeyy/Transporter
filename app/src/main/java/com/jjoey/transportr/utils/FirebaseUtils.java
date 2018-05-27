package com.jjoey.transportr.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jjoey.transportr.activities.ClientHomeActivity;
import com.jjoey.transportr.activities.LandingActivity;
import com.jjoey.transportr.activities.LoginActivity;
import com.jjoey.transportr.models.ClientUser;

public class FirebaseUtils extends AppCompatActivity {

    private static final String TAG = FirebaseUtils.class.getSimpleName();

    public static FirebaseAuth mAuth;
    public static DatabaseReference usersRef;
    public static StorageReference sRef, imageRef;
    public static UploadTask uploadTask;
    public static String downloadURL = null;

    public static FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        usersRef = FirebaseDatabase.getInstance().getReference(AppConstants.USERS_REF);
        usersRef.keepSynced(true);

        sRef = FirebaseStorage.getInstance().getReference(AppConstants.STORAGE_REF);

    }

    public static FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public static String getUid() {
        return mAuth.getCurrentUser().getUid();
    }

    public static void signOut(Context context) {
        if (mAuth != null){
            mAuth.signOut();
            mAuth = null;
        }
        Intent signOutIntent = new Intent(context, LoginActivity.class);
        signOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(signOutIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();

//        user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null){
//            startActivity(new Intent(this, ClientHomeActivity.class));
//        } else {
//            startActivity(new Intent(this, LandingActivity.class));
//        }

    }
}
