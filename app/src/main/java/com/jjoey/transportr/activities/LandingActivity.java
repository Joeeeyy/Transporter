package com.jjoey.transportr.activities;

import android.content.Intent;
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

import com.jjoey.transportr.R;
import com.reginald.editspinner.EditSpinner;

public class LandingActivity extends AppCompatActivity {

    private static final String TAG = LandingActivity.class.getSimpleName();

    private EditSpinner et_country_code;
    private EditText et_phone;
    private ArrayAdapter<String> adapter;
    private Button otp_btn;

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
        String code = et_country_code.getText().toString();
        String phone = et_phone.getText().toString();

        Log.d(TAG, "Country Code:\t" + code);
        Log.d(TAG, "Phone:\t" + phone);

        startActivity(new Intent(LandingActivity.this, VerifyOTPActivity.class));

//        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code)){
//            initOTPFlow();
//        }

    }

    private void initOTPFlow() {

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

    private void showList() {
        TextView tv = new TextView(this);
        tv.setText("Philippines");
    }
}
