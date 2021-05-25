package com.rongc.demo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.rongc.demo.api.RepoServiceProvider
import com.rongc.demo.api.RepoServiceProvider.repoDao
import com.rongc.demo.vo.Repo
import com.rongc.demo.vo.RepoSearchResponse
import com.rongc.demo.vo.RepoSearchResult
import com.rongc.feature.api.ApiResponse
import com.rongc.feature.api.ApiSuccessResponse
import com.rongc.feature.repository.NetworkBoundResource
import com.rongc.feature.utils.AbsentLiveData
import com.rongc.feature.utils.RateLimiter
import com.rongc.feature.vo.Resource
import java.util.concurrent.TimeUnit

class RepoRepository {

    private val repoApi = RepoServiceProvider.apiService

    // 发起同一条件的查询时，距离上次请求超过一定时间后重新发起网络请求
    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.SECONDS)

    fun searchRepo(query: String): LiveData<Resource<List<Repo>>> {
        return object: NetworkBoundResource<List<Repo>, RepoSearchResponse>() {
            // 缓存请求数据
            override fun saveCallResult(item: RepoSearchResponse) {
                val repoIds = item.items.map { it.id }
                val repoSearchResult = RepoSearchResult(
                    query = query,
                    repoIds = repoIds,
                    totalCount = item.total,
                    next = item.nextPage
                )
                RepoServiceProvider.repoDb.runInTransaction {
                    repoDao.insertRepos(item.items)
                    repoDao.insert(repoSearchResult)
                }
            }

            // 根据缓存数据判断是否要重新发起网络请求
            override fun shouldFetch(data:List<Repo>?): Boolean {
                return data == null || repoListRateLimit.shouldFetch(query)
            }

            // 加载缓存
            override fun loadFromDb(): LiveData<List<Repo>> {
                return repoDao.search(query).switchMap { searchData ->
                    if (searchData == null) {
                        AbsentLiveData.create()
                    } else {
                        repoDao.loadOrdered(searchData.repoIds)
                    }
                }
            }

            // 发起网络请求
            override fun createCall(): LiveData<ApiResponse<RepoSearchResponse>> {
                return repoApi.searchRepos(query, 0)
            }

            // 请求拉去失败
            override fun onFetchFailed() {
                repoListRateLimit.reset(query)
            }

            // 请求结果缓存（saveCallResult）前可再次加工
            override fun processResponse(response: ApiSuccessResponse<RepoSearchResponse>): RepoSearchResponse {
                return super.processResponse(response)
            }
        }.asLiveData()
    }
    
    fun getRepos(owner: String): LiveData<Resource<List<Repo>>> {
        return object : NetworkBoundResource<List<Repo>, List<Repo>>() {
            override fun saveCallResult(item: List<Repo>) {
                repoDao.insertRepos(item)
            }

            override fun shouldFetch(data: List<Repo>?): Boolean {
                return data == null || repoListRateLimit.shouldFetch(owner)
            }

            override fun loadFromDb(): LiveData<List<Repo>> {
                return repoDao.loadRepositories(owner)
            }

            override fun createCall(): LiveData<ApiResponse<List<Repo>>> {
                return repoApi.getRepos(owner)
            }

            override fun onFetchFailed() {
                repoListRateLimit.reset(owner)
            }
        }.asLiveData()
    }
}