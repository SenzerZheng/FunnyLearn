package net.frontdo.funnylearn.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.app.AppConstants;
import net.frontdo.funnylearn.app.AppContext;
import net.frontdo.funnylearn.base.BaseHoldBackActivity;
import net.frontdo.funnylearn.common.SoftKeyboardManager;
import net.frontdo.funnylearn.net.FrontdoSubcriber;
import net.frontdo.funnylearn.net.bean.ReqUpdateInfo;
import net.frontdo.funnylearn.ui.entity.UserInfo;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.frontdo.funnylearn.api.ParamsHelper.USER_INFO_EDIT;

public class MineDetailsActivity extends BaseHoldBackActivity {
    private static final String TAG = MineDetailsActivity.class.getSimpleName();

    @Bind(R.id.et_mobile)
    TextView etMobile;
    @Bind(R.id.et_birthday)
    EditText etBirthday;
    @Bind(R.id.et_sex)
    EditText etSex;

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

            etBirthday.setText(userInfo.getMemberBirthday());
            String sexTxt = "";
            if (UserInfo.SEX_MALE == userInfo.getMemberBabyGender()) {
                sexTxt = AppConstants.SEX_MALE_TXT;
            } else {
                sexTxt = AppConstants.SEX_FEMALE_TXT;
            }
            etSex.setText(sexTxt);
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_edit, R.id.iv_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:                          // 返回

                finish();
                break;

            case R.id.iv_edit:                          // 编辑

                if (!etBirthday.isEnabled()) {      // 开始编辑
                    etBirthday.setEnabled(true);
                    etSex.setEnabled(true);

                    etBirthday.requestFocus();
                    if (!SoftKeyboardManager.isShowing(this, etBirthday.getWindowToken())) {
                        SoftKeyboardManager.show(this, etBirthday);
                    }
                } else {
                    etBirthday.setEnabled(false);
                    etSex.setEnabled(false);
                    if (SoftKeyboardManager.isShowing(this, etBirthday.getWindowToken())) {
                        SoftKeyboardManager.hide(this, etBirthday.getWindowToken());
                    }

                    ReqUpdateInfo reqEditInfo = new ReqUpdateInfo();
                    reqEditInfo.setMemberMobile(AppContext.getInstance().getMobile());
                    reqEditInfo.setMemberBirthday(etBirthday.getText().toString().trim());

                    String sexTxt = etSex.getText().toString().trim();
                    if (AppConstants.SEX_MALE_TXT.equals(sexTxt)) {
                        reqEditInfo.setMemberBabyGender(UserInfo.SEX_MALE);
                    } else {
                        reqEditInfo.setMemberBabyGender(UserInfo.SEX_FEMALE);
                    }

                    editUserInfo(reqEditInfo);
                }
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
