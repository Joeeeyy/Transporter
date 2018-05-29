package com.jjoey.transportr.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoey.transportr.R;

public class VehiclesViewHolder extends RecyclerView.ViewHolder {

    public TextView vehicleTypeTV, estimatedTimeTV;
    public ImageView vehicleImg;

    public VehiclesViewHolder(View itemView) {
        super(itemView);

        estimatedTimeTV = itemView.findViewById(R.id.estimatedTimeTV);
        vehicleTypeTV = itemView.findViewById(R.id.vehicleTypeTV);
        vehicleImg = itemView.findViewById(R.id.vehicleImg);

    }
}
