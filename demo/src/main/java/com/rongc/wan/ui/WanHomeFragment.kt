package com.rongc.wan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.rongc.demo.databinding.FragmentWanHomeBinding
import com.rongc.feature.ui.BaseFragment
import com.rongc.list.ability.IPagerList
import com.rongc.list.ability.PagerAbility
import com.rongc.list.binding.items
import com.rongc.list.viewpager2.BaseFragmentPagerAdapter
import com.rongc.list.viewpager2.IPagerItem

class WanHomeFragment : BaseFragment<FragmentWanHomeBinding, WanHomeViewModel>(), IPagerList {

    override val viewPager: ViewPager2
        get() = mBinding.pagerList

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registerAbility(PagerAbility(viewModel, this))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.banners.observe(lifecycleOwner) {
            mBinding.viewPager.items(it.data)
        }
        viewModel.result.observe(lifecycleOwner) {
            it.data?.forEach { project ->
                val tab = mBinding.tabStrip.newTab()
                tab.text = project.name
                mBinding.tabStrip.addTab(tab)
            }
            val ids = it.data?.map { project ->
                project.courseId
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
        return object : BaseFragmentPagerAdapter<Int>(this) {
            override fun createItemFragment(item: Int, position: Int): IPagerItem<Int> {
                return ProjectListFragment().apply {
                    arguments = bundleOf("cid" to item)
                }
            }
        }
    }
}