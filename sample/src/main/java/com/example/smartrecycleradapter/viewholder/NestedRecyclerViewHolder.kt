package com.example.smartrecycleradapter.viewholder

/*
 * Created by Manne Öhlund on 2019-06-25.
 * Copyright (c) All rights reserved.
 */

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.example.smartrecycleradapter.R
import com.example.smartrecycleradapter.extension.GridAutoLayoutManager
import com.example.smartrecycleradapter.models.NestedRecyclerViewModel
import smartadapter.SmartRecyclerAdapter
import smartadapter.viewholder.SmartAdapterHolder
import smartadapter.viewholder.SmartViewHolder

open class NestedRecyclerViewHolder(parentView: ViewGroup) : SmartViewHolder<NestedRecyclerViewModel>(
        LayoutInflater.from(parentView.context)
                .inflate(R.layout.nested_recycler_view, parentView, false)),
        SmartAdapterHolder {

    private val title: TextView = itemView.findViewById(R.id.title)
    protected val recyclerView: RecyclerView = itemView.findViewById(R.id.nested_recycler_view)

    override fun setSmartRecyclerAdapter(smartRecyclerAdapter: SmartRecyclerAdapter) {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, HORIZONTAL, false)
        recyclerView.adapter = smartRecyclerAdapter
    }

    override fun bind(item: NestedRecyclerViewModel) {
        title.text = item.title
    }

    internal interface OnMoreButtonClickListener : smartadapter.listener.OnItemClickListener {

        @JvmDefault
        override fun getViewId() = R.id.more
    }
}

class ComingSoonMoviesViewHolder(parentView: ViewGroup) : NestedRecyclerViewHolder(parentView) {

    internal interface OnMoreButtonClickListener : NestedRecyclerViewHolder.OnMoreButtonClickListener {

        @JvmDefault
        override fun getViewHolderType(): Class<out SmartViewHolder<*>> = ComingSoonMoviesViewHolder::class.java
    }
}

class MyWatchListViewHolder(parentView: ViewGroup) : NestedRecyclerViewHolder(parentView) {

    internal interface OnMoreButtonClickListener : NestedRecyclerViewHolder.OnMoreButtonClickListener {

        @JvmDefault
        override fun getViewHolderType(): Class<out SmartViewHolder<*>> = MyWatchListViewHolder::class.java
    }
}

class ActionMoviesViewHolder(parentView: ViewGroup) : NestedRecyclerViewHolder(parentView) {

    internal interface OnMoreButtonClickListener : NestedRecyclerViewHolder.OnMoreButtonClickListener {

        @JvmDefault
        override fun getViewHolderType(): Class<out SmartViewHolder<*>> = ActionMoviesViewHolder::class.java
    }
}

class AdventureMoviesViewHolder(parentView: ViewGroup) : NestedRecyclerViewHolder(parentView) {

    internal interface OnMoreButtonClickListener : NestedRecyclerViewHolder.OnMoreButtonClickListener {

        @JvmDefault
        override fun getViewHolderType(): Class<out SmartViewHolder<*>> = AdventureMoviesViewHolder::class.java
    }
}

class AnimatedMoviesViewHolder(parentView: ViewGroup) : NestedRecyclerViewHolder(parentView) {

    internal interface OnMoreButtonClickListener : NestedRecyclerViewHolder.OnMoreButtonClickListener {

        @JvmDefault
        override fun getViewHolderType(): Class<out SmartViewHolder<*>> = AnimatedMoviesViewHolder::class.java
    }
}

class SciFiMoviesViewHolder(parentView: ViewGroup) : NestedRecyclerViewHolder(parentView) {

    internal interface OnMoreButtonClickListener : NestedRecyclerViewHolder.OnMoreButtonClickListener {

        @JvmDefault
        override fun getViewHolderType(): Class<out SmartViewHolder<*>> = SciFiMoviesViewHolder::class.java
    }
}

class RecentlyPlayedMoviesViewHolder(parentView: ViewGroup) : NestedRecyclerViewHolder(parentView) {

    private val more: TextView = itemView.findViewById(R.id.more)

    init {
        more.visibility = GONE
    }

    override fun setSmartRecyclerAdapter(smartRecyclerAdapter: SmartRecyclerAdapter) {
        recyclerView.layoutManager = GridAutoLayoutManager(recyclerView.context, 60)
        recyclerView.adapter = smartRecyclerAdapter
    }
}