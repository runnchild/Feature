package com.rongc.wan.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.rongc.demo.databinding.FragmentWanHomeBinding
import com.rongc.feature.repository.whenSuccess
import com.rongc.feature.ui.BaseFragment
import com.rongc.list.ability.IPagerHost
import com.rongc.list.ability.PagerAbility
import com.rongc.list.viewmodel.EmptyBuilder
import com.rongc.list.viewmodel.whenDataIsEmpty
import com.rongc.list.viewpager2.BaseFragmentPagerAdapter
import com.rongc.list.viewpager2.IPagerItem

class WanHomeFragment : BaseFragment<FragmentWanHomeBinding, WanHomeViewModel>(), IPagerHost {

    /**
     * 返回需要PagerAbility的ViewPager
     */
    override val viewPager: ViewPager2 get() = mBinding.pagerList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 注册PagerAbility
        registerAbility(PagerAbility(viewModel, this))

        // 分类数据请求结果订阅，ignoreLoading()为忽略加载中状态只关心结果
        // whenSuccess，只在请求成功后接收通知
        viewModel.result.whenSuccess(lifecycleOwner) {
            // 配置TabLayout
            it.data?.forEach { project ->
                val tab = mBinding.tabStrip.newTab()
                tab.text = project.name
                mBinding.tabStrip.addTab(tab)
            }

            TabLayoutMediator(
                mBinding.tabStrip, mBinding.pagerList, true, true
            ) { tab, position ->
                tab.text = it.data?.getOrNull(position)?.name ?: ""
            }.attach()
        }
    }

    /**
     * 若ViewPager2子页面非View（是Fragment）时重载此方法返回BaseFragmentPagerAdapter
     */
    override fun providerAdapter(): RecyclerView.Adapter<*> {
        return object : BaseFragmentPagerAdapter<String>(this) {
            override fun createItemFragment(item: String, position: Int): IPagerItem<String> {
                // 根据position返回子页面
                return ProjectListFragment().apply {
                    arguments = bundleOf("cid" to item)
                }
            }
        }
    }

    /**
     * BaseFragmentPagerAdapter照样支持空页面
     */
    override fun setupEmptyView(builder: EmptyBuilder) {
        builder.whenDataIsEmpty {
            tip = "no data"
        }
    }
}