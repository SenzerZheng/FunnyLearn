package net.frontdo.funnylearn.ui.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.app.AppContext;
import net.frontdo.funnylearn.base.BaseHoldBackActivity;
import net.frontdo.funnylearn.common.DatePickerDialogUtil;
import net.frontdo.funnylearn.common.StringUtil;
import net.frontdo.funnylearn.net.FrontdoSubcriber;
import net.frontdo.funnylearn.net.bean.ReqRegister;
import net.frontdo.funnylearn.ui.entity.SMSCode;
import net.frontdo.funnylearn.ui.entity.UserInfo;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.frontdo.funnylearn.api.ParamsHelper.GET_CODE_ACTION_REG;

public class RegisterActivity extends BaseHoldBackActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    @Bind(R.id.et_area_code)
    EditText etAreaCode;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_verify_code)
    EditText etVerifyCode;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.tv_birth_date)
    TextView tvBirthDate;
    @Bind(R.id.tv_sel_birth_date)
    TextView tvSelBirthDate;

    private int sex = UserInfo.SEX_UNKNOWN;

    @Override
    protected int onBindLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        UserInfo uInfo = AppContext.getInstance().gainUserInfo();
        if (null == uInfo) {
            return;
        }

        tvSelBirthDate.setText(uInfo.getMemberBirthday());
        sex = uInfo.getMemberBabyGender();
    }

    public static void boot(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.iv_back, R.id.iv_get_verify_code, R.id.iv_male,
            R.id.iv_female, R.id.iv_complete, R.id.tv_sel_birth_date})
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

            case R.id.iv_male:                          // male
                sex = UserInfo.SEX_MALE;
                toast("男");
                break;

            case R.id.iv_female:                        // female
                sex = UserInfo.SEX_FEMALE;
                toast("女");
                break;

            case R.id.iv_complete:                      // complete

                ReqRegister register = new ReqRegister();
                String mobile = etPhone.getText().toString().trim();
                String verifyCode = etVerifyCode.getText().toString().trim();
                String pwd = etPassword.getText().toString().trim();
                String birthday = tvSelBirthDate.getText().toString().trim();
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

                if (StringUtil.checkEmpty(birthday)) {
                    toast(R.string.toast_input_birthday);
                    return;
                }

                if (UserInfo.SEX_UNKNOWN == sex) {
                    toast(R.string.toast_input_sex);
                    return;
                }

                register.setMemberMobile(mobile);
                register.setMemberBirthday(birthday);
                register.setMemberBabyGender(sex);
                register.setPwd(pwd);
                register.setVericode(verifyCode);
                register(register);
                break;

            case R.id.tv_sel_birth_date:
                DatePickerDialogUtil.showCur(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvSelBirthDate.setText(year + "-" + month + "-" + dayOfMonth);
                    }
                });
                break;

            default:
                break;
        }
    }

    // ################### Network Request Start ###################
    private void register(ReqRegister reqRegister) {
        showProgressDlg(null, true);

        Subscription subscription = apiService.userRegister(reqRegister)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FrontdoSubcriber<Response, UserInfo>() {
                    @Override
                    public void onSuccess(String code, UserInfo userInfo) {
                        dismissProgressDlg();

//                        AppContext.getInstance().saveUserInfo(userInfo);
                        toast("注册成功，请登录！");
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

        Subscription subscription = apiService.getVerifyCode(GET_CODE_ACTION_REG, phone)
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
}
