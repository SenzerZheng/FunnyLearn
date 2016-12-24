package net.frontdo.funnylearn.ui.entity;


import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * ProjectName: MainInfo
 * Description: 主页信息，包括封面图
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 2016/8/16 20:08
 */
@Data
public class MainInfo implements Serializable {

    /**
     * memberDO : {"id":2,"memberMobile":"13598430000","pwd":"123456","memberBirthday":"2016-11-13",
     *              "memberBabyGender":1,"memberDeleted":0,"memberName":"Felix","vericode":"9090","memberAgeScope":3}
     * layoutDOs : [
     *              {"id":1,"layoutName":"layout","layoutPosition":1,"layoutValue":1,
     *              "layoutProdUrl":"http://localhost:8080/eden/resource/images/prod1.jpg",
     *              "layoutProductId":0,"layoutDeleted":0},
     *              {...}
     *              ]
     * "categoryDOs": [
     *              {
     *              "id": 1,
     *              "categoryName": "语言",
     *              "categoryUpdateDate": "2016-09-09",
     *              "categoryLevel": 1,
     *              "categoryPrevious": 0,
     *              "categoryDeleted": 0
     *              },
     *              {...}
     *              ]
     */

    private UserInfo memberDO;
    private List<LayoutBean> layoutDOs;
    private List<CategoryBean> categoryDOs;
}
