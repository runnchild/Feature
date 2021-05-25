package com.rongc.demo.api

import com.rongc.feature.network.HttpProvider
import okhttp3.Interceptor
import retrofit2.Converter

class RepoHttpProvider private constructor() : HttpProvider {

    companion object {
        private var instance: HttpProvider? = null
        fun get(): HttpProvider {
            return instance ?: RepoHttpProvider().apply { instance = this }
        }
    }

    override fun baseUrl(): String {
        return "https://api.github.com/"
    }

    override fun providerConverterFactories(): Array<Converter.Factory>? {
        return null
    }

    override fun providerInterceptors(): Array<Interceptor>? {
        return null
    }
}