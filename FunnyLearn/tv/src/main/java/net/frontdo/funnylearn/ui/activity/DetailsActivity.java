package net.frontdo.funnylearn.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.api.ParamsHelper;
import net.frontdo.funnylearn.app.AppContext;
import net.frontdo.funnylearn.base.BaseHoldBackActivity;
import net.frontdo.funnylearn.common.CameraUtils;
import net.frontdo.funnylearn.common.DateUtil;
import net.frontdo.funnylearn.common.MyUpdateNotification;
import net.frontdo.funnylearn.common.PhotoLoader;
import net.frontdo.funnylearn.common.PixelUtils;
import net.frontdo.funnylearn.common.StringUtil;
import net.frontdo.funnylearn.common.VersionUtils;
import net.frontdo.funnylearn.logger.FrontdoLogger;
import net.frontdo.funnylearn.net.FrontdoSubcriber;
import net.frontdo.funnylearn.net.bean.ReqOperFavOrApp;
import net.frontdo.funnylearn.ui.adapter.DetailsAppImgsRVAdapter;
import net.frontdo.funnylearn.ui.adapter.DetailsVideoRVAdapter;
import net.frontdo.funnylearn.ui.entity.GetFavState;
import net.frontdo.funnylearn.ui.entity.OperFavOrApp;
import net.frontdo.funnylearn.ui.entity.Product;
import net.frontdo.funnylearn.ui.widget.ProgressDialogUtil;
import net.frontdo.funnylearn.ui.widget.VerticalStarBar;
import net.frontdo.funnylearn.ui.widget.ZoomableCheckBox;
import net.frontdo.funnylearn.ui.widget.ZoomableImageView;
import net.frontdo.funnylearn.ui.widget.ZoomableRelativeLay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.frontdo.funnylearn.api.ParamsHelper.MY_APP_ADD;
import static net.frontdo.funnylearn.api.ParamsHelper.MY_FAV_ADD;
import static net.frontdo.funnylearn.api.ParamsHelper.MY_FAV_CANCEL;
import static net.frontdo.funnylearn.api.ParamsHelper.TYPE_APP;
import static net.frontdo.funnylearn.api.ParamsHelper.TYPE_FAV;
import static net.frontdo.funnylearn.app.AppConstants.IKEY_PRODUCT_ID;

/**
 * ProjectName: DetailsActivity
 * Description: 详情页面（视频与APP详情）
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 11/06/2016 11:21
 */
public class DetailsActivity extends BaseHoldBackActivity implements
        DetailsVideoRVAdapter.OnItemActionsListener {
    private static final String TAG = DetailsActivity.class.getSimpleName();

    private final int PRODUCT_ID_NO_EFFECT = -1;
    // dot size
    private final int INDICATOR_SIZE_BIG = 6;
    private final int INDICATOR_SIZE_SMALL = 6;
    private static final int DEFAULT_SHOW_IMG_INDEX = 0;

    @Bind(R.id.iv_cover)
    ImageView ivCover;
    @Bind(R.id.iv_latest)
    ImageView ivLatest;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.cb_fav)
    ZoomableCheckBox cbFav;
    @Bind(R.id.tv_score)
    TextView tvScore;
    @Bind(R.id.rb_score)
    VerticalStarBar rbScore;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_size)
    TextView tvSize;
    @Bind(R.id.tv_ver)
    TextView tvVer;
    @Bind(R.id.tv_class)
    TextView tvClass;
    @Bind(R.id.tv_dev)
    TextView tvDev;
    @Bind(R.id.tv_update)
    TextView tvUpdate;
    @Bind(R.id.iv_install_status)
    ImageView ivInstallStatus;
    @Bind(R.id.iv_top_qr_code)
    ImageView ivTopQrCode;
    @Bind(R.id.rl_app)
    RelativeLayout rlApp;
    @Bind(R.id.rl_video)
    RelativeLayout rlVideo;
    @Bind(R.id.tv_video_class)
    TextView tvVideoClass;
    @Bind(R.id.tv_video_dev)
    TextView tvVideoDev;
    @Bind(R.id.tv_video_update)
    TextView tvVideoUpdate;
    @Bind(R.id.rv_video)
    RecyclerView rvVideo;
    @Bind(R.id.iv_is_has_camera)
    ImageView ivIsHasCamera;
    @Bind(R.id.iv_goods_desc)
    ImageView ivGoodsDesc;
    @Bind(R.id.iv_trial_tips)
    ImageView ivTrialTips;
    @Bind(R.id.iv_good_trial)
    ImageView ivGoodTrial;
    @Bind(R.id.rv_app_imgs)
    RecyclerView rvAppImgs;
    @Bind(R.id.ll_indicator)
    LinearLayout llIndicator;
    @Bind(R.id.iv_back)
    ZoomableImageView ivBack;
    @Bind(R.id.iv_next)
    ZoomableImageView ivNext;
    @Bind(R.id.ll_good_trial)
    LinearLayout llGoodTrial;
    @Bind(R.id.iv_app_img)
    ImageView ivAppImg;
    @Bind(R.id.rl_app_imgs)
    RelativeLayout rlAppImgs;
    @Bind(R.id.iv_play_video_tips)
    ImageView ivPlayVideoTips;
    @Bind(R.id.rl_play_video)
    ZoomableRelativeLay rlPlayVideo;

    private List<ImageView> dotIVs = new ArrayList<>();
    private DetailsAppImgsRVAdapter appImgsAdapter;
    private List appImgs;
    private int appImgsCurPage = DEFAULT_SHOW_IMG_INDEX;

    private DetailsVideoRVAdapter videoAdapter;
    private List videos;

    private int productId = PRODUCT_ID_NO_EFFECT;
    private Product product;
    private boolean isFavChecked;

    @Override
    protected int onBindLayout() {
        return R.layout.activity_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra(IKEY_PRODUCT_ID)) {
            productId = intent.getIntExtra(IKEY_PRODUCT_ID, PRODUCT_ID_NO_EFFECT);
        }

        if (PRODUCT_ID_NO_EFFECT == productId) {
            FrontdoLogger.getLogger().e(TAG, "[ " + TAG + " - onCreate] productId is error!");
            finish();
            return;
        }

        initView();
        initListener();

        reqDetails(productId);
    }

    private void initView() {
        // video piece
        GridLayoutManager glm = new GridLayoutManager(this, 4);
        rvVideo.setLayoutManager(glm);

        videos = new ArrayList<>();
        videoAdapter = new DetailsVideoRVAdapter(this, videos);
        videoAdapter.setItemActionsListener(this);
        rvVideo.setAdapter(videoAdapter);

        // app imgs
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAppImgs.setLayoutManager(llm);

        appImgs = new ArrayList<>();
        appImgsAdapter = new DetailsAppImgsRVAdapter(this, appImgs);
        rvAppImgs.setAdapter(appImgsAdapter);
    }

    private void initListener() {
    }

    // ------------------- dots start -------------------

    /**
     * get dots
     *
     * @param pageIndex
     * @return
     */
    private ImageView getDotIV(int pageIndex) {
        final int DEFAULT_LEFT_MARGIN = 7;
        final int FIRST_PAGE_INDEX = 0;

        int indicatorSize = PixelUtils.dp2px(this, INDICATOR_SIZE_BIG);
        int leftMargin = PixelUtils.dp2px(this, DEFAULT_LEFT_MARGIN);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.page_dots_shape_selector);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorSize, indicatorSize);
        if (pageIndex != FIRST_PAGE_INDEX) {
            // 设置圆点左边距
            params.leftMargin = leftMargin;
        }
        imageView.setLayoutParams(params);

        return imageView;
    }

    /**
     * update the dotIvs that is the big or small points
     *
     * @param position
     */
    private void updateDotIVs(int position) {
        for (ImageView dotIv : dotIVs) {
            dotIv.setSelected(false);

            LinearLayout.LayoutParams params = getDotLp(INDICATOR_SIZE_BIG, dotIv);
            dotIv.setLayoutParams(params);
        }

        ImageView curDotIv = dotIVs.get(position);
        LinearLayout.LayoutParams params = getDotLp(INDICATOR_SIZE_SMALL, curDotIv);
        curDotIv.setLayoutParams(params);

        curDotIv.setSelected(true);
    }

    public LinearLayout.LayoutParams getDotLp(int size, ImageView dotIv) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dotIv.getLayoutParams();
        params.width = PixelUtils.dp2px(this, size);
        params.height = PixelUtils.dp2px(this, size);

        return params;
    }

    // ------------------- dots end -------------------

    @OnClick({R.id.rl_play_video, R.id.iv_back, R.id.iv_next, R.id.iv_install_status, R.id.cb_fav})
    public void onClick(View view) {
        if (null == product) {
            toast("数据异常！");
            return;
        }

        switch (view.getId()) {
            case R.id.rl_play_video:
//                toast("播放");
//                String url = "http://218.200.69.66:8302/upload/Media/20150327/43bfda1b-7280-469c-a83b-82fa311c79d7.m4v";
                String url = product.getProductPlayAddr();
                VideoActivity.boot(this, url);
                break;

            case R.id.iv_back:
//                toast("上一页");

                appImgsCurPage--;
                if (appImgsCurPage <= 0) {
                    appImgsCurPage = DEFAULT_SHOW_IMG_INDEX;
                }
                rvAppImgs.scrollToPosition(appImgsCurPage);
                updateDotIVs(appImgsCurPage);
                break;

            case R.id.iv_next:
//                toast("下一页");

                appImgsCurPage++;
                if (appImgsCurPage >= appImgsAdapter.getItemCount()) {
                    appImgsCurPage = appImgsAdapter.getItemCount() - 1;
                }
                rvAppImgs.scrollToPosition(appImgsCurPage);
                updateDotIVs(appImgsCurPage);
                break;

            case R.id.iv_install_status:
//                toast("安装。。。");
                String mobile = AppContext.getInstance().getMobile();
                if (StringUtil.checkEmpty(mobile)) {
                    LoginActivity.boot(this);
                    return;
                }

//                String apkDownUrl = "http://app.znds.com/down/20161117/douyu_1.1.6_dangbei.apk";
                String apkDownUrl = product.getProductApkDownUrl();
                /** 下载最新版本Apk，打开手动进行安装。不修改底层框架（Framework）无法使用静默安装。 */
                ProgressDialogUtil.openHorizontalProgressDialog(DetailsActivity.this, "正在下载更新...");
                downloadApk(apkDownUrl, ProgressDialogUtil.getProgressDialog());
//                downloadApk(apkDownUrl);

                int productId = product.getId();
                ReqOperFavOrApp reqApp = new ReqOperFavOrApp(mobile, productId);
                operFavOrApp(TYPE_APP, MY_APP_ADD, reqApp);
                break;

            case R.id.cb_fav:                               // 添加或取消收藏
                String mobileFav = AppContext.getInstance().getMobile();
                if (StringUtil.checkEmpty(mobileFav)) {
                    cbFav.setChecked(false);
                    LoginActivity.boot(this);
                    return;
                }

                int productIdFav = product.getId();
                ReqOperFavOrApp reqFav = new ReqOperFavOrApp(mobileFav, productIdFav);
                if (!isFavChecked) {                // 收藏
                    operFavOrApp(TYPE_FAV, MY_FAV_ADD, reqFav);
                } else {                            // 取消收藏
                    operFavOrApp(TYPE_FAV, MY_FAV_CANCEL, reqFav);
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
                    MyUpdateNotification.initNotification(DetailsActivity.this,
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
                    VersionUtils.installApk(DetailsActivity.this, apkFile);

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

    // --------- DetailsAppImgsRVAdapter.OnItemActionsListener - onItemClick ----------
    @Override
    public void onItemClick(int pos) {
        toast(pos + ", 播放");

        if (null == videos || videos.isEmpty()) {
            return;
        }

        Product.VideoDOsBean videoBean = (Product.VideoDOsBean) videos.get(pos);
        String url = videoBean.getVideoUrl();
        VideoActivity.boot(this, url);
    }

    /**
     * add to the local sees
     *
     * @param tempPro
     */
    private void fillData(Product tempPro) {
        if (null == tempPro) {
            toast("数据异常！");
            return;
        }

        // images
        PhotoLoader.display(this, ivCover, tempPro.getProductCoverVerti(), R.mipmap.ic_launcher);
        PhotoLoader.display(this, ivGoodsDesc, tempPro.getProductDesc(), R.mipmap.ic_launcher);
        // qrCode
        String qrCodeUrl = "";
        if (Product.P_ENABLE == product.getProductAppEnabled()) {
            qrCodeUrl = tempPro.getProductAppQRCode();
        } else {
            qrCodeUrl = tempPro.getProductMicroStoreByecodeAddr();
        }
        PhotoLoader.display(this, ivTopQrCode, qrCodeUrl, R.mipmap.ic_launcher);

        // trial
        if (Product.P_ENABLE == product.getProductTrialEnabled()) {

            ivTrialTips.setVisibility(View.VISIBLE);
            ivGoodTrial.setVisibility(View.VISIBLE);
            PhotoLoader.display(this, ivGoodTrial, tempPro.getProductTrialAddr(), R.mipmap.ic_launcher);
        } else {

            ivTrialTips.setVisibility(View.GONE);
            ivGoodTrial.setVisibility(View.GONE);
        }

        // recommend
        if (tempPro.getProductRecommend() == Product.P_RCMD_YES) {
            ivLatest.setVisibility(View.VISIBLE);
        } else {
            ivLatest.setVisibility(View.GONE);
        }

        // show the camera
        if (Product.P_TYPE_APP == product.getType()) {      // APP Good
            rlApp.setVisibility(View.VISIBLE);
            rlVideo.setVisibility(View.GONE);

            // texts
            tvClass.setText(tempPro.getProductCategoryTag());
            tvDev.setText(tempPro.getProductDeveloper());
            tvUpdate.setText(DateUtil.getCNDate(tempPro.getProductModifyDate()));

            tvPrice.setText(String.valueOf(tempPro.getPrice()));
            // size
            if (StringUtil.checkEmpty(tempPro.getProductSize())) {
                tvSize.setText("0 MB");
            } else {
                tvSize.setText(tempPro.getProductSize() + " MB");
            }
            tvVer.setText(tempPro.getProductApkVersion());

            if (!CameraUtils.hasFrontFacingCamera()) {
                ivIsHasCamera.setVisibility(View.VISIBLE);
                ivInstallStatus.setImageResource(R.mipmap.details_install_no);
                ivInstallStatus.setEnabled(false);
            }
        } else {                                            // AR Good
            rlApp.setVisibility(View.GONE);
            rlVideo.setVisibility(View.VISIBLE);

            // texts
            tvVideoClass.setText(tempPro.getProductCategoryTag());
            tvVideoDev.setText(tempPro.getProductDeveloper());
            tvVideoUpdate.setText(DateUtil.getCNDate(tempPro.getProductModifyDate()));

            videos = product.getVideoDOs();
            videoAdapter.setDataSource(videos, false);
        }

        tvTitle.setText(tempPro.getName());
        // ratingBar
        final float socre = product.getProductScore();
        tvScore.setText(String.valueOf(socre));
        rbScore.setTouchable(false);
        rbScore.setStarMark(1 - (socre / 10f));

        // oper playVideoView
        if (Product.P_UNABLE == product.getProductPlayEnabled()) {

            ivPlayVideoTips.setVisibility(View.GONE);
            rlPlayVideo.setVisibility(View.GONE);
        } else {

            ivPlayVideoTips.setVisibility(View.VISIBLE);
            rlPlayVideo.setVisibility(View.VISIBLE);
        }

        // goodImgs
        fillGoodImgs();
    }

    private void fillGoodImgs() {
        if (StringUtil.checkEmpty(product.getProductImages())) {         // hide the app images
            ivAppImg.setVisibility(View.GONE);
            rlAppImgs.setVisibility(View.GONE);
            llIndicator.setVisibility(View.GONE);
            return;
        }

        // data
        final String IMG_SPLIT = ",";
        String[] strImgs = product.getProductImages().split(IMG_SPLIT);
        appImgs.clear();
        for (String img : strImgs) {
            appImgs.add(img);
        }
        appImgsAdapter.setDataSource(appImgs, false);

        // dot
        int size = appImgs.size();
        for (int i = 0; i < size; i++) {
            ImageView dotIv = getDotIV(i);
            dotIVs.add(dotIv);
            llIndicator.addView(dotIv);
        }
        updateDotIVs(appImgsCurPage);

        // back and next
        final int SCREEN_COUNT = 3;
        if (size > SCREEN_COUNT) {
            ivBack.setVisibility(View.VISIBLE);
            ivNext.setVisibility(View.VISIBLE);
        }
    }

    /**
     * add to the local sees
     *
     * @param tempPro
     */
    private void add2Sees(Product tempPro) {
        if (null == tempPro) {
            return;
        }

        List<Product> products = AppContext.getInstance().gainSees();
        if (null == products) {
            products = new ArrayList<>();
        }

        // remove the duplicate data
        List<Product> rmObjs = new ArrayList<>();
        for (Product product : products) {
            if (product.getId() == tempPro.getId()) {
                rmObjs.add(product);
            }
        }

        for (Product product : rmObjs) {
            products.remove(product);
        }

        tempPro.setProductModifyDate(DateUtil.getENDate());
        products.add(0, tempPro);
        AppContext.getInstance().saveSees(products);
    }

    // ################### Network Request Start ###################
    private void reqDetails(int productId) {
        showProgressDlg(null, true);

        Subscription subscription = apiService.getDetails(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FrontdoSubcriber<Response, Product>() {
                    @Override
                    public void onSuccess(String code, Product tempPro) {
                        dismissProgressDlg();
                        if (null == tempPro) {
                            return;
                        }

                        // updat the data
                        product = tempPro;
                        add2Sees(tempPro);
                        fillData(tempPro);

                        // get the fav status
                        String mobile = AppContext.getInstance().getMobile();
                        if (!StringUtil.checkEmpty(mobile)) {       // 已登录
                            reqFavState(mobile, tempPro.getId());
                        }
                    }

                    @Override
                    public void onFailure(String code, String message) {
                        dismissProgressDlg();
                        toast(message);

                        // 802: no data
                        if (code.equals("802")) {
                            finish();
                        }
                    }
                });
        addSubscription(subscription);
    }

    private void operFavOrApp(final String type, String status, ReqOperFavOrApp reqOperFavOrApp) {
        showProgressDlg(null, true);

        Subscription subscription = apiService.operFavOrApp(type, status, reqOperFavOrApp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FrontdoSubcriber<Response, OperFavOrApp>() {
                    @Override
                    public void onSuccess(String code, OperFavOrApp data) {
                        dismissProgressDlg();

                        if (ParamsHelper.TYPE_FAV.equals(type)) {       // 操作收藏功能（收藏与取消）
                            if (!isFavChecked) {        // 提示收藏成功

                                toast("收藏成功");
                                isFavChecked = true;
                                cbFav.setChecked(true);
                            } else {                    // 提示取消收藏成功

                                isFavChecked = false;
                                cbFav.setChecked(false);
                                toast("取消收藏成功");
                            }
                        } else {                                        // 操作应用功能（点击下载，添加后台成功）

                            FrontdoLogger.getLogger().i(TAG, "[ " + TAG + " - operFavOrApp] add app suc!");
                        }
                    }

                    @Override
                    public void onFailure(String code, String message) {
                        dismissProgressDlg();
                        toast(message);

                        cbFav.setChecked(isFavChecked);
                    }
                });
        addSubscription(subscription);
    }

    private void reqFavState(String mobile, int productId) {
        showProgressDlg(null, true);

        Subscription subscription = apiService.getDetailsFavStatus(mobile, productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FrontdoSubcriber<Response, GetFavState>() {
                    @Override
                    public void onSuccess(String code, GetFavState data) {
                        dismissProgressDlg();

                        if (GetFavState.STATE_FAV == data.getState()) {         // 已收藏

                            isFavChecked = true;
                            cbFav.setChecked(true);
                        } else {                                                // 未收藏

                            isFavChecked = false;
                            cbFav.setChecked(false);
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

    public static void boot(Context context, int productId) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(IKEY_PRODUCT_ID, productId);
        context.startActivity(intent);
    }
}
