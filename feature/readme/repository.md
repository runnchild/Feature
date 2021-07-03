# 数据仓库

先看下官方推荐的架构图

![architecture](final-architecture.png)

可以看到，除了存储区（Repository），其他组件都仅依赖下一级各司其职。
存储区依赖可以是本地数据库或者远程，相比无论数据是否有更新每次都从远程获取数据，使用本地数据或者在从远程获取最新数据期间使用本地缓存数据以缩减用户等待时间，然后在数据请求回来后更新到数据库以供下次使用，这无疑是更友好的做法。

无论Repository决定从使用哪个数据源，它的职责都是提供单一可信来源的数据。

## 一、数据来源

1. 从远程获取

[http](http.md)
中提到Http相关配置，除了向HttpProvider提供的CallAdapterFactory外，内部添加了支持LiveData作为数据源的CallAdapter，在定义后端接口时可以使用LiveData作为方法返回值：

```
interface WanService {

    @GET("banner/json")
    fun getBanner(): LiveData<ApiResponse<List<WanBanner>>>
}
```

但LiveData的直接泛型是ApiResponse而不是List<WanBanner>，是因为我们还需要处理异常情况的结果。

ApiResponse密封类中定义了以下三种情况：

+ ApiEmptyResponse：HTTP 204或无法获取到非空的结果。
+ ApiSuccessResponse(body: T)：请求正常。
+ ApiErrorResponse(error: Throwable)：请求错误

CallAdapterFactory会把获取到的请求按情况封装成上述三种状态并通过LiveData发送出来.

2. 从数据库获取

首选Room，具体用法自行获取。

[comment]: <> (## 二、Repository完整使用)

[comment]: <> (1. 可观察的请求状态)

[comment]: <> (在某些用例（如下拉刷新）中，界面务必要向用户显示当前正在执行某项网络操作。我们将网络操作状态包含在LiveData中以便及时通知页面更新，大致做法为在发起请求前将LiveData的值设为loading状态，在接收到请求结果后更新为success或failed状态，稍后会详解。)

[comment]: <> (## 一、公开的网络状态 Resource)

## 二、存取方案 NetworkBoundResource

引用官方的解释
> 它首先观察资源的数据库。首次从数据库中加载条目时，NetworkBoundResource 会检查结果是好到足以分派，还是应从网络中重新获取。请注意，考虑到您可能会希望在通过网络更新数据的同时显示缓存的数据，这两种情况可能会同时发生。 如果网络调用成功完成，它会将响应保存到数据库中并重新初始化数据流。如果网络请求失败，NetworkBoundResource 会直接分派失败消息。 注意：在将新数据保存到磁盘后，我们会重新初始化来自数据库的数据流。不过，通常我们不需要这样做，因为数据库本身正好会分派更改。 请注意，依赖于数据库来分派更改将产生相关副作用，这样不太好，原因是，如果由于数据未更改而使得数据库最终未分派更改，就会出现这些副作用的未定义行为。 此外，不要分派来自网络的结果，因为这样将违背单一可信来源原则。毕竟，数据库可能包含在“保存”操作期间更改数据值的触发器。同样，不要在没有新数据的情况下分派 `SUCCESS`，因为如果这样做，客户端会接收错误版本的数据。

看不下去对吧，大致整理下：

1. 在发起请求前NetworkBoundResource先检查数据库，然后判断是否直接使用还是继续请求网络数据。
2. 可在网络请求期间先使用缓存数据。
3. 如果网络请求成功会更新数据库并向外派送通知，如果网络请求失败会直接派送失败消息。
4. 不要依赖数据库的变动通知虽然他有这个功能，因为可能数据库未更改而使得这个通知未派送。
5. 不要直接分派来自网络请求的结果，因为违背了单一可信赖原则。在保存期间可能需要更改数据。

```
abstract class NetworkBoundResource<ResultType, RequestType> {
    private val result = MediatorLiveData<Resource<ResultType>>()
        
    protected open fun onFetchFailed() {}
    
    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}
```

NetworkBoundResource定义了两个类型参数（ResultType 和 RequestType），因为从 API 返回的数据类型可能与本地使用的数据类型不匹配。
RequestType为Api返回的类型，ResultType为本地需要的类型。

NetworkBoundResource内有一个名为result的LiveData，他的泛型为Resource，包含了请求中，请求成功，请求失败3中状态。

简要分析下整个流程：

1. 当程序发起请求时会派送loading状态的通知。 
2. 通过loadFromDb()从数据库中加载数据，并通过shouldFetch(data)询问是否需要继续从网络获取。
3. 如果不需要从网络获取则派发这个结果，否则使用createCall()创建网络请求。 
4. 当请求完成时：
    + 请求成功（ApiSuccessResponse）：执行processResponse()，可在此时加工数据。然后调用saveCallResult()将结果保存到数据库。然后重新loadFromDb()才把结果派送出去，Resource状态为Success。
    + 请求为空（ApiEmptyResponse）：重新loadFromDb()把结果派送出去，Resource状态为Success。
    + 请求失败（ApiErrorResponse）：调用onFetchFailed()，并把上次从数据库取到的数据和此错误派送出去，Resource状态为error。

## 三、使用