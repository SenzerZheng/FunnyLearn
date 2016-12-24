package net.frontdo.funnylearn.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import net.frontdo.funnylearn.common.FrontdoBaseDialog.OnFrontdoBaseDlgClickListener;

import static net.frontdo.funnylearn.common.FrontdoBaseDialog.Type.ONE_BTN_DIALOG;
import static net.frontdo.funnylearn.common.FrontdoBaseDialog.Type.TWO_BTN_DIALOG;


/**
 * ProjectName: NotificationUtils
 * Description: 通知工具类
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 2016/8/12 17:01
 */
@SuppressWarnings("ALL")
public class NotificationUtils {

    /**
     * 显示列表对话框
     *
     * @param context
     * @param title
     * @param items
     * @param listener
     * @return
     */
    public static AlertDialog showListAlertDialog(
            Context context, String title, CharSequence[] items,
            DialogInterface.OnClickListener listener) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setItems(items, listener)
                .create();

        dialog.show();
        return dialog;
    }


    /**
     * 显示一个按钮的对话框
     *
     * @param context
     * @param title
     * @param content
     * @param ok
     * @param listener 点击监听器
     */
    public static void showOneBtnDlg(Context context,
                                     String title, String content, String ok,
                                     OnFrontdoBaseDlgClickListener listener) {

        FrontdoBaseDialog dlg = new FrontdoBaseDialog(
                context, ONE_BTN_DIALOG, title, content, ok, null);

        dlg.setOnClickListener(listener);
        dlg.show();

    }


    /**
     * 显示两个按钮的对话框
     *
     * @param context
     * @param title
     * @param content
     * @param ok      rigthBtn
     * @param cancel  leftBtn
     */
    public static void showTwoBtnDlg(Context context, String title,
                                     String content, String ok, String cancel,
                                     OnFrontdoBaseDlgClickListener listener) {

        FrontdoBaseDialog dlg = new FrontdoBaseDialog(
                context, TWO_BTN_DIALOG, title, content, ok, cancel);

        dlg.setOnClickListener(listener);
        dlg.show();

    }

    /**
     * 显示两个按钮的对话框
     *
     * @param context
     * @param title
     * @param content
     * @param ok           rigthBtn
     * @param cancel       leftBtn
     * @param isShowCancel 是否显示取消View
     */
    public static void showTwoBtnDlg(Context context, String title,
                                     String content, String ok, String cancel, boolean isShowCancel,
                                     OnFrontdoBaseDlgClickListener listener) {

        FrontdoBaseDialog dlg = new FrontdoBaseDialog(
                context, TWO_BTN_DIALOG, title, content, ok, cancel, isShowCancel);

        dlg.setOnClickListener(listener);
        dlg.show();

    }

    /**
     * 显示两个按钮的对话框（是否点击空白区，关闭Dialog）
     *
     * @param context
     * @param title
     * @param content
     * @param ok
     * @param cancelable 是否点击空白区，关闭Dialog
     */
    public static void showTwoBtnDlg(Context context, String title,
                                     String content, String ok, String cancel,
                                     OnFrontdoBaseDlgClickListener listener, boolean cancelable) {

        FrontdoBaseDialog dlg = new FrontdoBaseDialog(
                context, TWO_BTN_DIALOG, title, content, ok, cancel);

        dlg.setOnClickListener(listener);
        //dlg.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        dlg.setCanceledOnTouchOutside(cancelable);
        dlg.setCancelable(cancelable);
        dlg.show();
    }
}
