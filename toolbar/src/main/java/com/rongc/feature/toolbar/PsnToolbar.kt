package com.rongc.feature.toolbar

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import com.rongc.feature.toolbar.databinding.PsnToolbarBinding
import com.rongc.feature.utils.idp

/**
 * @description 全局标题栏
 * @author rongc
 * @date 20-8-24$
 * @update
 */
class PsnToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IToolBar {

    lateinit var binding: PsnToolbarBinding

    init {
        if (isInEditMode) {
            LayoutInflater.from(context).inflate(R.layout.psn_toolbar, this, true)
        } else {
            binding = PsnToolbarBinding.inflate(LayoutInflater.from(context), this, true)
        }
    }

    override var model: ToolBarModel? = null
        get() = binding.model
        set(value) {
            field = value
            binding.model = value
        }

    fun setTitleColor(color: Int) {
        binding.tvTitle.setTextColor(color)
    }

    fun setTitle(title: CharSequence?) {
        binding.model?.title?.set(title)
    }

    fun title(block: TextView.() -> Unit) {
        binding.tvTitle.apply(block)
    }

    fun menu(index: Int = 0, block: TextView.() -> Unit) {
        (binding.menuParent.getChildAt(index) as TextView).apply(block)
    }

    fun navigation(block: ImageView.() -> Unit) {
        binding.ivBack.apply(block)
    }

    fun setBackImageDrawable(drawable: Drawable?) {
        binding.model?.backIcon?.set(drawable)
    }

    fun setBackVisible(visible: Boolean) {
        binding.model?.backVisible?.set(visible)
    }

    override fun setBackgroundColor(color: Int) {
        binding.model?.background?.set(color.toDrawable())
    }

    fun setLightMode(isLight: Boolean) {
        binding.ivBack.drawable?.setTint(if (!isLight) Color.WHITE else Color.BLACK)
        setTitleColor(if (!isLight) Color.WHITE else Color.parseColor("#353535"))
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