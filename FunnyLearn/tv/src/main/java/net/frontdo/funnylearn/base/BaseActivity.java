package net.frontdo.funnylearn.base;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.api.FrontdoApiService;
import net.frontdo.funnylearn.app.AppContext;
import net.frontdo.funnylearn.common.ActivityStack;
import net.frontdo.funnylearn.common.StringUtil;
import net.frontdo.funnylearn.common.SystemBarTintManager;
import net.frontdo.funnylearn.common.ToastHelper;
import net.frontdo.funnylearn.ui.widget.ProgressDlgFragment;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * ProjectName: BaseActivity
 * Description: 基类（toast，网络加载对话框，Theme）
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 2016/8/29 15:00
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected ProgressDlgFragment dlgFragment;

    protected FrontdoApiService apiService;
    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // set the color of the status bar
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarAlpha(0.0f);
            // 此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.translucent));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(R.color.translucent));
        }

        // 方向锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // network
        apiService = AppContext.getInstance().getFrontdoApiService();
        compositeSubscription = new CompositeSubscription();
        ActivityStack.getInstanse().addActivity(this);
    }

    /**
     * 添加订阅
     *
     * @param subscription
     */
    protected void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);

        // inject
        ButterKnife.bind(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
//            SoftKeyboardManager.hide(this, getCurrentFocus().getWindowToken());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityStack.getInstanse().removeActivity(this);
        compositeSubscription.unsubscribe();
        // do not inject
        ButterKnife.unbind(this);
    }

    protected void toast(String content) {
        ToastHelper.toast(this, content, Toast.LENGTH_SHORT);
    }

    protected void toast(int resId) {
        ToastHelper.toast(this, resId, Toast.LENGTH_SHORT);
    }

    protected void toastNetError(String message) {
        if (StringUtil.checkEmpty(message)) {
            toast(R.string.tips_unknow_mistake);
        } else {
            toast(message);
        }
    }

    protected void hide() {
        ToastHelper.hide();
    }

    /**
     * 显示进度对话框
     *
     * @param msg        内容信息
     * @param cancelable 是否点击取消
     */
    protected void showProgressDlg(String msg, boolean cancelable) {
        if (dlgFragment == null) {
            dlgFragment = new ProgressDlgFragment();
        }

        dlgFragment.setText(msg);
        dlgFragment.setCancelable(cancelable);

        if (!dlgFragment.isAdded()) {
            dlgFragment.show(getSupportFragmentManager(), null);
        }
    }

    /**
     * dismiss 对话框
     */
    protected void dismissProgressDlg() {
        if (dlgFragment != null) {
//            dlgFragment.dismissAllowingStateLoss();
            dlgFragment.dismiss();
        }
    }
}
