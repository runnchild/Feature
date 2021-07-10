package com.rongc.demo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.rongc.demo.api.RepoService
import com.rongc.demo.api.RepoServiceProvider.repoDao
import com.rongc.demo.db.GithubDb
import com.rongc.demo.vo.Repo
import com.rongc.demo.vo.RepoSearchResponse
import com.rongc.demo.vo.RepoSearchResult
import com.rongc.feature.api.ApiResponse
import com.rongc.feature.api.ApiSuccessResponse
import com.rongc.feature.repository.NetworkBoundResource
import com.rongc.feature.repository.networkOnly
import com.rongc.feature.utils.AbsentLiveData
import com.rongc.feature.utils.RateLimiter
import com.rongc.feature.vo.Resource
import java.util.concurrent.TimeUnit

class RepoRepository(val repoApi: RepoService, val repoDb: GithubDb) {

    // 发起同一条件的查询时，距离上次请求时长超过$timeout后将重新发起网络请求
    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.SECONDS)

    fun searchRepo(query: String, page: Int): LiveData<Resource<List<Repo>>> {
        return object : NetworkBoundResource<List<Repo>, RepoSearchResponse>() {
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

            // 根据缓存数据判断是否要重新发起网络请求
            override fun shouldFetch(data: List<Repo>?): Boolean {
                // demo未实现数据库分页加载
                return page > 1 || (data == null || repoListRateLimit.shouldFetch(query))
            }

            // 发起网络请求
            override fun createCall(): LiveData<ApiResponse<RepoSearchResponse>> {
                return repoApi.searchRepos(query, page)
            }

            // 请求结果缓存（saveCallResult）前可再次加工
            override fun processResponse(response: ApiSuccessResponse<RepoSearchResponse>): RepoSearchResponse {
                return super.processResponse(response)
            }

            // 缓存请求数据
            override fun saveCallResult(item: RepoSearchResponse) {
                val repoIds = item.items.map { it.id }
                val repoSearchResult = RepoSearchResult(
                    query = query,
                    repoIds = repoIds,
                    totalCount = item.total,
                    next = item.nextPage
                )
                repoDb.runInTransaction {
                    repoDb.repoDao().insertRepos(item.items)
                    repoDb.repoDao().insert(repoSearchResult)
                }
            }

            // 请求失败
            override fun onFetchFailed() {
                repoListRateLimit.reset(query)
            }
        }.asLiveData()
    }

    fun getRepos(owner: String): LiveData<Resource<List<Repo>>> {
        // 只发起网络请求，不做缓存
        return repoApi.getRepos(owner).networkOnly()
    }
}