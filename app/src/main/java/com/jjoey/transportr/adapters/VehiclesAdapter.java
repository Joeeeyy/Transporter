package com.jjoey.transportr.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoey.transportr.R;
import com.jjoey.transportr.models.Vehicle;
import com.jjoey.transportr.viewholders.VehiclesViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class
VehiclesAdapter extends RecyclerView.Adapter<VehiclesViewHolder> {

    private final Context context;
    private List<Vehicle> itemsList;

    public VehiclesAdapter(Context context, List<Vehicle> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public VehiclesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_items_layout, parent, false);
        return new VehiclesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VehiclesViewHolder viewholder, int position) {
        Vehicle vehicle = itemsList.get(position);
        viewholder.vehicleTypeTV.setText(vehicle.getVehicleType());
        viewholder.estimatedTimeTV.setText(vehicle.getEstimatedTime());
        Picasso.with(context)
                .load(vehicle.getVehicleIcon())
                .placeholder(R.drawable.taxi_cab)
                .into(viewholder.vehicleImg);
    }

    @Override
    public int getItemCount() {
        if (itemsList == null) {
            return 0;
        }
        return itemsList.size();
    }
}