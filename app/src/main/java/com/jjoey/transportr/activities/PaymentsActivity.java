package com.jjoey.transportr.activities;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoey.transportr.R;
import com.jjoey.transportr.utils.EmptyRecyclerView;

public class PaymentsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView backIV;
    private EmptyRecyclerView paymentsRV;
    private TextView emptyState;
    private LinearLayout addPaymentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        initViews();
        setSupportActionBar(toolbar);

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
            }
        });

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        backIV = findViewById(R.id.backIV);
        emptyState = findViewById(R.id.emptyState);
        paymentsRV = findViewById(R.id.paymentsRV);
        addPaymentBtn = findViewById(R.id.addPaymentBtn);
    }

}
