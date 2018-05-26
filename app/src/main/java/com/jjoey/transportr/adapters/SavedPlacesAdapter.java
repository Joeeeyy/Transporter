package com.jjoey.transportr.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoey.transportr.R;
import com.jjoey.transportr.models.SavedPlaces;
import com.jjoey.transportr.viewholders.SavedPlacesViewHolder;

import java.util.List;

public class SavedPlacesAdapter extends RecyclerView.Adapter<SavedPlacesViewHolder> {

    private final Context context;
    private List<SavedPlaces> itemsList;

    public SavedPlacesAdapter(Context context, List<SavedPlaces> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public SavedPlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_places_items, parent, false);
        return new SavedPlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedPlacesViewHolder viewholder, int position) {
        SavedPlaces places = itemsList.get(position);
        viewholder.placeTypeTV.setText(places.getPlaceType());
        viewholder.placeAddressTV.setText(places.getFullAddress());
    }

    @Override
    public int getItemCount() {
        if (itemsList == null) {
            return 0;
        }
        return itemsList.size();
    }
}