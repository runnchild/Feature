package com.rongc.list.ability

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.list.ItemDecoration
import com.rongc.list.R
import com.rongc.list.adapter.BaseRecyclerItemBinder
import com.rongc.list.binding.itemBinders
import com.rongc.list.binding.itemDecoration
import java.util.*

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/6/7
 */
class PagerAbility(viewModel: BaseViewModel, private val pagerHost: IPagerList) :
    ListAbilityIml(viewModel, pagerHost) {

    override fun onCreate(owner: LifecycleOwner) {
        val viewPager = pagerHost.viewPager
        viewPager.adapter = adapter
        super.onCreate(owner)

        @Suppress("UNCHECKED_CAST")
        val call = viewPager.getTag(R.id.tag_adapter_callback) as? (RecyclerView.Adapter<*>) -> Unit
        call?.invoke(adapter)
    }

    override fun setupItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>) {
        pagerHost.viewPager.itemBinders(binders)
    }

    override fun setupItemDecoration(decoration: ItemDecoration) {
        pagerHost.viewPager.itemDecoration(decoration)
    }
}