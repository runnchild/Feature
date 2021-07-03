# Feature [![](https://jitpack.io/v/runnchild/Feature.svg)](https://jitpack.io/#runnchild/Feature)

## 一、简介

这是我见过的可能最简洁，最轻量的MVVM框架。拒绝复杂的、冗余的、过度的Base设计，不为封装而封装，让开发回归他应有的样子。

特色：

+ 1.遵循Google推荐的开发原则：
    - 分离关注点，Activity/Fragment仅包含处理界面和交互逻辑。
    - Model驱动界面，数据不受应用生命周期影响，UI由数据驱动。
+ 2.数据源获取与缓存。可以选择数据源获取的方式为网络或者本地缓存，并可缓存此次数据以供下次使用。
+ 3.公开的网络状态。轻松管理每个请求的状态，请求状态分为加载中、加载成功、加载失败（包含业务失败/请求失败）
+ 4.(Ability)热插拔式扩展能力。使用横向扩展的方式代替纵向（继承）扩展，只在需要时引用，不需要时不会给页面增加一行无关代码。
    - (ListAbility)
      新颖的RecyclerView/ViewPager2的使用体验，支持空页面显示、刷新和分页加载功能，单个/多个ItemType简单地无差别使用，并且可以忘了Adapter。
    - (ProgressAbility)让页面显示加载进度弹窗的能力（如在耗时操作时显示，结束后关闭的加载弹窗）。
    - (ToolbarAbility)让页面拥有简约的Toolbar的能力。
    - 其他可由开发者根据业务需求自行扩展。
+ 5.DataBinding/ViewBinding。人生苦短，用它！少写一半代码。
+ 6.事件总线。LiveEventBus。

## 二、使用

> 1.依赖 根目录build.gradle添加jitpack仓库

```
buildscript {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

```
dependencies {
    // core
    implementation "com.github.runnchild.Feature:feature:$latest_version"
    
    // RecyclerView/ViewPager2扩展能力，可选
    implementation "com.github.runnchild.Feature:list:$latest_version" 
    
    // 简约Toolbar扩展能力，可选
    implementation "com.github.runnchild.Feature:toolbar:$latest_version"
    
    // 低版本adapter兼容库，可选
    implementation "com.github.runnchild.Feature:adaptercompat:$latest_version"
    
    // LiveEventBus，可选
    implementation "com.github.runnchild.Feature:liveevent:$latest_version"
}

```

> 2.使用

+ **普通页面**

1. 创建Fragment/Activity继承BaseFragment/Activity

```
class WanHomeFragment : BaseFragment<FragmentWanHomeBinding, WanHomeViewModel>() {
}
```

2. 创建ViewModel继承BaseViewModel

```
class WanHomeViewModel : BaseViewModel() {
}
```

3. 好了，剩下只需要关注业务的实现了。

---

+ **列表页面**

1. 和普通页面第一步一样(UI无特殊要求可使用框架内置的BaseRecyclerWithRefreshBinding)，注册ListAbility/PagerAbility

```
class ProjectListFragment : BaseFragment<BaseRecyclerWithRefreshBinding, ProjectListViewModel>(),IRecyclerHost {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 1.注册列表能力
        registerAbility(ListAbility(viewModel, this))
        // 如果是ViewPager2, 实现改为IPagerHost
        // registerAbility(PagerAbility(viewModel, this))
    }
    
    override fun registerItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>) {
        // 2.添加列表Item样式, 可添加多个样式
        binders.add(ProjectItemBinder())
    }
}
```

2. 创建列表ItemBinder

```
class ProjectItemBinder : BaseItemBindingBinder<ItemProjectListBinding, ProjectList>() {
    override fun convert(
        binding: ItemProjectListBinding, holder: BaseViewHolder, data: ProjectList
    ) {
      // 简单的ui-数据绑定都可写在xml中。这里可空实现
    }
}
```

3. 创建ViewModel继承BaseListViewModel<T>，实现数据源获取方法loadListData。

```
class ProjectListViewModel : BaseListViewModel<ProjectList>() {
    override fun loadListData(page: Int): LiveData<Resource<List<ProjectList>>> {
        return repository.getProjectList(page, cid)
    }
}
```

一个具有刷新、分页加载、空页面（空数据，无网络等情况）的列表页面就写好了。

+ **DialogFragment**

1.继承BaseDialogFragment, 其余与普通/列表页面完全一致。

```
class xxxDialogFragment : BaseDialogFragment<xxxDialogFragmentBinding, xxxDialogViewModel>() {
}
```

+ **Toolbar**

在页面onCreate时注册ToolbarAbility，并设置相关信息

```
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    registerAbility(ToolbarAbility(this) {
      // 页面标题,如果页面是Activity，默认会使用Activity的Title
      title = "UserRepository"
      // 右上角菜单按钮，可添加多个
      menu {
          // 闭包的this为TextView，可当此菜单为TextView做的相关设置
          text = "more"
          setOnClickListener {
              findNavController().navigate(R.id.demo_dialog)
          }
      }
    }
}
```

+ **加载中提示弹窗**

1.新建全局的ProgressAbility继承AbsProgressAbility，定义自己的弹窗样式

  ```
class ProgressAbility(context: Context): AbsProgressAbility(context) {

    // 定义自己的弹窗样式
    override val dialog: AlertDialog
        get() = super.dialog
        
            
    init {
        // 设置弹窗延时展示时长，在此时间断内若耗时操作完成则不会显示弹窗。以此解决响应快时弹窗一闪即逝问题
        delay = 400
    }
}
  ```

2.在需要展示加载弹窗的地方直接调用 showProgress()/dismissProgress()，可不必手动注册ProgressAbility，调用时内部会自动注册。
在接口请求的情景下只需调用showProgressIfLoading(it)，就能在网络加载中展示，失败或成功后自动关闭;

```
class RepoSearchFragment : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 不需要 registerAbility(ProgressAbility(this))
        
        // 订阅接口返回结果，正常情况加载中/加载成功/加载失败都会接收
        viewModel.result.observe(viewLifecycleOwner) {
            // 仅在加载中显示进度弹窗，其他情况自动关闭
            showProgressIfLoading(it)
        }
    }
}
```

## 三、 进阶用法

> **1.网络配置**

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

> 2.[**关于数据请求和缓存看这里**](/feature/network.md)

> 3.[**带Toolbar的页面**](/toolbar/readme.md)

> 4.[**列表页面**](/list/readme.md)

## 其他

> 1. RecycerView和ViewPager2的简单使用

如果不是列表页面，但也想使用RecyclerView或ViewPager2时：

+ app:items绑定列表数据源，
+ app:itemBinders添加列表ItemBinder，
+ app:itemBinderName 添加列表itemBinder名称
+ app:itemDecoration添加装饰

```

//or <androidx.viewpager2.widget.ViewPager2
<androidx.recyclerview.widget.RecyclerView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:items="@{viewModel.items}"
    app:itemBinders="@{ui.itemBinder}"
    // app:itemBinderName='@{"com.rongc.ui.binders.TextItemBinder"}'
    app:itemDecoration="@{ui.itemDecoration}"
    app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager" />

    // 或者直接设置分割线间距
    app:decoration_left="@{10dp}"
    app:decoration_top="@{10dp}"
    app:decoration_right="@{10dp}"
    app:decoration_bottom="@{10dp}"
    app:decoration_vertical_line="@{10dp}"
    app:decoration_horizontal_line="@{10dp}"

ViewPager2同理，另可简单设置轮播
<androidx.viewpager2.widget.ViewPager2
   android:layout_width="match_parent"
   android:layout_height="match_parent"
   app:auto_scroll="@{true}" // 是否支持自动滚动
   app:loop="@{true}" // 是否循环轮播
   app:scroll_interval="@{1000}" // 滚动间隔


class ViewModel {
  // ObservableArrayList,数据变化自动更新列表
  val items = ObservableArrayList<T>()
}

class Activity {
  val itemBinder = mutableListOf(SomeItemBinder())
  val itemDecoration = ItemDecoration().builder()
                        .setVerticalLineWidth(10.idp)
                        .setHorizontalLineWidth(10.idp)
                        .build()

     fun initView() {
        recycerView.doOnAdapter { adapter->
            // 如果recycerView没有设置Adapter则会在设置adapter后调用，否则直接调用
        }

     }
}

```

或者代码中设置：

```
class Activity {
    override fun initData() {
        recyclerView.items(items)
        recyclerView.itemBinders(binders)
        recyclerView.decoration(left, top, right, bottom)
        recyclerView.adapter(adapter)
    }
}
```

> 2. LiveDataBus：自动管理生命周期的EventBus，不需要反注册。

   ```
    val key: String
    // 发送事件
    key.liveBus.setValue("hello")
    // 子线程发送事件
    key.liveBus().postValue("hello")
    // 发送粘性事件
     key.liveBu().setStickValue("hello")
    // 注册监听
    key.liveBus<T>().observe(this) {
    }
    // 默认情况下，订阅者在非激活状态时不会收到通知，如果事件发送多次仅能收到最后一次事件，
    // observeAny能保证所有事件不丢失，保存非激活状态的事件，并能够在激活状态回调，且没有内存泄漏
    key.liveBus().observeAny(this) {
    }
    // 仅支持回调单个订阅的LiveData
    val singleLiveData: = SingleLiveData<String>()

   ```

> 3.ViewBinding

+ android:onClick:

 ```

// 普通点击事件 android:onClick="@{ui.login}"
// 传递参数View本身 android:onClick="@{ui.viewClick}"
// 传递其他参数 android:onClick="@{ui.clickWithParam(any)}"
// 直接toast android:onClick='@{ui.toast("waw")}' // 默认添加防抖动 app:debounce="@{false}"

```

```

class UI { // 点击事件 val login = { // to login }

// 点击事件将View本身传递过来 val viewClick = {v: View ->
}

// 点击事件传递其他参数 fun clickWithParam(params: Any): () -> Unit = { log(params)
} }

```

+ 圆角：

 ```

app:round_radius="@{10dp}"
app:tl_radius="@{10dp}"
app:tr_radius="@{10dp}"
app:bl_radius="@{10dp}"
app:bl_radius="@{10dp}"
app:round_color="@{@color/white}"
// or android:background="@color/white"

 ```

+ 扩展函数

+ dimension:

 ```

    10.dp
    14.sp
    15.idp
    16.isp
    ...

 ```

+ resource:

 ```

R.string.hello.string()
R.color.white.color()
R.drawable.ic_launcher.drawable()

"hello word".toast()
"log anything".logd()
"log error".loge()
...

```

+ gson:

 ```

val user = "{"id":"1"}".parse<User>()
val list: List<User> = "[{"name":"a"},{"name":"b"}]".parseList<User>()
val jsonStr = user.toJson()

    "{"id":"1"}".optString("id")
    // or
    "{"id":"1"}".opt { obj->
        obj.optString("id")
        obj.optInt("...")
    }

```
