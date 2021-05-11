package com.uxcam.bloomweather.helpers;

import androidx.recyclerview.widget.RecyclerView;


public abstract class AppRecyclerView extends RecyclerView.Adapter {

    abstract protected void add(Object object);

    abstract protected void clear();
}
