package smartadapter.viewevent.listener

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import io.github.manneohlund.smartrecycleradapter.viewevent.R
import smartadapter.SmartRecyclerAdapter
import smartadapter.SmartViewHolderBinder
import smartadapter.SmartViewHolderType
import smartadapter.ViewId
import smartadapter.findView
import smartadapter.listener.OnCreateViewHolderListener
import smartadapter.viewevent.model.ViewEvent
import smartadapter.viewholder.OnItemTouchEventListener
import smartadapter.viewholder.SmartViewHolder

/**
 * Contains the logic for the multi view holder views click for recycler adapter positions.
 */
@SuppressLint("ClickableViewAccessibility")
open class OnTouchEventListener(
    override val viewHolderType: SmartViewHolderType = SmartViewHolder::class,
    @IdRes
    override vararg val viewIds: ViewId = intArrayOf(R.id.undefined),
    override var eventListener: (ViewEvent.OnTouchEvent) -> Unit
) : OnViewEventListener<ViewEvent.OnTouchEvent>,
    SmartViewHolderBinder,
    OnCreateViewHolderListener {

    override fun onCreateViewHolder(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<Any>
    ) {
        viewIds.forEach {
            with(findView(it, viewHolder)) {
                setOnTouchListener { view, motionEvent ->
                    val event = ViewEvent.OnTouchEvent(
                        adapter,
                        viewHolder,
                        viewHolder.adapterPosition,
                        view,
                        motionEvent
                    )
                    (viewHolder as? OnItemTouchEventListener)?.let {
                        viewHolder.onViewEvent(event)
                    }
                    eventListener.invoke(event)
                    false
                }
            }
        }
    }
}
