package smartadapter.listener

/*
 * Created by Manne Öhlund on 2019-08-01.
 * Copyright (c) All rights reserved.
 */

import android.view.View
import io.github.manneohlund.smartrecycleradapter.R
import smartadapter.Position
import smartadapter.ViewEventId

/**
 * Default implementation of adapter item row long click listener.
 */
interface OnItemLongClickListener : OnViewEventListener {
    override val viewEventId: ViewEventId
        get() = R.id.event_on_long_click
}

/**
 * Helper method to provide lambda call to interface instances of [OnItemLongClickListener].
 */
inline fun onItemLongClickListener(crossinline viewEvent: (
        view: View,
        viewEventId: ViewEventId,
        position: Position) -> Unit) = object : OnItemLongClickListener {
    override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
        viewEvent(view, viewEventId, position)
    }
}
