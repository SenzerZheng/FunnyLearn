package net.frontdo.funnylearn.ui.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * ProjectName: Product
 * Description: 商品详情
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 11/19/2016 22:40
 */
@Data
public class Product implements Serializable {
    public static final int P_TYPE_VIDEO = 0;   // 产品类型: 0:视频产品
    public static final int P_TYPE_APP = 1;     // 产品类型: 1:AR产品或产品

    public static final int P_RCMD_NO = 1;      // 是否推荐: 1:否
    public static final int P_RCMD_YES = 0;     // 是否推荐: 0:是

    public static final int P_ENABLE = 0;       // 0:启用， 1：未启用
    public static final int P_UNABLE = 1;       // 0:启用， 1：未启用

    /**
     * "id" : 8,
     * "name" : "故事",
     * "price" : 23.23,
     * "type" : 1,
     * "media" : "电子书,,,教具,,",
     * "productLevelTwo" : "two",
     * "productCategoryTag" : "AR;启蒙",
     * "productDeleted" : 0,
     * "productDesc" : "http://118.178.124.197:8080/pro_img/desc.jpg",
     * "productCover" : "http://118.178.124.197:8080/pro_img/provovery2.jpg",
     * "productImages" : "http://118.178.124.197:8080/pro_img/pro2_001.jpg, http://118.178.124.197:8080/pro_img/pro2_002.jpg",
     * "productPlayEnabled" : 1,
     * "productTrialAddr" : "localhost:8080/images/1.html",
     * "productTrialEnabled" : 1,
     * "productAppEnabled" : 1,
     * "productVersion" : "1.0",
     * "productApkVersion" : "2.0",
     * "productMicroStoreByecodeAddr" : "addr",
     * "productUploadDate" : "2016-11-26",
     * "productMatchScope" : ",4,5,6,",
     * "productMenuType" : 1,
     * "productPackName" : "packname",
     * "productApkDownUrl" : "http://218.200.69.66:8302/upload/Media/20150327/43bfda1b-7280-469c-a83b-82fa311c79d7.m4v",
     * "productModifyDate" : "2016-11-26",
     * "videoDOs" : [{
     * "id" : 15,
     * "videoName" : "part1",
     * "videoUrl" : "http://118.178.124.197:8080/pro_img/part1.mp4",
     * "videoDeleted" : 0
     * }, {
     * "id" : 16,
     * "videoName" : "part2",
     * "videoUrl" : "http://118.178.124.197:8080/pro_img/part1.mp4",
     * "videoDeleted" : 0
     * }
     * ],
     * "publishState" : 0,
     * "productRecommend" : 0,
     * "productCategory" : "3,",
     * "productScore" : 9.8,
     * "productCategoryId" : 0,
     * "productAppQRCode" : "http://118.178.124.197:8080/pro_img/14820446846447805.png"
     */

    private int id;
    private String name;
    private double price;
    private int type;                                               // 产品类型 1:AR产品; 0:视频产品
    private String media;
    private CategoryBean category;                                  // 产品的分类（只是为了适配数据，对客户端没有具体的实际页面显示）
    private String productLevelTwo;
    private int productDeleted;
    private String productDesc;
    private String productCoverVerti;                               // 竖向封面图
    private String productCoverHori;                                // 横向封面图
    private String productCover;                                    // 首页大图
    private int productPlayEnabled;
    private String productPlayAddr;
    private String productTrialAddr;
    private int productTrialEnabled;
    private int productAppEnabled;
    private String productVersion;
    private String productApkVersion;
    private String productMicroStoreByecodeAddr;
    private String productUploadDate;                                   // 常用日期(yyyy-MM-dd)：比较，显示等
    private String productMatchScope;                                   // 匹配年龄段：",4,5,6,"
    private int productMenuType;
    private String productPackName;
    private String productApkDownUrl;
    private String productModifyDate;                                   // 常用日期(yyyy-MM-dd)：比较，显示等
    private int publishState;
    private int productRecommend;                                   // 是否推荐: 1:否；0:是
    private String productCategory;                                 // 分类ID: "3,4"；该产品即属于分类ID：3；也属于分类ID：4
    private String productCategoryTag;                              // 分类名称："AR;启蒙"；该产品即属于分类：AR；也属于分类ID：启蒙
    private String productDeveloper;
    private String productSize;
    private float productScore;
    private int productCategoryId;
    private String productAppQRCode;                                // 产品QRCode

    private String productImages;                                   // 多张图片以","分割
    private List<VideoDOsBean> videoDOs;

    @Data
    public class VideoDOsBean implements Serializable {
        /**
         * id : 13
         * videoName : part1
         * videoUrl : http://118.178.124.197:8080/pro_img/part1.mp4
         * videoDeleted : 0
         */

        @SerializedName("id")
        private int id;
        private String videoName;
        private String videoUrl;
        private int videoDeleted;
    }
}
