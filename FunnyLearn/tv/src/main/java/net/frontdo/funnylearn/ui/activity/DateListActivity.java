package net.frontdo.funnylearn.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.app.AppConstants;
import net.frontdo.funnylearn.app.AppContext;
import net.frontdo.funnylearn.base.BaseHoldBackActivity;
import net.frontdo.funnylearn.common.DateUtil;
import net.frontdo.funnylearn.common.PixelUtil;
import net.frontdo.funnylearn.common.StringUtil;
import net.frontdo.funnylearn.net.FrontdoSubcriber;
import net.frontdo.funnylearn.net.bean.ReqOperFavOrApp;
import net.frontdo.funnylearn.ui.adapter.DateListRVAdapter;
import net.frontdo.funnylearn.ui.entity.DateListGood;
import net.frontdo.funnylearn.ui.entity.OperFavOrApp;
import net.frontdo.funnylearn.ui.entity.Product;
import net.frontdo.funnylearn.ui.widget.SlidingRecyclerView;
import net.frontdo.funnylearn.ui.widget.ZoomableImageView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.frontdo.funnylearn.api.ParamsHelper.MY_FAV_CANCEL;
import static net.frontdo.funnylearn.api.ParamsHelper.TYPE_FAV;
import static net.frontdo.funnylearn.app.AppConstants.IKEY_DATE_LIST_TYPE;
import static net.frontdo.funnylearn.app.AppConstants.IVALUE_LIST_FAV;
import static net.frontdo.funnylearn.app.AppConstants.IVALUE_LIST_LATEST;
import static net.frontdo.funnylearn.app.AppConstants.IVALUE_LIST_SEES;

/**
 * ProjectName: DateListActivity
 * Description: 日期列表（我的收藏（Server），最新上架（Server），最近访问（Local，进入详情时记录））
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 11/06/2016 11:21
 */
public class DateListActivity extends BaseHoldBackActivity implements
        DateListRVAdapter.OnItemActionsListener {
    private static final String TAG = DateListActivity.class.getSimpleName();

    // mGoods recyclerView: column
    public static final int GOODS_DEFAULT_COLUMN = 5;
    public static final int DATE_LIST_TOP_INDEX = 0;

    @Bind(R.id.iv_back)
    ZoomableImageView ivBack;
    @Bind(R.id.ll_left)
    LinearLayout llLeft;
    @Bind(R.id.view_line)
    View viewLine;
    @Bind(R.id.rv_list)
    SlidingRecyclerView rvList;
    @Bind(R.id.iv_left_title)
    ImageView ivLeftTitle;
    @Bind(R.id.iv_empty)
    ImageView ivEmpty;

    private GridLayoutManager manager;
    private DateListRVAdapter mGoodsAdapter;
    private List<Product> mGoods;

    private float lineTranslationY = 0;
    private Comparator mComparator = new Comparator() {
        @Override
        public int compare(Object lObj, Object rObj) {
            // yyyy-MM compare to yyyy-MM
            String lDate = DateUtil.formatYearMonth(((Product) lObj).getProductModifyDate());
            String rDate = DateUtil.formatYearMonth(((Product) rObj).getProductModifyDate());
            return lDate.compareTo(rDate);
        }
    };

    private int listType;               // list type

    @Override
    protected int onBindLayout() {
        return R.layout.activity_date_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra(AppConstants.IKEY_DATE_LIST_TYPE)) {
            listType = intent.getIntExtra(IKEY_DATE_LIST_TYPE, IVALUE_LIST_LATEST);
        }

        initListView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (listType == IVALUE_LIST_FAV) {
            String mobile = AppContext.getInstance().getMobile();
            reqMyFav(TYPE_FAV, mobile);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (null == mGoods) {
            return;
        }

        // set the line height
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewLine.getLayoutParams();

        final int FULL_SCREEN_SECTION = 2;
        final int FULL_SCREEN_SIZE = 10;
        final int BOTTOM_DIS = 25;
        int lineH = 0;
        int section = mGoodsAdapter.getSectionSize();
        int size = mGoodsAdapter.getDatasource().size();
        if (section < FULL_SCREEN_SECTION) {
            if (size <= FULL_SCREEN_SIZE) {
                lineH = rvList.getMeasuredHeight() - PixelUtil.dp2px(BOTTOM_DIS, this);
            } else {
                lineH = rvList.getMeasuredHeight();
            }
        } else {
            lineH = rvList.getMeasuredHeight();
        }
        layoutParams.height = lineH;
        viewLine.setLayoutParams(layoutParams);
    }

    private void initListView() {
        mGoods = new ArrayList<>();
        mGoodsAdapter = new DateListRVAdapter(this, mComparator, mGoods, listType);

        manager = new GridLayoutManager(this, GOODS_DEFAULT_COLUMN);
        mGoodsAdapter.setLayoutManager(manager);

        rvList.setLayoutManager(manager);
        rvList.setAdapter(mGoodsAdapter);
        rvList.setCanSliding(false);
        mGoodsAdapter.setItemActionsListener(this);
        rvList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (manager.findFirstVisibleItemPosition() == 0
                        && rvList.getChildAt(0).getTop() == 0) {
                    lineTranslationY = 0;
                } else {
                    if (dy > 0) {
                        if (lineTranslationY > -25) {
                            lineTranslationY -= 5;
                        }
                    }
                }
                viewLine.setTranslationY(lineTranslationY);
//                FrontdoLogger.getLogger().e(TAG, "[ " + TAG + " - onScrolled] dx " + dx + ", dy " + dy
//                        + ", ty " + rvList.getChildAt(0).getTop());
            }
        });
    }

    private void initData() {
//        showProgressDlg(null, false);
//        mGoods = getTestData();
//        mGoodsAdapter.setDataSource(mGoods, false);

        // 最近访问需要显示本地，其他两个显示后台
        if (listType == IVALUE_LIST_SEES) {                 // 最近访问
            ivLeftTitle.setImageResource(R.mipmap.date_list_left_sees);

            mComparator = new Comparator() {
                @Override
                public int compare(Object lObj, Object rObj) {
                    // yyyy-MM-dd compare to yyyy-MM-dd
                    String lDate = ((Product) lObj).getProductModifyDate();
                    String rDate = ((Product) rObj).getProductModifyDate();
                    return lDate.compareTo(rDate);
                }
            };
            mGoodsAdapter.setComparator(mComparator);

            mGoods = getRecentThreeDaysData();
            mGoodsAdapter.setDataSource(mGoods, false);
        } else if (listType == IVALUE_LIST_LATEST) {        // 最新上架
            ivLeftTitle.setImageResource(R.mipmap.date_list_left_latest);

            reqLatestPublish();
        } else {                                            // 我的收藏
            ivLeftTitle.setImageResource(R.mipmap.date_list_left_fav);
        }
    }

    private List<Product> getRecentThreeDaysData() {
        final int RECENT_DAY_COUNT = 3;
        List<Product> srcPros = AppContext.getInstance().gainSees();
        if (null == srcPros || srcPros.isEmpty()) {
            ivEmpty.setVisibility(View.VISIBLE);
            return new ArrayList<>();
        }

        ivEmpty.setVisibility(View.GONE);
        List<Product> desPros = new ArrayList<>();
        desPros.add(srcPros.get(0));

        int dayCount = 0;
        int size = srcPros.size();
        for (int i = 1; i < size; i++) {
            if (mComparator.compare(srcPros.get(i - 1), srcPros.get(i)) != 0) {
                dayCount++;
            }

            if (dayCount >= RECENT_DAY_COUNT) {
                break;
            }
            desPros.add(srcPros.get(i));
        }
        return desPros;
    }

    /**
     * TODO: 测试数据，后期删除
     *
     * @return
     */
    private List getTestData() {
        List<DateListGood> issues = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            DateListGood issue = new DateListGood();
            if (i < 5) {
                issue.setDate("2016.08.01");
            } else if (i < 15) {
                issue.setDate("2016.09.01");
            } else if (i < 23) {
                issue.setDate("2016.09.01");
            } else {
                issue.setDate("2016.10.01");
            }

            issues.add(issue);
        }

        return issues;
    }

    // ----------- OnItemActionsListener - onItemClick ----------
    @Override
    public void onItemClick(int pos) {

//        toast(pos + " selected");
        Product product = mGoods.get(pos);
        DetailsActivity.boot(this, product.getId());
    }

    @Override
    public void onScrollToTop() {
        rvList.smoothScrollToPosition(DATE_LIST_TOP_INDEX);
    }

    @Override
    public void onCancel(int pos) {
//        if (AppConstants.IVALUE_LIST_FAV != listType) {
//            return;
//        }
        toast("onCancel " + pos);
        String mobile = AppContext.getInstance().getMobile();
        if (StringUtil.checkEmpty(mobile)) {
            toast(R.string.data_err);
            return;
        }

        int productId = mGoods.get(pos).getId();
        ReqOperFavOrApp reqOperFavOrApp = new ReqOperFavOrApp(mobile, productId);
        cancelFav(TYPE_FAV, MY_FAV_CANCEL, reqOperFavOrApp);
    }

    @OnClick(R.id.iv_back)
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            default:
                break;
        }
    }

    // ################### Network Request Start ###################
    private void reqLatestPublish() {
        showProgressDlg(null, true);

        Subscription subscription = apiService.getLatestPublishs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FrontdoSubcriber<Response, List<Product>>() {
                    @Override
                    public void onSuccess(String code, List<Product> products) {
                        dismissProgressDlg();
                        if (null != products && !products.isEmpty()) {
                            ivEmpty.setVisibility(View.GONE);

                            mGoods = products;
                            mGoodsAdapter.setDataSource(products, false);
                        } else {
                            ivEmpty.setVisibility(View.VISIBLE);
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

    private void reqMyFav(String type, String mobile) {
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

                            mGoods = products;
                            mGoodsAdapter.setDataSource(products, false);
                        } else {
                            ivEmpty.setVisibility(View.VISIBLE);
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

    private void cancelFav(String type, String status, final ReqOperFavOrApp reqOperFavOrApp) {
        showProgressDlg(null, true);

        Subscription subscription = apiService.operFavOrApp(type, status, reqOperFavOrApp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FrontdoSubcriber<Response, OperFavOrApp>() {
                    @Override
                    public void onSuccess(String code, OperFavOrApp result) {
                        dismissProgressDlg();

                        toast("取消收藏成功");
                        int productId = reqOperFavOrApp.getProductId();
                        List<Product> products = mGoodsAdapter.getDatasource();
                        for (Product product : products) {
                            if (product.getId() == productId) {

                                products.remove(product);
                                break;
                            }
                        }

                        if (null == products || products.isEmpty()) {
                            ivEmpty.setVisibility(View.VISIBLE);
                        }
                        mGoodsAdapter.notifyDataSetChanged();
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

    public static void boot(Context context, int listType) {
        Intent intent = new Intent(context, DateListActivity.class);
        intent.putExtra(IKEY_DATE_LIST_TYPE, listType);
        context.startActivity(intent);
    }
}
