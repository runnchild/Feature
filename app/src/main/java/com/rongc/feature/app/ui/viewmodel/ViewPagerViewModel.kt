package com.rongc.feature.app.ui.viewmodel

import com.rongc.feature.model.BaseModel
import com.rongc.feature.viewmodel.BaseViewModel

/**
 * @description 作用描述
 * @author rongc
 * @date 2020/8/31$
 * @update
 */
class ViewPagerViewModel : BaseViewModel<BaseModel>() {
    val items = arrayListOf<String>()

    override fun onCreate() {
        super.onCreate()
        repeat(10) {
            items.add("items: $it")
        }
    }
}