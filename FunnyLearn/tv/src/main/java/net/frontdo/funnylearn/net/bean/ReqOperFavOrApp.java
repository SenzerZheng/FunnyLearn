package net.frontdo.funnylearn.net.bean;

import lombok.Data;

/**
 * ProjectName: ReqLogin
 * Description: 请求Body实体类（JSON对象） - 收藏（添加与取消）、应用（添加与取消）
 *
 * author: JeyZheng
 * version: 4.0
 * created at: 11/17/2016 22:37
 */
@Data
public class ReqOperFavOrApp {
    /**
     * mobile : 13598430000
     * productId : 4
     */

    private String mobile;
    private int productId;

    public ReqOperFavOrApp(String mobile, int productId) {
        this.mobile = mobile;
        this.productId = productId;
    }
}
