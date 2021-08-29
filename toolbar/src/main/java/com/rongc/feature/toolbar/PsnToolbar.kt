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
import com.rongc.feature.utils.singleClick

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
            binding.ivBack.singleClick { viewModel.backClick() }
        }
    }

    override fun setBarConfig(barConfig: BarConfig) {
        super.setBarConfig(barConfig)
        binding.config = viewModel.toolbarConfig
    }

    override fun title(block: (TextView.() -> Unit)?) {
        block?.let { binding.tvTitle.apply(it) }
    }

    override fun navigation(block: (ImageView.() -> Unit)?) {
        block?.let { binding.ivBack.apply(it) }
    }

    fun menu(index: Int = 0, block: TextView.() -> Unit) {
        post {
            (binding.menuParent.getChildAt(index) as? TextView)?.apply(block)
        }
    }

    override fun setBackgroundColor(color: Int) {
        toolbar { background = color }
    }

    private fun addImageMenu(item: ImageView.() -> Unit) {
        val menu = ImageView(context).apply {
            setPadding(15.idp, 0, 15.idp, 0)
        }.apply(item)

        binding.menuParent.addView(menu, LayoutParams(-2, -1))
    }
}