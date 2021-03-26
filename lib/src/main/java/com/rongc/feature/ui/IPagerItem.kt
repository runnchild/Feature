package com.rongc.feature.ui

import androidx.fragment.app.Fragment
import com.chad.library.adapter.base.BaseBinderAdapter

interface IPagerItem<T> {
    fun convert(position: Int, item: T, payloads: MutableList<Any>?)

    val adapter get() = ((this as Fragment).parentFragment as BaseViewPagerFragment<*,*>).mAdapter

    @Suppress("UNCHECKED_CAST")
    val pagerAdapter
        get() = adapter as? BaseViewPagerAdapter<T>

    val binderAdapter get() = adapter as BaseBinderAdapter
}