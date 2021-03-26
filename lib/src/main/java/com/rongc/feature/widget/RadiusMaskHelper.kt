package com.rongc.feature.widget

import android.graphics.*

class RadiusMaskHelper {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF: RectF = RectF()
    private val path: Path = Path()
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    private var radii: FloatArray = floatArrayOf()
    private var roundColor: Int = -1
    private val offsets = FloatArray(4)

    fun setRadii(radii: FloatArray) {
        this.radii = radii
    }

    fun setRadii(left: Float = 0f, top: Float = 0f, right: Float = 0f, bottom: Float = 0f) {
        this.radii = floatArrayOf(top, top, right, right, left, left, bottom, bottom)
    }

    fun setMaskColor(roundColor: Int) {
        this.roundColor = roundColor
    }

    fun setOffset(left: Float = 0f, top: Float = 0f, right: Float = 0f, bottom: Float = 0f) {
        offsets[0] = left
        offsets[1] = top
        offsets[2] = right
        offsets[3] = bottom
    }

    fun onDraw(canvas: Canvas?, width: Int, height: Int, top: Float = 0f) {
        if (canvas == null || radii.size < 8) {
            return
        }
        val save = canvas.saveLayer(null, null)
        rectF.set(
            offsets[0],
            top + offsets[1],
            width.toFloat() + offsets[2],
            height.toFloat() + offsets[3]
        )
        paint.color = roundColor
        paint.style = Paint.Style.FILL
        canvas.drawRect(rectF, paint)

        paint.xfermode = xfermode
        paint.color = Color.TRANSPARENT
        path.reset()
        path.addRoundRect(rectF, radii, Path.Direction.CCW)
        canvas.drawPath(path, paint)
        paint.reset()
        canvas.restoreToCount(save)
    }
}