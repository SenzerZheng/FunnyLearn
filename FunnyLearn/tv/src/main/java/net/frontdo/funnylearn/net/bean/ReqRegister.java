package net.frontdo.funnylearn.net.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * ProjectName: ReqRegister
 * Description: 请求Body实体类（JSON对象） - 注册用户
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 11/17/2016 22:12
 */
@Data
public class ReqRegister implements Serializable {

    /**
     * memberMobile : 13598430987
     * pwd : 123456
     * memberBirthday : 2016-11-13
     * memberBabyGender : 1
     * memberDeleted : 0
     * memberName : Felix
     * vericode : 9090
     * memberAgeScope : 3
     */

    private String memberMobile;
    private String pwd;
    private String memberBirthday;
    private int memberBabyGender;
    private String memberDeleted;
    private String memberName;
    private String vericode;
    private String memberAgeScope;

    public ReqRegister() {
    }

    /**
     * @param memberMobile
     * @param pwd
     * @param memberBirthday
     * @param memberBabyGender
     * @param memberDeleted
     * @param memberName
     * @param vericode
     * @param memberAgeScope
     */
    public ReqRegister(String memberMobile, String pwd, String memberBirthday, int memberBabyGender,
                       String memberDeleted, String memberName, String vericode, String memberAgeScope) {

        this.memberMobile = memberMobile;
        this.pwd = pwd;
        this.memberBirthday = memberBirthday;
        this.memberBabyGender = memberBabyGender;
        this.memberDeleted = memberDeleted;
        this.memberName = memberName;
        this.vericode = vericode;
        this.memberAgeScope = memberAgeScope;
    }
}
