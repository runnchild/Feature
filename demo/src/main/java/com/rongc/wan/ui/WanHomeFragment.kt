package com.rongc.wan.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.rongc.demo.databinding.FragmentWanHomeBinding
import com.rongc.feature.ui.BaseFragment
import com.rongc.list.ability.IPagerHost
import com.rongc.list.ability.PagerAbility
import com.rongc.list.binding.items
import com.rongc.list.viewmodel.EmptyBuilder
import com.rongc.list.viewmodel.RefreshEmptyViewModel
import com.rongc.list.viewpager2.BaseFragmentPagerAdapter
import com.rongc.list.viewpager2.IPagerItem

class WanHomeFragment : BaseFragment<FragmentWanHomeBinding, WanHomeViewModel>(), IPagerHost {

    override val viewPager: ViewPager2 get() = mBinding.pagerList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerAbility(PagerAbility(viewModel, this))

        viewModel.result.observe(lifecycleOwner) {
            it.data?.forEach { project ->
                val tab = mBinding.tabStrip.newTab()
                tab.text = project.name
                mBinding.tabStrip.addTab(tab)
            }
            val ids = it.data?.map { project ->
                project.id.toString()
            }
            mBinding.pagerList.items(ids)

            TabLayoutMediator(
                mBinding.tabStrip, mBinding.pagerList, true, true
            ) { tab, position ->
                tab.text = it.data?.getOrNull(position)?.name ?: ""
            }.attach()
        }
    }

    override fun providerAdapter(): RecyclerView.Adapter<*> {
        return object : BaseFragmentPagerAdapter<String>(this) {
            override fun createItemFragment(item: String, position: Int): IPagerItem<String> {
                return ProjectListFragment().apply {
                    arguments = bundleOf("cid" to item)
                }
            }
        }
    }

    override fun setupEmptyView(state: RefreshEmptyViewModel.State): EmptyBuilder.() -> Unit {
        return onlySetupEmptyData(state) {
            tip = "no data"
        }
    }
}