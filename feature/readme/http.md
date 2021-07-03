# 网络配置

除了通用的基础库外，每个项目还会有一些自己的额外配置。比如接口的baseUrl及其他网络配置，后端定义的接口数据结构等等。这些配置需要在项目开发前配置好，以demo的玩安卓网络配置为例（具体可参考demo）：

1. 创建HttpProvider继承HttpProvider，提供baseUrl等

```
class WanHttpProvider: HttpProvider {
    // 提供baseUrl
    override fun baseUrl(): String {
        return "https://www.wanandroid.com"
    }
    // 打印日志等级
    override fun logLevel() = Level.BODY
    // 可添加ConvertFactory
    override fun providerConverterFactories() = arrayOf(WrapperConverterFactory.create())
    // 可添加拦截器
    override fun providerInterceptors() = null
    // 可添加CallAdapterFactory
    override fun providerCallAdapterFactories(): Array<CallAdapter.Factory>? = null
}
```

2. 可创建ConverterFactory，以统一处理项目所有请求的错误码或者其他通用业务。例如：接口定义的数据结构为

```
{
    "data": ...,
    "errorCode": 0,
    "errorMsg": ""
}
// 创建对应的实体类
data class BaseResponse<T>(val errorCode: Int, val errorMsg: String, val data: T)
```

定义WrapperConverterFactory，并添加到HttpProvider中

```
class WrapperConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        return WrapperResponseBodyConverter<Any>(type, gson)
    }

    companion object {
        fun create(gson: Gson? = Gson()): WrapperConverterFactory {
            if (gson == null) throw NullPointerException("gson == null")
            return WrapperConverterFactory(gson)
        }
    }
}
```

```
class WrapperResponseBodyConverter<T> internal constructor(private val mType: Type, private val gson: Gson) :
    Converter<ResponseBody, T?> {

    override fun convert(value: ResponseBody): T? {
        val result = value.string()
        val data = JSONTokener(result).nextValue()
        if (data is JSONObject) {
            return gson.fromJson<T>(
                if ((mType as? ParameterizedType)?.rawType == BaseResponse::class.java) {
                    // 如果需要完整数据，Api方法的返回值类型的泛型应为BaseResponse<T>
                    result
                } else {
                    // 否则不关注错误码，只接收data，接口请求不成功（错误码不为0）时抛出异常
                    val code = data.optInt("errorCode", -1)
                    if (code != 0) {
                        throw ServicesException(code, data.optString("errorMsg"))
                    }
                    data.optString("data")
                }, mType
            )
        }
        throw ServicesException(500, "response error, response value = $result")
    }
}
```

3. 创建ServicesException承接接口定义的业务错误码和错误信息

```
class ServicesException(val code: Int, message: String?) : Exception(message)
```

4. 定义数据源提供方。数据的本地缓存repoDb或者网络接口来源apiService，这些实例在同个模块中可能是相同的，所以可申明成单实例。

```
object WanServiceProvider {

    // 数据库实例
    val repoDb: GithubDb = Room
        .databaseBuilder(Utils.getApp(), GithubDb::class.java, "github.db")
        .fallbackToDestructiveMigration()
        .build()

    // Api接口实例
    val apiService: WanService = WanHttpProvider().getService()

    val repoDao: RepoDao
        get() {
            return repoDb.repoDao()
        }

    // 数据仓库，决定数据的来源。
    val wanRepository = WanRepository()
}
```

通过以上代码就完成项目基本网络配置，当接口发生业务上错误时，可根据ServicesException code或message处理错误逻辑。