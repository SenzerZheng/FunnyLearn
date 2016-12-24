package net.frontdo.funnylearn.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.app.AppContext;
import net.frontdo.funnylearn.base.BaseHoldBackActivity;
import net.frontdo.funnylearn.common.StringUtil;
import net.frontdo.funnylearn.net.FrontdoSubcriber;
import net.frontdo.funnylearn.net.bean.ReqLogin;
import net.frontdo.funnylearn.ui.entity.MainInfo;
import net.frontdo.funnylearn.ui.entity.UserInfo;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseHoldBackActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final int PWD_STATE_VISIBLE = 0x001;
    private static final int PWD_STATE_INVISIBLE = 0x002;

    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.iv_pwd_visible)
    ImageView ivPwdVisible;
    @Bind(R.id.tv_oper_pwd)
    TextView tvOperPwd;

    private MainInfo mainInfo;
    private int pwdState = PWD_STATE_INVISIBLE;

    @Override
    protected int onBindLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
    }

    @OnClick({R.id.iv_back, R.id.tv_oper_pwd, R.id.tv_forget_pwd, R.id.iv_login, R.id.iv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:                      // close the view
                this.finish();
                break;

            case R.id.tv_oper_pwd:                  // oper pwd
                if (pwdState == PWD_STATE_INVISIBLE) {      // show pwd

                    ivPwdVisible.setImageResource(R.mipmap.login_pwd_checked);
                    tvOperPwd.setText("隐藏密码");

                    pwdState = PWD_STATE_VISIBLE;
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                    toast("密码显示");
                } else {

                    ivPwdVisible.setImageResource(R.mipmap.login_pwd_unchecked);
                    tvOperPwd.setText("显示密码");

                    pwdState = PWD_STATE_INVISIBLE;
                    etPassword.setInputType(
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    toast("密码隐藏");
                }
                break;

            case R.id.tv_forget_pwd:                // forget pwd
                toast("忘记密码");

                ForgetPwdActivity.boot(this);
                break;

            case R.id.iv_login:                     // login

                String mobile = etPhone.getText().toString().trim();
                String pwd = etPassword.getText().toString().trim();
                if (StringUtil.checkEmpty(mobile)) {

                    toast(R.string.toast_input_phone);
                    return;
                }

                if (!StringUtil.isMobileSimple(mobile)) {

                    toast(R.string.toast_input_phone_error);
                    return;
                }

                if (StringUtil.checkEmpty(pwd)) {

                    toast(R.string.toast_input_pwd);
                    return;
                }

                ReqLogin reqLogin = new ReqLogin(mobile, pwd);
                login(reqLogin);
                break;

            case R.id.iv_register:                  // register

                RegisterActivity.boot(this);
                break;

            default:
                break;
        }
    }

    // ################### Network Request Start ###################
    private void login(ReqLogin reqLogin) {
        showProgressDlg(null, true);

        Subscription subscription = apiService.userLogin(reqLogin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FrontdoSubcriber<Response, MainInfo>() {
                    @Override
                    public void onSuccess(String code, MainInfo data) {
                        dismissProgressDlg();

                        mainInfo = data;
                        UserInfo uInfo = data.getMemberDO();
                        AppContext.getInstance().saveUserInfo(uInfo);
                        toast("登录成功！");
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
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
