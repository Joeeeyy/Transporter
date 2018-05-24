package com.jjoey.transportr.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjoey.transportr.R;

/**
 * Created by JosephJoey on 5/24/2018.
 */

public class DrawerItemsViewHolder extends RecyclerView.ViewHolder {

    public ImageView iconImg;
    public TextView itemTitleTV;
    public RelativeLayout drawerItemsHolderLayout;

    public DrawerItemsViewHolder(View itemView) {
        super(itemView);

        iconImg = itemView.findViewById(R.id.iconImg);
        itemTitleTV = itemView.findViewById(R.id.itemTitleTV);
        drawerItemsHolderLayout = itemView.findViewById(R.id.drawerItemsHolderLayout);

    }
}
