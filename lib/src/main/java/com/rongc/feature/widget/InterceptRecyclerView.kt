package com.rongc.feature.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.utils.Compat.logd
import kotlin.math.abs

/**
 * 横向解决滑动冲突的RecyclerView
 */
class InterceptRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var downX = 0f
    private var downY = 0f

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        when (e?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = e.x
                downY = e.y
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = e.x - downX
                val dy = e.y - downY
                if (abs(dx) > abs(dy)) {
                    if ((dx < 0 && canScrollHorizontally(1))
                        || dx > 0 && canScrollHorizontally(-1)
                    ) {
                        parent?.requestDisallowInterceptTouchEvent(true)
                    }
                } else {
                    if ((dy < 0 && canScrollVertically(1))
                        || dy > 0 && canScrollVertically(-1)
                    ) {
                        parent?.requestDisallowInterceptTouchEvent(true)
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(e)
    }
}