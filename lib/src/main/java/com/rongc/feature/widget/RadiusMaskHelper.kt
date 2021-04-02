package com.rongc.feature.widget

import android.graphics.*
import android.view.View

class RadiusMaskHelper(val view: View) {

    init {
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF: RectF = RectF()
    private val path: Path = Path()
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    private var radii: FloatArray = floatArrayOf()
    private var roundColor: Int = -1
    private val offsets = FloatArray(4)

    fun setRadii(radii: FloatArray) {
        this.radii = radii
        view.invalidate()
    }

    fun setRadii(left: Float = 0f, top: Float = 0f, right: Float = 0f, bottom: Float = 0f) {
        setRadii(floatArrayOf(top, top, right, right, left, left, bottom, bottom))
    }

    fun setMaskColor(roundColor: Int) {
        this.roundColor = roundColor
        view.invalidate()
    }

    fun setOffset(left: Float = offsets[0], top: Float = offsets[1], right: Float = offsets[2], bottom: Float = offsets[3]) {
        offsets[0] = left
        offsets[1] = top
        offsets[2] = right
        offsets[3] = bottom
        view.invalidate()
    }

    fun onDraw(canvas: Canvas?, width: Int, height: Int, top: Float = 0f) {
        if (canvas == null || radii.size < 8) {
            return
        }
        rectF.set(
            offsets[0],
            top + offsets[1],
            width.toFloat() + offsets[2],
            height.toFloat() + offsets[3] + top
        )
        val save = canvas.saveLayer(rectF, null)

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