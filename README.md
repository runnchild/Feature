# Feature [![](https://jitpack.io/v/runnchild/Feature.svg)](https://jitpack.io/#runnchild/Feature)

## 一、简介

一个非常简洁、轻量的MVVM框架。拒绝复杂的、冗余的、过度的Base设计，不为封装而封装，让开发回归他应有的样子。

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
+ 5.DataBinding/ViewBinding；人生苦短，用它！少写一半代码。
+ 6.事件总线；LiveEventBus。

## 二、目录

**1. Feature**

+ [Base](https://github.com/runnchild/Feature/wiki/Base)：页面基本使用
+ [Ability](https://github.com/runnchild/Feature/wiki/Ability)：横向扩展能力
+ [Http](https://github.com/runnchild/Feature/wiki/Http)：网络配置
+ [Repository](https://github.com/runnchild/Feature/wiki/Repository)：数据存取方案
+ [Status](https://github.com/runnchild/Feature/wiki/Status)：公开的网络状态

**2. List**

+ [List/PagerAbility](https://github.com/runnchild/Feature/wiki/ListAbility-PagerAbility)：列表扩展能力
+ [EmptyView](https://github.com/runnchild/Feature/wiki/EmptyView)：无内容占位页面

**3. Toolbar**

+ [PsnToolbarAbility](https://github.com/runnchild/Feature/wiki/PsnToolbar)：简约Toolbar扩展能力

**4. LiveEvent**

+ [liveBus](https://github.com/runnchild/Feature/wiki/LiveEvent)：事件总线

**5. 其他**

+ [Extentsion](https://github.com/runnchild/Feature/wiki/Binding)：基础扩展方法

## 三、引用

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
    def latest_version = "3.0.1"
    // core
    implementation "com.github.runnchild.Feature:feature:$latest_version"
    
    // RecyclerView/ViewPager2扩展能力，可选
    implementation "com.github.runnchild.Feature:list:$latest_version" 
    // 页面空内容占位页面，list库已包含
    implementation "com.github.runnchild.Feature:emptyview:$latest_version" 
    
    // 简约Toolbar扩展能力，可选
    implementation "com.github.runnchild.Feature:toolbar:$latest_version"
    
    // 低版本adapter兼容库，可选
    implementation "com.github.runnchild.Feature:adaptercompat:$latest_version"
    
    // LiveEventBus，可选
    implementation "com.github.runnchild.Feature:liveevent:$latest_version"
}
```

## 四、文章


## 五、鸣谢

1.[BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

2.[SmartRefresh](https://github.com/scwang90/SmartRefreshLayout)

3.[AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)
