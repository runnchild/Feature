package com.rongc.feature.app.ui.viewmodel

import com.rongc.feature.model.BaseModel
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.refresh.DataRequestCallback
import com.rongc.feature.viewmodel.BaseRefreshViewModel
import kotlinx.coroutines.delay

class MainRefreshViewModel : BaseRefreshViewModel<Any, BaseModel>() {

    var empty = false

    override fun loadData(page: Int, dataRequestCall: DataRequestCallback<List<Any>>) {
        val items = arrayListOf<Any>()
        if (empty) {
            dataRequestCall.onSuccess(1, items)
            return
        }
        if (page < 5) {
            launch({
                delay(2000)
                repeat(10) {
                    if (it % 2 == 0) {
                        items.add("$it")
                    } else {
                        items.add(1)
                    }
                }
                dataRequestCall.onSuccess(page, items)
            }, showDialog = true, showToast = true)
        }
    }

    override fun providerItemBinders(binders: MutableSet<BaseRecyclerItemBinder<out Any>>) {
        binders.add(MainItemHolder())
        binders.add(MainOtherItemHolder())
    }

    fun emptyList() {
        empty = true
        refresh()
        empty = false
    }
}