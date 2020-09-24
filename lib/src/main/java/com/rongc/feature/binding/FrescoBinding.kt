package com.rongc.feature.binding

import android.graphics.PointF
import android.net.Uri
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.Utils
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor
import com.facebook.imagepipeline.request.ImageRequestBuilder

object FrescoBinding {
    @JvmStatic
    @BindingAdapter("url", "blurRadius", requireAll = false)
    fun SimpleDraweeView.blur(url: String?, radius: Int = 15) {
        if (url.isNullOrEmpty()) {
            return
        }
        val controller: DraweeController = Fresco.newDraweeControllerBuilder()
            .setOldController(controller)
            .setImageRequest(
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                    .setPostprocessor(IterativeBoxBlurPostProcessor(10, radius)).build()
            )
            .build()
        this.controller = controller
    }
}

@BindingAdapter("url", "placeHolder", "placeScaleType", requireAll = false)
fun SimpleDraweeView.url(
    url: String?,
    placeHolder: Int = 0,
    placeHolderScale: ScalingUtils.ScaleType? = null
) {
    if (placeHolder != 0) {
        hierarchy.setPlaceholderImage(
            placeHolder, placeHolderScale ?: ScalingUtils.ScaleType.CENTER
        )
    }
    when {
        url == null -> {
            setImageURI(null as String?)
        }
        url.startsWith("http") -> {
            setImageURI(url)
        }
        else -> {
            file(url)
        }
    }
}

@BindingAdapter("src")
fun SimpleDraweeView.src(src: Int) {
    url("res://${Utils.getApp().packageName}/$src")
}

@BindingAdapter("file")
fun SimpleDraweeView.file(file: String) {
    setImageURI("file://$file")
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
    isCircle: Boolean = false,
    corners: Float = 0f,
    tl: Float = 0f,
    tr: Float = 0f,
    bl: Float = 0f,
    br: Float = 0f
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