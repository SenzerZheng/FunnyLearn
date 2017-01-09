package net.frontdo.funnylearn.app;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.bumptech.glide.Glide;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.api.FrontdoApiService;
import net.frontdo.funnylearn.api.HttpUrls;
import net.frontdo.funnylearn.common.ApplicationInfoHelper;
import net.frontdo.funnylearn.common.DateUtil;
import net.frontdo.funnylearn.common.DeviceHelper;
import net.frontdo.funnylearn.common.StringUtil;
import net.frontdo.funnylearn.common.UIHelper;
import net.frontdo.funnylearn.logger.FrontdoLogger;
import net.frontdo.funnylearn.net.FrontdoApiServiceFactory;
import net.frontdo.funnylearn.ui.entity.Product;
import net.frontdo.funnylearn.ui.entity.UserInfo;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * ProjectName: AppContext
 * Description: 应用程序上下文
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 10/22/2016 15:40
 */
@SuppressWarnings("ALL")
public class AppContext extends MultiDexApplication {

    public static final String TAG = "AppContext";

    public static String sDeviceId = "";
    public static String sIpAddr = "";

    private volatile static AppContext appContext;

    // retrofit
    private FrontdoApiService frontdoApiService;

    private LinkedHashSet<String> jpushTagSet;

    public static boolean showUpdateDlg;

    public static String URL = "";//默认为空

    // 是否登录
    private boolean isLogin;        // true: 已登录，false: 未登录
    private UserInfo mUserInfo;
    private String mMobile = "";
    private int ageScope = AppConstants.AGE_ARE_ALL;

    // 用户最近访问记录
    private List<Product> mPSees;

    // 用户最近访问记录
    private List<Product> mPApks;

    private boolean initLoc; //是否初始定位

    public static String token;// 第三方登录token

    public static AppContext getInstance() {
        if (appContext == null) {
            synchronized (AppContext.class) {
                if (appContext == null) {
                    appContext = new AppContext();
                }
            }
        }
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        sDeviceId = DeviceHelper.getIMEI(this);

        UIHelper.initStandardSize(640, 1136);

        // retrofit start
        // common api service
        frontdoApiService = FrontdoApiServiceFactory.getSpiderApiService(HttpUrls.LOCAL_HOST);

        FrontdoLogger.getLogger().init(this);

        try {
//            JpushHelper.initJpush(this);
//            initEMChat();
//            initLocation();
        } catch (Exception e) {
            FrontdoLogger.getLogger().e(TAG, e.getMessage());
        } finally {
        }

        initAgeScope();
    }

    private void initAgeScope() {
        if (null == gainUserInfo()) {
            return;
        }

        String birthday = gainUserInfo().getMemberBirthday();
        if (StringUtil.checkEmpty(birthday)) {
            return;
        }

        int age = DateUtil.getAge(birthday);
        if (age < AppConstants.AGE_ARE_THREE) {

            age = AppConstants.AGE_ARE_THREE;
        } else if (age <= AppConstants.AGE_ARE_SIX) {

            age = age;
        } else {

            age = AppConstants.AGE_ARE_SIX;
        }
        setAgeScope(age);
    }

    /**
     * 获取基本接口类
     *
     * @return
     */
    public FrontdoApiService getFrontdoApiService() {
        return frontdoApiService;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    /**
     * 退出登录/注销用户
     */
    public void logout() {
        isLogin = false;
        mMobile = null;
        mUserInfo = null;

        AppConfig appConfig = AppConfig.getConfig(this);
        appConfig.clearUserInfo();
    }


    /**
     * 获取极光推送tag
     *
     * @return
     */
    private LinkedHashSet<String> getJpushTags() {

        if (jpushTagSet == null) {

            jpushTagSet = new LinkedHashSet<String>();
            String mac = DeviceHelper.getLocalMacAddress(getApplicationContext());
            jpushTagSet.add(mac);
            jpushTagSet.add(ApplicationInfoHelper.getVersionName(
                    AppContext.this).replace(".", ""));
            jpushTagSet.add(ApplicationInfoHelper
                    .getChannelId(AppContext.this));

        }

        return jpushTagSet;
    }

    /**
     * 设置极光推送别名
     *
     * @param reset 是否重设
     */
    public void setJpushAlias(boolean reset) {

//        boolean hasSet = AppConfig.getConfig(this).hasSetJpushAlias(appContext);

//        if (!hasSet || reset) {
//            // 移除正在执行的别名
//            handler.removeMessages(MSG_SET_ALIAS);
//
//            String mac = DeviceHelper.getLocalMacAddress(getApplicationContext());
//            // 判断是否登录如果登录则将用户名作为别名否则使用mac地址
//            String alias = isLogin() ? getUserId() : mac;
//
//            FrontdoLogger.getLogger().d(TAG, alias);
//
//            handler.sendMessage(handler.obtainMessage(MSG_SET_ALIAS, alias));
//        }

    }

    public int getAgeScope() {
        return ageScope;
    }

    public void setAgeScope(int ageScope) {
        this.ageScope = ageScope;
    }

    /**
     * 获取用户手机号
     *
     * @return
     */
    public String getMobile() {
        if (StringUtil.checkEmpty(mMobile)) {

            gainUserInfo();
            if (null != mUserInfo) {
                mMobile = mUserInfo.getMemberMobile();
            }
        }
        return mMobile;
    }

    /**
     * 获取硬盘缓存用户信息
     *
     * @return
     */
    public UserInfo gainUserInfo() {
        if (null == mUserInfo) {
            mUserInfo = AppConfig.getConfig(this).getUserInfo();
        }
        return mUserInfo;
    }

    /**
     * 保存硬盘缓存用户信息，内存用户信息
     */
    public void saveUserInfo(UserInfo userInfo) {
        if (null == userInfo) {
            FrontdoLogger.getLogger().e(TAG, "[ " + TAG + " - saveUserInfo] userInfo is null!");
            return;
        }

        mUserInfo = userInfo;
        //mMobile = userInfo.getUserId();

        // save to sharePreference
        AppConfig.getConfig(this).saveUserInfo(userInfo);
    }

    /**
     * 是否登录
     *
     * @return
     */
    public boolean isLogin() {
        getMobile();

        isLogin = !TextUtils.isEmpty(mMobile);
        return isLogin;
    }

    /**
     * 获取硬盘缓存用户最近访问
     *
     * @return
     */
    public List<Product> gainSees() {
        if (null == mPSees) {
            mPSees = AppConfig.getConfig(this).getSees();
        }
        return mPSees;
    }

    /**
     * 保存硬盘缓存用户信息，内存用户最近访问
     */
    public void saveSees(List<Product> products) {
        if (null == products) {
            FrontdoLogger.getLogger().e(TAG, "[ " + TAG + " - saveSees] products is null!");
            return;
        }

        mPSees = products;
        //mMobile = userInfo.getUserId();

        // save to sharePreference
        AppConfig.getConfig(this).saveSees(products);
    }

    /**
     * 获取硬盘缓存，用户下载APK记录
     *
     * @return
     */
    public List<Product> gainDLogs() {
        if (null == mPApks) {
            mPApks = AppConfig.getConfig(this).getDLogs();
        }
        return mPApks;
    }

    /**
     * 保存硬盘缓存，用户下载APK记录
     */
    public void saveDLogs(List<Product> products) {
        if (null == products) {
            FrontdoLogger.getLogger().e(TAG, "[ " + TAG + " - saveDLogs] products is null!");
            return;
        }

        mPApks = products;

        // save to sharePreference
        AppConfig.getConfig(this).saveDLogs(products);
    }

    /**
     * 检查用户信息是否有效
     *
     * @param userInfo
     * @return
     */
//    public boolean checkUserValid(MainInfo userInfo) {
//        boolean valid = false;
//        if (null != userInfo) {
//            if (!TextUtils.isEmpty(userInfo.getUserId())
//                    && !TextUtils.isEmpty(userInfo.getUserName())) {
//                valid = true;
//            }
//        }
//        return valid;
//    }


    /**
     * 跳转动画
     *
     * @param context
     */
    public static void overridePendingTransition(Context context) {
        if (context != null && context instanceof Activity) {
            ((Activity) context).overridePendingTransition(
                    R.anim.slid_in_right, R.anim.slid_out_left);
        }
    }
}