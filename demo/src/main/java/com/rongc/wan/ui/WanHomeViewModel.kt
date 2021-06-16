package com.rongc.wan.ui

import androidx.lifecycle.LiveData
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.feature.vo.Resource
import com.rongc.wan.WanBanner
import com.rongc.wan.service.WanServiceProvider

class WanHomeViewModel : BaseViewModel() {
    val repository = WanServiceProvider.wanRepository

    fun fetchBanners(): LiveData<Resource<List<WanBanner>>> {
        return repository.fetchBanners()
    }
}