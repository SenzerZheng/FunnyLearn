package net.frontdo.funnylearn.ui.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * ProjectName: SMSCode
 * Description: 系统信息
 *
 * author: JeyZheng
 * version: 4.0
 * created at: 11/24/2016 21:23
 */
@Data
public class SystemInfo implements Serializable {


    /**
     * id : 1
     * systemCurrentVersion : 1.1
     * systemLatestVersion : 1.2
     * systemApkDownUrl : http://118.178.124.197:8080/pro_img/14816004926054510.apk
     * systemTypeName : version
     * systemDeleted : 0
     */

    private int id;
    private String systemCurrentVersion;
    private String systemLatestVersion;
    private String systemApkDownUrl;
    private String systemTypeName;
    private int systemDeleted;
}
