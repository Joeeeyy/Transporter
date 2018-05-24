package com.jjoey.transportr.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jjoey.transportr.R;

public class VerifyOTPActivity extends AppCompatActivity {

    private static final String TAG = VerifyOTPActivity.class.getSimpleName();

    private EditText firstDigit, secondDigit, thirdDigit, fourthDigit;
    private TextView resendOTP_tv;
    private Button continueBtn;

    private String inputOne, inputTwo, inputThree, inputFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        initViews();

        resendOTP_tv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        resendOTP_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reSendOTP();
            }
        });

        handleOTPView();

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(firstDigit.getText().toString()) && !TextUtils.isEmpty(secondDigit.getText().toString()) && !TextUtils.isEmpty(thirdDigit.getText().toString()) && !TextUtils.isEmpty(fourthDigit.getText().toString())){
                    startActivity(new Intent(VerifyOTPActivity.this, AuthActivity.class));
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Enter All OTP Fields", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void handleOTPView() {
        firstDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                firstDigit.setBackground(getResources().getDrawable(R.drawable.otp_empty_bkg));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    firstDigit.setBackground(getResources().getDrawable(R.drawable.otp_filled_bkg));
                    secondDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        secondDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                secondDigit.setBackground(getResources().getDrawable(R.drawable.otp_empty_bkg));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    secondDigit.setBackground(getResources().getDrawable(R.drawable.otp_filled_bkg));
                    thirdDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        thirdDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                thirdDigit.setBackground(getResources().getDrawable(R.drawable.otp_empty_bkg));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    thirdDigit.setBackground(getResources().getDrawable(R.drawable.otp_filled_bkg));
                    fourthDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        fourthDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                fourthDigit.setBackground(getResources().getDrawable(R.drawable.otp_empty_bkg));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    fourthDigit.setBackground(getResources().getDrawable(R.drawable.otp_filled_bkg));
                    fourthDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    private void reSendOTP() {

    }

    private void initViews() {
        firstDigit = findViewById(R.id.firstDigit);
        secondDigit = findViewById(R.id.secondDigit);
        thirdDigit = findViewById(R.id.thirdDigit);
        fourthDigit = findViewById(R.id.fourthDigit);
        resendOTP_tv = findViewById(R.id.resendOTP_tv);
        continueBtn = findViewById(R.id.continueBtn);
    }
}
