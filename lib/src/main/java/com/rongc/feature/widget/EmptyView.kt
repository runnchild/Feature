package com.rongc.feature.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.rongc.feature.databinding.EmptyViewBinding
import com.rongc.feature.viewmodel.RefreshEmptyViewModel

class EmptyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var binding: EmptyViewBinding =
        EmptyViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setViewModel(viewModel: RefreshEmptyViewModel) {
        binding.viewModel = viewModel
    }
}