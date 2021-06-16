package com.rongc.wan

import androidx.lifecycle.LiveData
import com.rongc.feature.api.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WanService {

    @GET("banner/json")
    fun getBanner(): LiveData<ApiResponse<List<WanBanner>>>

    @GET("project/tree/json")
    fun projectTree(): LiveData<ApiResponse<List<ProjectTree>>>

    @GET("project/list/{page}/json")
    fun projectList(@Path("page") page: Int, @Query("cid") cid: String): LiveData<ApiResponse<ProjectData>>
}