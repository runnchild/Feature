package com.rongc.demo.dp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rongc.demo.vo.Repo
import com.rongc.demo.vo.RepoSearchResult

/**
 * Main database description.
 */
@Database(
    entities = [
        Repo::class,
        RepoSearchResult::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GithubDb : RoomDatabase() {

    abstract fun repoDao(): RepoDao
}
