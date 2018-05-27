package com.jjoey.transportr.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jjoey.transportr.R;
import com.jjoey.transportr.adapters.SavedPlacesAdapter;
import com.jjoey.transportr.models.ClientUser;
import com.jjoey.transportr.models.SavedPlaces;
import com.jjoey.transportr.utils.AppConstants;
import com.jjoey.transportr.utils.EmptyRecyclerView;
import com.jjoey.transportr.utils.FirebaseUtils;
import com.jjoey.transportr.utils.SharedPrefsHelper;
import com.jjoey.transportr.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettingsActivity extends FirebaseUtils {

    private static final String TAG = ProfileSettingsActivity.class.getSimpleName();

    private Toolbar toolbar;
    private ImageView backIV, editIV;
    private CircleImageView profileImg, cameraCIV;
    private TextView profileNameTV, phoneTV, emailTV, addPlacesTV;
    private EditText et_change_pwd;
    private EmptyRecyclerView savedPlacesRV;
    private LinearLayout emptyPlacesLayout, logOutLayout;

    private boolean isInEditPwdMode = false;

    private List<SavedPlaces> list = new ArrayList<>();
    private SavedPlacesAdapter placesAdapter;

    private SharedPrefsHelper prefsHelper;

    private static final int REQ_CODE = 201;
    private static final int CAM_CODE = 301;
    private static final int GAL_CODE = 302;

    private boolean isCameraImage = false, isGalleryImage = false;
    private Bitmap cameraBitmap = null;
    private Uri galleryUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        prefsHelper = new SharedPrefsHelper(this);

        initViews();
        setSupportActionBar(toolbar);
        disableInputPassword();

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileSettingsActivity.this, ClientHomeActivity.class));
            }
        });

        cameraCIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPerms();
                } else {
                    showChooserDialog();
                }
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
                prefsHelper.setLoggedOut(true);
                FirebaseUtils.signOut(ProfileSettingsActivity.this);
            }
        });

        showDetails();

    }

    @TargetApi(23)
    private void checkPerms() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_CODE);
        } else {
            showChooserDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    showChooserDialog();
                } else {
                    Snackbar sk = Snackbar.make(findViewById(android.R.id.content), "Some permissions are Required. Go Into Settings and GRANT these permission", Snackbar.LENGTH_LONG);
                    sk.setAction("GRANT", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent settingsIntent = new Intent();
                            Uri uri = Uri.fromParts("package", getPackageName(), "ProfileSettingsActivity");
                            settingsIntent.setData(uri);
                            startActivityForResult(settingsIntent, REQ_CODE);
                        }
                    });
                    sk.show();
                }
                break;
        }
    }

    private void showChooserDialog() {
        final CharSequence[] items = {"Open From Camera", "Select from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Open From Camera")) {
                    Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camIntent, CAM_CODE);
                    isCameraImage = true;
                    isGalleryImage = false;
                } else if (items[i].equals("Select from Gallery")) {
                    Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    pickIntent.setType("image/*");
                    startActivityForResult(pickIntent, GAL_CODE);
                    isCameraImage = false;
                    isGalleryImage = true;
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAM_CODE:
                cameraBitmap = (Bitmap) data.getExtras().get("data");
                profileImg.setImageBitmap(cameraBitmap);
                uploadImage(cameraBitmap, galleryUri);
                break;
            case GAL_CODE:
                galleryUri = data.getData();
                profileImg.setImageURI(galleryUri);
                uploadImage(cameraBitmap, galleryUri);
                break;
        }
    }

    private void uploadImage(Bitmap cameraBitmap, Uri galleryUri) {
        if (isCameraImage) {
            byte[] imageBytes = Utils.bitmapToByteArray(cameraBitmap);

            imageRef = sRef.child(AppConstants.CLIENT).child(AppConstants.PROFILE_IMAGES).child(FirebaseUtils.getUid() + ".jpg");

            uploadTask = imageRef.putBytes(imageBytes);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded Successfully", Snackbar.LENGTH_LONG).show();
                        downloadURL = task.getResult().getDownloadUrl().toString();
                        usersRef.child(mAuth.getCurrentUser().getUid())
                                .child("imgURL")
                                .setValue(downloadURL)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Image Saved in DB");
                                        } else {
                                            Log.d(TAG, "Image NOT Saved in DB");
                                        }
                                    }
                                });
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Image Upload Failed...Re-Select Image", Snackbar.LENGTH_LONG).show();
                    }
                }
            });

        } else if (isGalleryImage) {
            //File imageFile = Utils.uriToFile(galleryUri);
            Bitmap bitmap = null;
            byte[] bytes = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), galleryUri);
                bytes = Utils.bitmapToByteArray(bitmap);

                imageRef = sRef.child(AppConstants.CLIENT).child(AppConstants.PROFILE_IMAGES).child(FirebaseUtils.getUid() + ".jpg");
                uploadTask = imageRef.putBytes(bytes);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(findViewById(android.R.id.content), "Image Uploaded Successfully", Snackbar.LENGTH_LONG).show();
                            downloadURL = task.getResult().getDownloadUrl().toString();
                            usersRef.child(mAuth.getCurrentUser().getUid())
                                    .child("imgURL")
                                    .setValue(downloadURL)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Image Saved in DB");
                                            } else {
                                                Log.d(TAG, "Image NOT Saved in DB");
                                            }
                                        }
                                    });
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Image Upload Failed...Re-Select Image", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void showDetails() {
        usersRef.child(FirebaseUtils.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ClientUser user = dataSnapshot.getValue(ClientUser.class);
                        Log.d(TAG, user.fullName);
                        Log.d(TAG, user.emailAddr);
                        profileNameTV.setText(user.fullName);
                        emailTV.setText(user.emailAddr);
                        String imgURL = user.imgURL;
                        Log.d(TAG, "Image URL:\t" + imgURL);
                        Picasso.with(ProfileSettingsActivity.this)
                                .load(imgURL)
                                .placeholder(R.drawable.profile_avatar)
                                .into(profileImg);

//                        phoneTV.setText(clientUser.phoneNumber);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
        cameraCIV = findViewById(R.id.cameraCIV);
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

        initPlacesView();

    }

    private void initPlacesView() {
        savedPlacesRV.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        savedPlacesRV.setLayoutManager(llm);
    }

}
