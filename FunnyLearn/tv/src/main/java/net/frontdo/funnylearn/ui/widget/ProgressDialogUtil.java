package net.frontdo.funnylearn.ui.widget;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * ProjectName: ProgressDialogUtil
 * Description:
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 11/27/2016 20:22
 */
public class ProgressDialogUtil {
    private static ProgressDialog progressDialog;

    /**
     * 显示圆形进度框
     *
     * @param context
     * @param title   标题
     * @param msg     显示内容
     */
    public static void openProgressDialog(Context context, String title, String msg) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(msg);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // default
        // false : close progressDialog when you touch outside
        // true : do not close progressDialog when you touch outside
//        progressDialog.setCanceledOnTouchOutside(false);
        if (null != progressDialog && !progressDialog.isShowing()) {
            progressDialog.show();
        }

    }

    /**
     * 显示圆形进度框
     *
     * @param context
     * @param title
     * @param msg
     * @param isCancelOnTouchOutside Sets whether this dialog is canceled when touched outside the window's bounds.
     */
    public static void openProgressDialog(Context context, String title,
                                          boolean isCancelOnTouchOutside, String msg) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(msg);
        progressDialog.setCanceledOnTouchOutside(isCancelOnTouchOutside);
        if (null != progressDialog && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    /**
     * 显示圆形进度框
     *
     * @param context
     * @param title
     * @param msg
     * @param isCanCancel Sets whether this dialog is cancelable with the BACK key.
     */
    public static void openProgressDialog(Context context, String title, String msg, boolean isCanCancel) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(isCanCancel);
        if (null != progressDialog && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    /**
     * 显示圆形进度框
     *
     * @param context
     * @param titleId 标题
     * @param msgId   显示内容
     */
    public static void openProgressDialog(Context context, int titleId, int msgId) {
//		Log.i(LogConfig.TOBY, "openProgressDialog::"+((Activity)context).getLocalClassName());
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getString(titleId));
        progressDialog.setMessage(context.getString(msgId));
        if (null != progressDialog && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    /**
     * 显示水平进度条
     *
     * @param context
     * @param msgId
     */
    public static void openHorizontalProgressDialog(Context context, int msgId) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(msgId));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressNumberFormat("%1d KB/%2d KB");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        if (null != progressDialog && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    /**
     * 显示长形进度条
     *
     * @param context
     * @param msgContent
     */
    public static void openHorizontalProgressDialog(Context context, String msgContent) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msgContent);
        progressDialog.setCanceledOnTouchOutside(false);
        // arg1/arg2%							20kb/100kb
        progressDialog.setProgressNumberFormat("%1d KB/%2d KB");
//		progressDialog.setProgressNumberFormat("%1d B/%2d B");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        if (null != progressDialog && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    /**
     * 关闭进度框
     */
    public static void closeProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

        }
    }

    public static ProgressDialog getProgressDialog() {
        return progressDialog;
    }
}
