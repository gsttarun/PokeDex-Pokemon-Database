package com.test.pokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


abstract class RecyclerViewPagingAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = RecyclerViewPagingAdapter::class.java.simpleName
    var arrayList: ArrayList<T?> = arrayListOf()
    private val VIEW_TYPE_LOADING = Int.MAX_VALUE
    internal val VIEW_TYPE_DEAFULT = Int.MIN_VALUE
    private var isLoading = false
    private var isListEnd = false
    private var visibleThreshold = 3
    private var loadingItemInsertPosition = 0
    private val pageItemLimit = 10
    var orientation: Orientation = Orientation.VERTICAL
    @LayoutRes
    private var loadingItemLayout: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LOADING) {
            if (loadingItemLayout != null) {
                val view = LayoutInflater.from(parent.context).inflate(loadingItemLayout!!, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }
            val rL = RelativeLayout(parent.context)
            val progressBar = ProgressBar(parent.context).apply {
                isIndeterminate = true
                layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
                    addRule(RelativeLayout.CENTER_IN_PARENT)
                }
            }
            when (orientation) {
                Orientation.VERTICAL -> {
                    rL.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                            RecyclerView.LayoutParams.WRAP_CONTENT)
                    rL.minimumHeight = 200
                    rL.addView(progressBar)
                }
                Orientation.HORIZONTAL -> {
                    rL.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                            RecyclerView.LayoutParams.MATCH_PARENT)
                    rL.minimumWidth = 200
                    rL.addView(progressBar)
                }
            }
            object : RecyclerView.ViewHolder(rL) {}
        } else {
            onViewHolderCreate(parent, viewType)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onViewHolderBind(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (arrayList.isNotEmpty() && arrayList[position] == null) VIEW_TYPE_LOADING else getViewType(position)
    }

    private fun addItems(list: ArrayList<T?>, completion: ((ViewTypeToShow) -> Unit)?) {
        if (arrayList.size > 0) {
            list.let {
                //removing loading item
                val insertLocation = arrayList.count() - 1
                arrayList.addAll(insertLocation, list)
                notifyItemRangeInserted(insertLocation, list.size - 1)
                removeLoadingItem()
            }
        } else {
            arrayList = list
            completion?.invoke(ViewTypeToShow.LIST_VIEW)
            notifyItemRangeInserted(0, list.size - 1)
//            notifyDataSetChanged()
        }
    }

    /**override add
     * this method returns the adapter to initial state
     * do this when you want to clear the paging state and start over
     */
    fun resetAdapter() {
        arrayList.clear()
        isInitialLoad = true
        isListEnd = false
        notifyDataSetChanged()
    }

    private fun removeLoadingItem() {
        if (arrayList.size > 0) {
            val removePosition = arrayList.count() - 1
            arrayList.removeAt(removePosition)
            notifyItemRemoved(removePosition)
        }
    }

    fun setUpLoadMoreListener(recyclerView1: RecyclerView, onLoadMoreListener: OnLoadMoreListener) {
        recyclerView1.apply {
            if (layoutManager is LinearLayoutManager || layoutManager is GridLayoutManager) {
                val layoutManager = when (layoutManager) {
                    is LinearLayoutManager -> layoutManager as LinearLayoutManager
                    is GridLayoutManager -> layoutManager as GridLayoutManager
                    else -> layoutManager as LinearLayoutManager
                }
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (!isListEnd) {
                            val totalItemCount = layoutManager.itemCount
                            val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                            if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                                arrayList.add(null)
                                loadingItemInsertPosition = arrayList.size - 1
                                notifyItemInserted(loadingItemInsertPosition)
                                setLoading()
                                onLoadMoreListener.onLoadMore()
                            }
                        } else {

                        }
                    }
                })
            }

            if (layoutManager is StaggeredGridLayoutManager) {
                val layoutManager = this.layoutManager as StaggeredGridLayoutManager
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (!isListEnd) {
                            val totalItemCount = layoutManager.itemCount
                            val lastVisiblePositions = layoutManager.findLastVisibleItemPositions(intArrayOf(0, 1))
                            val lastVisibleItem = if (lastVisiblePositions[0] > lastVisiblePositions[1]) lastVisiblePositions[0] else lastVisiblePositions[1]

                            if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                                arrayList.add(null)
                                loadingItemInsertPosition = arrayList.size - 1
                                notifyItemInserted(loadingItemInsertPosition)
                                setLoading()
                                onLoadMoreListener.onLoadMore()
                            }
                        } else {

                        }
                    }
                })
            }
        }
    }

    private fun setLoaded() {
        isLoading = false
    }

    private fun setLoading() {
        isLoading = true
    }

    private var isInitialLoad: Boolean = true

    fun addData(list: ArrayList<T?>?, completion: ((ViewTypeToShow) -> Unit)?) {
        if (list != null && list.isNotEmpty()) {
            if (list.size < pageItemLimit)
                isListEnd = true
            isInitialLoad = false
            addItems(list, completion)
            setLoaded()
        } else {
            if (isInitialLoad) completion?.invoke(ViewTypeToShow.EMPTY_VIEW)
            else listEndReached()
        }
    }

    fun updateData(list: ArrayList<T?>, completion: ((ViewTypeToShow) -> Unit)?) {
        if (list.isNotEmpty()) {
            arrayList = list
            notifyItemRangeInserted(0, list.size - 1)
            completion?.invoke(ViewTypeToShow.LIST_VIEW)
        } else {
            completion?.invoke(ViewTypeToShow.EMPTY_VIEW)
        }
    }

    private fun listEndReached() {
        isListEnd = true
        removeLoadingItem()
    }

    /**
     * this sets the visible threshold for the paging to be triggered
     * Default value is 3
     */
    fun setVisibleThreshold(visibleThreshold: Int) {
        this.visibleThreshold = visibleThreshold
    }

    /**
     * this sets the no of items per page for the paging end to be known if the list is empty initially
     * Default value is 10
     */
    fun setPageItemLimit(visibleThreshold: Int) {
        this.visibleThreshold = visibleThreshold
    }

    /**
     *@param position of the item
     */
    fun getItem(position: Int) = arrayList[position]!!

    abstract fun onViewHolderCreate(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    abstract fun onViewHolderBind(holder: RecyclerView.ViewHolder, position: Int)

    /**
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     *                 <code>position</code>. Type codes need not be contiguous.
     *                 Make sure you dont use Int.MAX_VALUE or Int.MIN_VALUE for any view type
     */
    abstract fun getViewType(position: Int): Int

    fun setLoadingLayout(@LayoutRes loadingLayoutId: Int) {
        this.loadingItemLayout = loadingLayoutId
    }

    enum class Orientation {
        VERTICAL,
        HORIZONTAL
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }
}

enum class ViewTypeToShow {
    LIST_VIEW,
    EMPTY_VIEW
}