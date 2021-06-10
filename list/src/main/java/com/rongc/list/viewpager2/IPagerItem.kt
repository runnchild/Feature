package com.rongc.list.viewpager2

interface IPagerItem<T> {
    fun convert(position: Int, item: T, payloads: MutableList<Any>?)

    fun attachAdapter(adapter: BaseFragemntPagerAdapter<T>) {}
}