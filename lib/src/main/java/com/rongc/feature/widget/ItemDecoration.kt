package com.rongc.feature.widget

import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.Utils
import com.rongc.feature.R
import com.rongc.feature.utils.Compat.color

/**
 * Desc:
 * <p>
 * Date: 2020/3/11
 * Copyright: Copyright (c) 2010-2020
 * Company: @微微科技有限公司
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * @Author: zhuanghongzhan
 */
class ItemDecoration(builder: Builder) : RecyclerView.ItemDecoration() {


    private var horizontalLineWidth = builder.getHorizontalLineWidth()
    private var verticalLineWidth = builder.getVerticalLineWidth()
    private val lineColor = builder.getLineColor()
    private val horizontalStartWidth = builder.getHorizontalStartWidth()
    private val horizontalEndWidth = builder.getHorizontalEndWidth()
    private val verticalTopWidth = builder.getVerticalTopWidth()
    private val verticalBottomWidth = builder.getVerticalBottomWidth()
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.color = lineColor
    }

    /**
     * Desc:获取每行的列数
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-03-12
     * @param parent RecyclerView
     * @return Int
     */
    private fun getSpanCount(parent: RecyclerView): Int {
        var spanCount = 0
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            spanCount = layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager.spanCount
        } else if (layoutManager is LinearLayoutManager) {
            return 0
        }
        return spanCount
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val left = getLeftOffsets(view, parent)
        val top = getTopOffsets(view, parent)
        val right = getRightOffsets(view, parent, state)
        val bottom = getBottomOffsets(view, parent, state)
        outRect.set(left, top, right, bottom)
    }

    /**
     * Desc:获取绘制的左边绘制起点
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-03-12
     * @param view View
     * @param parent RecyclerView
     * @return Int
     */
    private fun getLeftOffsets(view: View, parent: RecyclerView): Int {
        val lp = parent.layoutManager
        val isLeftFirst = isLeftFirstItem(view, parent)
        return if (lp is GridLayoutManager || lp is StaggeredGridLayoutManager) {
            if (isLeftFirst) {
                horizontalLineWidth / 2 + horizontalStartWidth
            } else {
                horizontalLineWidth / 2
            }
        } else {
            if (isLeftFirst) {
                horizontalStartWidth
            } else {
                0
            }

        }
    }

    /**
     * Desc:判断是否是水平方向的第一个item
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-03-12
     * @param view View
     * @param parent RecyclerView
     */
    private fun isLeftFirstItem(view: View, parent: RecyclerView): Boolean {
        val lp = parent.layoutManager
        val spanCount = getSpanCount(parent)
        var position = getPosition(view, parent)
        if (lp is GridLayoutManager) {
            val spanSizeLookup = lp.spanSizeLookup
            return spanSizeLookup.getSpanGroupIndex(
                0,
                spanCount
            ) == spanSizeLookup.getSpanGroupIndex(position, spanCount)
        } else if (lp is LinearLayoutManager) {
            if (lp.canScrollVertically()) {
                return true
            } else if (lp.canScrollHorizontally()) {
                return position == 0
            }
        }
        return false
    }


    /**
     * Desc:获取绘制的上方起点
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-03-12
     * @param view View
     * @param parent RecyclerView
     * @return Int
     */
    private fun getTopOffsets(view: View, parent: RecyclerView): Int {
        val lp: RecyclerView.LayoutManager? = parent.layoutManager
        if (lp != null) {
            if ((lp is GridLayoutManager || lp is StaggeredGridLayoutManager) && isGridTopItem(
                    view,
                    parent
                )
            ) {
                return verticalTopWidth
            } else {
                val hasTopWidth =
                    lp.canScrollVertically() || lp.canScrollHorizontally() && verticalTopWidth > 0
                val isFirstItem = isFirstRowItem(view, parent)
                if (hasTopWidth && isFirstItem) {
                    return verticalTopWidth
                }
            }
        }
        return 0
    }

    /**
     * Desc:判断是否是第一排的数据
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-03-12
     * @param view View
     * @param parent RecyclerView
     */
    private fun isFirstRowItem(view: View, parent: RecyclerView): Boolean {
        val lp: RecyclerView.LayoutManager? = parent.layoutManager
        val spanCount = getSpanCount(parent)
        var position = getPosition(view, parent)
        if (lp is GridLayoutManager || lp is GridLayoutManager) {
            return position < spanCount
        } else if (lp is LinearLayoutManager) {
            return (lp.canScrollVertically() && position == 0) || lp.canScrollHorizontally()
        }
        return false
    }

    /**
     * Desc:获取绘制的右边终点
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-03-12
     * @param view View
     * @param parent RecyclerView
     * @return Int
     */
    private fun getRightOffsets(view: View, parent: RecyclerView, state: RecyclerView.State): Int {
        //如果是右边的最后一项则判断是否添加定制的宽度
        val lp = parent.layoutManager
        if (lp is GridLayoutManager || lp is GridLayoutManager) {
            return if (isLastRightItem(view, parent, state)) {
                horizontalLineWidth / 2 + horizontalEndWidth
            } else {
                horizontalLineWidth / 2
            }
        }
        return if (isLastRightItem(view, parent, state)) {
            horizontalEndWidth
        } else {
            horizontalLineWidth
        }
    }


    /**
     * Desc:判断是否是右边最后一项
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-03-12
     * @param view View
     * @param parent RecyclerView
     * @param state State
     */
    private fun isLastRightItem(
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ): Boolean {
        val lp: RecyclerView.LayoutManager? = parent.layoutManager
        val spanCount = getSpanCount(parent)
        var position = getPosition(view, parent)
        val childCount = state.itemCount
        if (lp is GridLayoutManager) {
            val spanSizeLookup = lp.getSpanSizeLookup()
            return spanSizeLookup.getSpanGroupIndex(
                childCount - 1,
                spanCount
            ) == spanSizeLookup.getSpanGroupIndex(position, spanCount)
        } else if (lp is LinearLayoutManager) {
            return (lp.canScrollHorizontally() && position == childCount - 1)
        }
        return false
    }

    /**
     * Desc:获取绘制的下边终点
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-03-12
     * @param parent RecyclerView
     * @return Int
     */
    private fun getBottomOffsets(view: View, parent: RecyclerView, state: RecyclerView.State): Int {
        return if (isBottomItem(view, parent, state)) {
            verticalBottomWidth
        } else {
            verticalLineWidth
        }
    }


    /**
     * Desc:是否是最下面的一个item  纵向的则判断最下面的几个   横向的则需要判断不同spanCount
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-03-27
     */
    private fun isBottomItem(view: View, parent: RecyclerView, state: RecyclerView.State): Boolean {
        val lp = parent.layoutManager ?: return false
        var position = getPosition(view, parent)
        val childCount = state.itemCount
        if (lp is GridLayoutManager || lp is StaggeredGridLayoutManager) {
            return isGridBottomItem(view, parent, state)
        } else if (lp is LinearLayoutManager) {
            if (lp.canScrollVertically()) {
                return position == childCount - 1
            } else {
                return true
            }
        }
        return false
    }


    /**
     * Desc:判断是否是grid最下面的一排item
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-03-27
     * @param view View
     * @param parent RecyclerView
     * @param state State
     * @return Boolean
     */
    private fun isGridBottomItem(
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ): Boolean {
        val lp = parent.layoutManager ?: return false
        var position = getPosition(view, parent)
        val childCount = state.itemCount
        if (lp is GridLayoutManager) {
            //判断当前项和下一项是否处在同一个groupIndex里面,如果不是则表示换列了，当前项是最下面的一项
            val spanSizeLookup = lp.getSpanSizeLookup()
            if (lp.canScrollHorizontally()) {
                var nextPosition = position + 1
                if (nextPosition >= childCount) {
                    nextPosition = childCount
                }
                val current = spanSizeLookup.getSpanGroupIndex(position, getSpanCount(parent))
                val next = spanSizeLookup.getSpanGroupIndex(nextPosition, getSpanCount(parent))
                return current != next
            } else {
                //判断当前的groupIndex是否和最后一项一致即可
                return (spanSizeLookup.getSpanGroupIndex(
                    position,
                    getSpanCount(parent)
                ) == spanSizeLookup.getSpanGroupIndex(childCount - 1, getSpanCount(parent)))
            }
        } else if (lp is StaggeredGridLayoutManager) {
            return position == childCount - 1 && lp.spanCount == getSpanCount(parent)
        }
        return false
    }

    /**
     * Desc:判断是否是grid最上面的一排item
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-03-27
     * @param view View
     * @param parent RecyclerView
     * @return Boolean
     */
    private fun isGridTopItem(view: View, parent: RecyclerView): Boolean {
        val lp = parent.layoutManager ?: return false
        var position = getPosition(view, parent)
        if (lp is GridLayoutManager) {
            val spanSizeLookup = lp.spanSizeLookup
            return spanSizeLookup.getSpanGroupIndex(position, getSpanCount(parent)) == 0
        } else if (lp is StaggeredGridLayoutManager) {
            if (lp.orientation == StaggeredGridLayoutManager.VERTICAL) {
                return position < getSpanCount(parent)
            }
        }
        return false
    }


    /**
     * Desc:获取当前View的position
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-03-12
     * @param view View
     * @param parent RecyclerView
     * @return Int
     */
    private fun getPosition(view: View, parent: RecyclerView): Int {
        var position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) {
            position = parent.getChildViewHolder(view).oldPosition
        }
        return position
    }


    class Builder {


        /**
         * 横向列表的分割线的宽度(也就是横向item的左右边距)，包括网格列表的分割线
         */
        private var horizontalLineWidth = 0

        /**
         * 纵向列表的分割线的高度（纵向列表的item的上下边距）
         */
        private var verticalLineWidth = 0

        /**
         * 分割线的颜色，默认为白色
         */
        private var lineColor = R.color.white

        /**
         *横向列表首个item距离start的宽度
         */
        private var horizontalStartWidth = 0

        private var horizontalEndWidth = 0

        private var verticalTopWidth = 0

        private var verticalBottomWidth = 0


        fun setLineWidth(lineWidth: Int): Builder {
            horizontalLineWidth = lineWidth
            verticalLineWidth = lineWidth
            return this
        }

        fun setLineWidthRes(res: Int): Builder {
            val lineWidth = Utils.getApp().resources.getDimensionPixelOffset(res)
            this.horizontalLineWidth = lineWidth
            this.verticalLineWidth = lineWidth
            return this
        }

        fun setHorizontalLineWidth(horizontalLineWidth: Int): Builder {
            this.horizontalLineWidth = horizontalLineWidth
            return this
        }

        fun setHorizontalLineWidthRes(res: Int): Builder {
            this.horizontalLineWidth = Utils.getApp().resources.getDimensionPixelOffset(res)
            return this
        }

        fun getHorizontalLineWidth() = horizontalLineWidth


        fun setVerticalLineWidth(verticalLineWidth: Int): Builder {
            this.verticalLineWidth = verticalLineWidth
            return this
        }

        fun setVerticalLineWidthRes(res: Int): Builder {
            this.verticalLineWidth = Utils.getApp().resources.getDimensionPixelOffset(res)
            return this
        }

        fun getVerticalLineWidth() = verticalLineWidth


        fun setLineColor(lineColor: Int): Builder {
            this.lineColor = lineColor
            return this
        }

        fun setLineColorRes(lineColorRes: Int): Builder {
            this.lineColor = lineColorRes.color()
            return this
        }


        fun getLineColor() = lineColor


        fun setHorizontalStartWidth(horizontalStartWidth: Int): Builder {
            this.horizontalStartWidth = horizontalStartWidth
            return this
        }

        fun setHorizontalStartWidthRes(res: Int): Builder {
            this.horizontalStartWidth = Utils.getApp().resources.getDimensionPixelOffset(res)
            return this
        }


        fun getHorizontalStartWidth() = horizontalStartWidth


        fun setHorizontalEndWidth(horizontalEndWidth: Int): Builder {
            this.horizontalEndWidth = horizontalEndWidth
            return this
        }

        fun setHorizontalEndWidthRes(res: Int): Builder {
            this.horizontalEndWidth = Utils.getApp().resources.getDimensionPixelOffset(res)
            return this
        }


        fun getHorizontalEndWidth() = horizontalEndWidth


        fun setVerticalTopWidth(verticalTopWidth: Int): Builder {
            this.verticalTopWidth = verticalTopWidth
            return this
        }

        fun setVerticalTopWidthRes(res: Int): Builder {
            this.verticalTopWidth = Utils.getApp().resources.getDimensionPixelOffset(res)
            return this
        }


        fun getVerticalTopWidth() = verticalTopWidth


        fun setVerticalBottomWidth(verticalBottomWidth: Int): Builder {
            this.verticalBottomWidth = verticalBottomWidth
            return this
        }

        fun setVerticalBottomWidthRes(res: Int): Builder {
            this.verticalBottomWidth = Utils.getApp().resources.getDimensionPixelOffset(res)
            return this
        }

        fun getVerticalBottomWidth() = verticalBottomWidth


        fun build(): ItemDecoration {
            return ItemDecoration(this)
        }
    }
}