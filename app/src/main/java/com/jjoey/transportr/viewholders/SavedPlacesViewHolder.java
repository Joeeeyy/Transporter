package com.jjoey.transportr.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jjoey.transportr.R;

public class SavedPlacesViewHolder extends RecyclerView.ViewHolder {

    public TextView placeTypeTV, placeAddressTV;

    public SavedPlacesViewHolder(View itemView) {
        super(itemView);

        placeTypeTV = itemView.findViewById(R.id.placeTypeTV);
        placeAddressTV = itemView.findViewById(R.id.placeAddressTV);

    }
}
