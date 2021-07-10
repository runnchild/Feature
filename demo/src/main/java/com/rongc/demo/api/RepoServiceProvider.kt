package com.rongc.demo.api

import androidx.room.Room
import com.blankj.utilcode.util.Utils
import com.rongc.demo.db.GithubDb
import com.rongc.demo.db.RepoDao
import com.rongc.demo.repository.RepoRepository
import com.rongc.feature.network.getService

object RepoServiceProvider {

    val repoDb: GithubDb = Room
            .databaseBuilder(Utils.getApp(), GithubDb::class.java, "github.db")
            .fallbackToDestructiveMigration()
            .build()

    val apiService: RepoService get() {
        return RepoHttpProvider.get().getService()
    }

    val repoDao: RepoDao get() {
        return repoDb.repoDao()
    }

    val repoRepository = RepoRepository(apiService, repoDb)
}