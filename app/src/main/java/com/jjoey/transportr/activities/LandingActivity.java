package com.jjoey.transportr.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoey.transportr.R;
import com.jjoey.transportr.utils.FirebaseUtils;
import com.reginald.editspinner.EditSpinner;

public class LandingActivity extends AppCompatActivity {

    private static final String TAG = LandingActivity.class.getSimpleName();

    private EditSpinner et_country_code;
    private EditText et_phone;
    private ArrayAdapter<String> adapter;
    private Button otp_btn;
    private String phone;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        initViews();

        otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

    }

    private void validate() {
        code = et_country_code.getText().toString();
        phone = et_phone.getText().toString();

        Log.d(TAG, "Country Code:\t" + code);
        Log.d(TAG, "Phone:\t" + phone);

        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code)){
            initOTPFlow(phone);
        } else {
            if (phone.length() <= 10){
                et_phone.requestFocus();
                Snackbar.make(findViewById(android.R.id.content), "Phone Number Invalid", Snackbar.LENGTH_SHORT).show();
            }

            if (code.isEmpty()){
                Toast.makeText(this, "Phone Number Must Include Country Code", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void initOTPFlow(String phone) {
        Intent otpIntent = new Intent(LandingActivity.this, VerifyOTPActivity.class);
        otpIntent.putExtra("phone", phone);
        startActivity(otpIntent);
    }

    private void initViews() {
        et_country_code = findViewById(R.id.et_country_code);
        et_phone = findViewById(R.id.et_phone);
        otp_btn = findViewById(R.id.otp_btn);

        et_country_code.setEditable(false);
        et_country_code.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {
                adapter = new ArrayAdapter<String>(LandingActivity.this, android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.countries));
                et_country_code.setAdapter(adapter);
            }
        });

        et_country_code.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position){
                    case 0:
                        et_country_code.setText(adapter.getItem(position).toString());
                        break;
                }
            }
        });

    }

}
