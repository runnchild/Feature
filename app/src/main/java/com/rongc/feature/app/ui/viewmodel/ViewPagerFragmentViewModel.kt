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
class ViewPagerFragmentViewModel : BaseListViewModel<String, BaseModel>() {
    override suspend fun fetchListData(page: Int): List<String> {
        "fetch page = $page".logd()
        return Array(20) {
            "item:$it"
        }.toList()
    }
}