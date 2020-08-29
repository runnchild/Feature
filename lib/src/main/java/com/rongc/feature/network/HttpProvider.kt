package com.rongc.feature.network

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter

/**
 * 网络请求基本信息提供方
 * @see HttpClient#getRetrofit(HttpProvider)
 * 如果项目没有特殊要求，在项目公共模块中实现他并提供封装方法返回HttpClient即可
 * 如若在某个组建需要更改或增加配置，在这个组件中实现他并返回具体值
 */
interface HttpProvider {
    fun baseUrl(): String

    fun logLevel() = HttpLoggingInterceptor.Level.BODY

    fun providerConverterFactories(): Array<Converter.Factory>?

    fun providerInterceptors(): Array<Interceptor>?
}