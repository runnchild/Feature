package com.rongc.list

class PageIndicator {
    companion object {
        const val PAGE_START = 1
    }

    var pageSize = 20

    var page = PAGE_START

    fun next() {
        page++
    }

    fun revert() {
        page = PAGE_START
    }
}