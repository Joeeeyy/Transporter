package com.jjoey.transportr.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.jjoey.transportr.R;
import com.jjoey.transportr.utils.FirebaseUtils;
import com.jjoey.transportr.utils.SharedPrefsHelper;
import com.jjoey.transportr.utils.Utils;

public class BookRideActivity extends FirebaseUtils {

    private static final String TAG = BookRideActivity.class.getSimpleName();

    private Toolbar toolbar;
    private ImageView backIV;
    private SharedPrefsHelper prefsHelper;

    private EditText startET, dropET, et_coupon, et_payment_options, et_rider;
    private TextView estimatedPUTimeTV;
    private Button bookNowBtn, cancelBtn;

    private String start = null, drop = null;
    private boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ride);

        initViews();
        start = getIntent().getStringExtra("start_addr");
        drop = getIntent().getStringExtra("dest_addr");

        if (start != null && drop != null){
            startET.setText(start);
            dropET.setText(drop);
        } else {
            clearInputs();
        }

        setSupportActionBar(toolbar);
        prefsHelper = new SharedPrefsHelper(this);
        prefsHelper.setLoggedOut(false);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookRideActivity.this, ClientHomeActivity.class));
            }
        });

        et_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Coupon Clicked");
                showCouponDialog();
            }
        });

        et_rider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRiderDialog();
            }
        });

        et_payment_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPaymentsDialog();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookRideActivity.this, ClientHomeActivity.class));
                finish();
            }
        });

        bookNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate() && Utils.isNetwork(BookRideActivity.this)){

                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Enter Start and Drop Locations with Trip Meta Info", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void showCouponDialog() {
        new MaterialDialog.Builder(this)
                .theme(Theme.DARK)
                .title(getResources().getString(R.string.coupon_title_text))
                .content(R.string.input_content)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(R.string.coupon_hint, R.string.pre_fill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        validateCoupon(input);
                    }
                }).show();

    }

    private void validateCoupon(CharSequence input) {
        String coupon = input.toString();
    }

    private void showRiderDialog() {
        final CharSequence[] items = {"My Self", "A Friend", "Relative"};
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .theme(Theme.DARK)
                .title(getResources().getString(R.string.select_payment_method))
                .items(items)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                et_rider.setText(text);
                                break;
                            case 1:
                                et_rider.setText(text);
                                break;
                            case 2:
                                et_rider.setText(text);
                                break;
                        }
                        return true;
                    }
                });
        MaterialDialog materialDialog = builder.build();
        materialDialog.setActionButton(DialogAction.NEGATIVE, "CANCEL");
        materialDialog.setActionButton(DialogAction.POSITIVE, "OK");
        materialDialog.show();
    }

    private boolean validate() {
        String startAddr = startET.getText().toString();
        String destAddr = dropET.getText().toString();

        if (TextUtils.isEmpty(startAddr) && TextUtils.isEmpty(destAddr)) {
            isValid = false;
        } else if (!TextUtils.isEmpty(startAddr) && !TextUtils.isEmpty(destAddr)) {
            isValid = true;
        }
        return isValid;
    }

    private void clearInputs() {
        startET.setText("");
        dropET.setText("");
    }

    private void showPaymentsDialog() {
        // TODO: 5/30/2018 Get User Payment Options From Firebase
        final CharSequence[] items = {"Cash", "Card", "PayPal"};
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .theme(Theme.DARK)
                .title(getResources().getString(R.string.select_payment_method))
                .items(items)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                et_payment_options.setText(text);
                                break;
                            case 1:
                                et_payment_options.setText(text);
                                break;
                            case 2:
                                et_payment_options.setText(text);
                                break;
                        }
                        return true;
                    }
                });
        MaterialDialog materialDialog = builder.build();
        materialDialog.setActionButton(DialogAction.NEGATIVE, "CANCEL");
        materialDialog.setActionButton(DialogAction.POSITIVE, "OK");
        materialDialog.show();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        backIV = findViewById(R.id.backIV);
        dropET = findViewById(R.id.dropET);
        startET = findViewById(R.id.startET);
        et_payment_options = findViewById(R.id.et_payment_options);
        et_coupon = findViewById(R.id.et_coupon);
        et_rider = findViewById(R.id.et_rider);
        bookNowBtn = findViewById(R.id.bookNowBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
    }

}
