package hr.bm.scanandsave.ui.activities.main

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.bm.scanandsave.R

class RecyclerItemDecoration(
    private val context: Context,
    headerHeight: Int,
    isSticky: Boolean,
    callback: SectionCallback
) : RecyclerView.ItemDecoration() {

    private var headerOffset: Int = headerHeight
    private var sticky: Boolean = isSticky
    private var sectionCallback: SectionCallback = callback
    private var headerView: View? = null
    var date: TextView? = null

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val pos = parent.getChildAdapterPosition(view)
        if (sectionCallback.isSection(pos)) {
            outRect.top = headerOffset
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (headerView == null) {
            headerView = inflateHeader(parent)
            date = headerView!!.findViewById(R.id.txtDate)
            fixLayoutSize(headerView, parent)
        }
        var prevTitle = ""
        var prevChild: View? = null
        for (i in 0 until parent.childCount) {
            val child: View = parent.getChildAt(i)
            val childPos = parent.getChildAdapterPosition(child)
            val title = sectionCallback.getSectionHeaderName(childPos)
            date!!.text = title
            if (!prevTitle.equals(
                    title,
                    ignoreCase = true
                ) || sectionCallback.isSection(childPos)
            ) {
                drawHeader(c, child, headerView, 0.0f)

                if ((child.top - headerOffset).toFloat() < headerOffset && prevChild != null) {
                    date!!.text = prevTitle
                    drawHeader(c, prevChild, headerView, headerOffset - (child.top - headerOffset).toFloat())
                }

                prevChild = child
                prevTitle = title
            }
        }
    }

    private fun drawHeader(c: Canvas, child: View, headerView: View?, diff: Float) {
        c.save()
        if (diff != 0.0f) {
            if (headerView != null) {
                c.translate(0F, -diff)
            }
        } else {
            if (sticky) {
                if (headerView != null) {
                    c.translate(0F, 0.coerceAtLeast(child.top - headerOffset).toFloat())
                }
            } else {
                if (headerView != null) {
                    c.translate(0F, (child.top - headerOffset).toFloat())
                }
            }
        }
        headerView?.draw(c)
        c.restore()
    }

    private fun fixLayoutSize(view: View?, viewGroup: ViewGroup) {
        val widthSpec: Int =
            View.MeasureSpec.makeMeasureSpec(viewGroup.width, View.MeasureSpec.EXACTLY)
        val heightSpec: Int =
            View.MeasureSpec.makeMeasureSpec(viewGroup.height, View.MeasureSpec.UNSPECIFIED)
        val childWidth = view?.layoutParams?.width?.let {
            ViewGroup.getChildMeasureSpec(
                widthSpec,
                viewGroup.paddingLeft + viewGroup.paddingRight,
                it
            )
        }
        val childHeight = view?.layoutParams?.height?.let {
            ViewGroup.getChildMeasureSpec(
                heightSpec,
                viewGroup.paddingTop + viewGroup.paddingBottom,
                it
            )
        }
        if (childWidth != null && childHeight != null) {
            view.measure(childWidth, childHeight)
        }
        view?.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    private fun inflateHeader(recyclerView: RecyclerView): View {
        return LayoutInflater.from(context)
            .inflate(R.layout.view_header_recycler, recyclerView, false)
    }

    interface SectionCallback {
        fun isSection(pos: Int): Boolean
        fun getSectionHeaderName(pos: Int): String
    }
}