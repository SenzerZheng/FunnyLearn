package net.frontdo.funnylearn.ui.fragment;

import android.view.View;
import android.widget.ImageView;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.app.AppConstants;
import net.frontdo.funnylearn.app.AppContext;
import net.frontdo.funnylearn.base.BaseFragment;
import net.frontdo.funnylearn.common.glide.PhotoLoader;
import net.frontdo.funnylearn.net.FrontdoSubcriber;
import net.frontdo.funnylearn.ui.activity.DateListActivity;
import net.frontdo.funnylearn.ui.activity.DetailsActivity;
import net.frontdo.funnylearn.ui.activity.LoginActivity;
import net.frontdo.funnylearn.ui.entity.LayoutBean;
import net.frontdo.funnylearn.ui.entity.MainInfo;
import net.frontdo.funnylearn.ui.entity.Product;
import net.frontdo.funnylearn.ui.pop.SelAgePop;
import net.frontdo.funnylearn.ui.widget.ZoomableImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ProjectName: BoutiqueFragment
 * Description: 精品页面
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 10/23/2016 11:45
 */
public class BoutiqueFragment extends BaseFragment implements SelAgePop.OnAgeAreaSeledListener {

    @Bind(R.id.iv_btq_age)
    ZoomableImageView ivBtqAge;
    @Bind(R.id.iv_latest)
    ImageView ivLatest;
    @Bind(R.id.iv_rcmd_02)
    ImageView ivRcmd02;
    @Bind(R.id.iv_rcmd_03)
    ImageView ivRcmd03;
    @Bind(R.id.iv_rcmd_04)
    ImageView ivRcmd04;
    @Bind(R.id.iv_cover_01)
    ImageView ivCover01;
    @Bind(R.id.iv_cover_02)
    ImageView ivCover02;
    @Bind(R.id.iv_cover_03)
    ImageView ivCover03;
    @Bind(R.id.iv_cover_04)
    ImageView ivCover04;

    private View rootView;
    private SelAgePop mSelAgePop;
    private List<LayoutBean> layouts;

    /**·
     * TODO: 根据用户的输入生日日期，得出的年龄段数，现只作为测试数据；需要协商将此功能去除
     */
    private int ageAreaType = AppConstants.AGE_ARE_THREE;

    public static BaseFragment newInstance() {
        BoutiqueFragment fragment = new BoutiqueFragment();
        return getArgsFrag(fragment);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_boutique;
    }

    @Override
    protected void onCreateView(View view) {

        rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);

        // update layout
        unlogin();
        changeSeledAgeBg(AppContext.getInstance().getAgeScope());
    }

    @OnClick({R.id.iv_btq_age, R.id.iv_btq_new, R.id.iv_btq_see, R.id.iv_btq_fav,
            R.id.rl_cover_01, R.id.rl_cover_02, R.id.rl_cover_03, R.id.rl_cover_04})
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_btq_age:
//                toast("切换年龄段");

                if (null == mSelAgePop) {
                    mSelAgePop = SelAgePop.getInstance(getActivity());
                    mSelAgePop.setOnAgeAreaSeledListener(this);
                }

                mSelAgePop.seledAge(ageAreaType);
                mSelAgePop.showPopupView(rootView);
                break;

            case R.id.iv_btq_new:
//                toast("最新");

                DateListActivity.boot(getActivity(), AppConstants.IVALUE_LIST_LATEST);
                break;

            case R.id.iv_btq_see:
//                toast("最近");

                DateListActivity.boot(getActivity(), AppConstants.IVALUE_LIST_SEES);
                break;

            case R.id.iv_btq_fav:
//                toast("我的收藏");

                if (!AppContext.getInstance().isLogin()) {
                    LoginActivity.boot(getActivity());
                    return;
                }
                DateListActivity.boot(getActivity(), AppConstants.IVALUE_LIST_FAV);
                break;

            case R.id.rl_cover_01:
                if (null == layouts || layouts.isEmpty()) {
                    toast(R.string.data_err);
                    return;
                }

//                toast("cover_01");
                DetailsActivity.boot(getActivity(), layouts.get(0).getLayoutValue());
                break;

            case R.id.rl_cover_02:
                if (null == layouts || layouts.isEmpty()) {
                    toast(R.string.data_err);
                    return;
                }

//                toast("cover_02");
                DetailsActivity.boot(getActivity(), layouts.get(1).getLayoutValue());
                break;

            case R.id.rl_cover_03:
                if (null == layouts || layouts.isEmpty()) {
                    toast(R.string.data_err);
                    return;
                }

//                toast("cover_03");
                DetailsActivity.boot(getActivity(), layouts.get(2).getLayoutValue());
                break;

            case R.id.rl_cover_04:
                if (null == layouts || layouts.isEmpty()) {
                    toast(R.string.data_err);
                    return;
                }

//                toast("cover_04");
                DetailsActivity.boot(getActivity(), layouts.get(3).getLayoutValue());
                break;

            default:
                break;
        }
    }

    @Override
    public void onSeleted(int seledType) {
        mSelAgePop.seledAge(seledType);
        ageAreaType = seledType;
        AppContext.getInstance().setAgeScope(seledType);

        changeSeledAgeBg(seledType);
    }

    private void changeSeledAgeBg(int seledType) {
        int resId = 0;
        switch (seledType) {
            case AppConstants.AGE_ARE_ALL:       // all age
                resId = R.mipmap.bg_btq_age_all;
                break;

            case AppConstants.AGE_ARE_THREE:     // three - four
                resId = R.mipmap.bg_btq_age_three;
                break;

            case AppConstants.AGE_ARE_FOUR:      // four - five
                resId = R.mipmap.bg_btq_age_four;
                break;

            case AppConstants.AGE_ARE_FIVE:      // five -six
                resId = R.mipmap.bg_btq_age_five;
                break;

            case AppConstants.AGE_ARE_SIX:       // six - seven
                resId = R.mipmap.bg_btq_age_six;
                break;

            default:
                break;
        }
        ivBtqAge.setImageResource(resId);
    }

    // ################### Network Request Start ###################
    private void unlogin() {
        showProgressDlg(null, true);

        Subscription subscription = apiService.userUnlogin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FrontdoSubcriber<Response, MainInfo>() {
                    @Override
                    public void onSuccess(String code, MainInfo data) {
                        dismissProgressDlg();

                        layouts = data.getLayoutDOs();
                        if (null != layouts && !layouts.isEmpty()) {
                            PhotoLoader.display(getActivity(), ivCover01,
                                    layouts.get(0).getLayoutProdUrl(), R.mipmap.place_holder);

                            PhotoLoader.display(getActivity(), ivCover02,
                                    layouts.get(1).getLayoutProdUrl(), R.mipmap.place_holder);
                            PhotoLoader.display(getActivity(), ivCover03,
                                    layouts.get(2).getLayoutProdUrl(), R.mipmap.place_holder);
                            PhotoLoader.display(getActivity(), ivCover04,
                                    layouts.get(3).getLayoutProdUrl(), R.mipmap.place_holder);

                            if (layouts.get(1).getProductRecommend() == Product.P_RCMD_NO) {
                                ivRcmd02.setVisibility(View.GONE);
                            } else {
                                ivRcmd02.setVisibility(View.VISIBLE);
                            }

                            if (layouts.get(2).getProductRecommend() == Product.P_RCMD_NO) {
                                ivRcmd03.setVisibility(View.GONE);
                            } else {
                                ivRcmd03.setVisibility(View.VISIBLE);
                            }

                            if (layouts.get(3).getProductRecommend() == Product.P_RCMD_NO) {
                                ivRcmd04.setVisibility(View.GONE);
                            } else {
                                ivRcmd04.setVisibility(View.VISIBLE);
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
