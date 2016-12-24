package net.frontdo.funnylearn.ui.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * ProjectName: CategoryBean
 * Description: 分类目录实体类
 * <p>
 * author: JeyZheng
 * version: 2.0
 * created at: 11/22/2016 21:54
 */
@Data
public class CategoryBean implements Serializable {

    /**
     * id : 1
     * categoryName : 语言
     * categoryUpdateDate : 2016-09-09
     * categoryLevel : 1
     * categoryPrevious : 0
     * categoryDeleted : 0
     */

    private int id;
    private String categoryName;
    private String categoryUpdateDate;
    private int categoryLevel;
    private int categoryPrevious;
    private int categoryDeleted;
}
