package smartrecycleradapter.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_simple_item.*
import smartadapter.SmartRecyclerAdapter
import smartadapter.listener.OnItemSelectedListener
import smartadapter.state.SelectionStateHolder
import smartadapter.viewholder.SmartViewHolder
import smartadapter.viewholder.StatefulViewHolder
import smartrecycleradapter.R

/*
 * Created by Manne Öhlund on 2019-08-23.
 * Copyright (c) All rights reserved.
 */

class MultipleExpandableItemActivity : BaseSampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Multi Expandable Item";

        val items = (0..100).toList()

        SmartRecyclerAdapter
                .items(items)
                .map(Integer::class.java, SimpleExpandableItemViewHolder::class.java)
                .addViewEventListener(object : OnItemExpandedListener {
                    override fun onViewEvent(view: View, actionId: Int, position: Int) {
                        supportActionBar?.subtitle = "${expandedStateHolder.selectedItemsCount} / ${items.size} expanded"
                    }
                })
                .into<SmartRecyclerAdapter>(recyclerView)
    }
}

class SimpleExpandableItemViewHolder(parentView: ViewGroup) : SmartViewHolder<Int>(
        LayoutInflater.from(parentView.context)
                .inflate(R.layout.simple_expandable_item, parentView, false)),
        StatefulViewHolder<SelectionStateHolder> {

    private val title: TextView = itemView.findViewById(R.id.itemTitle)
    private val subItem: LinearLayout = itemView.findViewById(R.id.subItemContainer)
    private val subItemTitle: TextView = itemView.findViewById(R.id.subItemTitle)

    lateinit var selectionStateHolder: SelectionStateHolder

    override fun setStateHolder(selectionStateHolder: SelectionStateHolder) {
        this.selectionStateHolder = selectionStateHolder
    }

    override fun bind(item: Int?) {
        title.text = "Item $item"
        subItemTitle.text = "Sub item of '$item'"
        when(selectionStateHolder.isSelected(adapterPosition)) {
            true -> {
                title.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_expand_less_black_24dp,0)
                subItem.visibility = View.VISIBLE
            }
            false -> {
                title.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_expand_more_black_24dp,0)
                subItem.visibility = View.GONE
            }
        }
    }
}

var expandedStateHolder = SelectionStateHolder()

interface OnItemExpandedListener : OnItemSelectedListener {

    @JvmDefault
    override fun getSelectionStateHolder() = expandedStateHolder

    @JvmDefault
    override fun getViewId() = R.id.itemTitle
}