package net.frontdo.funnylearn.ui.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * ProjectName: SMSCode
 * Description: 我的的个人中心（用户信息与系统信息）
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 11/24/2016 21:23
 */
@Data
public class MineInfo implements Serializable {

    /**
     * memberDO : {"id":1,"memberMobile":"13598430987","pwd":"123456","memberBirthday":"2016-11-13",
     * "memberBabyGender":1,"memberDeleted":0,"memberName":"Felix","vericode":"9090","memberAgeScope":3}
     *
     * systemDO : {"id":1,"systemCurrentVersion":"1.0","systemLatestVersion":"2.0",
     * "systemTypeName":"version","systemCreateDate":"2016-09-09","systemDeleted":0}
     */

    private UserInfo memberDO;
    private SystemInfo systemDO;
}
