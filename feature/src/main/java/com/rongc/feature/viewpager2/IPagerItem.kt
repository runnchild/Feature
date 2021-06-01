package com.rongc.feature.viewpager2

interface IPagerItem<T> {
    fun convert(position: Int, item: T, payloads: MutableList<Any>?)

    fun attachAdapter(adapter: BaseViewPagerAdapter<T>) {}
}