package com.rongc.habit.binding

import android.graphics.PointF
import android.net.Uri
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.Utils
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView

@BindingAdapter("url")
fun SimpleDraweeView.url(url: String?) {
    setImageURI(if (url == null) url else Uri.parse(url), null)
}

@BindingAdapter("src")
fun SimpleDraweeView.src(src: Int) {
    url("res://${Utils.getApp().packageName}/$src")
}

@BindingAdapter("file")
fun SimpleDraweeView.file(file: String) {
    url("file://$file")
}

@BindingAdapter("assets")
fun SimpleDraweeView.assets(assets: String) {
    url("assets:///$assets")
}

@BindingAdapter("scaleType", "focusPoint", requireAll = false)
fun SimpleDraweeView.scale(scale: String, focusPoint: PointF? = null) {
    val scaleType = when (scale) {
        "center" -> ScalingUtils.ScaleType.CENTER
        "fitXY" -> ScalingUtils.ScaleType.FIT_XY
        "center_crop" -> ScalingUtils.ScaleType.CENTER_CROP
        "center_inside" -> ScalingUtils.ScaleType.CENTER_INSIDE
        "focus_crop" -> {
            hierarchy.actualImageFocusPoint = focusPoint
            ScalingUtils.ScaleType.FOCUS_CROP
        }
        else -> ScalingUtils.ScaleType.CENTER
    }
    hierarchy.actualImageScaleType = scaleType
}

@BindingAdapter("isCircle", "corner", "tl", "tr", "bl", "br", requireAll = false)
fun SimpleDraweeView.round(
    isCircle: Boolean = false, corners: Float = 0f
    , tl: Float = 0f, tr: Float = 0f, bl: Float = 0f, br: Float = 0f
) {
    val roundingParams = hierarchy.roundingParams ?: RoundingParams.fromCornersRadius(corners)
    roundingParams.run {
        roundAsCircle = isCircle
        if (tl * tr * bl * br != 0f) {
            setCornersRadii(tl, tr, br, bl)
        } else {
            setCornersRadius(corners)
        }
        hierarchy.roundingParams = this
    }
}