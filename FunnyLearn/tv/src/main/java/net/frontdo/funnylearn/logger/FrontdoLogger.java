/*
 * SpiderLogger.java
 * classes : com.spider.subscriber.util.SpiderLogger
 * @author liujun
 * V 1.0.0
 * Create at 2014-12-29 下午6:23:01
 * Copyright (c) 2014年  Spider. All Rights Reserved.
 */
package net.frontdo.funnylearn.logger;

import android.content.Context;

import net.frontdo.funnylearn.BuildConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuJun
 * @ClassName: FrontdoLogger
 * @Description: 日志类
 * @date 2014-12-29 下午6:23:01
 */
@SuppressWarnings("ALL")
public class FrontdoLogger implements IFrontdoLogger {
    /** BUILD_TYPE */
    /**
     * debug for testing
     */
    public static final String BT_DEBUG = "debug";
    /**
     * debug for releasing
     */
    public static final String BT_DEBUG_REL = "debugRel";
    /**
     * ver: alpha
     */
    public static final String BT_ALPHA = "alpha";
    /**
     * ver: release & online
     */
    public static final String BT_RELEASE = "release";

    private static final String TAG = "SpiderLoger";

    private static FrontdoLogger logger = new FrontdoLogger();

    private Context context;
    // 默认打日志,打正式包时，修改为 LEVEL_NOLOG（千万不要忘记）LEVEL_DEBUG//测试
    private static int level = LEVEL_DEBUG;
    private List<IFrontdoLogger> loggers = new ArrayList<>();

    private FrontdoLogger() {
        if (BT_RELEASE.equalsIgnoreCase(BuildConfig.BUILD_TYPE)) {
            level = LEVEL_NOLOG;
        }
    }

    /**
     * 初始化日志
     *
     * @param context
     */
    public void init(Context context) {
        this.context = context;
        loggers.add(new FrontdoConsoleLogger(level));
        loggers.add(new FrontdoFileLogger(context, level));
    }

    /**
     * 获取日志类实例
     */
    public static FrontdoLogger getLogger() {
        return logger;
    }

    @Override
    public void d(String tag, String msg) {
        if (level <= LEVEL_DEBUG) {
            for (IFrontdoLogger loger : loggers) {
                loger.d(tag, msg);
            }
        }
    }

    @Override
    public void i(String tag, String msg) {
        if (level <= LEVEL_INFO) {
            for (IFrontdoLogger loger : loggers) {
                loger.i(tag, msg);
            }
        }
    }

    @Override
    public void w(String tag, String msg) {
        if (level <= LEVEL_WARN) {
            for (IFrontdoLogger loger : loggers) {
                loger.w(tag, msg);
            }
        }
    }

    @Override
    public void e(String tag, String msg) {
        if (level <= LEVEL_ERROR) {
            for (IFrontdoLogger logger : loggers) {
                logger.e(tag, msg);
            }
        }
    }

    @Override
    public void e(String tag, Exception e) {
        if (level <= LEVEL_ERROR) {
            for (IFrontdoLogger logger : loggers) {
                logger.e(tag, e);
            }
        }
    }
}
