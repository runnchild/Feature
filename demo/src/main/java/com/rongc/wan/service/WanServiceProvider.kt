package com.rongc.wan.service

import androidx.room.Room
import com.blankj.utilcode.util.Utils
import com.rongc.demo.db.GithubDb
import com.rongc.demo.db.RepoDao
import com.rongc.feature.network.getService
import com.rongc.wan.WanRepository
import com.rongc.wan.WanService

object WanServiceProvider {

    val repoDb: GithubDb = Room
        .databaseBuilder(Utils.getApp(), GithubDb::class.java, "github.db")
        .fallbackToDestructiveMigration()
        .build()

    val apiService: WanService = WanHttpProvider().getService()

    val repoDao: RepoDao
        get() {
            return repoDb.repoDao()
        }

    val wanRepository = WanRepository()
}