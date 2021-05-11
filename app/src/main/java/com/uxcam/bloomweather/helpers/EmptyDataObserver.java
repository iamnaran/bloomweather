package com.uxcam.bloomweather.helpers;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class EmptyDataObserver extends RecyclerView.AdapterDataObserver {

    private View emptyView;
    private RecyclerView recyclerView;

    public EmptyDataObserver(RecyclerView rv, View ev) {
        this.recyclerView = rv;
        this.emptyView = ev;
        checkIfEmpty();
    }

    private void checkIfEmpty() {
        if (emptyView != null && recyclerView.getAdapter() != null) {
            boolean emptyViewVisible = recyclerView.getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onChanged() {
        super.onChanged();
        checkIfEmpty();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        super.onItemRangeChanged(positionStart, itemCount);
    }

}