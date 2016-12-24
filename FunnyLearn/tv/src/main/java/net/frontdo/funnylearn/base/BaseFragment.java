package net.frontdo.funnylearn.base;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.api.FrontdoApiService;
import net.frontdo.funnylearn.app.AppContext;
import net.frontdo.funnylearn.common.StringUtil;
import net.frontdo.funnylearn.common.ToastHelper;
import net.frontdo.funnylearn.common.UIHelper;
import net.frontdo.funnylearn.ui.widget.ProgressDlgFragment;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * ProjectName: BaseFragment
 * Description:
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 2016/8/22 17:30
 */
public abstract class BaseFragment extends Fragment {
    public Context mContext;
    public Resources resources;

    protected ProgressDlgFragment dlgFragment;
    protected FrontdoApiService apiService;
    private CompositeSubscription compositeSubscription;

    public static final String BUNDLE_TYPE = "BUNDLE_TYPE";

    protected static BaseFragment getArgsFrag(BaseFragment frag) {

        Bundle bundle = new Bundle();
//        bundle.putInt(BUNDLE_TYPE, type);
        // Fragment传递数据方式
        frag.setArguments(bundle);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        resources = mContext.getResources();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);

        // network
        apiService = AppContext.getInstance().getFrontdoApiService();
        compositeSubscription = new CompositeSubscription();

        onCreateView(view);
        return view;
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
    public void onDestroyView() {
        super.onDestroyView();
        compositeSubscription.unsubscribe();
        ButterKnife.unbind(this);
    }

    protected abstract int getLayoutId();

    protected abstract void onCreateView(View view);

    /**
     * 重新调整顶部高度
     *
     * @param naviBar
     */
    protected void resizeNaviBar(final View naviBar) {
        naviBar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                UIHelper.resizeNaviBarOptimize(getActivity(), naviBar, naviBar.getHeight());

                naviBar.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
    }

    protected void toast(String content) {
        ToastHelper.toast(getActivity(), content, Toast.LENGTH_SHORT);
    }

    protected void toast(int resId) {
        ToastHelper.toast(getActivity(), resId, Toast.LENGTH_SHORT);
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
            dlgFragment.show(getActivity().getSupportFragmentManager(), null);
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
