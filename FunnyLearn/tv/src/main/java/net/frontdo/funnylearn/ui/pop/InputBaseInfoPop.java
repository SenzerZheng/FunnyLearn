package net.frontdo.funnylearn.ui.pop;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.app.AppContext;
import net.frontdo.funnylearn.common.DatePickerDialogUtil;
import net.frontdo.funnylearn.common.StringUtil;
import net.frontdo.funnylearn.common.ToastHelper;
import net.frontdo.funnylearn.ui.entity.UserInfo;

import static net.frontdo.funnylearn.ui.entity.UserInfo.SEX_UNKNOWN;

/**
 * ProjectName: InputBaseInfoPop
 * Description: 输入基础信息Pop（出生日期，性别）
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 2016/5/25 10:21
 */
@SuppressWarnings("ALL")
public class InputBaseInfoPop {
    private static InputBaseInfoPop instance;

    private Context mContext;
    private PopupWindow mPopupWindow;

    // 1（男），0（女），-1（未知）
    private int sex = SEX_UNKNOWN;

    private InputBaseInfoPop(Context context) {
        this.mContext = context;

//        initPopAtDown(context);
        initPopAtLocation(context);
    }

    public static InputBaseInfoPop getInstance(Context context) {
        if (null == instance) {
            instance = new InputBaseInfoPop(context);
        }

        return instance;
    }

    private void initPopAtDown(Context context) {
        View contentView = getContentView(context);
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(false);
//        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                dismissPopupView();
            }
        });
    }

    private void initPopAtLocation(Context context) {
        View contentView = getContentView(context);
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setAnimationStyle(R.style.base_popup_anim_style);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(false);
//        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // measure the view's width and height
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            contentView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        }
    }

    private View getContentView(Context context) {
        View contentView = View.inflate(context, R.layout.pop_input_base_info, null);

        final TextView tvBirthday = (TextView) contentView.findViewById(R.id.tv_sel_birth_date);
        View male = contentView.findViewById(R.id.iv_male);
        final View female = contentView.findViewById(R.id.iv_female);
        View confirm = contentView.findViewById(R.id.iv_complete);

        tvBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogUtil.showCur(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvBirthday.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                });
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sex = UserInfo.SEX_MALE;
                ToastHelper.toast(mContext, "男");
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sex = UserInfo.SEX_FEMALE;
                ToastHelper.toast(mContext, "女");
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String birthday = tvBirthday.getText().toString().trim();
                if (StringUtil.checkEmpty(birthday)) {
                    ToastHelper.toast(mContext, R.string.toast_input_birthday);
                    return;
                }

                if (SEX_UNKNOWN == sex) {
                    ToastHelper.toast(mContext, R.string.toast_input_sex);
                    return;
                }

                UserInfo uInfo = new UserInfo();
                // 1: 男；0：女
                uInfo.setMemberBabyGender(sex);
                uInfo.setMemberBirthday(birthday);

                AppContext.getInstance().saveUserInfo(uInfo);
//                ToastHelper.toast(mContext, "设置成功！性别：" + sex + ", 出生日期：" + birthday);
                dismissPopupView();
            }
        });

        return contentView;
    }

    /**
     * 控件底部显示view
     *
     * @param relativeView down at relativeView
     */
    protected void showPopupViewAtDown(View relativeView) {
        int[] location = new int[2];
        relativeView.getLocationOnScreen(location);
        mPopupWindow.showAsDropDown(relativeView);
    }

    /**
     * 指定View为依附类
     *
     * @param parentView down at parentView
     */
    public void showPopupView(View parentView) {
        int[] location = new int[2];
        parentView.getLocationOnScreen(location);
        mPopupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    public void setOnDismissListener(final PopupWindow.OnDismissListener listener) {
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                listener.onDismiss();

                dismissPopupView();
            }
        });
    }

    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    /**
     * 关闭弹出框
     */
    public void dismissPopupView() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public void release() {
        if (mPopupWindow != null) {
            mPopupWindow.setFocusable(false);
            mPopupWindow = null;

            mContext = null;
            instance = null;
        }
    }
}
