package net.frontdo.funnylearn.ui.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * ProjectName: LayoutBean
 * Description: 四个图片布局信息
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 11/22/2016 21:54
 */
@Data
public class LayoutBean implements Serializable {
    /**
     * id : 1
     * layoutName : layout
     * layoutPosition : 1
     * layoutValue : 1
     * layoutProdUrl : http://localhost:8080/eden/resource/images/prod1.jpg
     * layoutProductId : 0
     * layoutDeleted : 0
     */
    private int id;
    private String layoutName;
    private int layoutPosition;
    private int layoutValue;
    private String layoutProdUrl;
    private int layoutProductId;
    private int layoutDeleted;
    private int productRecommend;                                   // 是否推荐: 1:否；0:是
}
