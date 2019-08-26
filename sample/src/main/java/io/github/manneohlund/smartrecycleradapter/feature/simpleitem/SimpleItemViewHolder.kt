package io.github.manneohlund.smartrecycleradapter.feature.simpleitem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.manneohlund.smartrecycleradapter.R
import smartadapter.viewholder.SmartViewHolder


/*
 * Created by Manne Öhlund on 2019-08-04.
 * Copyright (c) All rights reserved.
 */

open class SimpleItemViewHolder(parentView: View) : SmartViewHolder<Int>(
        LayoutInflater.from(parentView.context)
                .inflate(R.layout.simple_item, parentView as ViewGroup, false)) {

    override fun bind(index: Int?) {
        (itemView as TextView).text = "Item $index"
    }
}