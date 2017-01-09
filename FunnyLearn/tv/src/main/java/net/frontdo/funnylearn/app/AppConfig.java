/*
 * AppConfig.java
 * classes : com.spider.subscriber.app.AppConfig
 * @author liujun
 * V 1.0.0
 * Create at 2015-1-19 下午1:07:21
 * Copyright (c) 2015年  Spider. All Rights Reserved.
 */
package net.frontdo.funnylearn.app;

import android.content.Context;

import net.frontdo.funnylearn.common.SharedPrefsUtils;
import net.frontdo.funnylearn.ui.entity.Product;
import net.frontdo.funnylearn.ui.entity.UserInfo;

import java.util.List;

/**
 * ProjectName: AppConfig
 * Description: 保存应用的配置信息
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 2016/8/17 12:02
 */
public class AppConfig {
    private static final String TAG = "AppConfig";

    private static AppConfig appConfig;
    private Context context;


    /**
     * 获取实例
     *
     * @param context
     * @return
     */
    public static AppConfig getConfig(Context context) {
        synchronized (AppConfig.class) {
            if (null == appConfig) {
                appConfig = new AppConfig(context);
            }
        }
        return appConfig;
    }

    private AppConfig(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * 保存用户信息
     *
     * @param userInfo
     */
    public void saveUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            SharedPrefsUtils sharedPrefs = new SharedPrefsUtils(context, SharedPrefsUtils.SP_FILE_NAME_USER);
            sharedPrefs.setObject(SharedPrefsUtils.SP_KEY_USER_INFO, userInfo);
        }
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public UserInfo getUserInfo() {
        SharedPrefsUtils sharedPrefs = new SharedPrefsUtils(context, SharedPrefsUtils.SP_FILE_NAME_USER);
        UserInfo user = sharedPrefs.getObject(SharedPrefsUtils.SP_KEY_USER_INFO, UserInfo.class);
        return user;
    }

    /**
     * 清除用户信息
     */
    public void clearUserInfo() {
        SharedPrefsUtils sharedPrefs = new SharedPrefsUtils(context, SharedPrefsUtils.SP_FILE_NAME_USER);
        sharedPrefs.clearSharedPreferences();
    }

    /**
     * 保存用户最近访问
     *
     * @param products
     */
    public void saveSees(List<Product> products) {
        if (products != null) {
            SharedPrefsUtils sharedPrefs = new SharedPrefsUtils(context, SharedPrefsUtils.SP_FILE_NAME_SEES);
            sharedPrefs.setObject(SharedPrefsUtils.SP_KEY_USER_SEES, products);
        }
    }

    /**
     * 获取用户最近访问
     *
     * @return
     */
    public List<Product> getSees() {
        SharedPrefsUtils sharedPrefs = new SharedPrefsUtils(context, SharedPrefsUtils.SP_FILE_NAME_SEES);
        List<Product> products = sharedPrefs.getObject(SharedPrefsUtils.SP_KEY_USER_SEES, List.class);
        return products;
    }

    /**
     * 清除用户最近访问
     */
    public void clearSees() {
        SharedPrefsUtils sharedPrefs = new SharedPrefsUtils(context, SharedPrefsUtils.SP_FILE_NAME_SEES);
        sharedPrefs.clearSharedPreferences();
    }

    /**
     * 保存用户下载APK记录文件名
     *
     * @param products
     */
    public void saveDLogs(List<Product> products) {
        if (products != null) {
            SharedPrefsUtils sharedPrefs = new SharedPrefsUtils(context, SharedPrefsUtils.SP_FILE_NAME_DOWNED_LOGS);
            sharedPrefs.setObject(SharedPrefsUtils.SP_KEY_DOWNED_LOGS, products);
        }
    }

    /**
     * 获取用户下载APK记录文件名
     *
     * @return
     */
    public List<Product> getDLogs() {
        SharedPrefsUtils sharedPrefs = new SharedPrefsUtils(context, SharedPrefsUtils.SP_FILE_NAME_DOWNED_LOGS);
        List<Product> products = sharedPrefs.getObject(SharedPrefsUtils.SP_KEY_DOWNED_LOGS, List.class);
        return products;
    }

    /**
     * 清除用户下载APK记录文件名
     */
    public void clearDLogs() {
        SharedPrefsUtils sharedPrefs = new SharedPrefsUtils(context, SharedPrefsUtils.SP_FILE_NAME_DOWNED_LOGS);
        sharedPrefs.clearSharedPreferences();
    }
}
