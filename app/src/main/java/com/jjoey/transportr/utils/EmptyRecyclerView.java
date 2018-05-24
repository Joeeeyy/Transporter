package com.jjoey.transportr.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by JosephJoey on 3/28/2018.
 */

public class EmptyRecyclerView extends RecyclerView {

    private View emptyView, nonEmptyView;

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private final AdapterDataObserver dataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkEmpty();
        }
    };

    private void checkEmpty() {
        if (emptyView != null && getAdapter() != null){
            boolean emptyViewVisible = getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        Adapter oldAdapter = getAdapter();
        if (oldAdapter != null){
            oldAdapter.unregisterAdapterDataObserver(dataObserver);
        }
        super.setAdapter(adapter);
        if (adapter != null){
            adapter.registerAdapterDataObserver(dataObserver);
        }

        checkEmpty();
    }

    public void setEmptyView(View view){
        this.emptyView = view;
        checkEmpty();
    }

}
