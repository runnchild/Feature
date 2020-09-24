# Feature
a niubility android develop library
> 1.依赖：
implementation 'com.rongc:feature:$latest_version'

> 2.简介
站在巨人肩膀上撸码，集成必要的优秀开源库, 旨在加快平时开发效率，减少不必要的bug。 

+ 框架使用了jetpack 中的LiveData, Lifecycle, ViewModel, DataBinding。 大大减少编写的代码量，完美处理生命周期，再也不怕内存泄露。
+ 网络请求：Retrofit+Okhttp+Coroutines。 颠覆传统的网络请求编写体验，顺序从上到下告别蜜汁回调。
+ RecyclerView Adapter: [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
+ 下拉刷新：[SmartRefresh](https://github.com/scwang90/SmartRefreshLayout)
+ 工具库：[AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)

> 3.使用
1.不使用DatabBinding, 继承BaseActivity 并指定这个页面的ViewModel
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
2. 使用DataBinding, 首先创建xml并使用<layout></layout>包裹， Activity继承BaseBindingActivity并填写DataBinding 和 ViewModel泛型
```
class ViewBindingAdapterActivity : BaseBindingActivity<ActivityViewBindingAdapterBinding, EmptyViewModel>() {

    override fun binding(inflater: LayoutInflater, container: ViewGroup?): ActivityViewBindingAdapterBinding {
        return ActivityViewBindingAdapterBinding.inflate(inflater)
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
> 4. 列表页面
只需在1或者2步中多实现IRefreshDelegate接口， 对应的ViewModel需要继承BaseRefreshViewModel<T, Model>。
  + 1 该方案已实现下拉刷新和上拉加载、没有数据或没有网络的空页面。简单交互的列表可不需要编写Adapter，只需在ViewModel中添加ItemBinder，支持多布局列表样式,每种Binder对应一种样式.
  + 2 数据请求操作在loadData()中配合kotlin的协程即可方便实现
  + 3 如果页面是普通的一个列表加ToolBar，布局可以使用通用的BaseRefreshLayoutBinding或者base_refresh_layout.xml。
  + 4 如果需要自己编写页面， RecycerView的id必须设置为@id/base_recyclerView。如果需要下拉刷新也得在xml中添加SmartRefreshLayout
  + 5 如果需要配置没数据时的空页面，在Activity/Fragment中重写setupEmptyView()设置需要的属性.
  + 6 默认进入页面自动获取数据，如果不需要重写autoRefresh()返回false

```
class ViewBindingAdapterActivity : BaseBindingActivity<BaseRefreshLayoutBinding, EmptyViewModel>(), IRefreshDelegate {

    override fun setupEmptyView(state: Int): EmptyBuilder.() -> Unit {
        return {
            tip = "暂无数据"
            btnText = "Refresh"
            btnVisible = true
            refreshClick = {
              "to Refresh".toast()
            }
            iconDrawable = R.mipmap.empty_no_net_work.drawable()
        }
    }
    
    override fun autoRefresh() = true
}

class MainRefreshViewModel : BaseRefreshViewModel<Any, BaseModel>() {

    override fun loadData(page: Int, dataRequestCall: DataRequestCallback<List<Any>>) {
        launch({
                delay(2000)
                repeat(10) {
                    if (it % 2 == 0) {
                        items.add("$it")
                    } else {
                        items.add(1)
                    }
                }
                // 请求成功
                dataRequestCall.onSuccess(page, items)
            }, 
            {
                // 请求失败
                dataRequestCall.onFailed(page)
            }, showDialog = true, showToast = true)
     }
     
     override fun providerItemBinders(binders: MutableSet<BaseRecyclerItemBinder<out Any>>) {
        // 可添加多种Item样式
        binders.add(MainItemHolder())
        binders.add(MainOtherItemHolder())
    }
```
> ItemBinder的使用：
ItemBinder是列表Item的UI管理器，相当于将Adapter的onCreateViewHolder()操作移动至ItemBinder中， ItemBinder也可选择是否支持DataBinding,以支持DataBinding为例
```
class MainOtherItemBinder : BaseItemBindingBinder<MainOtherBindingItemBinding, Int>() {
    override fun convert(binding: MainOtherBindingItemBinding, holder: BaseViewHolder, data: Int) {
        // 内容填充
    }

    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
        return false
    }

    override fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup) =
        MainOtherBindingItemBinding.inflate(inflater, parent, false).apply { 
            addChildClickViewIds(tvTitle.id)
        }

    override fun onClick(holder: BaseViewHolder, view: View, data: Int, position: Int) {
        // item 点击事件
        ToastUtils.showShort("image: $position")
    }
     override fun onChildClick(holder: BaseViewHolder, view: View, data: Int, position: Int) {
     // item 子控件点击事件，（需通过addChildClickViewIds(id)提前添加需要点击的id）
    }
}
```

> 5. 如果不是列表页面，但也想使用RecycerView时：
+ app:items绑定列表数据源， 
+ app:itemBinders添加列表样式，
+ app:itemDecoration添加间距等装饰
```
<androidx.recyclerview.widget.RecyclerView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                app:items="@{viewModel.items}"
                app:itemBinders="@{viewModel.itemBinder}"
                app:itemDecoration="@{viewModel.itemDecoration}"
                app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager" />
                
class ViewModel {
  // ObservableArrayList,数据变化自动更新列表 
  val items = ObservableArrayList<T>()
  val itemBinder = mutableListOf(SomeItemBinder())
  // 此处后期可优化为BindingAdapter
  val itemDecoration = ItemDecoration().builder()
                        .setVerticalLineWidth(10.idp())
                        .setHorizontalLineWidth(10.idp())
                        .build()
}
           
```
> 6. LiveDataBus：自动管理生命周期的EventBus，不需要反注册。只会在页面可见时接收
    ```
    // 注册监听
    LiveDataBus.with<T>(key).observe(this) {
        // do str with it
    }
    // 发送事件
    LiveDataBus.with<T>(key).setValue("hello")
    // 子线程发送事件
    LiveDataBus.with<T>(key).postValue("hello")
    // 发送粘性事件
    LiveDataBus.with<T>(key).setStickValue("hello")
    ```
> 7. 其他
 + ViewPager2， 跟RecyclerView一样的使用体验
 ```
    <androidx.viewpager2.widget.ViewPager2
       android:id="@+id/viewPager"
       app:items="@{viewModel.items}"
       app:itemBinders="@{viewModel.itemBinder}"
       android:layout_width="match_parent"
       android:layout_height="match_parent" />
 ```
 + android:onCLick, 轻松设置点击事件
 UI相关的操作都建议放在Activity/Fragment中，可再xml的控件中加上android:onClick="@{viewModel.viewsClick}"，并在Activity/Fragment中重写viewClick()方法
 ```
    android:onClick="@{viewModel.viewsClick}"
 ```
 
 ```
 class XXXActivity : BaseActivity() {
    override fun viewClick(view: View) {
        when (view.id) {
            R.id.x-> {}
        }
    }
 }
 ```
 或者业务操作放在ViewModel中：
```
android:onClick="@{viewModel.login}"
// 传递参数View本身
android:onClick="@{viewModel.someClick}"
// 传递其他参数
android:onClick="@{viewModel.clickWithParam(any)}"
// 直接toast
android:onClick='@{viewModel.toast("waw")}'
// 可选， 防抖动
app:debounce="@{true}"
```

```
class ViewModel {
  // 点击事件
  val login = {
    // to login
  }
  
  // 点击事件将View本身传递过来
  val someClick = {v: View ->
  }
  
  // 点击事件传递其他参数
  fun clickWithParam(params: Any): () -> Unit = {
     log(params)
  }
}
```
> 扩展函数
 + dimension:
 10.dp()
 14.sp()
 ...

+ resource:
 R.string.hello.string()
 R.color.white.color()
 R.drawable.ic_launcher.drawable()

 "hello word".toast()
 "log anything".logd()
 "log error".loge()
 ...

 + gson:
 val user = "{"id":"1"}".parse<User>()
 val list: List<User> = "[{"name":"a"},{"name":"b"}]".parseList<User>()
 val jsonStr = user.toJson()

 + View
 view.setRoundBg(color, radius)
 view.visible(false)
更多可看library， 项目正在完善中。。。
