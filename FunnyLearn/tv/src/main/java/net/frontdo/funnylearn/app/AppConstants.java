package net.frontdo.funnylearn.app;

/**
 * ProjectName: AppConstants
 * Description: 常量类
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 2016/8/30 10:19
 */
public class AppConstants {
    /*********************
     * request code: activity
     *********************/
    /**
     * 请求登录
     */
    public static final int REQ_CODE_GO_LOGIN = 0x1001;

    /*********************
     * request code: permission
     *********************/
    /**
     * 拍照请求权限
     */
    public static final int REQ_CODE_PM_TAKE_PHOTO = 0x2001;


    /****************************************************
     * 			        Intent Key				    	*
     ****************************************************/
    /**
     * 列表类型（我的收藏，最新上架，最近访问）
     */
    public static final String IKEY_DATE_LIST_TYPE = "listType";
    public static final int IVALUE_LIST_FAV = 0x0001;                       // 我的收藏
    public static final int IVALUE_LIST_LATEST = 0x0002;                    // 最新上架
    public static final int IVALUE_LIST_SEES = 0x0003;                      // 最近访问

    public static final String IKEY_PRODUCT_ID = "productId";               // 产品ID
    public static final String IKEY_VIDEO_URL = "videoUrl";                 // 视频URL


    /*********************
     * common parameter
     *********************/
    /**
     * 年龄段
     */
    public static final int AGE_ARE_ALL = 0;                            // 全部年龄段
    public static final int AGE_ARE_THREE = 3;                          // 3-4 岁
    public static final int AGE_ARE_FOUR = 4;                           // 4-5 岁
    public static final int AGE_ARE_FIVE = 5;                           // 5-6 岁
    public static final int AGE_ARE_SIX = 6;                            // 6-7 岁

    public static final String SEX_FEMALE_TXT = "女";                    // 女
    public static final String SEX_MALE_TXT = "男";                      // 男
}
