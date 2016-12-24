package net.frontdo.funnylearn.ui.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * ProjectName: SMSCode
 * Description: 验证码实体类
 *
 * author: JeyZheng
 * version: 4.0
 * created at: 11/24/2016 21:23
 */
@Data
public class SMSCode implements Serializable {

    /**
     * verifyCode : 5499
     */

    private String verifyCode;
}
