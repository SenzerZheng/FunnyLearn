package net.frontdo.funnylearn.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.app.AppContext;
import net.frontdo.funnylearn.base.BaseHoldBackActivity;
import net.frontdo.funnylearn.common.DatePickerDialogUtil;
import net.frontdo.funnylearn.net.FrontdoSubcriber;
import net.frontdo.funnylearn.net.bean.ReqUpdateInfo;
import net.frontdo.funnylearn.ui.entity.UserInfo;
import net.frontdo.funnylearn.ui.widget.ZoomableImageView;
import net.frontdo.funnylearn.ui.widget.ZoomableTextView;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.frontdo.funnylearn.api.ParamsHelper.USER_INFO_EDIT;
import static net.frontdo.funnylearn.ui.entity.UserInfo.SEX_UNKNOWN;

public class MineDetailsActivity extends BaseHoldBackActivity {
    private static final String TAG = MineDetailsActivity.class.getSimpleName();

    @Bind(R.id.et_mobile)
    TextView etMobile;
    @Bind(R.id.tv_sel_birth_date)
    ZoomableTextView tvSelBirthDate;
    @Bind(R.id.iv_male)
    ZoomableImageView ivMale;
    @Bind(R.id.iv_female)
    ZoomableImageView ivFemale;

    // 1（男），0（女），-1（未知）
    private int sex = SEX_UNKNOWN;

    @Override
    protected int onBindLayout() {
        return R.layout.activity_mine_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    private void initData() {
        UserInfo userInfo = AppContext.getInstance().gainUserInfo();
        if (null != userInfo) {
            etMobile.setText(userInfo.getMemberMobile());

            tvSelBirthDate.setText(userInfo.getMemberBirthday());
            if (UserInfo.SEX_MALE == userInfo.getMemberBabyGender()) {

                ivMale.setBackgroundResource(R.mipmap.icon_male);
                ivFemale.setBackgroundResource(R.mipmap.mine_details_woman_unselected);
            } else {

                ivMale.setBackgroundResource(R.mipmap.mine_details_man_unselected);
                ivFemale.setBackgroundResource(R.mipmap.icon_female);
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_sel_birth_date, R.id.iv_edit, R.id.iv_logout, R.id.iv_male, R.id.iv_female})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:                          // 返回

                finish();
                break;

            case R.id.tv_sel_birth_date:                // 选择出生日期

                DatePickerDialogUtil.showCustomDPV(this, tvSelBirthDate);
                break;

            case R.id.iv_edit:                          // 编辑，进行保存

                ReqUpdateInfo reqEditInfo = new ReqUpdateInfo();
                reqEditInfo.setMemberMobile(AppContext.getInstance().getMobile());
                reqEditInfo.setMemberBirthday(tvSelBirthDate.getText().toString().trim());
                reqEditInfo.setMemberBabyGender(sex);

                editUserInfo(reqEditInfo);
                break;

            case R.id.iv_logout:                        // 注销
                toast("退出成功");

                UserInfo userInfo = AppContext.getInstance().gainUserInfo();
                UserInfo cacheUInfo = new UserInfo();
                cacheUInfo.setMemberBirthday(userInfo.getMemberBirthday());
                cacheUInfo.setMemberBabyGender(userInfo.getMemberBabyGender());
                // clear
                AppContext.getInstance().logout();
                // cache
                AppContext.getInstance().saveUserInfo(cacheUInfo);
                finish();
                break;

            case R.id.iv_male:                          // 选中男
                sex = UserInfo.SEX_MALE;
                toast("男");

                ivMale.setBackgroundResource(R.mipmap.icon_male);
                ivFemale.setBackgroundResource(R.mipmap.mine_details_woman_unselected);
                break;

            case R.id.iv_female:                        // 选中女
                sex = UserInfo.SEX_FEMALE;
                toast("女");

                ivMale.setBackgroundResource(R.mipmap.mine_details_man_unselected);
                ivFemale.setBackgroundResource(R.mipmap.icon_female);
                break;

            default:
                break;
        }
    }

    // ################### Network Request Start ###################
    private void editUserInfo(final ReqUpdateInfo reqUpdateInfo) {
        showProgressDlg(null, true);

        Subscription subscription = apiService.operUserInfo(USER_INFO_EDIT, reqUpdateInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FrontdoSubcriber<Response, UserInfo>() {
                    @Override
                    public void onSuccess(String code, UserInfo user) {
                        dismissProgressDlg();

                        toast("编辑成功！");
                        UserInfo userInfo = AppContext.getInstance().gainUserInfo();
                        userInfo.setMemberBabyGender(reqUpdateInfo.getMemberBabyGender());
                        userInfo.setMemberBirthday(reqUpdateInfo.getMemberBirthday());
                        AppContext.getInstance().saveUserInfo(userInfo);
                        finish();
                    }

                    @Override
                    public void onFailure(String code, String message) {
                        dismissProgressDlg();
                        toast(message);
                    }
                });
        addSubscription(subscription);
    }
    // ################### Network Request End #####################

    public static void boot(Context context) {
        Intent intent = new Intent(context, MineDetailsActivity.class);
        context.startActivity(intent);
    }
}
