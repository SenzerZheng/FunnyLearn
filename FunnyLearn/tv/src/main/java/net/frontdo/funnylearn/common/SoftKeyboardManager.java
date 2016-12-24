package net.frontdo.funnylearn.common;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ProjectName: SoftKeyboardManager
 * Description:
 * <p/>
 * author: JeyZheng
 * version: 2.1
 * created at: 2016/5/25 16:05
 */
public class SoftKeyboardManager {
    /**
     * 判断软键盘是否显示
     *
     * @param context
     * @param windowToken
     * @return
     */
    public static boolean isShowing(Context context, IBinder windowToken) {
        InputMethodManager imm
                = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.hideSoftInputFromWindow(windowToken, 0)) {
            // 软键盘已弹出
            return true;
        } else {
            // 软键盘未弹出
            return false;
        }
    }

    /**
     * 如果输入法在窗口上已经显示，则隐藏，反之则显示
     *
     * @param context
     */
    public static void operKeyboard(Context context) {
        InputMethodManager imm
                = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示软键盘
     *
     * @param context
     * @param view
     */
    public static void show(final Context context, final View view) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm
                        = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     * @param windowToken
     */
    public static void hide(Context context, IBinder windowToken) {
        InputMethodManager imm
                = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }
}
