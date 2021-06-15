package com.rongc.demo.ui.wan

import androidx.lifecycle.LiveData
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.feature.vo.Resource

class WanHomeViewModel : BaseViewModel() {
    val repository = WanServiceProvider.wanRepository

    fun fetchBanners(): LiveData<Resource<List<WanBanner>>> {
        return repository.fetchBanners()
    }
}