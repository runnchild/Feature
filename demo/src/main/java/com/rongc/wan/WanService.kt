package com.rongc.wan

import androidx.lifecycle.LiveData
import com.rongc.feature.api.ApiResponse
import retrofit2.http.GET

interface WanService {

    @GET("banner/json")
    fun getBanner(): LiveData<ApiResponse<List<WanBanner>>>
}