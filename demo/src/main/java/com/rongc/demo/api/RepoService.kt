package com.rongc.demo.api

import androidx.lifecycle.LiveData
import com.rongc.demo.vo.Repo
import com.rongc.demo.vo.RepoSearchResponse
import com.rongc.feature.api.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RepoService {

    @GET("search/repositories")
    fun searchRepos(@Query("q") query: String, @Query("page") page: Int): LiveData<ApiResponse<RepoSearchResponse>>

    @GET("users/{login}/repos")
    fun getRepos(@Path("login") login: String): LiveData<ApiResponse<List<Repo>>>
}