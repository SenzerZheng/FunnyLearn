package net.frontdo.funnylearn.net;

import java.io.Serializable;

/**
 * ProjectName: BaseBean
 * Description:
 *
 * author: JeyZheng
 * version: 1.0
 * created at: 10/22/2016 12:46
 */
public class BaseBean implements Serializable {
    private String result;
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "[result = " + result + "," + "message = " + message + "]";
    }
}
