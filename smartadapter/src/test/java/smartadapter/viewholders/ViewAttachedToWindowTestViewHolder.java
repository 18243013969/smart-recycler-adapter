package smartadapter.viewholders;

/*
 * Created by Manne Öhlund on 2019-07-16.
 * Copyright (c) All rights reserved.
 */

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import smartadapter.listener.OnViewAttachedToWindowListener;
import smartadapter.listener.OnViewDetachedFromWindowListener;
import smartadapter.viewholder.SmartViewHolder;

public class ViewAttachedToWindowTestViewHolder extends SmartViewHolder implements OnViewAttachedToWindowListener, OnViewDetachedFromWindowListener {

    public ViewAttachedToWindowTestViewHolder(View view) {
        super(view);
    }

    @Override
    public void bind(@NonNull Object item) {

    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder viewHolder) {

    }
}
