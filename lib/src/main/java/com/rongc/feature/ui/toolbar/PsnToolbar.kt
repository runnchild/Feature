package com.rongc.feature.ui.toolbar

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.LifecycleOwner
import com.rongc.feature.R
import com.rongc.feature.databinding.CommonToolbarBinding
import com.rongc.feature.utils.Compat.color
import com.rongc.feature.utils.Compat.idp
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

    val binding = CommonToolbarBinding.inflate(LayoutInflater.from(context), this, true)

    fun setViewModel(owner: LifecycleOwner, viewModel: ToolBarViewModel) {
        binding.viewModel = viewModel
        binding.executePendingBindings()

        viewModel.menuItems.forEach { addItemMenu(it) }
        viewModel.background.observe(owner, {
            val value = (it as? ColorDrawable)?.color ?: 1
            val isLightMode = ColorUtils.calculateLuminance(value) > 0.5f
            setLightMode(isLightMode)
        })
    }

    fun setTitleColor(color: Int) {
        binding.tvTitle.setTextColor(color)
    }

    fun setTitle(title: CharSequence?) {
        binding.viewModel?.title?.set(title)
    }

    fun setBackImageDrawable(drawable: Drawable) {
        binding.viewModel?.backIcon?.set(drawable)
    }

    fun setBackVisible(visible: Boolean) {
        binding.viewModel?.backVisible?.set(visible)
    }

    override fun setBackgroundColor(color: Int) {
        binding.viewModel?.background?.value = color.toDrawable()
    }

    fun setLightMode(isLight: Boolean) {
        binding.ivBack.drawable?.setTint(if (!isLight) Color.WHITE else Color.BLACK)
        setTitleColor(if (!isLight) Color.WHITE else R.color.gray_353535.color())
    }

    private fun addItemMenu(item: TextView.() -> Unit) {
        val menu = TextView(context).apply {
            textSize = 16f
            setTextColor(R.color.gray_353535.color())
            gravity = Gravity.CENTER
            val padding = if (binding.menuParent.childCount > 0) {
                7.idp()
            } else {
                15.idp()
            }
            setPadding(7.idp(), 0, padding, 0)
        }.apply(item)

        binding.menuParent.addView(menu, binding.menuParent.childCount - 1,
                LayoutParams(-2, -1))
    }

    private fun addImageMenu(item: ImageView.() -> Unit) {
        val menu = ImageView(context).apply {

            setPadding(15.idp(), 0, 15.idp(), 0)
        }.apply(item)

        binding.menuParent.addView(menu, LayoutParams(-2, -1))
    }
}