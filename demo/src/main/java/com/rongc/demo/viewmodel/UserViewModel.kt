package com.rongc.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.rongc.demo.api.RepoServiceProvider
import com.rongc.feature.utils.AbsentLiveData
import com.rongc.feature.viewmodel.BaseViewModel

class UserViewModel : BaseViewModel() {
    val login = MutableLiveData<String>()
    private val repository = RepoServiceProvider.repoRepository

    val repositories = login.switchMap {
        if (it.isNullOrEmpty()) {
            AbsentLiveData.create()
        } else {
            repository.getRepos(it)
        }
    }

}