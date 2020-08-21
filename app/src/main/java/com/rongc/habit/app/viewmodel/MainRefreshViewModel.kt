package com.rongc.habit.app.viewmodel

import com.rongc.habit.model.BaseModel
import com.rongc.habit.refresh.BaseRecyclerItemBinder
import com.rongc.habit.refresh.DataRequestCallback
import com.rongc.habit.viewmodel.BaseRefreshViewModel
import kotlinx.coroutines.delay

class MainRefreshViewModel : BaseRefreshViewModel<Any, BaseModel>() {

    override fun loadData(page: Int, dataRequestCall: DataRequestCallback<List<Any>>) {
        val items = arrayListOf<Any>()
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
}