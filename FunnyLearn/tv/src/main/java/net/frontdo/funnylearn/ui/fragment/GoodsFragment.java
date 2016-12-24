package net.frontdo.funnylearn.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.app.AppContext;
import net.frontdo.funnylearn.base.BaseFragment;
import net.frontdo.funnylearn.net.FrontdoSubcriber;
import net.frontdo.funnylearn.ui.activity.DetailsActivity;
import net.frontdo.funnylearn.ui.adapter.GoodsRVAdapter;
import net.frontdo.funnylearn.ui.entity.Product;
import net.frontdo.funnylearn.ui.widget.ZoomableImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ProjectName: GoodsFragment
 * Description: 常规普通页面（识字拼音，英语，数学，故事，美术，科普。。。）
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 10/23/2016 11:45
 */
public class GoodsFragment extends BaseFragment implements
        GoodsRVAdapter.OnItemActionsListener {

    private static final String TAG = GoodsFragment.class.getSimpleName();
    private static final int START_INDEX = 0;
    private static final int PAGE_SIZE = 5;

    @Bind(R.id.recycler)
    RecyclerView recycler;
    @Bind(R.id.iv_previous)
    ZoomableImageView ivPrevious;
    @Bind(R.id.iv_next)
    ZoomableImageView ivNext;

    private LinearLayoutManager llmGoods;
    private GoodsRVAdapter adapter;
    private List mDatasource;

    private int categoryId;

    private boolean isFirstLoad = true;

    public static BaseFragment newInstance(int categoryId) {
        GoodsFragment fragment = new GoodsFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_TYPE, categoryId);
        // Fragment传递数据方式
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods;
    }

    @Override
    protected void onCreateView(View view) {

        Bundle bundle = getArguments();
        if (bundle.containsKey(BUNDLE_TYPE)) {
            categoryId = bundle.getInt(BUNDLE_TYPE);
        }

        initView();
        reqClassList();

        recycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isFirstLoad = false;
            }
        }, 500);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isFirstLoad) {         // update the data by the different ageScope

//            toast("updating");
            reqClassList();
        }
    }

    private void initView() {
        llmGoods = new LinearLayoutManager(getActivity());
        llmGoods.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(llmGoods);
    }

    @OnClick({R.id.iv_previous, R.id.iv_next})
    public void onClickEvent(View view) {
        int firstItemPos = llmGoods.findFirstVisibleItemPosition();
        int lastItemPos = llmGoods.findLastVisibleItemPosition();

        int delta = 0;
        switch (view.getId()) {
            case R.id.iv_previous:          // 上一页
                toast("上一页");

                delta = firstItemPos - PAGE_SIZE;
                if (delta < START_INDEX) {
                    delta = START_INDEX;
                }
                recycler.scrollToPosition(delta);
                break;

            case R.id.iv_next:              // 下一页
                toast("下一页");

                delta = lastItemPos + PAGE_SIZE;
                if (delta >= adapter.getItemCount()) {
                    delta = adapter.getItemCount() - 1;
                }
                recycler.scrollToPosition(delta);
                break;

            default:
                break;
        }
    }

    // --------- GoodsRVAdapter.OnItemActionsListener - onItemClick ---------
    @Override
    public void onItemClick(int pos) {
        if (mDatasource.isEmpty()) {
            return;
        }

        Product product = (Product) mDatasource.get(pos);
        DetailsActivity.boot(getActivity(), product.getId());
    }

    // ################### Network Request Start ###################
    private void reqClassList() {
        showProgressDlg(null, true);

        int aScope = AppContext.getInstance().getAgeScope();
        Subscription subscription = apiService.getCategoryList(categoryId, aScope)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FrontdoSubcriber<Response, List<Product>>() {
                    @Override
                    public void onSuccess(String code, List<Product> products) {
                        dismissProgressDlg();
                        if (null != products && !products.isEmpty()) {

                            mDatasource = products;
                            adapter = new GoodsRVAdapter(getActivity(), products);
                            adapter.setItemActionsListener(GoodsFragment.this);
                            recycler.setAdapter(adapter);

                            final int SCREEN_COUNT = 5;
                            if (products.size() > SCREEN_COUNT) {
                                ivPrevious.setVisibility(View.VISIBLE);
                                ivNext.setVisibility(View.VISIBLE);
                            }
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
