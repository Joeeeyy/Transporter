package com.jjoey.transportr.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoey.transportr.R;
import com.jjoey.transportr.models.PaymentOptions;
import com.jjoey.transportr.viewholders.PaymentsViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsViewHolder> {

    private static final String TAG = PaymentsAdapter.class.getSimpleName();

    private final Context context;
    private List<PaymentOptions> itemsList;

    public PaymentsAdapter(Context context, List<PaymentOptions> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public PaymentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_items_layout, parent, false);
        return new PaymentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PaymentsViewHolder viewholder, int position) {
        PaymentOptions options = itemsList.get(position);
        if (options.isCash()){
            viewholder.paymentTypeTV.setGravity(Gravity.CENTER_VERTICAL);
        }
        viewholder.paymentTypeTV.setText(options.getPaymentType());
//        if (options.isCard()){
//            String cardNumber = options.getOtherDetails();
//            String formattedTxt = cardNumber.replaceAll("\\w(?=\\w{4})", "*");
//            viewholder.otherDetailsTV.setText(formattedTxt);
//        } else {
//            viewholder.otherDetailsTV.setText(options.getOtherDetails());
//        }
        Picasso.with(context)
                .load(options.getPaymentIcon())
                .placeholder(R.mipmap.ic_launcher)
                .into(viewholder.paymentIcon);
    }

    @Override
    public int getItemCount() {
        if (itemsList == null) {
            return 0;
        }
        return itemsList.size();
    }
}