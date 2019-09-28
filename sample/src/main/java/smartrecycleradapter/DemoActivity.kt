package smartrecycleradapter

/*
 * Created by Manne Öhlund on 29/05/17.
 * Copyright © 2019 All rights reserved.
 */

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import smartadapter.*
import smartadapter.listener.OnItemClickListener
import smartadapter.listener.OnViewEventListener
import smartadapter.listener.onItemClickListener
import smartrecycleradapter.data.MovieDataItems
import smartrecycleradapter.extension.PreCachingLinearLayoutManager
import smartrecycleradapter.feature.*
import smartrecycleradapter.models.*
import smartrecycleradapter.viewholder.*
import java.util.*
import kotlin.reflect.KClass

class DemoActivity : AppCompatActivity() {

    internal lateinit var recyclerView: RecyclerView
    internal lateinit var mainSmartMovieAdapter: SmartEndlessScrollRecyclerAdapter
    internal lateinit var comingSoonSmartMovieAdapter: SmartEndlessScrollRecyclerAdapter
    internal lateinit var myWatchListSmartMovieAdapter: SmartRecyclerAdapter
    internal lateinit var actionMoviesSmartMovieAdapter: SmartRecyclerAdapter
    internal lateinit var adventuresMoviesSmartMovieAdapter: SmartRecyclerAdapter
    internal lateinit var animatedMoviesSmartMovieAdapter: SmartRecyclerAdapter
    internal lateinit var sciFiMoviesSmartMovieAdapter: SmartRecyclerAdapter
    private lateinit var recentlyPlayedMoviesSmartMovieAdapter: SmartRecyclerAdapter
    private lateinit var dialogAdapter: SmartRecyclerAdapter

    private lateinit var moreSamplesDialog: AlertDialog

    private var moreItemsLoadedCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        recyclerView = findViewById(R.id.recycler_view)

        initNestedSmartRecyclerAdapters()
        initSmartRecyclerAdapter()
        initMoreDemosButton()
    }

    private fun initSmartRecyclerAdapter() {
        val items = ArrayList<Any>()

        items.add(MoviePosterModel(MovieDataItems.randomPoster))
        items.add(
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_widgets_black_24dp,
                "More samples"
            )
        )
        items.add(ComingSoonMoviesModel("Coming soon"))
        items.add(MyWatchListModel("My watch list"))
        items.add(MovieBannerModel("Recommended", MovieDataItems.randomBanner))
        items.add(ActionMoviesModel("Action"))
        items.add(AdventureMoviesModel("Adventure"))
        items.add(MovieBannerModel("Trending", MovieDataItems.randomBanner))
        items.add(AnimatedMoviesModel("Animated"))
        items.add(SciFiMoviesModel("Sci-Fi"))
        items.add(MovieBannerModel("Promotion", MovieDataItems.randomBanner))
        items.add(RecentlyPlayedMoviesModel("Recently played"))
        items.add(
            CopyrightModel(
                String.format(
                    "SmartRecyclerAdapter v%s\n\nDeveloped by Manne Öhlund",
                    BuildConfig.VERSION_NAME
                )
            )
        )

        mainSmartMovieAdapter = SmartEndlessScrollRecyclerAdapter
            .items(items)
            .map(MoviePosterModel::class, PosterViewHolder::class)
            .map(SampleFabViewHolder.SimpleFabItem::class, SampleFabViewHolder::class)
            .map(MovieBannerModel::class, BannerViewHolder::class)
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
            .map(RecentlyPlayedMoviesViewHolder::class, recentlyPlayedMoviesSmartMovieAdapter)

            .setLayoutManager(PreCachingLinearLayoutManager.getInstance(this))

            // You need to define your own view event listeners like onClickListener on a view
            .addViewEventListener(object : OnViewEventListener {
                override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
                    showToast(getActionName(viewEventId) + " " + position)
                }
            })

            /** Adds event listener and also automatically adds row item [View.OnClickListener] on all items root view  */
            .addViewEventListener(onItemClickListener { view, viewEventId, position ->
                showToast(getActionName(viewEventId) + " " + position)
            })

            /** Adds event listener and also automatically adds row item [View.OnLongClickListener] on all items root view  */
            .addViewEventListener(onItemClickListener { view, viewEventId, position ->
                showToast(getActionName(viewEventId) + " " + position)
            })

            .addViewEventListener(object : PosterViewHolder.OnItemClickListener {
                override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
                    mainSmartMovieAdapter.replaceItem(
                        0,
                        MoviePosterModel(MovieDataItems.randomPoster)
                    )
                }
            })

            // .addViewEventListener((PosterViewHolder.OnPlayButtonClickListener)(view, viewEventId, position) -> showToast("PLAY"))

            .addViewEventListener(object : PosterViewHolder.OnStarButtonClickListener {
                override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
                    showToast("ADD to favorites")
                }
            })

            .addViewEventListener(object : PosterViewHolder.OnInfoButtonClickListener {
                override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
                    showToast("INFO")
                }
            })

            .addViewEventListener(ComingSoonMoviesViewHolder.onMoreButtonClickListener { _, _, _ ->
                startMovieCategoryDetailsActivity(MovieType.COMING_SOON)
            })

            .addViewEventListener(MyWatchListViewHolder.onMoreButtonClickListener { _, _, _ ->
                startMovieCategoryDetailsActivity(MovieType.MY_WATCH_LIST)
            })

            .addViewEventListener(ActionMoviesViewHolder.onMoreButtonClickListener { _, _, _ ->
                startMovieCategoryDetailsActivity(MovieType.ACTION)
            })

            .addViewEventListener(AdventureMoviesViewHolder.onMoreButtonClickListener { _, _, _ ->
                startMovieCategoryDetailsActivity(MovieType.ADVENTURE)
            })

            .addViewEventListener(AnimatedMoviesViewHolder.onMoreButtonClickListener { _, _, _ ->
                startMovieCategoryDetailsActivity(MovieType.ANIMATED)
            })

            .addViewEventListener(SciFiMoviesViewHolder.onMoreButtonClickListener { _, _, _ ->
                startMovieCategoryDetailsActivity(MovieType.SCI_FI)
            })

            .addViewEventListener(onFabClickListener { _, _, _ ->
                moreSamplesDialog.show()
            })
            .addViewEventListener(onItemClickListener { _, _, _ ->
                moreSamplesDialog.show()
            })
            .into(recyclerView)

        // Endless pagination
        mainSmartMovieAdapter.autoLoadMoreEnabled = false
        mainSmartMovieAdapter.setOnLoadMoreListener { loadMoreViewHolder ->
            val indexBeforeCopyright = 2
            Handler().postDelayed({
                mainSmartMovieAdapter.addItem(
                    mainSmartMovieAdapter.itemCount - indexBeforeCopyright,
                    MovieBannerModel("More items loaded", MovieDataItems.randomBanner)
                )
                loadMoreViewHolder.toggleLoading(false)
            }, 800)
        }
    }

    private fun initNestedSmartRecyclerAdapters() {
        comingSoonSmartMovieAdapter =
            SmartEndlessScrollRecyclerAdapter.items(MovieDataItems.comingSoonItems)
                .map(MovieModel::class, LargeThumbViewHolder::class)
                .addViewEventListener(object : LargeThumbViewHolder.OnItemClickListener {
                    override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
                        showToast(
                            "Coming soon \n%s \n%s index: %d",
                            getMovieTitle(comingSoonSmartMovieAdapter, position),
                            getActionName(viewEventId),
                            position
                        )
                    }
                })
                .addViewEventListener(object : LargeThumbViewHolder.OnItemLongClickListener {
                    override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
                        showToast(
                            String.format(
                                "Add \n%s \nto My watch list",
                                getMovieTitle(comingSoonSmartMovieAdapter, position)
                            )
                        )
                        myWatchListSmartMovieAdapter.addItem(
                            1,
                            comingSoonSmartMovieAdapter.getItem(position)
                        )
                    }
                })
                .create()

        comingSoonSmartMovieAdapter.autoLoadMoreEnabled = true
        // Set custom load more view
        comingSoonSmartMovieAdapter.setCustomLoadMoreLayoutResource(R.layout.custom_loadmore_view)

        // Pagination ends after 3 loads
        comingSoonSmartMovieAdapter.setOnLoadMoreListener {
            Handler().postDelayed({
                comingSoonSmartMovieAdapter.addItems(
                    comingSoonSmartMovieAdapter.itemCount - 1,
                    MovieDataItems.loadMoreItems
                )
                if (moreItemsLoadedCount++ == 2)
                    comingSoonSmartMovieAdapter.isEndlessScrollEnabled = false
            }, 1000)
        }

        myWatchListSmartMovieAdapter = SmartRecyclerAdapter.items(MovieDataItems.myWatchListItems)
            .map(MovieModel::class, ThumbViewHolder::class)
            .addViewEventListener(object : ThumbViewHolder.OnItemLongClickListener {
                override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
                    showToast("Remove " + getActionName(viewEventId) + " item: " + position)
                    myWatchListSmartMovieAdapter.removeItem(position)
                }
            })
            .addViewEventListener(object : ThumbViewHolder.OnItemClickListener {
                override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
                    showToast(
                        "My watch list \n%s \n%s index: %d",
                        getMovieTitle(myWatchListSmartMovieAdapter, position),
                        getActionName(viewEventId),
                        position
                    )
                }
            })
            .create()

        actionMoviesSmartMovieAdapter = SmartRecyclerAdapter.items(MovieDataItems.nestedActionItems)
            .map(MovieModel::class, ThumbViewHolder::class)
            .addViewEventListener(object : ThumbViewHolder.OnItemClickListener {
                override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
                    showToast(
                        "Action \n%s \n%s index: %d",
                        getMovieTitle(actionMoviesSmartMovieAdapter, position),
                        getActionName(viewEventId),
                        position
                    )
                }
            })
            .create()

        adventuresMoviesSmartMovieAdapter =
            SmartRecyclerAdapter.items(MovieDataItems.nestedAdventureItems)
                .map(MovieModel::class, ThumbViewHolder::class)
                .addViewEventListener(object : ThumbViewHolder.OnItemClickListener {
                    override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
                        showToast(
                            "Adventure \n%s \n%s index: %d",
                            getMovieTitle(adventuresMoviesSmartMovieAdapter, position),
                            getActionName(viewEventId),
                            position
                        )
                    }
                })
                .create()

        animatedMoviesSmartMovieAdapter =
            SmartRecyclerAdapter.items(MovieDataItems.nestedAnimatedItems)
                .map(MovieModel::class, ThumbViewHolder::class)
                .addViewEventListener(object : ThumbViewHolder.OnItemClickListener {
                    override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
                        showToast(
                            "Animated \n%s \n%s index: %d",
                            getMovieTitle(animatedMoviesSmartMovieAdapter, position),
                            getActionName(viewEventId),
                            position
                        )
                    }
                })
                .create()

        sciFiMoviesSmartMovieAdapter = SmartRecyclerAdapter.items(MovieDataItems.nestedSciFiItems)
            .map(MovieModel::class, ThumbViewHolder::class)
            .addViewEventListener(object : ThumbViewHolder.OnItemClickListener {
                override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
                    showToast(
                        "Sci-Fi \n%s \n%s index: %d",
                        getMovieTitle(sciFiMoviesSmartMovieAdapter, position),
                        getActionName(viewEventId),
                        position
                    )
                }
            })
            .create()

        recentlyPlayedMoviesSmartMovieAdapter =
            SmartRecyclerAdapter.items(MovieDataItems.nestedRecentViewedItems)
                .map(MovieModel::class, SmallThumbViewHolder::class)
                .addViewEventListener(object : SmallThumbViewHolder.OnItemClickListener {
                    override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
                        showToast("Remove " + getActionName(viewEventId.toInt()) + " item: " + position)
                        recentlyPlayedMoviesSmartMovieAdapter.removeItem(position.toInt())
                    }
                })
                /*.addViewEventListener(
                        { view, viewEventId, position ->
                            showToast("Remove " + getActionName(viewEventId.toInt()) + " item: " + position)
                            recentlyPlayedMoviesSmartMovieAdapter.removeItem(position.toInt())
                        } as SmallThumbViewHolder.OnItemLongClickListener)*/
                .create()
    }

    internal interface OnFabClickListener : OnItemClickListener {
        override val viewHolderType: SmartViewHolderType
            get() = SampleFabViewHolder::class

        override val viewId: ViewId
            get() = R.id.fabItem
    }

    private inline fun onFabClickListener(crossinline viewEvent: (view: View, viewEventId: ViewEventId, position: Position) -> Unit) =
        object : OnFabClickListener {
            override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
                viewEvent(view, viewEventId, position)
            }
        }

    @SuppressLint("InflateParams")
    private fun initMoreDemosButton() {
        val dialogRecyclerView =
            LayoutInflater.from(this).inflate(R.layout.dialog_recycler_view, null) as RecyclerView
        moreSamplesDialog = AlertDialog.Builder(this@DemoActivity)
            .setView(dialogRecyclerView)
            .setCancelable(true)
            .create()

        if (moreSamplesDialog.window != null) {
            moreSamplesDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        }

        val items = mutableListOf(
            "More Samples",
            SampleFabViewHolder.SimpleFabItem(R.drawable.ic_sample_list_black_24dp, "Simple Item"),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_sample_touch_app_black_24dp,
                "Smart onClick/onLongClick"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_sample_edit_black_24dp,
                "Custom View Event"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_sample_drag_drop_swap_vert_black_24dp,
                "Drag & drop"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_drag_handle_black_24dp,
                "Drag & drop handle"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_sample_swipe_black_24dp,
                "Swipe remove item"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_sample_multiple_events_gesture_black_24dp,
                "Drag & Drop + Swipe"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_sample_grid_black_24dp,
                "Grid + Drag & Drop"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_sample_list_numbered_black_24dp,
                "Multiple Types Resolver"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_sample_select_all_black_24dp,
                "Multiple Items Select"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_radio_button_checked_black_24dp,
                "Single RadioButton Select"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_sample_check_box_black_24dp,
                "Multiple CheckBox Select"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_switch_black_24dp,
                "Multiple Switch Select"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_expand_more_black_24dp,
                "Multiple Expandable item"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_expand_less_black_24dp,
                "Single Expandable item"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_sample_nested_scroll_layers_black_24dp,
                "Nested Smart Adapter"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_sample_endless_scroll_arrow_downward_black_24dp,
                "Endless Scroll"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_endless_scroll_load_more_black_24dp,
                "Endless Scroll Load More"
            ),
            SampleFabViewHolder.SimpleFabItem(
                R.drawable.ic_sample_diff_shuffle_black_24dp,
                "Diff Util Extension"
            )
        )

        dialogAdapter = SmartRecyclerAdapter.items(items)
            .map(String::class, HeaderViewHolder::class)
            .map(SampleFabViewHolder.SimpleFabItem::class, SampleFabViewHolder::class)
            .addViewEventListener(onFabClickListener { view, viewEventId, position ->
                val sfi = dialogAdapter.getItem(position) as SampleFabViewHolder.SimpleFabItem
                when (sfi.icon) {
                    R.drawable.ic_sample_list_black_24dp -> startActivity(SimpleItemActivity::class)
                    R.drawable.ic_sample_touch_app_black_24dp -> startActivity(
                        SimpleItemOnClickOnLongClickActivity::class
                    )
                    R.drawable.ic_sample_edit_black_24dp -> startActivity(CustomViewEventActivity::class)
                    R.drawable.ic_sample_list_numbered_black_24dp -> startActivity(
                        MultipleViewTypesResolverActivity::class
                    )
                    R.drawable.ic_sample_drag_drop_swap_vert_black_24dp -> startActivity(
                        DragAndDropItemActivity::class
                    )
                    R.drawable.ic_drag_handle_black_24dp -> startActivity(
                        DragAndDropHandleItemActivity::class
                    )
                    R.drawable.ic_sample_swipe_black_24dp -> startActivity(SwipeRemoveItemActivity::class)
                    R.drawable.ic_sample_multiple_events_gesture_black_24dp -> startActivity(
                        MultipleEventsAndExtensionsActivity::class
                    )
                    R.drawable.ic_sample_grid_black_24dp -> startActivity(GridActivity::class)
                    R.drawable.ic_sample_select_all_black_24dp -> startActivity(
                        MultiSelectItemsActivity::class
                    )
                    R.drawable.ic_radio_button_checked_black_24dp -> startActivity(
                        SingleSelectRadioButtonItemActivity::class
                    )
                    R.drawable.ic_sample_check_box_black_24dp -> startActivity(
                        MultiSelectCheckBoxItemsActivity::class
                    )
                    R.drawable.ic_switch_black_24dp -> startActivity(MultiSelectSwitchItemsActivity::class)
                    R.drawable.ic_expand_more_black_24dp -> startActivity(
                        MultipleExpandableItemActivity::class
                    )
                    R.drawable.ic_expand_less_black_24dp -> startActivity(
                        SingleExpandableItemActivity::class
                    )
                    R.drawable.ic_sample_nested_scroll_layers_black_24dp -> startActivity(
                        NestedSmartRecyclerAdaptersActivity::class
                    )
                    R.drawable.ic_sample_endless_scroll_arrow_downward_black_24dp -> startActivity(
                        EndlessScrollActivity::class
                    )
                    R.drawable.ic_endless_scroll_load_more_black_24dp -> startActivity(
                        EndlessScrollLoadMoreButtonActivity::class
                    )
                    R.drawable.ic_sample_diff_shuffle_black_24dp -> startActivity(DiffUtilActivity::class)
                }
                // moreSamplesDialog.dismiss();
            })
            .into(dialogRecyclerView)
    }

    private fun startActivity(clazz: KClass<*>) {
        val intent = Intent(this@DemoActivity, clazz.java)
        ActivityCompat.startActivity(this, intent, null)
    }

    private fun startMovieCategoryDetailsActivity(movieType: MovieType) {
        val intent = Intent(this, MovieCategoryDetailsActivity::class.java)
        intent.putExtra("MovieType", movieType.ordinal)
        ActivityCompat.startActivity(this, intent, null)
    }

    private fun getMovieTitle(smartRecyclerAdapter: SmartRecyclerAdapter, position: Position): String {
        val movieModel = smartRecyclerAdapter.getItem(position) as MovieModel
        return movieModel.title
    }

    fun showToast(format: String, vararg args: Any) {
        Toast.makeText(
            this@DemoActivity,
            String.format(Locale.ENGLISH, format, *args),
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {

        fun getActionName(viewEventId: ViewEventId): String {
            return when (viewEventId) {
                R.id.event_on_click -> "onClick"
                R.id.event_on_long_click -> "onLongClick"
                R.id.event_play -> "PLAY"
                else -> "Unknown"
            }
        }
    }
}
