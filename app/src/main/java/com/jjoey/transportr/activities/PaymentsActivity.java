package com.jjoey.transportr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.jjoey.transportr.R;
import com.jjoey.transportr.adapters.PaymentsAdapter;
import com.jjoey.transportr.models.PaymentOptions;
import com.jjoey.transportr.utils.EmptyRecyclerView;
import com.jjoey.transportr.utils.FirebaseUtils;
import com.jjoey.transportr.utils.SharedPrefsHelper;

import java.util.ArrayList;
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

        addPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 5/24/2018 Show Select Payment Options
                showOptionsDialog();
            }
        });

        adapter = new PaymentsAdapter(this, list);
        paymentsRV.setAdapter(adapter);

    }

    private void showOptionsDialog() {
        options = new PaymentOptions();
        final CharSequence[] items = {"Cash Payment", "Pay with Credit/Debit Card", "Pay with PayPal"};
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .theme(Theme.DARK)
                .title(getResources().getString(R.string.select_payment_method))
                .items(items)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                if (isCashSetup == false){
                                    options.setCash(true);
                                    options.setCard(false);
                                    options.setProvider(false);
                                    options.setPaymentType(text.toString());
                                    options.setPaymentIcon(R.drawable.cash_icon);
                                    list.add(options);
                                    adapter.notifyDataSetChanged();
                                    isCashSetup = true;
                                } else {
                                    Toast.makeText(PaymentsActivity.this, "Cash Payment Mode Already Setup...Try Other Modes", Toast.LENGTH_LONG).show();
                                }
                                break;
                            case 1:

                                new MaterialDialog.Builder(PaymentsActivity.this)
                                        .theme(Theme.DARK)
                                        .title(getResources().getString(R.string.credit_card_name_title_text))
                                        .content(R.string.card_name_input_content)
                                        .inputRange(10, 25)
                                        .inputType(InputType.TYPE_CLASS_TEXT)
                                        .input(R.string.credit_card_name_hint, R.string.card_Holder_name_pre_fill, new MaterialDialog.InputCallback() {
                                            @Override
                                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                                String cardNum = input.toString();
                                                Log.d(TAG, "Credit Card Number:\t" + cardNum);
                                                options.setOtherDetails(cardNum);

                                                // ask card number
                                                new MaterialDialog.Builder(PaymentsActivity.this)
                                                        .theme(Theme.DARK)
                                                        .title(getResources().getString(R.string.credit_card_title_text))
                                                        .content(R.string.card_input_content)
                                                        .inputRange(16, 19)
                                                        .inputType(InputType.TYPE_CLASS_NUMBER)
                                                        .input(R.string.credit_card_hint, R.string.card_pre_fill, new MaterialDialog.InputCallback() {
                                                            @Override
                                                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                                                String cardNum = input.toString();
                                                                Log.d(TAG, "Credit Card Number:\t" + cardNum);
                                                                options.setOtherDetails(cardNum);
                                                            }
                                                        }).show();

                                            }
                                        }).show();

                                options.setCard(true);
                                options.setCash(false);
                                options.setProvider(false);
                                options.setPaymentType(text.toString());
                                options.setPaymentIcon(R.drawable.card_icon);
                                list.add(options);
                                adapter.notifyDataSetChanged();
                                break;
                            case 2:
                                if (isPayPalSetup == false){
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
