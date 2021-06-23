package com.rongc.list.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.rongc.list.databinding.EmptyViewBinding
import com.rongc.list.viewmodel.EmptyViewConfig

@BindingAdapter("textBuilder", "defaultClick", requireAll = false)
fun TextView.textBuilder(builder: (TextView.() -> Unit)? = null, defaultClick: (() -> Unit)?) {
    builder?.let {
        apply(builder)
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

    private var binding: EmptyViewBinding =
        EmptyViewBinding.inflate(LayoutInflater.from(context), this, true)

    override fun setViewModel(config: EmptyViewConfig) {
        binding.viewModel = config
    }

    override fun getViewModel(): EmptyViewConfig? = binding.viewModel
}