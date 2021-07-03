# Ability

为优化项目中可能存在的多种不同类型页面（Activity/Fragment/Dialog）需要相似功能（如列表刷新分页功能）
而造成的需要编写重复代码和可能引起的多级继承造成可读性差和维护成本高问题，比如list中例举的[几个问题](../../list/readme.md)

本库尝试使用横向扩展的方式优化这些问题，其实方法很简单，利用lifecycle向Activity/Fragment中注册生命周期监听，并在对应生命周期 方法执行时做相关操作。

IAbility继承DefaultLifecycleObserver，DefaultLifecycleObserver包含基本的生命周期方法，注册后将会在对应时机被调用。

```
// 此时的IAbility并没有其他实现, 只是为扩展预留
interface IAbility : DefaultLifecycleObserver
```

然后编写扩展能力,实现IAbility，并在相应的生命周期方法中实现需要的业务。最后在页面onCreate中注册就行了。

以BindingAbility为例，期望在页面创建后自动使用DataBinding/ViewBinding绑定UI，并在UI销毁时自动解除绑定;

1. 新建BindingAbility实现IAbility

```
/**
 * 自动创建ViewBinding/DataBinding的能力
 * 如果layout中定义了viewModel属性，则会自动为他赋值
 */
class BindingAbility<B : ViewBinding> : IAbility {
   var mBinding: B? = null

    fun onCreateImmediately(
        host: IHost<*>, inflater: LayoutInflater, container: ViewGroup? = null
    ) {
        // 根据泛型B创建实例
        val binding = binding(host, inflater, container)
        if (binding is ViewDataBinding) {
            binding.lifecycleOwner = host.lifecycleOwner
            try {
                binding::class.java.superclass?.getDeclaredField("mViewModel")
                // 如果xml中定义了名为viewModel属性, 为他赋值
                binding.setVariable(BR.viewModel, host.viewModel)
            } catch (e: Exception) {
            }
        }
        mBinding = binding
    }
    
   /**
     * 页面销毁或者Fragment#onDestroyView执行时解除绑定
     */
    override fun onDestroy(owner: LifecycleOwner) {
        val binding = mBinding
        if (binding is ViewDataBinding) {
            binding.unbind()
        }
        mBinding = null
    }    
    
}
```

2.在页面onCreate中注册BindingAbility，就阔以了。BindingAbility将会在页面onCreate时根据泛型添加绑定，
并在销毁或者Fragment#onDestroyView执行时解除绑定

```
   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        bindingAbility = BindingAbility()
        registerAbility(bindingAbility)
        // BindingAbility比较特殊，需要在onCreate方法中绑定成功，而此时Lifecycle并未开始分发，因此这里手动调用
        bindingAbility.onCreateImmediately(this, inflater, container)
        return mBinding.root
    }
``` 

由于ViewBinding是基础功能，所以已在Base中加入，除此之外本库还提供了其他几中Ability以供不同情景下使用：

1. [ListAbility/PagerAbility](../../list/readme.md)：带刷新和分页加载、空数据时显示空页面的列表/ViewPager2能力;
2. [AbsProgressAbility](): 页面在耗时操作时显示进度弹窗，完成后关闭弹窗;
3. [ToolbarAbility](../../toolbar/readme.md)：让页面拥有简约Toolbar的能力;
4. 其他可自行扩展