package net.frontdo.funnylearn.common;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author: liujun
 * Date: 2015-10-26
 * Time: 10:26
 * Des:toast帮助类
 */
public class ToastHelper {

    private static Toast mToast;

    public static void toast(Context context, String msg) {
        toast(context, msg, Toast.LENGTH_LONG);
    }

    public static void toast(Context context, int msgResId) {
        toast(context, context.getString(msgResId), Toast.LENGTH_LONG);
    }

    public static void toast(Context context, int msgResId, int time) {
        toast(context, context.getString(msgResId), time);
    }

    public static void toast(Context context, String msg, int time) {
        if (null == mToast) {
            mToast = Toast.makeText(context, msg, time);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(msg);
            mToast.setDuration(time);
        }

        mToast.show();
    }

    public static void hide() {
        mToast.cancel();
    }
}