# ToolbarAbility

虽然android原生已有功能齐全的Toolbar，但也正因为它强大所以复杂，使代码不容易理解，想要更改一些简单属性麻烦。
所以很多开发者并不喜欢使用，而是喜欢自己写一个简单的标题栏，想咋改咋改。

PsnToolbar就是这个简单的Toolbar，包含左侧返回键、中间标题、右边菜单以及状态栏和虚拟导航栏的简单设置。

![toolbar](toobar.png)

1.添加依赖

```
implementation "com.github.runnchild.Feature:toolbar:$latest_version"
```

2.在页面xml中添加PsnToolbar(如果UI为BaseRecyclerWithRefreshBinding，已经使用ViewStub预置好了，不需要这一步)

```
<com.rongc.feature.toolbar.PsnToolbar
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

3.在Activity的onCreate或者Fragment的onViewCreated注册ToolbarAbility

```
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  registerAbility(ToolbarAbility(this) {
      // 更改返回键图标
      toolbarBackDrawable = R.mipmap.common_icon_back.drawable()
      // 返回键显示状态
      // toolbarBackVisible = false
      // 页面标题，默认会使用Activity的Title
      title = "UserRepository"
      // 右上角菜单按钮，可添加多个
      menu {
          // 闭包的this为TextView，可当此菜单为TextView做的相关设置
          text = "more"
          setOnClickListener {
              findNavController().navigate(R.id.demo_dialog)
          }
      }
      // 沉浸且状态栏icon黑色
      // statusBarState = BarConfig.TRANSPARENT_BLACK
      // 沉浸且状态栏icon白色
      // statusBarState = BarConfig.TRANSPARENT_WHITE
      // 设置状态栏颜色 和 statusBarState对立
      statusColor = Color.BLUE
      // 设置toolbar背景颜色
      toolBarBackground = Color.BLUE
      // toolbar底部分割线颜色
      toolBarDividerColor = Color.RED
      // 虚拟导航栏颜色
      navColor = Color.BLUE
  })
}
```

4.另外，也可不注册ToolbarAbility，使用dataBinding在xml中简单绑定(但要自己实现返回点击事件)

```
<com.rongc.feature.toolbar.PsnToolbar
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:backVisible="@{true}"
    app:titleColor="@{@color/title_color}"
    app:title='@{"UserRepository}' />
```