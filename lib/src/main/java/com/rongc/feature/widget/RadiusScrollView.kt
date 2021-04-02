package com.rongc.feature.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView

class RadiusScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : NestedScrollView(context, attrs) {

    val radiusHelper by lazy { RadiusMaskHelper(this) }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        radiusHelper.onDraw(canvas, width, height, scrollY.toFloat())
    }
}