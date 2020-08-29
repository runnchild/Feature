package com.rongc.feature.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * 具有生命周期感知的Scope， 
 * 需要在页面onCreate和onDestroy中通知MainScope创建和销毁Job
 */
class MainScope : CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun onCreate() {
        job = Job()
    }

    fun onDestroy() {
        job.cancel()
    }
}

