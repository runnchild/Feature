# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-optimizationpasses 5
-verbose
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes SourceFile,LineNumberTable
#不使用大小寫混合，混淆後類名稱為小寫
-dontusemixedcaseclassnames

-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
# 保留 Parcelable 子类中的 CREATOR 字段
-keepclassmembers class **.R$* {
    public static <fields>;
}
# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepclasseswithmembers class com.chad.library.adapter.base.BaseBinderAdapter {
    com.chad.library.adapter.base.BaseBinderAdapter addItemBinder(java.lang.Class,com.chad.library.adapter.base.binder.BaseItemBinder,androidx.recyclerview.widget.DiffUtil$ItemCallback);
}
-keep class * implements java.io.Serializable {
    static final long serialVersionUID;
}
-keep class com.rongc.feature.model.BaseModel
-keep class * extends com.rongc.feature.model.BaseModel
-keep class com.rongc.feature.refresh.BaseRecyclerItemBinder
-keep class * extends com.rongc.feature.refresh.BaseRecyclerItemBinder {}
-keep class * extends androidx.databinding.ViewDataBinding {
    ** mBean;
    ** mBinder;
    ** mUi;
}

# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class androidx.databinding.** { *; }
-keepclasseswithmembers class * extends androidx.databinding.ViewDataBinding {
    <methods>;
}

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

#okio
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*
# Gson
-dontwarn com.google.gson.**
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

-keep class * extends androidx.fragment.app.Fragment {
    public <init>();
}
-keep class * extends android.app.Activity
#-keep class com.rongc.feature.** {
#    public <init>(***);
#    public <methods>;
#    public <fields>;
#    public get*();
#    public set*(***);
#}