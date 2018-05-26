package com.jjoey.transportr.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.jjoey.transportr.R;
import com.jjoey.transportr.models.ClientUser;
import com.jjoey.transportr.utils.FirebaseUtils;
import com.jjoey.transportr.utils.SharedPrefsHelper;
import com.jjoey.transportr.utils.Utils;

import java.util.HashMap;

public class AuthActivity extends FirebaseUtils {

    private static final String TAG = AuthActivity.class.getSimpleName();

    private EditText et_fullname, et_email, et_pass, et_confirm_pass;
    private Button loginBtn;

    private boolean isValid = false;
    private String name, email, pwd, confirm_pwd, phone;

    private SharedPrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        prefsHelper = new SharedPrefsHelper(this);

        initViews();
        phone = getIntent().getStringExtra("phone_num");
        Log.d(TAG, "Phone:\t" + phone);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateViews()) {
                    if (pwd.length() < 6 || confirm_pwd.length() < 6) {
                        Toast.makeText(AuthActivity.this, "Password Length Must be 6 or More ", Toast.LENGTH_SHORT).show();
                        if (!confirm_pwd.equals(pwd)) {
                            Snackbar.make(findViewById(android.R.id.content), "Passwords Don't Match", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        if (Utils.isNetwork(AuthActivity.this)){
                            mAuth.createUserWithEmailAndPassword(email, pwd)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()){

                                                prefsHelper.setHasAccount(true);

                                                ClientUser clientUser = new ClientUser();
                                                clientUser.setEmailAddr(email);
                                                clientUser.setFullName(name);
                                                clientUser.setPhoneNumber(phone);

                                                Log.d(TAG, "UID:\t" + FirebaseUtils.getUid());

                                                HashMap<String, String> detailsMap = new HashMap<>();
                                                detailsMap.put("userId", FirebaseUtils.getUid());
                                                detailsMap.put("email", clientUser.getEmailAddr());
                                                detailsMap.put("fullName", clientUser.getFullName());
                                                detailsMap.put("phoneNumber", clientUser.getPhoneNumber());

                                                usersRef.child(FirebaseUtils.getUid()).setValue(detailsMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Log.d(TAG, "User Saved");
                                                                    Intent intent = new Intent(AuthActivity.this, ClientHomeActivity.class);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    startActivity(intent);
                                                                } else {
                                                                    Log.d(TAG, "User NOT Saved");
                                                                    Log.d(TAG, task.getException().getMessage());
                                                                }
                                                            }
                                                        });
                                            } else {
                                                Log.d(TAG, "Failed:\t" + task.getException().getMessage());
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(AuthActivity.this, "Check Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Enter all Fields", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean validateViews() {
        name = et_fullname.getText().toString();
        email = et_email.getText().toString();
        pwd = et_pass.getText().toString();
        confirm_pwd = et_confirm_pass.getText().toString();

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(pwd) && TextUtils.isEmpty(confirm_pwd)) {
            isValid = false;
            Log.d(TAG, "Fields Empty");
        } else {
            isValid = true;
            Log.d(TAG, "Fields Correct");
        }
        return isValid;
    }

    private void initViews() {
        et_fullname = findViewById(R.id.et_fullname);
        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        et_confirm_pass = findViewById(R.id.et_confirm_pass);
        loginBtn = findViewById(R.id.loginBtn);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.ACTION_DOWN) {
            super.onKeyDown(keyCode, event);
            return true;
        }
        return false;
    }


}
