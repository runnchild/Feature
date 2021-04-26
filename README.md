# Feature

## 一、简介
超轻量级，简单上手的框架，集成少量必要的优秀开源库, 旨在加快平时开发效率，减少不必要的bug。

+ 框架使用了jetpack 中的LiveData, Lifecycle, ViewModel, DataBinding。 大大减少编写的代码量，完美处理生命周期，再也不怕内存泄露。
+ 网络请求：Retrofit+Okhttp+Coroutines。 颠覆传统的网络请求编写体验，顺序从上到下告别蜜汁回调。
+ RecyclerView Adapter: [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
+ 下拉刷新：[SmartRefresh](https://github.com/scwang90/SmartRefreshLayout)
+ 工具库：[AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)

## 二、使用
> 1.依赖
implementation 'com.rongc:feature:$latest_version'

> 2.接入
1.使用或继承CoreApplication，或者在自己的Application attachBaseContext()中初始化Utils.init(this)。
2.继承BaseActivity 并指定这个页面的ViewModel
```
class MainActivity : BaseActivity<MainViewModel>() {

    override fun getContentViewRes() = R.layout.activity_main

    override fun initView(view: View) {
        supportFragmentManager.beginTransaction().add(
            R.id.fragmentLayout,
            MainFragment(), ""
        ).commit()
    }
}

```
2. 推荐使用DataBinding, 首先创建xml并使用<layout></layout>包裹， Activity继承BaseBindingActivity并填写DataBinding 和 ViewModel泛型,就行啦。
```
class ViewBindingAdapterActivity : BaseBindingActivity<ActivityViewBindingAdapterBinding, EmptyViewModel>() {
    override fun initView(view: View) {
        binding.tvText = "hello"
    }
}
```
3. 页面默认添加PsnToolBar, 也可在页面xml中添加。如不需要，可重写getBarConfig()方法配置toolBarVisible = false,
```
<com.rongc.feature.ui.toolbar.PsnToolbar
        app:layout_constraintTop_toTopOf="parent"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```
> PsnToolBar配置: Activity或者Fragment中重写getBarConfig()并设置需要的属性
```
override fun getBarConfig(): BarConfig.() -> Unit {
        return {
            // 沉浸且状态栏icon黑色
//            statusBarState = BarConfig.TRANSPARENT_BLACK
            // 沉浸且状态栏icon白色
//            statusBarState = BarConfig.TRANSPARENT_WHITE
            // 设置状态栏颜色 和 statusBarState对立
            statusColor = Color.BLUE
            // 设置toolbar颜色
            toolBarBackground = Color.BLUE
            // 返回键显示状态
//            toolbarBackVisible = false
            // toolbar底部分割线颜色
            toolBarDividerColor = Color.RED
            // 返回键图标
            toolbarBackDrawable = R.mipmap.common_icon_back.drawable()!!
            // 虚拟导航栏颜色
            navColor = Color.BLUE
            // 菜单， 可设置多个
            menu {
                text = "提交"
                setTextColor(Color.WHITE)
                setOnClickListener { title.toString().toast() }
            }
            toolBarBackground = Color.BLUE
        }
    }
```
## Ability
   日常开发时，有时候需要对页面赋予一些能力，这些能力在很多场景下是通用的(比如可刷新和加载更多的列表页面),此时开发者一般会封装一个BaseListActivity并实现列表相关功能蚁供具体页面继承。
看起来一切都很正常，但试着分析下：一般的框架会有最基础的BaseActivity,然后让BaseListActivity继承BaseActivity.这些Base职责必须单一且基础，不能耦合任何其他业务以能够让所有项目依赖使用。而在开发任何一个项目，也会有针对这个项目的一些通用业务或资源，此时一般会有一个依赖Base库的Common库承载。理应也就会有封装项目页面通用业务的继承BaseActivity的CommonActivity存在了。此时CommonListActivity该继承CommonActivity还是BaseListActivity? 还有Fragment/Dialog等呢？都复制一遍？
    对此尝试使用横向扩展的方式代替纵向扩展以解决多位置、多继承等问题。

IAbility接口申明了页面的基本生命周期方法，这些方法将会在页面对应的生命周期方法调用时回调。开发过程中如遇到需要监听页面生命周期方法被执行时指定业务的需求时可新建类实现IAbility，重写对应方法（例如页面统计）
```
class StatisticsAbility : IAbility {

   override fun onPageResume() {
      MobclickAgent.onPageStart(pageName)
   }

   override fun onPagePause() {
     MobclickAgent.onPageEnd(pageName)
   }
}
```
使用:
```
class CommonActivity : BaseActivity(), IAbility by StatisticsAbility() {
}
```
或者
```
class CommonActivity : BaseActivity() {
     override fun obtainAbility(abilities: ArrayList<IAbility>) {
        // 支持添加多个IAbility
        abilities.add(StatisticsAbility())
    }
}
```
Fragment/Dialog同理，这样就把相关业务抽取出来发，在需要使用的时候注册就行了。

> 框架中已有的Ability：

> 1. 列表: RecyclerListAbility
  + 1 该方案已实现下拉刷新和分页上拉加载、没有数据或没有网络的空页面。一般不需要编写Adapter，只需在页面中添加ItemBinder，支持多布局列表样式,每种Binder对应一种样式.
  + 2 如果需要配置没数据时的空页面，在Activity/Fragment中重写setupEmptyView()并设置需要的属性.
  + 3 默认进入页面自动请求数据，如果不需要重写autoRefresh()返回false
  + 4 默认使用内置的EmptyView，如果需要自定义重写providerEmptyView()返回自定义空页面。

```
class MainRefreshActivity : BaseBindingActivity<BaseRefreshListLayoutBinding, RefreshListViewModel>(), IRecyclerListAbility by RecyclerListAbility() {

    override fun registerItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>) {
        binders.add(TextItemBinder())
        binders.add(ImageItemBinder())
    }

    override fun decorationBuilder(): ItemDecoration.Builder.() -> Unit {
        return {
            setVerticalLineWidth(5.idp)
        }
    }

    override fun setupEmptyView(state: Int): EmptyBuilder.() -> Unit {
        return {
            tip = "empty data"
// or         tip {
//                text = "empty data"
//                textColor/textSize/...=
//            }

            subTip = "pull to refresh"
//  or      subTip {
//                text = "pull to refresh"
//                textColor/textSize/...=
//            }

//            btnText = "refresh"
//            btnVisible = true
            refreshBtn {
                text = "refresh"
                isVisible = true
                singleClick {
                    viewModel.refresh()
                }
            }
        }
    }

    override fun providerEmptyView(context: Context): IEmptyView? {
        return super.providerEmptyView(context)
    }

    override fun autoRefresh() = true
}

// ViewModel中加载数据
class MainRefreshViewModel : BaseListViewModel<Any, BaseModel>() {

    override suspend fun fetchListData(page: Int): List<Any> {
        val items = arrayListOf<Any>()
        repeat(10) {
            if (it % 2 == 0) {
                items.add("$it")
            } else {
                items.add(1)
            }
        }
        return items
    }
}

// 列表ItemView
class TextItemBinder : BaseItemBindingBinder<TextItemBinding, String>() {

    // 不需要设置binding.bean = data, binding.executePendingBindings()，父类已设置
    // 仅需在layout中的data中设置name为bean的variable。
    // <data>
    //      <variable>
    //          name="bean"
    //          type="String"
    //      </variable>
    // </data>
    override fun convert(binding: TextItemBinding, holder: BaseViewHolder, data: String) {
        // binding.item.text = "data:${holder.adapterPosition}"
    }

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return false
    }

    override fun onClick(binding: TextItemBinding, holder: BaseViewHolder, view: View, data: String, position: Int) {
        ToastUtils.showShort("data: $position")
    }

    override fun onChildClick(binding: TextItemBinding, holder: BaseViewHolder, view: View, data: Int, position: Int) {
     // item 子控件点击事件，（需通过addChildClickViewIds(id)提前添加需要点击的id）
    }
}
```

> 2. ViewPager2: PagerListAbility;同RecyclerListAbility:
  + 1 该方案已实现下拉刷新和分页上拉加载、没有数据或没有网络的空页面。
  + 2 类似RecyclerAbility，只需在页面中添加ItemBinder，支持多布局列表样式,每种Binder对应一种样式.如果Item为Fragment，重写providerAdapter(),返回BaseViewPagerAdapter
  + 2 如果需要配置没数据时的空页面，在Activity/Fragment中重写setupEmptyView()并设置需要的属性.
  + 3 默认进入页面自动获取数据，如果不需要重写autoRefresh()返回false
  + 4 PagerItem可选择View或者Fragment，如果为Fragment，重写providerAdapter(), 返回继承BaseViewPagerAdapter的Adapter即可。
```
class ViewPager2Activity : BaseBindingActivity<BaseViewpagerWithRefreshBinding, ViewPagerViewModel>(), IPagerListAbility by PagerListAbility() {

   // 默认使用BaseBinderAdapter，Item为View。如Item是Fragment，重写此方法返回BaseViewPagerAdapter
    override fun providerAdapter() = object : BaseViewPagerAdapter<String>(this) {
        override fun createItemFragment(position: Int): IPagerItem<String> {
            return EmptyViewPagerFragmentItem()
        }
    }

//    类似RecyclerAbility，仅在Item为View时生效
//    override fun registerItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>) {
//        binders.add(PagerSimpleBinder())
//    }

    override fun decorationBuilder(): ItemDecoration.Builder.() -> Unit {
        return {
            setHorizontalLineWidth(10.idp)
            setHorizontalStartWidth(15.idp)
        }
    }

    override fun getBarConfig(): BarConfig.() -> Unit {
        return {
            menu {
                text = "clear"
                singleClick {
                    viewModel.clearData()
                }
            }
        }
    }

    override fun autoRefresh(): Boolean {
        return true
    }

    override fun setupEmptyView(state: Int): EmptyBuilder.() -> Unit {
        return {
            tip = "empty pager"
            subTip = "pull to refresh"
        }
    }
}
```
## 网络配置
。。。
```

/**
 * 网络请求基本信息提供方
 * @see HttpClient#getRetrofit(HttpProvider)
 * 如果项目没有特殊要求，在项目公共模块中实现他并提供封装方法返回HttpClient即可
 * 如若在某个组件需要更改或增加配置，在这个组件中实现他并返回具体值
 */
interface HttpProvider {
    fun baseUrl(): String

    fun logLevel() = Level.BODY

    fun providerConverterFactories(): Array<Converter.Factory>?

    fun providerInterceptors(): Array<Interceptor>?
    fun providerProxy(): Proxy? = null
}

```
## 网络请求

```
viewModel.launch({
    val user = model.fetchUserInfo()
}, failed= {
    // 失败回调
}, showDialog = true // 是否在加载过程中显示进度条
, showToast = false // 是否弹出toast)
```


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
 // 普通点击事件
android:onClick="@{ui.login}"
// 传递参数View本身
android:onClick="@{ui.viewClick}"
// 传递其他参数
android:onClick="@{ui.clickWithParam(any)}"
// 直接toast
android:onClick='@{ui.toast("waw")}'
// 默认添加防抖动
app:debounce="@{false}"
```

```
class UI {
  // 点击事件
  val login = {
    // to login
  }

  // 点击事件将View本身传递过来
  val viewClick = {v: View ->
  }

  // 点击事件传递其他参数
  fun clickWithParam(params: Any): () -> Unit = {
     log(params)
  }
}
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
更多可看library， 项目正在完善中。。。