package net.frontdo.funnylearn.api;

import java.util.HashMap;
import java.util.Map;

/**
 * ProjectName: ParamsHelper
 * Description: 参数 帮助类
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 2016/8/16 19:12
 */
@SuppressWarnings("ALL")
public class ParamsHelper {
    public static final String TAG = "ParamsHelper";

    // 公共参数值（VALUE）
    public static final String FILE_TYPE_JSON = "json";
    public static final String KEY_READER = "spiderReader";                         // 蜘蛛精公钥，商户号
    public static final String PLATFORM = "android_reader";
    public static final String PSOURCE_VALUE = "readeapp";
    public static final String SOURCE = "reader";
    public static final String VERSION = "3.2.0";                                   // 服务器版本

    // 字段名（KEY），如有疑问见《蜘蛛精接口文档4.0》
    public static final String VERSION_APP = "versionApp";
    public static final String SIGN = "sign";
    public static final String FILE_TYPE = "fileType";
    public static final String KEY = "key";
    public static final String PLATFORM_ID = "platformId";
    public static final String PSOURCE_KEY = "psource";
    public static final String NET_PAY_AMOUNT = "netpayamount";
    public static final String RANDOM_CODE = "randomCode";              // 使用uuid生成
    public static final String IMUSER_FLAG = "imuserFlag";              // 是否需要用户环信信息
    public static final String IMUSER_FLAG_YES = "1";                   // 需要返回环信信息
    public static final String IMUSER_FLAG_NO = "0";                    // 不需要返回环信信息
    public static final String TIME_STAMP = "timeStamp";
    public static final String USER_NAME = "userName";
    public static final String USER_NAME_SMALL = "username";
    public static final String PASSWORD = "password";
    public static final String MOBILE = "mobile";
    public static final String MOBILE_NEW = "newMobile";

    public static final String IS_DEPOSIT = "isDeposit";                // 是否是存储的（充值）
    public static final String IS_DEPOSIT_YES = "true";                 // 是充值

    public static final String BUSINESS_TYPE = "businesstype";          // 验证码类型: regist|modifypd|login|forgetpd|bindPhone
    public static final String BUSI_TYPE_REGISTER = "regist";           // 注册
    public static final String BUSI_TYPE_MODIFY_PD = "modifypd";        // 修改密码
    public static final String BUSI_TYPE_FAST_LOGIN = "login";          // 登录
    public static final String BUSI_TYPE_RETRIEVE_PWD = "forgetpd";     // 忘记密码（找回密码）
    public static final String BUSI_TYPE_BIND_MOBILE = "bindPhone";     // 绑定手机号
    public static final String BUSI_TYPE_MODIFY_PHONE = "modifyPhone";  // 修改手机号
    public static final String BUSI_TYPE_SET_PAYPSD = "forgetPayPsd";   // 修改手机号

    public static final String IS_NEED_IMG_VCODE = "needImageCode";     // 是否需要验证‘图片验证码’（0：不需要，1：需要）
    public static final String IS_NEED_IMG_VCODE_NO = "0";
    public static final String IS_NEED_IMG_VCODE_YES = "1";
    public static final String VERIFYCODE = "verifyCode";
    public static final String VERIFYCODE_NEW = "newVerifyCode";
    public static final String VERIFY_TYPE = "verifyType";              // 需校验的验证码类型（SMS：短信验证码  Img：图片验证码）
    public static final String VERIFY_TYPE_SMS = "SMS";                 // SMS：短信验证码
    public static final String VERIFY_TYPE_IMG = "Img";                 // Img：图片验证码
    public static final String OLDPASSWORD = "oldPassword";
    public static final String NEWPASSWORD = "newPassword";
    public static final String DEVICEID = "deviceId";
    public static final String USER_IP = "userIp";
    public static final String USER_ID = "userId";                      // 用户编号
    public static final String SCREEN_WIDTH = "width";                  // 手机屏幕尺寸宽度，用于后台拼接下载链接地址
    public static final String ORDER_ID = "orderId";                    // 订单编号

    public static final String SPIDER_TOKEN = "spiderToken";
    public static final String ALIAS = "alias";
    public static final String SEX = "sex";
    public static final String INTRODUCTION = "introduction";

    private static String sUUID = "";
    private static String sTimeStamp = "";


    // 趣味乐园，相关字段与内容
    public static final String GET_CODE_ACTION_REG = "register";        //  获取注册验证码
    public static final String GET_CODE_ACTION_RESET = "reset";         //  获取忘记密码验证码

    public static final String TYPE_FAV = "0";                          //  收藏类型
    public static final String TYPE_APP = "1";                          //  应用类型
    public static final String MY_FAV_ADD = "save";                     //  添加到我的收藏列表
    public static final String MY_FAV_CANCEL = "update";                //  取消从我的收藏列表
    public static final String MY_APP_ADD = "save";                     //  添加到我的应用列表
    public static final String MY_APP_CANCEL = "update";                //  取消从我的应用列表

    public static final String USER_INFO_RESET_PWD = "update";          //  重置密码
    public static final String USER_INFO_EDIT = "edit";                 //  编辑用户信息

    /**
     * 初始化共同Map
     *
     * @param common
     */
    private static void initCommonMap(Map<String, String> common) {

        common.put("key", KEY_READER);                                          // 商户号

//        common.put("fileType", FILE_TYPE);                                    // 返回数据类型
        common.put("platform", PLATFORM);                                       // 终端，iPhone_film/android_reader/web/wap

        common.put("source", SOURCE);                                           // 来源，film，reader，subscriber
        common.put("version", VERSION);                                         // 版本，服务器当前版本 例如：v3.2.0

        sTimeStamp = String.valueOf(System.currentTimeMillis());
        common.put(TIME_STAMP, sTimeStamp);                                     // 时间戳

        common.put(RANDOM_CODE, sUUID);                                          // sUUID
    }

    // ---------------------- 接口文档 - 请求参数 ------------------

    /**
     * 用户登录Map
     *
     * @param loginParams
     * @return
     */
    public static Map<String, String> userLoginMap() {
        Map<String, String> map = new HashMap<>();
        initCommonMap(map);

//        map.put(USER_NAME, loginParams.getUsername());
//        map.put(PASSWORD, getBase64Str(loginParams.getPassword()));
//        map.put(IS_NEED_IMG_VCODE, loginParams.getIsNeedVerify());
//        map.put(VERIFYCODE, loginParams.getImgVerifyCode());
//        map.put(IMUSER_FLAG, loginParams.getIsNeedIMUserId());
//
//        map.put(DEVICEID, AppContext.sDeviceId);
//        map.put(USER_IP, AppContext.sIpAddr);

        map.put(SIGN, "");
        return map;
    }

    /**
     * 用户注册Map
     *
     * @param mobile
     * @param businessType 验证码类型（regist|modifypd|login|forgetpd|bindPhone） - （已弃用）
     * @param verifyCode   验证码
     * @param newPassword  新密码
     * @return
     */
    public static Map<String, String> userRegisterMap(
            String mobile, String businessType, String verifyCode, String newPassword) {

        Map<String, String> map = new HashMap<>();
        initCommonMap(map);

        // consistent with the account system
        map.put(USER_NAME, mobile);
        map.put(PASSWORD, newPassword);
        map.put(VERIFYCODE, verifyCode);
//        map.put(BUSINESS_TYPE, businessType);

        map.put(SIGN, "");
        return map;
    }
}