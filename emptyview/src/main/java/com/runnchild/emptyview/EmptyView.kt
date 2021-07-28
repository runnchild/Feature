package com.runnchild.emptyview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.runnchild.emptyview.databinding.EmptyViewBinding

@BindingAdapter("btnVisible", "textBuilder", "defaultClick", requireAll = false)
internal fun TextView.textBuilder(visible: Boolean?, builder: (TextView.() -> Unit)? = null, defaultClick: (() -> Unit)?) {
    builder?.let {
        apply(builder)
        if (visible != null) {
            isVisible = visible
        }
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

class EmptyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IEmptyView {

    private var binding = EmptyViewBinding.inflate(LayoutInflater.from(context), this, true)

    override var config: EmptyViewConfig?
        get() = binding.config
        set(value) {
            binding.config = value
        }
}