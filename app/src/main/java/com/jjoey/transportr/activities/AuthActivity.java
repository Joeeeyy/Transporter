package com.jjoey.transportr.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jjoey.transportr.R;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = AuthActivity.class.getSimpleName();

    private EditText et_fullname, et_email, et_pass, et_confirm_pass;
    private Button loginBtn;

    private boolean isValid = false;
    private String name;
    private String email;
    private String pwd;
    private String confirm_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initViews();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateViews()) {
                    // TODO: 5/24/2018 Go to ClientRiderActivity
                    if (pwd.length() < 6 || confirm_pwd.length() < 6) {
                        Toast.makeText(AuthActivity.this, "Password Length Must be 6 or More ", Toast.LENGTH_SHORT).show();
                        if (!confirm_pwd.equals(pwd)) {
                            Snackbar.make(findViewById(android.R.id.content), "Passwords Don't Match", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        startActivity(new Intent(AuthActivity.this, ClientHomeActivity.class));
                    }
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Enter all Fields properly", Snackbar.LENGTH_LONG).show();
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
}
