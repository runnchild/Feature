package com.rongc.list.ability

import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.list.ItemDecoration
import com.rongc.list.R
import com.rongc.list.adapter.BaseRecyclerItemBinder
import com.rongc.list.binding.itemBinders
import com.rongc.list.binding.itemDecoration
import com.rongc.list.viewmodel.EmptyViewConfig
import com.rongc.list.viewpager2.BaseFragmentPagerAdapter
import com.rongc.list.widget.IEmptyView
import java.util.*

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/6/7
 */
class PagerAbility(viewModel: BaseViewModel, private val pagerHost: IPagerHost) :
    AbsListAbility(viewModel, pagerHost) {

    init {
        val viewPager = pagerHost.viewPager
        viewPager.adapter = adapter

        @Suppress("UNCHECKED_CAST")
        val call = viewPager.getTag(R.id.tag_adapter_callback) as? (RecyclerView.Adapter<*>) -> Unit
        call?.invoke(adapter)
    }

    override fun setEmptyView(emptyConfig: EmptyViewConfig) {
        super.setEmptyView(emptyConfig)
        val pagerAdapter = adapter
        if (pagerAdapter is BaseFragmentPagerAdapter<*>) {
            pagerAdapter.setEmptyData(emptyConfig)
        }
    }

    override fun providerEmptyView(): IEmptyView? {
        return pagerHost.providerEmptyView(pagerHost.viewPager.context)
    }

    override fun setupItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>) {
        pagerHost.viewPager.itemBinders(binders)
    }

    override fun setupItemDecoration(decoration: ItemDecoration) {
        pagerHost.viewPager.itemDecoration(decoration)
    }
}