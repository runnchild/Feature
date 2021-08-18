package com.rongc.list.ability

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.list.ItemDecoration
import com.rongc.list.adapter.BaseRecyclerItemBinder
import com.rongc.list.binding.itemBinders
import com.rongc.list.binding.itemDecoration
import com.rongc.list.viewpager2.BaseFragmentPagerAdapter
import com.runnchild.emptyview.EmptyViewConfig
import com.runnchild.emptyview.IEmptyView
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

    override fun <T> onFetchData(adapter: RecyclerView.Adapter<*>, list: List<T>?) {
        super.onFetchData(adapter, list)
        val viewPager = pagerHost.viewPager
        if (viewPager.adapter != adapter) {
            viewPager.adapter = adapter
            invokeDoOnAdapter(viewPager, adapter)
        }
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

    override fun onDestroy(owner: LifecycleOwner) {
        pagerHost.viewPager.adapter = null
    }
}