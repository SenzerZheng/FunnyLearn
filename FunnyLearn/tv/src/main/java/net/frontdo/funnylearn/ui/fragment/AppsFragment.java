package net.frontdo.funnylearn.ui.fragment;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.app.AppContext;
import net.frontdo.funnylearn.base.BaseFragment;
import net.frontdo.funnylearn.common.AppManager;
import net.frontdo.funnylearn.common.FrontdoBaseDialog;
import net.frontdo.funnylearn.common.MyUpdateNotification;
import net.frontdo.funnylearn.common.NotificationUtils;
import net.frontdo.funnylearn.common.StringUtil;
import net.frontdo.funnylearn.common.VersionUtils;
import net.frontdo.funnylearn.logger.FrontdoLogger;
import net.frontdo.funnylearn.net.FrontdoSubcriber;
import net.frontdo.funnylearn.net.bean.ReqOperFavOrApp;
import net.frontdo.funnylearn.ui.adapter.AppsRVAdapter;
import net.frontdo.funnylearn.ui.entity.OperFavOrApp;
import net.frontdo.funnylearn.ui.entity.Product;
import net.frontdo.funnylearn.ui.widget.ProgressDialogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.frontdo.funnylearn.api.ParamsHelper.MY_APP_CANCEL;
import static net.frontdo.funnylearn.api.ParamsHelper.TYPE_APP;

/**
 * ProjectName: AppsFragment
 * Description: 我的应用页面
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 10/23/2016 11:45
 */
public class AppsFragment extends BaseFragment implements
        AppsRVAdapter.OnItemActionsListener {

    private static final String TAG = AppsFragment.class.getSimpleName();

    private static final String TEST_APP = "net.frontdo.uninstallapp";

    @Bind(R.id.recycler)
    RecyclerView recycler;
    @Bind(R.id.iv_empty)
    ImageView ivEmpty;

    private AppsRVAdapter adapter;
    private List mDatasource;

    public static BaseFragment newInstance() {
        AppsFragment fragment = new AppsFragment();
        return getArgsFrag(fragment);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_apps;
    }

    @Override
    protected void onCreateView(View view) {

        initView();
    }

    @Override
    public void onResume() {
        super.onResume();

        String mobile = AppContext.getInstance().getMobile();
        if (!StringUtil.checkEmpty(mobile)) {
            reqApps(TYPE_APP, mobile);
        } else {

//            toast(R.string.apps_unlogin_no_data);
        }
    }

    private void initView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(llm);

        mDatasource = new ArrayList<>();
        adapter = new AppsRVAdapter(getActivity(), mDatasource);
        adapter.setItemActionsListener(this);
        recycler.setAdapter(adapter);
    }

    // --------- GoodsRVAdapter.OnItemActionsListener - onItemClick ----------
    @Override
    public void onItemClick(int pos) {
        Product product = (Product) mDatasource.get(pos);
        if (!AppManager.isInstalled(getActivity(), product.getProductPackName())) {
            toast(R.string.data_err);
            return;
        }

        // deal the notification
        boolean isShowUpdate = false;
        String verName = AppManager.getAppVerName(getActivity(), product.getProductPackName());
        if (verName.compareTo(product.getProductApkVersion()) < 0) {
            isShowUpdate = true;
        }
        NotificationUtils.showTwoBtnDlg(getActivity(),
                getString(R.string.dlg_app_update_title),
                getString(R.string.dlg_app_update_tips),
                "",
                "",
                isShowUpdate,
                new UpdateOrBootDlg(product));
    }

    @Override
    public void onDelClick(int pos) {
//        toast(pos + ", deleted");
        String mobile = AppContext.getInstance().getMobile();
        if (StringUtil.checkEmpty(mobile)) {
            toast(R.string.data_err);
            return;
        }
        Product product = (Product) mDatasource.get(pos);

        // uninstall apk
//        AppManager.uninstallApk(getActivity(), TEST_APP);
        AppManager.uninstallApk(getActivity(), product.getProductPackName());
        // remove app from server
        ReqOperFavOrApp reqOperFavOrApp = new ReqOperFavOrApp(mobile, product.getId());
        uninstallApp(TYPE_APP, MY_APP_CANCEL, reqOperFavOrApp);
    }

    // ################### Network Request Start ###################
    private void reqApps(String type, String mobile) {
        showProgressDlg(null, true);

        Subscription subscription = apiService.getFavOrAppList(type, mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FrontdoSubcriber<Response, List<Product>>() {
                    @Override
                    public void onSuccess(String code, List<Product> products) {
                        dismissProgressDlg();
                        if (null != products && !products.isEmpty()) {

                            ivEmpty.setVisibility(View.GONE);
                            mDatasource = products;
                        } else {

                            ivEmpty.setVisibility(View.VISIBLE);
                            products = new ArrayList<>();
                        }
                        adapter.setDataSource(products, false);
                    }

                    @Override
                    public void onFailure(String code, String message) {
                        dismissProgressDlg();
                        toast(message);
                    }
                });
        addSubscription(subscription);
    }

    private void uninstallApp(String type, String status, ReqOperFavOrApp reqOperFavOrApp) {
        showProgressDlg(null, true);

        Subscription subscription = apiService.operFavOrApp(type, status, reqOperFavOrApp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FrontdoSubcriber<Response, OperFavOrApp>() {
                    @Override
                    public void onSuccess(String code, OperFavOrApp data) {
                        dismissProgressDlg();

                        FrontdoLogger.getLogger().i(TAG, "[ " + TAG + " - uninstallApp] remove app suc!");
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    // ----------------- download and install apk end ---------------------

    // ----------------------- dialog: update or boot -------------------------
    class UpdateOrBootDlg implements FrontdoBaseDialog.OnFrontdoBaseDlgClickListener {

        private Product product;

        public UpdateOrBootDlg(Product data) {
            this.product = data;
        }

        @Override
        public void onClose(View v) {
        }

        @Override
        public void onOk(View v) {              // 直接启动
            toast("直接启动");

            // NOTE: must be exist LAUNCHER, instead of only LEANBACK_LAUNCHER
//            AppManager.bootApp(getActivity(), TEST_APP);
            AppManager.bootApp(getActivity(), product.getProductPackName());
        }

        @Override
        public void onCancle(View v) {          // 立即更新
            toast("立即更新");

            String apkDownUrl = product.getProductApkDownUrl();
            /** 下载最新版本Apk，打开手动进行安装。不修改底层框架（Framework）无法使用静默安装。 */
            ProgressDialogUtil.openHorizontalProgressDialog(getActivity(), "正在下载更新...");
            downloadApk(apkDownUrl, ProgressDialogUtil.getProgressDialog());
//            downloadApk(apkDownUrl);
        }
    }
}
