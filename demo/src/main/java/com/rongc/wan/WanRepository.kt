package com.rongc.wan

import androidx.lifecycle.LiveData
import com.rongc.feature.repository.networkOnly
import com.rongc.feature.vo.Resource
import com.rongc.wan.service.WanServiceProvider

class WanRepository {
    val api = WanServiceProvider.apiService

    fun fetchBanners(): LiveData<Resource<List<WanBanner>>> {
        return api.getBanner().networkOnly()
    }
}