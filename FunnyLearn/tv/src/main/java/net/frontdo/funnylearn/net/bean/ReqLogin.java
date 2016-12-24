package net.frontdo.funnylearn.net.bean;

import lombok.Data;

/**
 * ProjectName: ReqLogin
 * Description: 请求Body实体类（JSON对象） - 登录用户
 *
 * author: JeyZheng
 * version: 4.0
 * created at: 11/17/2016 22:37
 */
@Data
public class ReqLogin {
    /**
     * mobile : 13598430000
     * pwd : 123456
     */

    private String mobile;
    private String pwd;

    public ReqLogin(String mobile, String pwd) {
        this.mobile = mobile;
        this.pwd = pwd;
    }
}
