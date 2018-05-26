package com.jjoey.transportr.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.jjoey.transportr.R;
import com.jjoey.transportr.utils.FirebaseUtils;
import com.jjoey.transportr.utils.Utils;

public class LoginActivity extends FirebaseUtils {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText et_email, et_pass;
    private TextView resetPwdTV;
    private Button signInBtn;

    private ProgressBar progressBar;
    private RelativeLayout loginLayout;

    private String email, pass;
    private boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        resetPwdTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetwork(LoginActivity.this)){
                    showPasswordDialog();
                } else {
                    Toast.makeText(LoginActivity.this, "Check Network Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.GONE);
                progressBar.setIndeterminate(true);
                if (validateViews()){
                    if (Utils.isNetwork(LoginActivity.this)){
                        mAuth.signInWithEmailAndPassword(email, pass)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            Intent intent = new Intent(LoginActivity.this, ClientHomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            Log.d(TAG, "Login Failed:\t" + task.getException().getMessage());
                                        }
                                    }
                                });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Check Network Connection", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Use Proper Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validateViews() {
        email = et_email.getText().toString();
        pass = et_pass.getText().toString();

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pass) ) {
            isValid = false;
            Log.d(TAG, "Fields Empty");
        } else {
            isValid = true;
            Log.d(TAG, "Fields Correct");
        }
        return isValid;
    }

    private void showPasswordDialog() {
        String resetMail = et_email.getText().toString();
        if (resetMail.isEmpty()){
            et_email.requestFocus();
            Snackbar.make(findViewById(android.R.id.content), "Enter Email from Input Field And Try Again", Snackbar.LENGTH_LONG).show();
        } else {
            if (mAuth != null){
                mAuth.sendPasswordResetEmail(resetMail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Snackbar.make(findViewById(android.R.id.content), "Check Email for Instructions", Snackbar.LENGTH_LONG).show();
                                } else {

                                }
                            }
                        });
            }
        }
    }

    private void disAbleUI(boolean value) {
        et_email.setEnabled(value);
        et_pass.setEnabled(value);
        signInBtn.setEnabled(value);
        resetPwdTV.setEnabled(value);
    }

    private void initViews() {
        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        resetPwdTV = findViewById(R.id.resetPwdTV);
        signInBtn = findViewById(R.id.signInBtn);
        progressBar = findViewById(R.id.progressBar);
        loginLayout = findViewById(R.id.loginLayout);

        resetPwdTV.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

    }
}
