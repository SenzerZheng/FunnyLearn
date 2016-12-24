/*
 * SSResultHelper.java
 * classes : com.spider.subscriber.util.SSResultHelper
 * @author liujun
 * V 1.0.0
 * Create at 2015-1-12 下午3:45:03
 * Copyright (c) 2015年  Spider. All Rights Reserved.
 */
package net.frontdo.funnylearn.net;


import net.frontdo.funnylearn.common.StringUtil;
import net.frontdo.funnylearn.logger.FrontdoLogger;

import retrofit.Response;

/**
 * ProjectName: HttpUrls
 * Description: 请求结果帮助类
 * <p>
 * 状态码: code, msg
 * 0=成功
 * 801=用户不存在
 * 802=没数据
 * 803=该用户已被注册
 * 804=请求参数有错
 * 805=设置布局时，产品不存在
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 2016/5/19 17:55
 */
public class FrontdoResultHelper {
    private static final String TAG = "FrontdoResultHelper";

    public static final String KEY_CODE = "code";
    public static final String KEY_MSG = "msg";

    private static final String CODE_SUC = "0";

    /**
     * 检查网络请求结果是否是成功
     *
     * @param result
     * @return 是否成功
     */
    public static boolean isSuccess(Response result) {
        String code = result.headers().get(KEY_CODE);
        return StringUtil.checkEmpty(code) ? false : (CODE_SUC.equals(code));
    }

    /**
     * 获取后台返回的Message信息
     *
     * @param result
     * @return
     */
    public static String getMessage(Response result) {
        String msg = result.headers().get(KEY_MSG);
        return StringUtil.checkEmpty(msg) ? "" : msg;
    }

    /**
     * 检查网络请求结果是否是成功
     *
     * @param result
     * @return 是否成功
     */
    public static boolean isSuccess(org.json.JSONObject result) {
        if (result == null) {
            return false;
        }

        String resultCode = null;
        try {

            resultCode = result.getString("result");
        } catch (Exception e) {

            FrontdoLogger.getLogger().e(TAG, e.getMessage());
        }

        return "0".equals(resultCode);
    }
}
