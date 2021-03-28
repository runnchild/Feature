package com.rongc.feature.app.ui.viewmodel

import com.rongc.feature.model.BaseModel
import com.rongc.feature.utils.Compat.logd
import com.rongc.feature.viewmodel.BaseListViewModel

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/21
 * @since 2.1.4
 */
class ViewPagerViewModel : BaseListViewModel<String, BaseModel>() {
    var fistIsEmpty = true

    override suspend fun fetchListData(page: Int): List<String> {
        if (fistIsEmpty) {
            fistIsEmpty = false
            return emptyList()
        }
        "fetch page = $page".logd()
        return Array(5) {
            "item:${items.size + it}"
        }.toList()
    }

    fun clearData() {
        items.clear()
    }
}