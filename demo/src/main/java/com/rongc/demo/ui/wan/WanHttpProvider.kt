package com.rongc.demo.ui.wan

import com.rongc.feature.network.HttpProvider
import okhttp3.Interceptor
import retrofit2.Converter

class WanHttpProvider: HttpProvider {
    override fun baseUrl(): String {
        return "https://www.wanandroid.com"
    }

    override fun providerConverterFactories(): Array<Converter.Factory>? {
        return arrayOf(WanFactory())
    }

    override fun providerInterceptors(): Array<Interceptor>? {
        return null
    }
}