package com.rongc.demo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.rongc.demo.api.RepoServiceProvider
import com.rongc.demo.api.RepoServiceProvider.repoDao
import com.rongc.demo.vo.Repo
import com.rongc.demo.vo.RepoSearchResponse
import com.rongc.demo.vo.RepoSearchResult
import com.rongc.feature.api.ApiResponse
import com.rongc.feature.repository.NetworkBoundResource
import com.rongc.feature.utils.AbsentLiveData
import com.rongc.feature.vo.Resource

class RepoRepository {

    val repoApi = RepoServiceProvider.apiService

    fun searchRepo(query: String): LiveData<Resource<List<Repo>>> {
        return object: NetworkBoundResource<List<Repo>, RepoSearchResponse>() {
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

            override fun shouldFetch(data:List<Repo>?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<List<Repo>> {
                return repoDao.search(query).switchMap { searchData ->
                    if (searchData == null) {
                        AbsentLiveData.create()
                    } else {
                        repoDao.loadOrdered(searchData.repoIds)
                    }
                }
            }

            override fun createCall(): LiveData<ApiResponse<RepoSearchResponse>> {
                return repoApi.searchRepos(query, 0)
            }

        }.asLiveData()
    }
}