package com.rongc.feature.ui.ability

import com.rongc.feature.ui.adapter.BaseViewPagerAdapter

interface IPagerItem<T> {
    fun convert(position: Int, item: T, payloads: MutableList<Any>?)

    fun attachAdapter(adapter: BaseViewPagerAdapter<T>) {}

//    val adapter get() = ((this as Fragment).parentFragment as BaseViewPagerFragment<*, *>).mAdapter
//
//    @Suppress("UNCHECKED_CAST")
//    val pagerAdapter
//        get() = adapter as? BaseViewPagerAdapter<T>
//
//    val binderAdapter get() = adapter as? BaseBinderAdapter
}