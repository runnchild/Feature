package com.rongc.feature.app.ui

import androidx.fragment.app.Fragment
import com.rongc.feature.app.ui.viewmodel.ViewPagerFragmentViewModel
import com.rongc.feature.ui.BaseViewPagerAdapter
import com.rongc.feature.ui.BaseViewPagerFragment
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
class ViewPagerFragment : BaseViewPagerFragment<ViewPagerFragmentViewModel>() {

    override fun obtainAdapter() = object : BaseViewPagerAdapter(this) {
        override fun createFragment(position: Int): Fragment {
            return MainFragment()
        }
    }

    override fun getBarConfig(): BarConfig.() -> Unit {
        return {
            toolbarVisible = false
        }
    }
}