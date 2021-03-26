package com.rongc.feature.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.rongc.feature.app.ui.viewmodel.ViewPagerFragmentViewModel
import com.rongc.feature.databinding.BaseViewpagerWithRefreshBinding
import com.rongc.feature.ui.*
import com.rongc.feature.ui.delegate.IFragmentAbility
import com.rongc.feature.ui.delegate.ViewPagerAbility
import com.rongc.feature.ui.toolbar.BarConfig

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/21
 * @since 2.1.4
 */
class ViewPagerFragment :
    BaseBindingFragment<BaseViewpagerWithRefreshBinding, ViewPagerFragmentViewModel>() {

    override fun binding(inflater: LayoutInflater, container: ViewGroup?) =
        BaseViewpagerWithRefreshBinding.inflate(inflater)

    override fun obtainAbility(): IFragmentAbility {
        return object : ViewPagerAbility(viewModel) {

            override val baseViewPager: ViewPager2
                get() = binding.baseViewPager

            override fun obtainAdapter() =
                object : BaseViewPagerAdapter<Any>(this@ViewPagerFragment) {
                    override fun createItemFragment(position: Int): IPagerItem<Any> {
                        return EmptyViewPagerFragmentItem()
                    }
                }
        }
    }


    override fun getBarConfig(): BarConfig.() -> Unit {
        return {
            toolbarVisible = false
        }
    }
}