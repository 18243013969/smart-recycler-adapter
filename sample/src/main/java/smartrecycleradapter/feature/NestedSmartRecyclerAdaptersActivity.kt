package smartrecycleradapter.feature

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_simple_item.*
import smartadapter.Position
import smartadapter.SmartEndlessScrollRecyclerAdapter
import smartadapter.SmartRecyclerAdapter
import smartrecycleradapter.BuildConfig
import smartrecycleradapter.DemoActivity.Companion.getActionName
import smartrecycleradapter.R
import smartrecycleradapter.data.MovieDataItems
import smartrecycleradapter.models.ActionMoviesModel
import smartrecycleradapter.models.AdventureMoviesModel
import smartrecycleradapter.models.AnimatedMoviesModel
import smartrecycleradapter.models.ComingSoonMoviesModel
import smartrecycleradapter.models.CopyrightModel
import smartrecycleradapter.models.MovieModel
import smartrecycleradapter.models.MoviePosterModel
import smartrecycleradapter.models.MyWatchListModel
import smartrecycleradapter.models.RecentlyPlayedMoviesModel
import smartrecycleradapter.models.SciFiMoviesModel
import smartrecycleradapter.viewholder.ActionMoviesViewHolder
import smartrecycleradapter.viewholder.AdventureMoviesViewHolder
import smartrecycleradapter.viewholder.AnimatedMoviesViewHolder
import smartrecycleradapter.viewholder.ComingSoonMoviesViewHolder
import smartrecycleradapter.viewholder.CopyrightViewHolder
import smartrecycleradapter.viewholder.LargeThumbViewHolder
import smartrecycleradapter.viewholder.MyWatchListViewHolder
import smartrecycleradapter.viewholder.PosterViewHolder
import smartrecycleradapter.viewholder.RecentlyPlayedMoviesViewHolder
import smartrecycleradapter.viewholder.SciFiMoviesViewHolder
import smartrecycleradapter.viewholder.ThumbViewHolder
import java.util.Locale

/*
 * Created by Manne Öhlund on 2019-08-14.
 * Copyright (c) All rights reserved.
 */

class NestedSmartRecyclerAdaptersActivity : BaseSampleActivity() {

    lateinit var comingSoonSmartMovieAdapter: SmartEndlessScrollRecyclerAdapter
    private lateinit var myWatchListSmartMovieAdapter: SmartRecyclerAdapter
    private lateinit var actionMoviesSmartMovieAdapter: SmartRecyclerAdapter
    private lateinit var adventuresMoviesSmartMovieAdapter: SmartRecyclerAdapter
    private lateinit var animatedMoviesSmartMovieAdapter: SmartRecyclerAdapter
    private lateinit var sciFiMoviesSmartMovieAdapter: SmartRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Nested SmartRecyclerAdapters"

        initNestedSmartRecyclerAdapters()
        initSmartRecyclerAdapter()
    }

    private fun initSmartRecyclerAdapter() {
        val items = mutableListOf(
            ComingSoonMoviesModel("Coming soon"),
            MyWatchListModel("My watch list", MovieDataItems.myWatchListItems.toMutableList()),
            ActionMoviesModel("Action", MovieDataItems.nestedActionItems.toMutableList()),
            AdventureMoviesModel("Adventure", MovieDataItems.nestedAdventureItems.toMutableList()),
            AnimatedMoviesModel("Animated"),
            SciFiMoviesModel("Sci-Fi"),
            CopyrightModel(
                String.format(
                    "SmartRecyclerAdapter v%s\n\nDeveloped by Manne Öhlund",
                    BuildConfig.VERSION_NAME
                )
            )
        )

        SmartRecyclerAdapter
            .items(items)
            .map(MoviePosterModel::class, PosterViewHolder::class)
            .map(ComingSoonMoviesModel::class, ComingSoonMoviesViewHolder::class)
            .map(MyWatchListModel::class, MyWatchListViewHolder::class)
            .map(ActionMoviesModel::class, ActionMoviesViewHolder::class)
            .map(AdventureMoviesModel::class, AdventureMoviesViewHolder::class)
            .map(AnimatedMoviesModel::class, AnimatedMoviesViewHolder::class)
            .map(SciFiMoviesModel::class, SciFiMoviesViewHolder::class)
            .map(RecentlyPlayedMoviesModel::class, RecentlyPlayedMoviesViewHolder::class)
            .map(CopyrightModel::class, CopyrightViewHolder::class)

            // Map nested SmartRecyclerAdapter
            .map(ComingSoonMoviesViewHolder::class, comingSoonSmartMovieAdapter)
            .map(MyWatchListViewHolder::class, myWatchListSmartMovieAdapter)
            .map(ActionMoviesViewHolder::class, actionMoviesSmartMovieAdapter)
            .map(AdventureMoviesViewHolder::class, adventuresMoviesSmartMovieAdapter)
            .map(AnimatedMoviesViewHolder::class, animatedMoviesSmartMovieAdapter)
            .map(SciFiMoviesViewHolder::class, sciFiMoviesSmartMovieAdapter)

            .into<SmartRecyclerAdapter>(recyclerView)
    }

    private var moreItemsLoadedCount = 0
    private fun initNestedSmartRecyclerAdapters() {

        val onComingSoonItemClickListener = LargeThumbViewHolder.OnItemClickListener { view, smartRecyclerAdapter, position ->
            showToast(
                "Coming soon \n%s \n%s index: %d",
                getMovieTitle(comingSoonSmartMovieAdapter, position),
                getActionName(R.id.event_on_click),
                position
            )
        }

        comingSoonSmartMovieAdapter =
            SmartEndlessScrollRecyclerAdapter.items(MovieDataItems.comingSoonItems)
                .map(MovieModel::class, LargeThumbViewHolder::class)
                .addViewEventListener(onComingSoonItemClickListener)
                .create()

        comingSoonSmartMovieAdapter.autoLoadMoreEnabled = true

        // Set custom load more view
        comingSoonSmartMovieAdapter.loadMoreLayoutResource = R.layout.custom_loadmore_view

        // Pagination ends after 3 loads
        comingSoonSmartMovieAdapter.onLoadMoreListener = {
            Handler().postDelayed({
                comingSoonSmartMovieAdapter.addItems(
                    comingSoonSmartMovieAdapter.itemCount - 1,
                    MovieDataItems.loadMoreItems
                )
                if (moreItemsLoadedCount++ == 2)
                    comingSoonSmartMovieAdapter.isEndlessScrollEnabled = false
            }, 1000)
        }

        val sharedOnItemClickListener = ThumbViewHolder.OnItemClickListener { view, smartRecyclerAdapter, position ->
            showToast(
                "%s \n%s \n%s index: %d",
                getAdapterName(smartRecyclerAdapter),
                getMovieTitle(smartRecyclerAdapter, position),
                getActionName(R.id.event_on_click),
                position
            )
        }

        myWatchListSmartMovieAdapter = SmartRecyclerAdapter.items(MovieDataItems.myWatchListItems)
            .map(MovieModel::class, ThumbViewHolder::class)
            .addViewEventListener(sharedOnItemClickListener)
            .create()

        actionMoviesSmartMovieAdapter = SmartRecyclerAdapter.items(MovieDataItems.nestedActionItems)
            .map(MovieModel::class, ThumbViewHolder::class)
            .addViewEventListener(sharedOnItemClickListener)
            .create()

        adventuresMoviesSmartMovieAdapter =
            SmartRecyclerAdapter.items(MovieDataItems.nestedAdventureItems)
                .map(MovieModel::class, ThumbViewHolder::class)
                .addViewEventListener(sharedOnItemClickListener)
                .create()

        animatedMoviesSmartMovieAdapter =
            SmartRecyclerAdapter.items(MovieDataItems.nestedAnimatedItems)
                .map(MovieModel::class, ThumbViewHolder::class)
                .addViewEventListener(sharedOnItemClickListener)
                .create()

        sciFiMoviesSmartMovieAdapter = SmartRecyclerAdapter.items(MovieDataItems.nestedSciFiItems)
            .map(MovieModel::class, ThumbViewHolder::class)
            .addViewEventListener(sharedOnItemClickListener)
            .create()
    }

    private fun getAdapterName(smartRecyclerAdapter: SmartRecyclerAdapter?) : String {
        return when(smartRecyclerAdapter) {
            myWatchListSmartMovieAdapter -> "MyWatchList"
            actionMoviesSmartMovieAdapter -> "ActionMovies"
            adventuresMoviesSmartMovieAdapter -> "AdventuresMovies"
            animatedMoviesSmartMovieAdapter -> "AnimatedMovies"
            sciFiMoviesSmartMovieAdapter -> "SciFiMovies"
            else -> "Unknown"
        }
    }

    private fun getMovieTitle(smartRecyclerAdapter: SmartRecyclerAdapter, position: Position): String {
        val movieModel = smartRecyclerAdapter.getItem(position) as MovieModel
        return movieModel.title
    }

    fun showToast(format: String, vararg args: Any) {
        Toast.makeText(
            this,
            String.format(Locale.ENGLISH, format, *args),
            Toast.LENGTH_LONG
        ).show()
    }
}
