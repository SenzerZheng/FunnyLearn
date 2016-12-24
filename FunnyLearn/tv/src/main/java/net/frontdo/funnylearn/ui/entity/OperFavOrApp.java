package net.frontdo.funnylearn.ui.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * ProjectName: OperFavOrApp
 * Description: 添加/取消（收藏或者应用）
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 11/27/2016 19:18
 */
@Data
public class OperFavOrApp implements Serializable {

    /**
     * id : 2
     * type : 0
     * mobile : 18903032323
     * state : 1
     * productId : 4
     * productCover : localhost:8080/imgs/xxx.jpg
     * collectDate : 2016-09-11
     * deleted : 0
     */

    private int id;
    private int type;
    private String mobile;
    private int state;
    private int productId;
    private String productCover;
    private String collectDate;
    private int deleted;
}
