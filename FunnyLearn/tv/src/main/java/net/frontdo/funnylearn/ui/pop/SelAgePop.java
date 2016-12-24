package net.frontdo.funnylearn.ui.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.app.AppConstants;
import net.frontdo.funnylearn.ui.widget.ZoomableCheckBox;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ProjectName: SelAgePop
 * Description: 选择不同年龄段，更新主页内容
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 2016/5/25 10:21
 */
@SuppressWarnings("ALL")
public class SelAgePop {
    private static SelAgePop instance;

    @Bind(R.id.cb_age_all)
    ZoomableCheckBox cbAgeAll;
    @Bind(R.id.cb_age_three)
    ZoomableCheckBox cbAgeThree;
    @Bind(R.id.cb_age_four)
    ZoomableCheckBox cbAgeFour;
    @Bind(R.id.cb_age_five)
    ZoomableCheckBox cbAgeFive;
    @Bind(R.id.cb_age_six)
    ZoomableCheckBox cbAgeSix;

    private Context mContext;
    private PopupWindow mPopupWindow;

//    private ZoomableCheckBox cbAll;
//    private ZoomableCheckBox cbThree;
//    private ZoomableCheckBox cbFour;
//    private ZoomableCheckBox cbFive;
//    private ZoomableCheckBox cbSix;


    private SelAgePop(Context context) {
        this.mContext = context;

//        initPopAtDown(context);
        initPopAtLocation(context);
    }

    public static SelAgePop getInstance(Context context) {
        if (null == instance) {
            instance = new SelAgePop(context);
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
        mPopupWindow.setOutsideTouchable(true);
//        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // measure the view's width and height
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            contentView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        }

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                dismissPopupView();
            }
        });
    }

    private View getContentView(Context context) {
        View contentView = View.inflate(context, R.layout.pop_age_sel, null);
        ButterKnife.bind(this, contentView);

//        cbAll = (ZoomableCheckBox) contentView.findViewById(R.id.cb_age_all);
//        cbThree = (ZoomableCheckBox) contentView.findViewById(R.id.cb_age_all);
//        cbFour = (ZoomableCheckBox) contentView.findViewById(R.id.cb_age_all);
//        cbFive = (ZoomableCheckBox) contentView.findViewById(R.id.cb_age_all);
//        cbSix = (ZoomableCheckBox) contentView.findViewById(R.id.cb_age_three);

        return contentView;
    }

    @OnClick({R.id.cb_age_all, R.id.cb_age_three, R.id.cb_age_four, R.id.cb_age_five, R.id.cb_age_six})
    public void onClick(View view) {

        int seledType = 0;
        switch (view.getId()) {
            case R.id.cb_age_all:
                seledType = AppConstants.AGE_ARE_ALL;       // all age
                break;

            case R.id.cb_age_three:
                seledType = AppConstants.AGE_ARE_THREE;     // three - four
                break;

            case R.id.cb_age_four:
                seledType = AppConstants.AGE_ARE_FOUR;      // four - five
                break;

            case R.id.cb_age_five:
                seledType = AppConstants.AGE_ARE_FIVE;      // five -six
                break;

            case R.id.cb_age_six:
                seledType = AppConstants.AGE_ARE_SIX;       // six - seven
                break;

            default:
                break;
        }

        onAgeAreaSeledListener.onSeleted(seledType);
        dismissPopupView();
    }

    /**
     * 重置
     */
    private void reset() {
        cbAgeAll.setChecked(false);
        cbAgeThree.setChecked(false);
        cbAgeFour.setChecked(false);
        cbAgeFive.setChecked(false);
        cbAgeSix.setChecked(false);
    }

    /**
     * 更新选中的状态
     *
     * @param ageAreaType
     */
    public void seledAge(int ageAreaType) {
        reset();

        switch (ageAreaType) {
            case AppConstants.AGE_ARE_THREE:                // 选中年龄段：3-4 岁
                cbAgeThree.setChecked(true);
                break;

            case AppConstants.AGE_ARE_FOUR:                 // 选中年龄段：4-5 岁
                cbAgeFour.setChecked(true);
                break;

            case AppConstants.AGE_ARE_FIVE:                 // 选中年龄段：5-6 岁
                cbAgeFive.setChecked(true);
                break;

            case AppConstants.AGE_ARE_SIX:                  // 选中年龄段：6-7 岁
                cbAgeSix.setChecked(true);
                break;

            case AppConstants.AGE_ARE_ALL:                  // 全部年龄段
                cbAgeAll.setChecked(true);
                break;

            default:
                break;
        }
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

    private OnAgeAreaSeledListener onAgeAreaSeledListener;

    public void setOnAgeAreaSeledListener(OnAgeAreaSeledListener onAgeAreaSeledListener) {
        this.onAgeAreaSeledListener = onAgeAreaSeledListener;
    }

    public interface OnAgeAreaSeledListener {
        void onSeleted(int seledType);
    }
}
