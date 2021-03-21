package com.rongc.feature.network

import okhttp3.Interceptor
import retrofit2.Converter
import java.net.Proxy

/**
 * 网络请求基本信息提供方
 * @see HttpClient#getRetrofit(HttpProvider)
 * 如果项目没有特殊要求，在项目公共模块中实现他并提供封装方法返回HttpClient即可
 * 如若在某个组建需要更改或增加配置，在这个组件中实现他并返回具体值
 */
interface HttpProvider {
    fun baseUrl(): String

    fun logLevel() = Level.BODY

    fun providerConverterFactories(): Array<Converter.Factory>?

    fun providerInterceptors(): Array<Interceptor>?
    fun providerProxy(): Proxy? = null
}

enum class Level {
    /** No logs. */
    NONE,

    /**
     * Logs request and response lines.
     *
     * Example:
     * ```
     * --> POST /greeting http/1.1 (3-byte body)
     *
     * <-- 200 OK (22ms, 6-byte body)
     * ```
     */
    BASIC,

    /**
     * Logs request and response lines and their respective headers.
     *
     * Example:
     * ```
     * --> POST /greeting http/1.1
     * Host: example.com
     * Content-Type: plain/text
     * Content-Length: 3
     * --> END POST
     *
     * <-- 200 OK (22ms)
     * Content-Type: plain/text
     * Content-Length: 6
     * <-- END HTTP
     * ```
     */
    HEADERS,

    /**
     * Logs request and response lines and their respective headers and bodies (if present).
     *
     * Example:
     * ```
     * --> POST /greeting http/1.1
     * Host: example.com
     * Content-Type: plain/text
     * Content-Length: 3
     *
     * Hi?
     * --> END POST
     *
     * <-- 200 OK (22ms)
     * Content-Type: plain/text
     * Content-Length: 6
     *
     * Hello!
     * <-- END HTTP
     * ```
     */
    BODY
}