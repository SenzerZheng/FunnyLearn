/*
 * CustomDialog.java
 * classes : com.spider.subscriber.view.CustomDialog
 * @author liujun
 * V 1.0.0
 * Create at 2014-11-25 下午3:27:00 
 * Copyright (c) 2014年 spider. All Rights Reserved.
 */
package net.frontdo.funnylearn.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.logger.FrontdoLogger;

/**
 * ProjectName: SpiderBaseDialog
 * Description: 自定义对话框（一个按钮，两个按钮），目前只对重启APP或更新对话框使用
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 2016/8/12 16:50
 */
@SuppressWarnings("ALL")
public class FrontdoBaseDialog extends Dialog implements View.OnClickListener {
    private final static String TAG = FrontdoBaseDialog.class.getSimpleName();

    // 对话框类型
    public static enum Type {
        ONE_BTN_DIALOG,
        TWO_BTN_DIALOG
    }

    private final static int[] dialogId = {R.layout.dlg_one_btn, R.layout.dlg_two_btn};

    private Context context;
    private Type type;

    private String title;
    private String content;
    private String ok;
    private String cancel;
    private boolean isShowCancel = true;           // 判断是否需要显示更新View

    private OnFrontdoBaseDlgClickListener onClickListener;

    public FrontdoBaseDialog(Context context, Type type) {
        this(context, type, null);
    }

    public FrontdoBaseDialog(Context context, Type type, String content) {
        this(context, type, content, R.style.BaseDialogTheme);
    }

    public FrontdoBaseDialog(Context context, Type type, String content, int theme) {
        this(context, type, null, content, theme);
    }

    public FrontdoBaseDialog(Context context, Type type, String title, String content) {
        this(context, type, title, content, null, null, R.style.BaseDialogTheme);

    }

    public FrontdoBaseDialog(Context context, Type type, String title,
                             String content, int theme) {
        this(context, type, title, content, null, null, theme);

    }

    public FrontdoBaseDialog(Context context, Type type, String title,
                             String content, String ok, String cancel) {
        this(context, type, title, content, ok, cancel, R.style.BaseDialogTheme);
    }

    public FrontdoBaseDialog(Context context, Type type, String title,
                             String content, String ok, String cancel, boolean isShowCancel) {
        this(context, type, title, content, ok, cancel, R.style.BaseDialogTheme);

        this.isShowCancel = isShowCancel;
    }

    /**
     * @param context
     * @param type    对话框类型
     * @param title   标题
     * @param content 内容
     * @param ok      确定按钮
     * @param cancel  取消按钮
     * @param theme   主题
     */
    public FrontdoBaseDialog(Context context, Type type, String title,
                             String content, String ok, String cancel, int theme) {
        super(context, theme);
        this.context = context;
        this.type = type;
        this.title = title;
        this.content = content;
        this.ok = ok;
        this.cancel = cancel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getlayoutId(type));

        // change width of the dialog
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (display.getWidth());
        getWindow().setAttributes(lp);

        // init view
        TextView titleTv = (TextView) findViewById(R.id.dlg_title);
        if (titleTv != null) {
            if (!TextUtils.isEmpty(title)) {
                titleTv.setText(title);
            } else {
                View titlelayout = findViewById(R.id.dlg_title_layout);
                if (titlelayout != null) {
                    titlelayout.setVisibility(View.GONE);
                }

                View dialogLayout = findViewById(R.id.dialoglayout);
                int minHeight =
                        context.getResources().getDimensionPixelSize(R.dimen.dlg_min_height_no_title);
                if (dialogLayout != null) {
                    dialogLayout.setMinimumHeight(minHeight);
                }
            }
        }

        TextView dlgContent = (TextView) findViewById(R.id.tv_content);
        if (dlgContent != null) {
            if (!TextUtils.isEmpty(content)) {
                dlgContent.setText(content);
            }
        }

        Button okBtn = (Button) findViewById(R.id.btn_ok);
        Button cancelBtn = (Button) findViewById(R.id.btn_cancel);
        if (okBtn != null) {
            if (!TextUtils.isEmpty(ok)) {
                okBtn.setText(ok);
            }
            okBtn.setOnClickListener(this);
        }

        if (!isShowCancel) {
            cancelBtn.setVisibility(View.GONE);
        }

        if (cancelBtn != null) {
            if (!TextUtils.isEmpty(cancel)) {
                cancelBtn.setText(cancel);
            }
            cancelBtn.setOnClickListener(this);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 设置监听器
     *
     * @param listener
     */
    public void setOnClickListener(OnFrontdoBaseDlgClickListener listener) {
        onClickListener = listener;
    }

    /**
     * 获取对话框id
     *
     * @param type
     * @return
     */
    private int getlayoutId(Type type) {

        int layoutResID = dialogId[0];
        switch (type) {
            case ONE_BTN_DIALOG:
                layoutResID = dialogId[0];
                break;

            case TWO_BTN_DIALOG:
                layoutResID = dialogId[1];
                break;

            default:
                FrontdoLogger.getLogger().e(TAG, "invalid dialog layoutId");
                break;
        }
        return layoutResID;

    }

    /**
     * 设置显示的内容
     *
     * @param content
     */
    public void setContent(String content) {
        TextView dlgContent = (TextView) findViewById(R.id.tv_content);
        if (dlgContent != null) {
            if (!TextUtils.isEmpty(content)) {
                dlgContent.setText(content);
            }
        }
    }

    /**
     * ProjectName: FrontdoBaseDialog
     * Description: 对话框点击事件回调
     * <p>
     * author: JeyZheng
     * version: 4.0
     * created at: 2016/8/12 16:50
     */
    public static interface OnFrontdoBaseDlgClickListener {

        void onClose(View v);

        void onOk(View v);

        void onCancle(View v);
    }

    /**
     * @author LiuJun
     * @ClassName: SimpleDialogClickListener
     * @Description: 简化对话框回调
     * @date 2014-11-25 下午3:27:55
     */
    public static class SimpleDialogClickListener implements
            OnFrontdoBaseDlgClickListener {

        @Override
        public void onClose(View v) {
        }

        @Override
        public void onOk(View v) {
        }

        @Override
        public void onCancle(View v) {
        }

    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null) {

            switch (v.getId()) {
                case R.id.btn_cancel:
                    onClickListener.onCancle(v);
                    break;

                case R.id.btn_ok:
                    onClickListener.onOk(v);
                    break;
            }
        }
        dismiss();
    }
}
