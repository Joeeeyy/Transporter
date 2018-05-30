package com.jjoey.transportr.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoey.transportr.R;

public class PaymentsViewHolder extends RecyclerView.ViewHolder {

    public TextView paymentTypeTV, otherDetailsTV;
    public ImageView paymentIcon;

    public PaymentsViewHolder(View itemView) {
        super(itemView);

        paymentIcon = itemView.findViewById(R.id.paymentIcon);
        paymentTypeTV = itemView.findViewById(R.id.paymentTypeTV);
        otherDetailsTV = itemView.findViewById(R.id.otherDetailsTV);

    }
}
