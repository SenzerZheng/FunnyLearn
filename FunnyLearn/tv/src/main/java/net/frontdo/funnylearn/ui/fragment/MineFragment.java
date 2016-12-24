package net.frontdo.funnylearn.ui.fragment;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.app.AppConstants;
import net.frontdo.funnylearn.app.AppContext;
import net.frontdo.funnylearn.base.BaseFragment;
import net.frontdo.funnylearn.common.MyUpdateNotification;
import net.frontdo.funnylearn.common.StringUtil;
import net.frontdo.funnylearn.common.VersionUtils;
import net.frontdo.funnylearn.logger.FrontdoLogger;
import net.frontdo.funnylearn.net.FrontdoSubcriber;
import net.frontdo.funnylearn.ui.activity.DateListActivity;
import net.frontdo.funnylearn.ui.activity.LoginActivity;
import net.frontdo.funnylearn.ui.activity.MineDetailsActivity;
import net.frontdo.funnylearn.ui.entity.MineInfo;
import net.frontdo.funnylearn.ui.entity.SystemInfo;
import net.frontdo.funnylearn.ui.widget.ProgressDialogUtil;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.frontdo.funnylearn.api.ParamsHelper.TAG;

/**
 * ProjectName: MineFragment
 * Description: 我的
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 10/23/2016 11:45
 */
public class MineFragment extends BaseFragment {

    @Bind(R.id.tv_curr_ver)
    TextView tvCurrVer;
    @Bind(R.id.tv_latest_ver)
    TextView tvLatestVer;

    private SystemInfo systemInfo;
    private String curVerName;
    private String latestVerName;

    public static BaseFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return getArgsFrag(fragment);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void onCreateView(View view) {

        String mobile = AppContext.getInstance().getMobile();
        if (!StringUtil.checkEmpty(mobile)) {
            getMyInfo(mobile);
        }
    }

    @OnClick({R.id.iv_details, R.id.iv_fav, R.id.iv_sys_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_details:               // 帐号信息
                toast("帐号信息");

                if (AppContext.getInstance().isLogin()) {       // 已登录

                    MineDetailsActivity.boot(getActivity());
                } else {                                        // 未登录

                    LoginActivity.boot(getActivity());
                }
                break;

            case R.id.iv_fav:                   // 我的收藏
//                toast("我的收藏");

                if (!AppContext.getInstance().isLogin()) {
                    LoginActivity.boot(getActivity());
                    return;
                }
                DateListActivity.boot(getActivity(), AppConstants.IVALUE_LIST_FAV);
                break;

            case R.id.iv_sys_update:            // 系统更新
                if (null == systemInfo) {
                    toast(R.string.data_err);
                    return;
                }

                if (curVerName.compareTo(latestVerName) < 0) {          // download apk
                    String apkDownUrl = systemInfo.getSystemApkDownUrl();
                    /** 下载最新版本Apk，打开手动进行安装。不修改底层框架（Framework）无法使用静默安装。 */
                    ProgressDialogUtil.openHorizontalProgressDialog(getActivity(), "正在下载更新...");
                    downloadApk(apkDownUrl, ProgressDialogUtil.getProgressDialog());
//                    downloadApk(apkDownUrl);
                }
                break;

            default:
                break;
        }
    }

    // ----------------- download and install apk start ---------------------
    private static final int MSG_WHAT_DOWN_FAULT = 0x001;

    private Handler downloadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_WHAT_DOWN_FAULT:       // 安装失败
                    toast(R.string.install_fault);
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * @param apkUrl
     * @unsed
     */
    private void downloadApk(final String apkUrl) {
        new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    // initialize my updating notification.
                    MyUpdateNotification.initNotification(getActivity(),
                            getString(R.string.app_name),
                            getString(R.string.app_downloading));

                    File apkFile = VersionUtils.getFileFromServer(apkUrl);
                    if (null == apkFile) {
                        FrontdoLogger.getLogger().e(TAG, "[ " + TAG + " - downloadApk] apkFile is null!");
                        toast("安装失败");
                        return;
                    }

                    sleep(1000);
                    /** updating step four : click and install APK */
                    MyUpdateNotification.setContentIntent(apkFile);
                    MyUpdateNotification.complete(getString(R.string.app_complete));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void downloadApk(final String apkUrl, final ProgressDialog pd) {
        new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    File apkFile = VersionUtils.getFileFromServer(apkUrl, pd);
                    if (null == apkFile) {
                        FrontdoLogger.getLogger().e(TAG, "[ " + TAG + " - downloadApk] apkFile is null!");

                        pd.dismiss();
                        downloadHandler.sendEmptyMessage(MSG_WHAT_DOWN_FAULT);
                        return;
                    }

                    sleep(2000);
                    /** updating step four : install APK */
                    VersionUtils.installApk(getActivity(), apkFile);

                    pd.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();

                    pd.dismiss();
                    downloadHandler.sendEmptyMessage(MSG_WHAT_DOWN_FAULT);
                }
            }
        }.start();
    }
    // ----------------- download and install apk end ---------------------

    // ################### Network Request Start ###################
    private void getMyInfo(String mobile) {
        showProgressDlg(null, true);

        Subscription subscription = apiService.getMyInfo(mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FrontdoSubcriber<Response, MineInfo>() {
                    @Override
                    public void onSuccess(String code, MineInfo data) {
                        dismissProgressDlg();

                        try {
                            systemInfo = data.getSystemDO();

                            curVerName = VersionUtils.getVersionName(getActivity());
                            String curVerTxt = getString(R.string.main_mine_curr_ver);
                            String curVerFormat = String.format(curVerTxt, curVerName);
                            tvCurrVer.setText(curVerFormat);

                            latestVerName = systemInfo.getSystemLatestVersion();
                            String newVerTxt = getString(R.string.main_mine_latest_ver);
                            String newVerFormat = String.format(newVerTxt, latestVerName);
                            tvLatestVer.setText(newVerFormat);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
