package net.frontdo.funnylearn.net.bean;

import lombok.Data;

/**
 * ProjectName: ReqAddDLogs2Server
 * Description: Download log: 新增下载日志
 *
 * author: JeyZheng
 * version: 4.0
 * created at: 1/11/2017 23:15
 */
@Data
public class ReqAddDLogs2Server {

    /**
     * logMobile : 13598430987
     * logDownloadDate : 2016-09-09
     * logProductName : English
     * logDeleted : 0
     */

    private String logMobile;
    private String logDownloadDate;
    private String logProductName;
    private int logDeleted;

    public ReqAddDLogs2Server(String logMobile, String logDownloadDate, String logProductName, int logDeleted) {
        this.logMobile = logMobile;
        this.logDownloadDate = logDownloadDate;
        this.logProductName = logProductName;
        this.logDeleted = logDeleted;
    }
}
