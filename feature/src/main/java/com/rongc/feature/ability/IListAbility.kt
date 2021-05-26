package com.rongc.feature.ability

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.refresh.ItemDecoration

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/26
 */
interface IListAbility {
    val recyclerView: RecyclerView

    /**
     * 默认使用BaseBinderAdapter，如果需要提供新Adapter, 重写此方法返回需要设置的Adapter
     */
    fun providerAdapter(): RecyclerView.Adapter<*>? = null

    /**
     * 是否进入页面自动刷新
     */
    fun autoRefresh() = true

    /**
     * 配置空页面UI
     */
//    fun setupEmptyView(state: Int): EmptyBuilder.() -> Unit = {}

    /**
     * 如果需要，重写并返回其他空页面
     */
//    fun providerEmptyView(context: Context): IEmptyView? = EmptyView(context)

    /**
     * 配置列表分割线
     */
    fun decorationBuilder(): ItemDecoration.Builder.() -> Unit {
        return { }
    }

    /**
     * 添加列表item样式
     */
//    fun registerItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>) {
//    }

    /**
     * 默认返回LinearLayoutManager
     * 如果需要其他的LayoutManager，重写此方法
     */
    fun providerLayoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}