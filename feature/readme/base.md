# Base

## 一、简介

Base部分主要包含Activity、Fragment、DialogFragment三种页面页面类型做基本封装，包括ViewModel的创建、ViewBinding的绑定、Ability的维护等。
除此之外没有做更多的事情，保证开发的原汁原味。

先看看页面的基本使用：

1. 创建Fragment/Activity/DialogFragment继承BaseFragment/Activity/DialogFragment，以Fragment为例：

```
class WanHomeFragment : BaseFragment<FragmentWanHomeBinding, WanHomeViewModel>() {
}
```

2. 创建ViewModel继承BaseViewModel

```
class WanHomeViewModel : BaseViewModel() {
}
```

3. ViewModel和FragmentWanHomeBinding都在内部创建好了，剩下只需要关注自己业务的实现了。

---

## 二、进阶

> 1. ViewModel的创建

BaseFragment定义了BaseViewModel的泛型, 页面的生命周期内只会被创建一次，默认使用SavedStateViewModelFactory创建对象。
如果ViewModel需要访问Fragment参数，可以使用SavedState模块，SavedStateHandle 允许 ViewModel 访问相关 Fragment 或 Activity 的已保存状态和参数。
使用方法为在ViewModel添加SavedStateHandle的构造就可以，SavedStateViewModelFactory在创建时会为他赋值。否则使用默认无参构造就行。

```
class HomeViewModel(private val savedStateHandle: SavedStateHandle) : BaseViewModel(savedStateHandle) {

    val count: Int
        get() = savedStateHandle["savedField"]
}
```

如果页面想以别的姿势提供ViewModel，可以在页面中重写viewModelCreator(Class<*>)并返回ViewModel

```
    /**
     * 以自己期望的方式创建ViewModel
     */
    override fun viewModelCreator(cls: Class<RepoSearchViewModel>): RepoSearchViewModel {
        return RepoSearchViewModel(RepoServiceProvider.repoRepository)
    }
```

> 2. ViewBinding的创建

BaseFragment定义了ViewBinding的泛型，在创建好页面的布局文件xml后编译器就会自动生成对应的ViewBinding类，前提是在模块的build.gradle添加了ViewBinding和DataBinding支持,（这也是使用本库的前提）。

```
android {
     buildFeatures {
        viewBinding true
        dataBinding true
    }
}
```

ViewBinding对象会通过BaseFragment内部已经集成的BindingAbility通过创建并绑定Fragment的生命周期，[这是关于Ability的介绍](ability.md)，
， 如果你认为反射的方式并不适合你，可以重写bindView(LayoutInflater,ViewGroup)
方法手动创建。ViewBinding将会在onCreateView时创建，并在View销毁的时候解绑。 如果在xml中有定义名称为viewModel的属性申明，ViewBinding会自动为它赋值。

```
    <data>
        <variable
            name="viewModel"
            type="com.rongc.wan.ui.WanHomeViewModel" />
    </data>
```

因此在onCreateView方法后就可以使用mBinding变量操作控件了。
