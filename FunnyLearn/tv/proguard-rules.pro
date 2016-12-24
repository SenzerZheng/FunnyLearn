# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\adt-bundle-windows-x86_64-20140702\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.widget.BaseAdapter
-keep public class * extends android.widget.CusorAdapter
-keep public class * extends android.app.Fragment

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep public class com.spider.subscriber.R$*{
    public static final int *;
}

-keep class **.R$* {*;}



#同样如果你觉得麻烦，就直接将BaseAdpater换成Adapter
#数据模型不要混淆
-keepnames class * implements java.io.Serializable
 #比如我们要向activity传递对象使用了Serializable接口的时候，这时候这个类及类里面#的所有内容都不能混淆
-keepclassmembers class * implements java.io.Serializable {*;}

-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**



-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**

-keep class com.facebook.**
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**

-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}

-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep public class [your_pkg].R$*{
    public static final int *;
}
-dontwarn android.support.**
-dontwarn com.google.android.maps.**
-dontwarn com.slidingmenu.lib.app.SlidingMapActivity
-keep class android.support.** { *; }
-keep class com.slidingmenu.** { *; }
-keep interface com.slidingmenu.** { *; }
#-keepnames class * implements java.io.Serializable

-keep public class * implements java.io.Serializable {
    public *;
}

-keepattributes *JavascriptInterface*
-dontwarn com.alibaba.fastjson.**
#-libraryjars libs/fastjson-1.2.1.jar
-keep class com.alibaba.fastjson.** { *; }

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }



-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep class com.tencent.android.tpush.**  {* ;}
-keep class com.tencent.mid.**  {* ;}



# libs/UPPayAssistEX.jar
#-libraryjars libs/UPPayPluginExPro.jar
-keep class com.unionpay.** {* ;}
-dontwarn com.unionpay.mobile.android.pboctransaction.**
-keep  public class com.unionpay.uppay.net.HttpConnection {
	public <methods>;
}
-keep  public class com.unionpay.uppay.net.HttpParameters {
	public <methods>;
}
-keep  public class com.unionpay.uppay.model.BankCardInfo {
	public <methods>;
}
-keep  public class com.unionpay.uppay.model.PAAInfo {
	public <methods>;
}
-keep  public class com.unionpay.uppay.model.ResponseInfo {
	public <methods>;
}
-keep  public class com.unionpay.uppay.model.PurchaseInfo {
	public <methods>;
}
-keep  public class com.unionpay.uppay.util.DeviceInfo {
	public <methods>;
}

-keep  public class com.unionpay.uppay.util.PayEngine {
	public <methods>;
	native <methods>;
}

-dontwarn com.alipay.apmobilesecuritysdk.**
-dontwarn com.aplipay.**
-dontwarn com.unionpay.**
-dontwarn okio.**
-dontwarn retrofit.**
-keep class com.squareup.okhttp.**{*;}
-dontwarn com.squareup.okhttp.**
-keep class com.mato.** { *; }
-dontwarn com.mato.**
-keep class com.handmark.** { *; }
-keep interface com.handmark.** { *; }

# 听云混淆配置ProGuard configurations for NetworkBench Lens
-keep class com.networkbench.** { *; }
-dontwarn com.networkbench.**
-keepattributes Exceptions, Signature, InnerClasses
-keepattributes SourceFile,LineNumberTable
# End NetworkBench Lens

#环信混淆配置
-keep class com.easemob.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.apache.** {*;}
-dontwarn  com.easemob.**


#极光推送混淆配置
-dontoptimize
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-dontwarn com.google.**
-keep class com.google.protobuf.** {*;}


#ButterKnife混淆配置
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#rxjava混淆配置
-dontwarn rx.internal.util.unsafe.*
-keep class rx.internal.util.unsafe.** {*;}

-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-dontwarn android.support.v7.**
-keep class android.support.v7.app.** { *; }

-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }

-keep public class android.util.** {*;}
-dontwarn android.util.**
-keep class android.net.SSLCertificateSocketFactory {*;}
-dontwarn android.net.**
-keep class android.app.Notification {*;}
-dontwarn android.app.**
-dontskipnonpubliclibraryclassmembers

#gson
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }

# webview + js
-keepattributes *JavascriptInterface*
# keep 使用 webview 的类
-keepclassmembers class  com.spider.subscriber.WebViewActivity {public *;}
# keep 使用 webview 的类的所有的内部类
-keepclassmembers  class  com.spider.subscriber.WebViewActivity$*{*;}

#上面这个是避免混淆你导入的maven类库之类的
-keep class com.github.** { *; }

-dontwarn
-ignorewarning
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt

#支付宝
-keep class com.alipay.** { *; }
-keep class com.ta.utdid2.**{*;}
-keep class com.ut.device.**{*;}
-keep class org.json.alipay.**{*;}

#百度地图
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}
-keep class com.google.gson.**{*;}
-keep class com.android.volley.**{*;}

#TalkingData
-keep class com.apptalkingdata.** { *; }
-keep class com.talkingdata.sdk.**{*;}
-keep class com.tendcloud.tenddata.**{*;}

#银联
-keep class com.unionpay.** { *; }
-keep class com.UCMobile.PayPlugin.** { *; }
-keep class cn.gov.pbc.tsm.client.mobile.android.bank.service.** { *; }
