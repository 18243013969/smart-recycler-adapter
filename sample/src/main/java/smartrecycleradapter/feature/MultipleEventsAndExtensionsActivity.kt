package smartrecycleradapter.feature

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_simple_item.*
import smartadapter.SmartRecyclerAdapter
import smartadapter.listener.OnClick
import smartadapter.listener.OnItemClickListener
import smartadapter.listener.OnItemLongClickListener
import smartadapter.listener.OnLongClick
import smartadapter.widget.AutoDragAndDropExtension
import smartadapter.widget.AutoRemoveItemSwipeExtension
import smartadapter.widget.DragAndDropExtensionBuilder
import smartadapter.widget.SwipeExtensionBuilder
import smartrecycleradapter.feature.simpleitem.SimpleItemViewHolder

/*
 * Created by Manne Öhlund on 2019-08-11.
 * Copyright (c) All rights reserved.
 */

class MultipleEventsAndExtensionsActivity : BaseSampleActivity() {

    private lateinit var smartRecyclerAdapter: SmartRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Drag & Drop, Swipe, ViewEvents"

        val items = (0..50).toMutableList()

        smartRecyclerAdapter = SmartRecyclerAdapter
            .items(items)
            .map(Integer::class, SimpleItemViewHolder::class)
            .addViewEventListener(object : OnItemClickListener {
                override val listener: OnClick = { view, adapter, position ->
                    Toast.makeText(applicationContext, "onClick $position", Toast.LENGTH_SHORT).show()
                }
            })
            .addViewEventListener(object : OnItemLongClickListener {
                override val listener: OnLongClick = { view, adapter, position ->
                    Toast.makeText(applicationContext, "onLongClick $position", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            .addExtensionBuilder(
                DragAndDropExtensionBuilder(AutoDragAndDropExtension()).apply {
                    dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                    longPressDragEnabled = true
                    onItemMovedListener = { oldViewHolder, targetViewHolder ->
                        Toast.makeText(
                            applicationContext,
                            "onItemMoved from ${oldViewHolder.adapterPosition} to ${targetViewHolder.adapterPosition}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
            .addExtensionBuilder(
                SwipeExtensionBuilder(AutoRemoveItemSwipeExtension()).apply {
                    swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    onItemSwipedListener = { viewHolder, direction ->
                        showToast(viewHolder, direction)
                    }
                }
            )
            .into(recyclerView)
    }

    val showToast = { viewHolder: RecyclerView.ViewHolder, direction: Int ->
        val directionStr = when (direction) {
            ItemTouchHelper.LEFT -> "Left"
            ItemTouchHelper.RIGHT -> "Right"
            else -> "??"
        }
        Toast.makeText(
            applicationContext,
            "onItemSwiped @ ${viewHolder.adapterPosition}, $directionStr",
            Toast.LENGTH_SHORT
        ).show()
    }
}
