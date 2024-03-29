package com.rongc.feature.network

import com.blankj.utilcode.util.Utils
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

inline fun <reified T> HttpProvider.getService(): T {
    return HttpClient.getRetrofit(this).create(T::class.java)
}

object HttpClient {
    private const val DEFAULT_CONNECT_TIME = 15L
    private const val DEFAULT_WRITE_TIME = 15L
    private const val DEFAULT_READ_TIME = 15L

    fun getRetrofit(provider: HttpProvider): Retrofit {
        //打印网络请求相关日志
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.valueOf(provider.logLevel().name))

        val cacheFile = File(Utils.getApp().cacheDir, "cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 100) //100Mb
        //为了打印日志，需要配置OkHttpClient
        val client = OkHttpClient.Builder()
            //配置SSlSocketFactory
            .sslSocketFactory(SocketFactory.get(), SocketFactory.getTrustManager())
            .hostnameVerifier { hostname, session -> true }
            .connectTimeout(DEFAULT_CONNECT_TIME, TimeUnit.SECONDS)//连接超时时间
            .writeTimeout(DEFAULT_WRITE_TIME, TimeUnit.SECONDS)//设置写操作超时时间
            .readTimeout(DEFAULT_READ_TIME, TimeUnit.SECONDS)//设置读操作超时时间
            .connectionPool(ConnectionPool(32, 5L, TimeUnit.MINUTES))
            .apply {
                provider.providerInterceptors()?.forEach {
                    addInterceptor(it)
                }
                val providerProxy = provider.providerProxy()
                if (providerProxy != null) {
                    proxy(providerProxy)
                }
            }.addInterceptor(loggingInterceptor)
            .cache(cache)
            .build()

        return Retrofit.Builder()
            .baseUrl(provider.baseUrl())
            .client(client)
            .apply {
                provider.providerConverterFactories()?.forEach {
                    addConverterFactory(it)
                }
                provider.providerCallAdapterFactories()?.forEach {
                    addCallAdapterFactory(it)
                }
            }
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}