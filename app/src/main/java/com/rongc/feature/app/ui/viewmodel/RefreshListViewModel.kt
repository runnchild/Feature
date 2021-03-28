package com.rongc.feature.app.ui.viewmodel

import com.rongc.feature.model.BaseModel
import com.rongc.feature.viewmodel.BaseListViewModel

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/26
 * @since 2.1.4
 */
class RefreshListViewModel : BaseListViewModel<Any, BaseModel>() {
    private var fistIsEmpty = true

    override suspend fun fetchListData(page: Int): List<Any> {
        val items = arrayListOf<Any>()
        if (fistIsEmpty) {
            fistIsEmpty = false
            return items
        }
        repeat(10) {
            if (it % 2 == 0) {
                items.add("$it")
            } else {
                items.add(1)
            }
        }
        return items
    }

    fun emptyList() {
        items.clear()
    }
}