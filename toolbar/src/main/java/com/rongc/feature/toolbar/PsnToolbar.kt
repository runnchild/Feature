package com.rongc.feature.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
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

    override val viewModel by lazy {
        ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get(ToolBarViewModel::class.java)
    }

    init {
        if (isInEditMode) {
            LayoutInflater.from(context).inflate(R.layout.psn_toolbar, this, true)
        } else {
            binding = PsnToolbarBinding.inflate(LayoutInflater.from(context), this, true)
            post {
                binding.model = viewModel
            }
        }
    }

    fun toolbar(config: ToolbarConfig.() -> Unit) {
        val configApply = (viewModel.toolbarConfig.value ?: ToolbarConfig()).apply(config)
        viewModel.setToolbarConfig(configApply)
    }

    fun statusBar(config: StatusBarConfig.() -> Unit) {
        val configApply = (viewModel.statusBarConfig.value ?: StatusBarConfig()).apply(config)
        viewModel.setStatusBarConfig(configApply)
    }

//    fun setTitleColor(color: Int) {
//        binding.tvTitle.setTextColor(color)
//    }
//
//    fun setTitle(title: CharSequence?) {
//        binding.model?.title?.set(title)
//    }
//
//    fun title(block: TextView.() -> Unit) {
//        binding.tvTitle.apply(block)
//    }

    fun menu(index: Int = 0, block: TextView.() -> Unit) {
        (binding.menuParent.getChildAt(index) as TextView).apply(block)
    }
//
//    fun navigation(block: ImageView.() -> Unit) {
//        binding.ivBack.apply(block)
//    }

//    fun setNavigationIcon(drawable: Drawable?) {
//        binding.model?.backIcon?.set(drawable)
//    }

//    fun setBackVisible(visible: Boolean) {
//        binding.model?.backVisible?.set(visible)
//    }

    override fun setBackgroundColor(color: Int) {
//        binding.model?.background?.set(color.toDrawable())
        toolbar { background = color }
    }

//    fun setLightMode(isLight: Boolean) {
//        binding.ivBack.drawable?.setTint(if (!isLight) Color.WHITE else Color.BLACK)
//        setTitleColor(if (!isLight) Color.WHITE else Color.parseColor("#353535"))
//    }

    private fun addImageMenu(item: ImageView.() -> Unit) {
        val menu = ImageView(context).apply {
            setPadding(15.idp, 0, 15.idp, 0)
        }.apply(item)

        binding.menuParent.addView(menu, LayoutParams(-2, -1))
    }

//    var navigationIcon: Drawable? = null
//        get() {
//            return binding.ivBack.drawable
//        }
//        set(value) {
//            binding.model?.backIcon?.set(value)
//            field = value
//        }
}