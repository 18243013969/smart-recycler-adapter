package smartrecycleradapter.viewholder

/*
 * Created by Manne Öhlund on 2019-06-25.
 * Copyright © 2019. All rights reserved.
 */

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import smartadapter.Position
import smartadapter.SmartRecyclerAdapter
import smartadapter.viewevent.models.ViewEvent
import smartadapter.viewholder.CustomViewEventListenerHolder
import smartadapter.viewholder.SmartAdapterHolder
import smartadapter.viewholder.SmartViewHolder
import smartrecycleradapter.R
import smartrecycleradapter.models.MovieModel
import smartrecycleradapter.utils.displayHeight
import smartrecycleradapter.utils.displayWidth

class PosterViewHolder(parentView: ViewGroup) : SmartViewHolder<MovieModel>(
    LayoutInflater.from(parentView.context).inflate(R.layout.poster_item, parentView, false)
), CustomViewEventListenerHolder, SmartAdapterHolder {

    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val playButton: ImageView = itemView.findViewById(R.id.playButton)

    override var smartRecyclerAdapter: SmartRecyclerAdapter? = null
    override lateinit var customViewEventListener: (ViewEvent) -> Unit

    init {
        playButton.setOnLongClickListener { playButton ->
            customViewEventListener.invoke(
                OnPlayLongClick(
                    smartRecyclerAdapter!!,
                    this@PosterViewHolder,
                    adapterPosition,
                    playButton
                )
            )
            false
        }
    }

    private val requestOption = RequestOptions()
        .error(R.drawable.ic_broken_image_black_48dp)
        .centerInside()

    override fun bind(movie: MovieModel) {
        Glide.with(imageView)
            .load(movie.iconUrl)
            .apply(requestOption)
            .override(imageView.context.displayWidth, imageView.context.displayHeight)
            .centerInside()
            .into(imageView)
    }

    override fun unbind() {
        Glide.with(imageView).clear(imageView)
    }

    class OnPlayLongClick(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<*>,
        position: Position /* = Int */,
        view: View
    ) : ViewEvent(adapter, viewHolder, position, view)
}
