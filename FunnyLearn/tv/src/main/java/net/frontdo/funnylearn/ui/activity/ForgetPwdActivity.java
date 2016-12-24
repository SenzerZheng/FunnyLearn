package net.frontdo.funnylearn.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.base.BaseHoldBackActivity;
import net.frontdo.funnylearn.common.StringUtil;
import net.frontdo.funnylearn.net.FrontdoSubcriber;
import net.frontdo.funnylearn.net.bean.ReqUpdateInfo;
import net.frontdo.funnylearn.ui.entity.SMSCode;
import net.frontdo.funnylearn.ui.entity.UserInfo;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.frontdo.funnylearn.api.ParamsHelper.GET_CODE_ACTION_RESET;
import static net.frontdo.funnylearn.api.ParamsHelper.USER_INFO_RESET_PWD;

public class ForgetPwdActivity extends BaseHoldBackActivity {
    private static final String TAG = ForgetPwdActivity.class.getSimpleName();
    @Bind(R.id.et_area_code)
    EditText etAreaCode;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_verify_code)
    EditText etVerifyCode;
    @Bind(R.id.et_password)
    EditText etPassword;

    @Override
    protected int onBindLayout() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
    }

    @OnClick({R.id.iv_back, R.id.iv_get_verify_code, R.id.iv_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:                          // back
                this.finish();
                break;

            case R.id.iv_get_verify_code:               // get verify code
                String phone = etPhone.getText().toString().trim();
                if (StringUtil.checkEmpty(phone)) {

                    toast(R.string.toast_input_phone);
                    return;
                }

                getVerifyCode(phone);
                break;

            case R.id.iv_complete:                      // complete

                String mobile = etPhone.getText().toString().trim();
                String verifyCode = etVerifyCode.getText().toString().trim();
                String pwd = etPassword.getText().toString().trim();
                if (StringUtil.checkEmpty(mobile)) {

                    toast(R.string.toast_input_phone);
                    return;
                }

                if (!StringUtil.isMobileSimple(mobile)) {

                    toast(R.string.toast_input_phone_error);
                    return;
                }

                if (StringUtil.checkEmpty(verifyCode)) {

                    toast(R.string.toast_input_verify_code);
                    return;
                }

                if (StringUtil.checkEmpty(pwd)) {

                    toast(R.string.toast_input_pwd);
                    return;
                }

                ReqUpdateInfo reqResetPwd = new ReqUpdateInfo(mobile, pwd, verifyCode);
                resetPwd(reqResetPwd);
                break;

            default:
                break;
        }
    }

    // ################### Network Request Start ###################
    private void resetPwd(ReqUpdateInfo reqResetPwd) {
        showProgressDlg(null, true);

        Subscription subscription = apiService.operUserInfo(USER_INFO_RESET_PWD, reqResetPwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FrontdoSubcriber<Response, UserInfo>() {
                    @Override
                    public void onSuccess(String code, UserInfo userInfo) {
                        dismissProgressDlg();

                        toast("重置密码成功！");
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

    public void getVerifyCode(String phone) {
        showProgressDlg(null, true);

        Subscription subscription = apiService.getVerifyCode(GET_CODE_ACTION_RESET, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FrontdoSubcriber<Response, SMSCode>() {
                    @Override
                    public void onSuccess(String code, SMSCode verifyCode) {
                        dismissProgressDlg();

//                        toast("验证码已发送成功：" + verifyCode.getVerifyCode());
                        toast("验证码已发送成功");
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
        Intent intent = new Intent(context, ForgetPwdActivity.class);
        context.startActivity(intent);
    }
}
