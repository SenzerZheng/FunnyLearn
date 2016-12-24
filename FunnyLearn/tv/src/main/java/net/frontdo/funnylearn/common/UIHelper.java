/*
 * UIHelper.java
 * classes : com.spider.subscriber.app.UIHelper
 * @author liujun
 * V 1.0.0
 * Create at 2015-6-2 上午10:53:32
 * Copyright (c) 2015年  Spider. All Rights Reserved.
 */
package net.frontdo.funnylearn.common;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * @author LiuJun
 * @ClassName: UIHelper
 * @Description: ui帮助类
 * @date 2015-6-2 上午10:53:32
 */
public class UIHelper {
    private static final String TAG = "UIHelper";
    public static int standardWidth;
    public static int standardHeight;
    private static Rect screenSize;

    /**
     * 判断是否支持状态栏透明
     *
     * @return
     */
    public static boolean isSupportTranslucentStatusBar() {

        return Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 调整导航栏高度
     *
     * @param context
     * @param naviBar
     * @param contentHeight 导航栏内容高度
     */
    public static void resizeNaviBar(Context context, View naviBar, int contentHeight) {
        if (naviBar != null) {

            int statusHeight = UIHelper.getStatusBarHeight(context);

            if (UIHelper.isSupportTranslucentStatusBar()) {

                naviBar.setPadding(0, statusHeight, 0, 0);

                contentHeight += statusHeight;
            }

            naviBar.getLayoutParams().height = (int) (contentHeight + TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 0.5f, context.getResources().getDisplayMetrics()));
        }
    }

    /**
     * 调整导航栏高度（优化版）
     *
     * @param context
     * @param naviBar
     * @param contentHeight 导航栏内容高度
     */
    public static void resizeNaviBarOptimize(Context context, View naviBar, int contentHeight) {
        if (naviBar != null) {

            int statusHeight = UIHelper.getStatusBarHeight(context);
            if (UIHelper.isSupportTranslucentStatusBar()) {
                int paddingLeft = naviBar.getPaddingLeft();
                int paddingRight = naviBar.getPaddingRight();
                int paddingBottom = naviBar.getPaddingBottom();

                naviBar.setPadding(paddingLeft, statusHeight, paddingRight, paddingBottom);
                contentHeight += statusHeight;
            }

            naviBar.getLayoutParams().height = (int) (contentHeight +
                    TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 0.5f,
                            context.getResources().getDisplayMetrics()));
        }
    }

    public static int getNaviBarExtraHeight(Context context) {
        if (UIHelper.isSupportTranslucentStatusBar()) {
            return UIHelper.getStatusBarHeight(context);
        }
        return 0;
    }

    /**
     * 获取屏幕尺寸
     *
     * @param context
     * @return
     */
    public static Rect getScreenSize(Context context) {
        if (screenSize == null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            screenSize = new Rect();
            screenSize.set(0, 0, display.getWidth(), display.getHeight());
        }
        return screenSize;
    }

    /**
     * 获取缩放的尺寸
     *
     * @param context
     * @param standardSize 标准尺寸
     * @param width        宽度
     * @param height       高度
     * @return
     */
    public static Rect getScaledRect(Context context, int standardSize, int width, int height) {
        Rect screenSize = getScreenSize(context);
        Rect scaledRect = new Rect();
        float scale = screenSize.width() * 1.0f / standardSize;
        int dstWidth = (int) (scale * width);
        int dstHeight = (int) (height * 1.0f / width * dstWidth);
        scaledRect.set(0, 0, dstWidth, dstHeight);
        Log.d("uihelper", screenSize + "/n" + scaledRect);
        return scaledRect;
    }

    /**
     * 获取等比例缩放尺寸
     *
     * @param standardSize
     * @param screenSize
     * @param size
     * @return
     */
    public static int getScaledSize(int standardSize, int screenSize, int size) {
        float scale = screenSize * 1.0f / standardSize;
        return (int) (scale * size);
    }

    /**
     * 获取屏幕宽度等比例缩放的尺寸
     *
     * @param context
     * @param size
     * @return
     */
    public static int getScaledWidthRatioSize(Context context, int size) {
        return getScaledSize(standardWidth, getScreenSize(context).width(), size);
    }

    /**
     * 获取屏幕高度等比例缩放的尺寸
     *
     * @param context
     * @param size
     * @return
     */
    public static int getScaledHeightRatioSize(Context context, int size) {
        return getScaledSize(standardHeight, getScreenSize(context).height(), size);
    }

    /**
     * 初始化标准尺寸
     *
     * @param width
     * @param height
     */
    public static void initStandardSize(int width, int height) {
        standardWidth = width;
        standardHeight = height;
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


}
