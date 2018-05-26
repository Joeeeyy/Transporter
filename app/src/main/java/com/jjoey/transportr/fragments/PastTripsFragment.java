package com.jjoey.transportr.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jjoey.transportr.R;
import com.jjoey.transportr.activities.ClientHomeActivity;
import com.jjoey.transportr.utils.EmptyRecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PastTripsFragment extends Fragment {

    private EmptyRecyclerView pastTripsRV;
    private LinearLayout emptyState;
    private Button makeTripBtn;

    public PastTripsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_trips, container, false);

        initViews(view);

        makeTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ClientHomeActivity.class));
            }
        });

        return view;
    }

    private void initViews(View view) {
        pastTripsRV = view.findViewById(R.id.pastTripsRV);
        emptyState = view.findViewById(R.id.emptyState);
        makeTripBtn = view.findViewById(R.id.makeTripBtn);

        pastTripsRV.setEmptyView(emptyState);

    }

}
