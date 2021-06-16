package com.rongc.list.ability

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.rongc.list.ItemDecoration
import com.rongc.list.adapter.BaseRecyclerItemBinder
import com.rongc.list.viewmodel.DefaultEmptyConfig
import com.rongc.list.viewmodel.EmptyBuilder
import com.rongc.list.viewmodel.RefreshEmptyViewModel
import com.rongc.list.widget.EmptyView
import com.rongc.list.widget.IEmptyView

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/26
 */
interface IList {

    /**
     * 默认使用BaseBinderAdapter，如果需要提供新Adapter, 重写此方法返回需要设置的Adapter
     */
    fun providerAdapter(): RecyclerView.Adapter<*>? = null

    /**
     * 是否进入页面立即获取数据
     */
    fun autoRefresh() = true

    /**
     * 配置空页面UI
     */
    fun setupEmptyView(state: RefreshEmptyViewModel.State): EmptyBuilder.() -> Unit = {
    }

    /**
     * 如果不使用默认的空页面，重写并返回其他空页面
     */
    fun providerEmptyView(context: Context): IEmptyView = EmptyView(context)

    /**
     * 只设置空数据配置，无网等其他情况使用默认配置
     * @see {@link com.rongc.feature.viewmodel.DefaultEmptyConfig}
     */
    fun onlySetupEmptyData(
        state: RefreshEmptyViewModel.State,
        build: EmptyBuilder.() -> Unit
    ): EmptyBuilder.() -> Unit {
        return when (state) {
            RefreshEmptyViewModel.State.EMPTY_NET_DISCONNECT -> DefaultEmptyConfig.noNetBuilder
            RefreshEmptyViewModel.State.EMPTY_NET_UNAVAILABLE -> DefaultEmptyConfig.netUnavailableBuilder
            else -> build
        }
    }

    /**
     * 配置列表分割线
     */
    fun decorationBuilder(): ItemDecoration.Builder.() -> Unit {
        return { }
    }

    /**
     * 添加列表item样式
     */
    fun registerItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>) {
    }
}