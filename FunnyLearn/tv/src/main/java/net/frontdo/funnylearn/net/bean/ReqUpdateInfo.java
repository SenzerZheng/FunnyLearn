package net.frontdo.funnylearn.net.bean;

import lombok.Data;

/**
 * ProjectName: ReqUpdateInfo
 * Description: 请求Body实体类（JSON对象） - 重置密码与更新用户信息
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 11/22/2016 23:47
 */
@Data
public class ReqUpdateInfo {
    /**
     * memberMobile : 13598430000
     * pwd : 123456
     * vericode:2323
     */

    private String memberMobile;
    private String pwd;
    private String vericode;

    /**
     * mobile : 13598430000
     * “memberBirthday:”2016-09-09”,
     * “memberBabyGender”:0
     */
    private String memberBirthday;
    private int memberBabyGender;

    public ReqUpdateInfo() {
    }

    public ReqUpdateInfo(String memberMobile, String pwd, String vericode) {
        this.memberMobile = memberMobile;
        this.pwd = pwd;
        this.vericode = vericode;
    }
}
