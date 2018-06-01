package com.jjoey.transportr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.OnCardFormValidListener;
import com.braintreepayments.cardform.view.CardForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jjoey.transportr.R;
import com.jjoey.transportr.adapters.PaymentsAdapter;
import com.jjoey.transportr.models.ClientUser;
import com.jjoey.transportr.models.PaymentOptions;
import com.jjoey.transportr.utils.AppConstants;
import com.jjoey.transportr.utils.EmptyRecyclerView;
import com.jjoey.transportr.utils.FirebaseUtils;
import com.jjoey.transportr.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentsActivity extends FirebaseUtils {

    private static final String TAG = PaymentsActivity.class.getSimpleName();

    private Toolbar toolbar;
    private ImageView backIV;
    private EmptyRecyclerView paymentsRV;
    private TextView emptyState;

    private LinearLayout addPaymentBtn;
    private SharedPrefsHelper prefsHelper;

    private List<PaymentOptions> list = new ArrayList();
    private PaymentsAdapter adapter;
    private PaymentOptions options = null;

    private CardForm cf = null;
    private boolean isCashSetup = false, isCardSetup = false, isPayPalSetup = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        initViews();
        setSupportActionBar(toolbar);
        prefsHelper = new SharedPrefsHelper(this);
        prefsHelper.setLoggedOut(false);

        paymentsRV.setEmptyView(emptyState);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentsActivity.this, ClientHomeActivity.class));
            }
        });

        options = new PaymentOptions();
        getUserPaymentInfo();

        addPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptionsDialog();
            }
        });

        adapter = new PaymentsAdapter(this, list);
        paymentsRV.setAdapter(adapter);

    }

    private void getUserPaymentInfo() {
        usersRef.child(FirebaseUtils.getUid()).child(AppConstants.PAYMENT_MODES)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Log.d(TAG, "DataSnapshot:\t" + ds.getChildren());
                            Log.d(TAG, "Keys:\t" + ds.getKey());

                            usersRef.child(FirebaseUtils.getUid()).child(AppConstants.PAYMENT_MODES)
                                    .child(ds.getKey().toString())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String paymentMode = (String) dataSnapshot.child("Payment Mode").getValue();
                                            Log.d(TAG, "Cash Payment from Firebase:\t" + paymentMode);
                                            if (paymentMode.equals(AppConstants.CASH_PAYMENT_MODES)){
                                                options.setCash(true);
                                                options.setCard(false);
                                                options.setProvider(false);
                                                options.setPaymentType(paymentMode);
                                                options.setPaymentIcon(R.drawable.cash_icon);
                                                list.add(options);
                                                adapter.notifyDataSetChanged();
                                                isCashSetup = true;
                                            }
                                            if (paymentMode.equals(AppConstants.CARD_PAYMENT_MODES)){
                                                PaymentOptions paymentOptions = new PaymentOptions();
                                                paymentOptions.setCard(true);
                                                paymentOptions.setCash(false);
                                                paymentOptions.setProvider(false);
                                                paymentOptions.setPaymentType(paymentMode);
                                                paymentOptions.setPaymentIcon(R.drawable.card_icon);
                                                list.add(paymentOptions);
                                                adapter.notifyDataSetChanged();
                                                isCardSetup = true;
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void showOptionsDialog() {
        final CharSequence[] items = {"Cash Payment", "Pay with Credit/Debit Card", "Pay with PayPal"};
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .theme(Theme.DARK)
                .title(getResources().getString(R.string.select_payment_method))
                .items(items)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(final MaterialDialog dialog, View itemView, int which, final CharSequence text) {
                        switch (which) {
                            case 0:
                                if (isCashSetup == false) {
                                    options.setCash(true);
                                    options.setCard(false);
                                    options.setProvider(false);
                                    options.setPaymentType(text.toString());
                                    options.setPaymentIcon(R.drawable.cash_icon);
                                    list.add(options);
                                    adapter.notifyDataSetChanged();
                                    isCashSetup = true;
                                    prefsHelper.saveCashMode(true);

                                    saveUserPaymentMode(isCashSetup, isCardSetup, isPayPalSetup);

                                } else {
                                    Toast.makeText(PaymentsActivity.this, "Cash Payment Mode Already Setup...Try Other Modes", Toast.LENGTH_LONG).show();
                                }
                                break;
                            case 1:
                                if (isCardSetup == false) {
                                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                    View formView = inflater.inflate(R.layout.card_form_layout, null, false);
                                    cf = formView.findViewById(R.id.card_form);

                                    final MaterialDialog mDialog = new MaterialDialog.Builder(PaymentsActivity.this)
                                            .customView(formView, true)
                                            .title("Enter Card Details")
                                            .show();

                                    cf.cardRequired(true)
                                            .expirationRequired(true)
                                            .cvvRequired(true)
                                            .postalCodeRequired(true)
                                            .maskCardNumber(true)
                                            .mobileNumberRequired(true)
                                            .mobileNumberExplanation("SMS is required on this number")
                                            .actionLabel("Setup Card Payment Mode")
                                            .setup(PaymentsActivity.this);
                                    cf.setOnCardFormValidListener(new OnCardFormValidListener() {
                                        @Override
                                        public void onCardFormValid(boolean valid) {
                                            if (valid) {
                                                Log.d(TAG, "Form isValid");
                                                cf.setOnCardFormSubmitListener(new OnCardFormSubmitListener() {
                                                    @Override
                                                    public void onCardFormSubmit() {
                                                        Log.d(TAG, "Form Submitted");
                                                        String num = cf.getCardNumber();
                                                        String cvv = cf.getCvv();
                                                        String mobile = cf.getMobileNumber();
                                                        String expiryYear = cf.getExpirationYear();
                                                        String expiryMonth = cf.getExpirationMonth();
                                                        prefsHelper.saveCardDetails("", num, expiryYear, expiryMonth, cvv);

                                                        options.setCard(true);
                                                        options.setCash(false);
                                                        options.setProvider(false);
                                                        options.setPaymentType(text.toString());
                                                        options.setPaymentIcon(R.drawable.card_icon);
                                                        mDialog.dismiss();
                                                        list.add(options);
                                                        adapter.notifyDataSetChanged();
                                                        isCardSetup = true;

                                                        saveUserPaymentMode(isCashSetup, isCardSetup, isPayPalSetup);

                                                    }
                                                });
                                            } else {
                                                Toast.makeText(PaymentsActivity.this, "Form is INVALID!!!", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "Form INVALID!!");
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(PaymentsActivity.this, "Card Payment Mode Already Setup. Swipe Right/Leftto Delete and Add New Card ", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 2:
                                // TODO: 5/30/2018 Get PayPal User Info and Save to Firebase
                                if (isPayPalSetup == false) {
                                    options.setProvider(true);
                                    options.setCash(false);
                                    options.setCard(false);
                                    options.setPaymentType(text.toString());
                                    options.setPaymentIcon(R.drawable.paypal_icon);
                                    list.add(options);
                                    adapter.notifyDataSetChanged();
                                    isPayPalSetup = true;
                                } else {
                                    Toast.makeText(PaymentsActivity.this, "PayPal Mode Already Setup...Try Other Modes", Toast.LENGTH_LONG).show();
                                }
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

    private void saveUserPaymentMode(boolean isCashSetup, boolean isCardSetup, boolean isPayPalSetup) {

        if (isCashSetup == true) {
            HashMap<String, String> cashMode = new HashMap<>();
            cashMode.put("Payment Mode", options.getPaymentType());

            Log.d(TAG, "CashMode Value from Prefs:\t" + prefsHelper.getCashMode());

            usersRef.child(FirebaseUtils.getUid()).child(AppConstants.PAYMENT_MODES)
                    .push()
                    .setValue(cashMode)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Saved Cash Payment Mode");
                            } else {
                                Log.d(TAG, "Failed to Save Cash Payment Mode");
                            }
                        }
                    });
        } else if (isCardSetup == true) {
            HashMap<String, String> cardMode = new HashMap<>();
            cardMode.put("Payment Mode", options.getPaymentType());
            Log.d(TAG, "Payment mode:\t" + AppConstants.PAYMENT_MODE_CARD_TYPE);

            HashMap<String, String> cardDetails = new HashMap<>();
            cardDetails.put("card_number", prefsHelper.getCardNumber());
            cardDetails.put("card_expiry_year", prefsHelper.getCardExpiryYear());
            cardDetails.put("card_expiry_month", prefsHelper.getCardExpiryMonth());

            String cvv = prefsHelper.getCardCVVNumber();
            cardDetails.put("card_cvv", cvv);

            cardMode.put("card_details", String.valueOf(cardDetails));

//            usersRef.child(FirebaseUtils.getUid()).child(AppConstants.PAYMENT_MODES + "/" + AppConstants.PAYMENT_MODE_CARD_TYPE )
            usersRef.child(FirebaseUtils.getUid()).child(AppConstants.PAYMENT_MODES)
                    .push()
                    .setValue(cardMode)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Saved Card Payment Mode");
                                Snackbar.make(findViewById(android.R.id.content), "Your Card Details Have Been Saved Securely. Future Trips Can Now be Paid Using this Card", Snackbar.LENGTH_LONG).show();
                            } else {
                                Log.d(TAG, "Failed to Save Card Payment Mode");
                            }
                        }
                    });

        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        backIV = findViewById(R.id.backIV);
        emptyState = findViewById(R.id.emptyState);
        paymentsRV = findViewById(R.id.paymentsRV);
        addPaymentBtn = findViewById(R.id.addPaymentBtn);

        paymentsRV.setHasFixedSize(true);
        paymentsRV.setLayoutManager(new LinearLayoutManager(this));

    }

}
