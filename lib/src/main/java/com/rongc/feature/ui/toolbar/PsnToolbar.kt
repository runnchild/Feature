package com.rongc.feature.ui.toolbar

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.graphics.drawable.toDrawable
import com.rongc.feature.R
import com.rongc.feature.databinding.PsnToolbarBinding
import com.rongc.feature.utils.Compat.color
import com.rongc.feature.utils.idp
import com.rongc.feature.viewmodel.ToolBarViewModel

/**
 * @description 全局标题栏
 * @author rongc
 * @date 20-8-24$
 * @update
 */
class PsnToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val binding = PsnToolbarBinding.inflate(LayoutInflater.from(context), this, true)

    fun setViewModel(viewModel: ToolBarViewModel) {
        binding.viewModel = viewModel
//        binding.executePendingBindings()
    }

    fun setTitleColor(color: Int) {
        binding.tvTitle.setTextColor(color)
    }

    fun setTitle(title: CharSequence?) {
        binding.viewModel?.title?.set(title)
    }

    fun setBackImageDrawable(drawable: Drawable?) {
        binding.viewModel?.backIcon?.set(drawable)
    }

    fun setBackVisible(visible: Boolean) {
        binding.viewModel?.backVisible?.set(visible)
    }

    override fun setBackgroundColor(color: Int) {
        binding.viewModel?.background?.set(color.toDrawable())
    }

    fun setLightMode(isLight: Boolean) {
        binding.ivBack.drawable?.setTint(if (!isLight) Color.WHITE else Color.BLACK)
        setTitleColor(if (!isLight) Color.WHITE else R.color.gray_353535.color())
    }

    private fun addImageMenu(item: ImageView.() -> Unit) {
        val menu = ImageView(context).apply {

            setPadding(15.idp, 0, 15.idp, 0)
        }.apply(item)

        binding.menuParent.addView(menu, LayoutParams(-2, -1))
    }

    var navigationIcon: Drawable? = null
        get() {
            return binding.ivBack.drawable
        }
        set(value) {
            setBackImageDrawable(value)
            field = value
        }
}