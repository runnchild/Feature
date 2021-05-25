package com.rongc.demo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.rongc.demo.repository.RepoRepository
import com.rongc.demo.vo.Repo
import com.rongc.feature.utils.AbsentLiveData
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.feature.vo.Resource

class ListViewModel(private val repository: RepoRepository) : BaseViewModel() {

    private val query = MutableLiveData<String>()

    val result: LiveData<Resource<List<Repo>>> = query.switchMap {
        if (it.isBlank()) {
            AbsentLiveData.create()
        } else {
            repository.searchRepo(it)
        }
    }

    fun setQuery(queryInput: String) {
        if (queryInput.trim() == query.value) {
            return
        }
        query.value = queryInput
    }

}