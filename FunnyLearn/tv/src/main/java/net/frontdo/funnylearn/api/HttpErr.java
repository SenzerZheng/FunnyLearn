package net.frontdo.funnylearn.api;

import android.util.ArrayMap;

/**
 * ProjectName: HttpErr
 * Description: 错误码
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 11/22/2016 21:09
 */
public class HttpErr {
    /*
     * 0=成功
     * 801=用户不存在
     * 802=没数据
     * 803=数据已经存在
     * 804=请求参数有错
     * 805=设置布局时，产品不存在
     * 806=用户已经存在
     * 807=注册时，验证码不相同
     */
    public static ArrayMap<Integer, String> errMsgMap = new ArrayMap<>();

    static {
        errMsgMap.put(0, "成功");
        errMsgMap.put(801, "用户不存在");
        errMsgMap.put(802, "没数据");
        errMsgMap.put(803, "数据已经存在");
        errMsgMap.put(804, "请求参数有错");
        errMsgMap.put(805, "设置布局时，产品不存在");
        errMsgMap.put(806, "用户已经存在");
        errMsgMap.put(807, "注册时，验证码不相同");
        errMsgMap.put(808, "密码错误");

    }
}
