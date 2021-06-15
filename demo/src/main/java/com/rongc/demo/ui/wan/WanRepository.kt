package com.rongc.demo.ui.wan

import androidx.lifecycle.LiveData
import com.rongc.feature.repository.networkOnly
import com.rongc.feature.vo.Resource

class WanRepository {
    val api = WanServiceProvider.apiService

    fun fetchBanners(): LiveData<Resource<List<WanBanner>>> {
        return api.getBanner().networkOnly()
    }
}