package hr.bm.scanandsave.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import hr.bm.scanandsave.databinding.ViewProgressRecyclerViewBinding
import hr.bm.scanandsave.ui.activities.main.ReceiptsListAdapter
import hr.bm.scanandsave.ui.activities.main.RecyclerItemDecoration

class ProgressRecyclerView(context: Context, attrs: AttributeSet?, defStyle: Int)
    : FrameLayout(context, attrs, defStyle) {

    private var mBinding: ViewProgressRecyclerViewBinding =
        ViewProgressRecyclerViewBinding.inflate(LayoutInflater.from(getContext()), this, true)

    private var mIsLoading: Boolean = false
    private var mProgressLayout: Int = 0

    fun setProgressLayout(progressLayout: Int) {
        mProgressLayout = progressLayout
        for (i in 1..10) {
            mBinding.shimmerContainer.addView(LayoutInflater.from(context).inflate(progressLayout, null))
        }
    }

    fun setLoading(loading: Boolean) {
        mIsLoading = loading

        if (loading) {
            mBinding.recyclerView.visibility = View.GONE
            if (mProgressLayout == 0) {
//                mBinding.progressBar.visibility = View.VISIBLE
                mBinding.shimmerLayout.visibility = View.GONE
            } else {
//                mBinding.progressBar.visibility = View.GONE
                mBinding.shimmerLayout.startShimmer()
                mBinding.shimmerLayout.visibility = View.VISIBLE
            }
        } else {
            mBinding.recyclerView.visibility = View.VISIBLE
//            mBinding.progressBar.visibility = View.GONE
            mBinding.shimmerLayout.stopShimmer()
            mBinding.shimmerLayout.visibility = View.GONE
        }
    }

    fun setLayoutManager(manager: RecyclerView.LayoutManager) {
        mBinding.recyclerView.layoutManager = manager
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        mBinding.recyclerView.adapter = adapter
    }

    fun addItemDecoration(decoration: RecyclerItemDecoration) {
        mBinding.recyclerView.addItemDecoration(decoration)
    }

    fun addOnScrollListener(listener: RecyclerView.OnScrollListener) {
        mBinding.recyclerView.addOnScrollListener(listener)
    }

    init {
        mBinding.recyclerView.clipToPadding = false
        mBinding.recyclerView.overScrollMode = OVER_SCROLL_NEVER
    }

    constructor(context: Context): this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
}