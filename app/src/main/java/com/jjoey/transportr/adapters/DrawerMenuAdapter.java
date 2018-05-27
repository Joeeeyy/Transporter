package com.jjoey.transportr.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoey.transportr.R;
import com.jjoey.transportr.models.DrawerHeader;
import com.jjoey.transportr.models.DrawerItems;
import com.jjoey.transportr.viewholders.DrawerHeaderViewHolder;
import com.jjoey.transportr.viewholders.DrawerItemsViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by JosephJoey on 5/24/2018.
 */

public class DrawerMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private List<Object> itemsList;

    public static final int HEADER_VIEW = 0;
    public static final int ITEMS_VIEW = 1;

    public DrawerMenuAdapter(Context context, List<Object> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEADER_VIEW){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header_layout, parent, false);
            return new DrawerHeaderViewHolder(view);
        } else if (viewType == ITEMS_VIEW){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_items_layout, parent, false);
            return new DrawerItemsViewHolder(view);
        }
        throw new RuntimeException("No matching viewTypes");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DrawerHeaderViewHolder){
            DrawerHeader header = (DrawerHeader) itemsList.get(position);
            ((DrawerHeaderViewHolder) holder).nameTV.setText(header.getProfileName());
            ((DrawerHeaderViewHolder) holder).phoneTV.setText(header.getPhoneNum());
            Picasso.with(context)
                    .load(header.getProfileIcon())
                    .placeholder(R.drawable.profile_avatar)
                    .into(((DrawerHeaderViewHolder) holder).profileIcon);

        } else if (holder instanceof DrawerItemsViewHolder){
            DrawerItems items = (DrawerItems) itemsList.get(position);
            ((DrawerItemsViewHolder) holder).itemTitleTV.setText(items.getItemTitle());
            Picasso.with(context)
                    .load(items.getIconImg())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(((DrawerItemsViewHolder) holder).iconImg);
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return HEADER_VIEW;
        return ITEMS_VIEW;
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}