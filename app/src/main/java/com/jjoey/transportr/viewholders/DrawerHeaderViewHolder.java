package com.jjoey.transportr.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoey.transportr.R;

/**
 * Created by JosephJoey on 5/24/2018.
 */

public class DrawerHeaderViewHolder extends RecyclerView.ViewHolder {

    public ImageView profileIcon;
    public TextView nameTV, phoneTV;

    public DrawerHeaderViewHolder(View itemView) {
        super(itemView);

        profileIcon = itemView.findViewById(R.id.profileIcon);
        nameTV = itemView.findViewById(R.id.nameTV);
        phoneTV = itemView.findViewById(R.id.phoneTV);

    }
}
