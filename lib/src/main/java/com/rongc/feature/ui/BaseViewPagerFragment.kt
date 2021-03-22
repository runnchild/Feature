package com.rongc.feature.ui

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/21
 * @since 2.1.4
 */
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.binding.ViewPager2Binding.doOnAdapter
import com.rongc.feature.binding.ViewPager2Binding.itemDecoration
import com.rongc.feature.databinding.BaseViewpagerWithRefreshBinding
import com.rongc.feature.model.BaseModel
import com.rongc.feature.viewmodel.BaseListViewModel
import com.rongc.feature.viewmodel.EmptyBuilder
import com.rongc.feature.widget.ItemDecoration

abstract class BaseViewPagerFragment<M : BaseListViewModel<*, out BaseModel>> :
    BaseBindingFragment<BaseViewpagerWithRefreshBinding, M>() {

    lateinit var mAdapter: RecyclerView.Adapter<*>

    override fun binding(inflater: LayoutInflater, container: ViewGroup?) =
        BaseViewpagerWithRefreshBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.autoRefresh = autoRefresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.baseViewPager.adapter = obtainAdapter()
        binding.baseViewPager.itemDecoration(itemDecoration)
        binding.baseViewPager.doOnAdapter<RecyclerView.Adapter<*>> {
            mAdapter = it
        }
    }

    /**
     * 默认使用BaseBinderAdapter，如果item需要使用Fragment，返回FragmentStateAdapter
     */
    open fun obtainAdapter(): RecyclerView.Adapter<*>? = null

    open fun autoRefresh() = true

    val itemDecoration: ItemDecoration
        get() = ItemDecoration.Builder().apply(decorationBuilder()).build()

    open fun decorationBuilder(): ItemDecoration.Builder.() -> Unit {
        return { }
    }

    open fun setupEmptyView(state: Int): EmptyBuilder.() -> Unit = {}
}