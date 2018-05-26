package com.jjoey.transportr.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jjoey.transportr.R;
import com.jjoey.transportr.utils.FirebaseUtils;

import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends FirebaseUtils {

    private static final String TAG = VerifyOTPActivity.class.getSimpleName();

    private EditText firstDigit, secondDigit, thirdDigit, fourthDigit, fifthDigit, sixthDigit;
    private TextView resendOTP_tv;
    private Button continueBtn;

    private String inputOne, inputTwo, inputThree, inputFour, inputFive, inputSix;
    private String phone, sms_code, verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        phone = getIntent().getStringExtra("phone");
        Log.d(TAG, "Phone:\t" + phone);

        initViews();

        resendOTP_tv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        resendOTP_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reSendOTP();
            }
        });

        if (phone != null){
            sendOTP(phone);
            resendOTP_tv.setEnabled(false);
        }
        handleOTPView();

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(firstDigit.getText().toString()) &&
                        !TextUtils.isEmpty(secondDigit.getText().toString()) &&
                        !TextUtils.isEmpty(thirdDigit.getText().toString()) &&
                        !TextUtils.isEmpty(fourthDigit.getText().toString()) &&
                        !TextUtils.isEmpty(fifthDigit.getText().toString()) &&
                        !TextUtils.isEmpty(sixthDigit.getText().toString())) {

                    inputOne = firstDigit.getText().toString();
                    inputTwo = secondDigit.getText().toString();
                    inputThree = thirdDigit.getText().toString();
                    inputFour = fourthDigit.getText().toString();
                    inputFive = fifthDigit.getText().toString();
                    inputSix = sixthDigit.getText().toString();

                    sms_code = inputOne + inputTwo + inputThree + inputFour + inputFive + inputSix;
                    Log.d(TAG, "SMS Code:\t" + sms_code);

                    startActivity(new Intent(VerifyOTPActivity.this, AuthActivity.class));

                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Enter All OTP Fields", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void sendOTP(String phone) {
//        String number = "+63" + phone;
//        Log.d(TAG, "FUll Number:\t" + number);
        String number = "+917326996784";

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
                    fifthDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        fifthDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                fifthDigit.setBackground(getResources().getDrawable(R.drawable.otp_empty_bkg));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    fifthDigit.setBackground(getResources().getDrawable(R.drawable.otp_filled_bkg));
                    sixthDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        sixthDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                sixthDigit.setBackground(getResources().getDrawable(R.drawable.otp_empty_bkg));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    sixthDigit.setBackground(getResources().getDrawable(R.drawable.otp_filled_bkg));
                    sixthDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    private void reSendOTP() {
        sendOTP(phone);
    }

    private void initViews() {
        firstDigit = findViewById(R.id.firstDigit);
        secondDigit = findViewById(R.id.secondDigit);
        thirdDigit = findViewById(R.id.thirdDigit);
        fourthDigit = findViewById(R.id.fourthDigit);
        fifthDigit = findViewById(R.id.fifthDigit);
        sixthDigit = findViewById(R.id.sixthDigit);
        resendOTP_tv = findViewById(R.id.resendOTP_tv);
        continueBtn = findViewById(R.id.continueBtn);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.ACTION_DOWN){
            super.onKeyDown(keyCode, event);
            return true;
        }
        return false;
    }

}
