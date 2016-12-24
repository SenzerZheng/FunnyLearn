package net.frontdo.funnylearn.ui.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * ProjectName: UserInfo
 * Description: 用户信息
 * <p>
 * author: JeyZheng
 * version: 2.0
 * created at: 11/19/2016 22:00
 */
@Data
public class UserInfo implements Serializable {
    public static final int SEX_UNKNOWN = -1;           // 未知
    public static final int SEX_MALE = 1;               // 1（男），0（女）
    public static final int SEX_FEMALE = 0;             // 1（男），0（女）

    /**
     * id : 2
     * memberMobile : 13598430000
     * pwd : 123456
     * memberBirthday : 2016-11-13
     * memberBabyGender : 1
     * memberDeleted : 0
     * memberName : Felix
     * vericode : 9090
     * memberAgeScope : 3
     */
    private int id;
    private String memberMobile;
    private String pwd;
    private String memberBirthday;
    private int memberBabyGender;           // 性别标识：1（男），0（女）
    private int memberDeleted;
    private String memberName;
    private String vericode;
    private int memberAgeScope;
}
