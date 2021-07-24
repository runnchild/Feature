package com.runnchild.emptyview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.runnchild.emptyview.databinding.EmptyViewBinding

@BindingAdapter("textBuilder", "defaultClick", requireAll = false)
fun TextView.textBuilder(builder: (TextView.() -> Unit)? = null, defaultClick: (() -> Unit)?) {
    builder?.let {
        apply(builder)
        isVisible = !text.isNullOrEmpty()
        if (!hasOnClickListeners() && defaultClick != null) {
            setOnClickListener {
                defaultClick.invoke()
            }
        }
    }
}

@BindingAdapter("imageBuilder")
fun ImageView.imageBuilder(builder: (ImageView.() -> Unit)? = null) {
    builder?.let {
        apply(builder)
    }
}

@BindingAdapter("visible")
internal fun View.visible(visible: Boolean) {
    isVisible = visible
}

class EmptyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IEmptyView {

    private var binding: EmptyViewBinding =
        EmptyViewBinding.inflate(LayoutInflater.from(context), this, true)

    override fun setConfig(config: EmptyViewConfig) {
        binding.viewModel = config
    }

    override fun getViewModel(): EmptyViewConfig? = binding.viewModel
}