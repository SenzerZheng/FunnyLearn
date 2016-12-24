package net.frontdo.funnylearn.api;


import android.support.annotation.NonNull;

import net.frontdo.funnylearn.BuildConfig;

/**
 * ProjectName: HttpUrls
 * Description: 网路请求的URL
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 2016/5/19 17:55
 */
@SuppressWarnings("ALL")
public class HttpUrls {
    /** BUILD_TYPE */

    /** debug for testing */
    public static final String BT_DEBUG = "debug";

    /** debug for releasing */
    public static final String BT_DEBUG_REL = "debugRel";

    /** ver: alpha */
    public static final String BT_ALPHA = "alpha";

    /** ver: release & online */
    public static final String BT_RELEASE = "release";

    // ------------------ 基类接口地址 -----------------
    /**
     * 常规请求，服务器基本地址
     */
    public static final String LOCAL_HOST = getLocalHost();

    @NonNull
    private static final String getLocalHost() {
        if (BT_RELEASE.equalsIgnoreCase(BuildConfig.BUILD_TYPE)) {              // release(生产), forbid the log and request
            return "http://118.178.124.197:8080/eden/";
        } else if (BT_DEBUG_REL.equalsIgnoreCase(BuildConfig.BUILD_TYPE)) {     // debugRelease(for viewBug)
            return "http://118.178.124.197:8080/eden/";
        } else if (BT_ALPHA.equalsIgnoreCase(BuildConfig.BUILD_TYPE)) {         // alpha(preRelease)
            return "http://118.178.124.197:8080/eden/";
        }

        return "http://118.178.124.197:8080/eden/";                             // debug(developer)
    }

    // ------------------ 接口名 -----------------
    // ----- 登录注册 -----
    /** 用户注册 */
    public static final String USER_REGISTER = "membs/save";

    /** 常规用户登录 */
    public static final String USER_LOGIN = "membs/login";

    /** 未登录时获取 主页 信息 */
    public static final String USER_UNLOGIN = "membs/unlogin";

    /** Other分类 others = 分类的ID */
    public static final String CATEGORY_LIST = "prods/category/list/{id}/{aScope}";

    /** Latest publishs（上架） */
    public static final String LATEST_PUBLISHS = "prods/latest/publishs";

    /** reset密码 或 编辑个人信息；status = update 重置密码 或者 edit编辑信息 */
    public static final String REST_PWD = "membs/{status}";

    /** 短信接口（注册/重置）action = register/reset；mobile = 手机号 */
    public static final String GET_CODE = "membs/sms/{action}/{mobile}";

    /** 我的信息：版本，账号 mobile = 手机号 */
    public static final String MY_INFO = "sys/myinfo/{mobile}";

    /** 收藏/应用（添加或取消） type = 0（收藏）或者1（应用） status = save/update */
    public static final String OPER_FAV_OR_APP = "collect/{type}/{status}";

    /** 收藏/应用（列表获取） type = 0（收藏）或者1（应用） mobile = 手机号 */
    public static final String FAV_OR_APP_LIST = "collect/{type}/{mobile}/lists";

    /** 查询产品详情，id = 产品ID */
    public static final String PRODUCT_DETAILS = "prods/{id}";

    /** 查询产品详情，收藏状态 id = 产品ID， mobile = 手机号 */
    public static final String PRODUCT_DETAILS_FAV_STATE = "collect/{mobile}/{id}";
}
