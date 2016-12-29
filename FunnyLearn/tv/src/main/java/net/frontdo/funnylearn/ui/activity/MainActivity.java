/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package net.frontdo.funnylearn.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.app.AppContext;
import net.frontdo.funnylearn.base.BaseFragment;
import net.frontdo.funnylearn.base.BaseHoldBackActivity;
import net.frontdo.funnylearn.common.ActivityStack;
import net.frontdo.funnylearn.common.DateUtil;
import net.frontdo.funnylearn.common.DeviceHelper;
import net.frontdo.funnylearn.common.StringUtil;
import net.frontdo.funnylearn.logger.FrontdoLogger;
import net.frontdo.funnylearn.net.FrontdoSubcriber;
import net.frontdo.funnylearn.ui.adapter.MainTitleRVAdapter;
import net.frontdo.funnylearn.ui.entity.CategoryBean;
import net.frontdo.funnylearn.ui.entity.MainInfo;
import net.frontdo.funnylearn.ui.entity.UserInfo;
import net.frontdo.funnylearn.ui.fragment.AppsFragment;
import net.frontdo.funnylearn.ui.fragment.BoutiqueFragment;
import net.frontdo.funnylearn.ui.fragment.GoodsFragment;
import net.frontdo.funnylearn.ui.fragment.MineFragment;
import net.frontdo.funnylearn.ui.pop.InputBaseInfoPop;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ProjectName: MainActivity
 * Description: 主页面
 * author: JeyZheng
 * version: 4.0
 * created at: 11/19/2016 22:44
 */
public class MainActivity extends BaseHoldBackActivity implements
        MainTitleRVAdapter.OnItemActionsListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int INDEX_BOUTIQUE = 0;
    private static final int INDEX_GOODS = 1;       // 识字拼音，英语，数学，故事，美术，科普，都在精品之后

    private static final int DEFAULT_SELECTED_INDEX = INDEX_BOUTIQUE;

    @Bind(R.id.recycler)
    RecyclerView recycler;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_age)
    TextView tvAge;
    @Bind(R.id.iv_sex)
    ImageView ivSex;
    @Bind(R.id.ll_sex)
    LinearLayout llSex;

    private Context mContext;
    private BaseFragment mCurFragment;
    // 存放需要的Fragment
    private List<BaseFragment> mFragments;

    private MainTitleRVAdapter adapter;
    private List<String> baseTitls;
    private String[] goodTitles = {"识字拼音", "英语", "数学", "故事", "美术", "科普"};
    private int[] categoryIds = {1, 2, 3, 4, 5, 6};

    // must input birthday and select sex
    private InputBaseInfoPop mInputBaseInfoPop;

    @Override
    protected int onBindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        reqServerData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        setSexAndAge(AppContext.getInstance().gainUserInfo());
    }

    private void reqServerData() {
//        int size = goodTitles.length;
//        for (int i = size - 1; i >= 0; i--) {
//            baseTitls.add(INDEX_GOODS, goodTitles[i]);
//
//            mFragments.add(INDEX_GOODS, GoodsFragment.newInstance(i));
//        }
//        adapter.setDataSource(baseTitls, false);

        // update title
        unlogin();
    }

    private void initView() {
        mContext = this;

        initTitle();
        // 添加基本三个不变的Fragment
        initFragments();
        initListener();
    }

    private void initTitle() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(llm);

        baseTitls = new ArrayList<>();
        baseTitls.add(getString(R.string.main_boutique));
        baseTitls.add(getString(R.string.main_apps));
        baseTitls.add(getString(R.string.main_mine));
        adapter = new MainTitleRVAdapter(this, baseTitls);
        adapter.setItemActionsListener(this);
        recycler.setAdapter(adapter);

        recycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                validBaseInfo();
            }
        }, 500);
        // sex and age
        llSex.bringToFront();
    }

    /**
     * init mFragments at main
     */
    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(BoutiqueFragment.newInstance());
        mFragments.add(AppsFragment.newInstance());
        mFragments.add(MineFragment.newInstance());

        switchMainFragment(mFragments.get(DEFAULT_SELECTED_INDEX));
    }

    private void initListener() {
        recycler.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (recycler.getChildCount() > 0) {
                        recycler.getChildAt(0).requestFocus();
                    }
                }
            }
        });
    }

    private BaseFragment getFragmentByPos(int pos) {
        return mFragments.get(pos);
    }

    private void switchMainFragment(BaseFragment fragment) {
        if (mCurFragment == fragment) {
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.main_content, fragment);
        }

        if (mCurFragment != null) {
            transaction.hide(mCurFragment);
        }

        transaction.commit();
        mCurFragment = fragment;

        // for updating the goods
        if (fragment instanceof GoodsFragment) {
            fragment.onResume();
        }
    }

    /**
     * judge the base info at first
     */
    private void validBaseInfo() {
        UserInfo uInfo = AppContext.getInstance().gainUserInfo();
        if (null == uInfo) {

            mInputBaseInfoPop = InputBaseInfoPop.getInstance(this);
            mInputBaseInfoPop.showPopupView(getRootView());
            mInputBaseInfoPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                    setSexAndAge(AppContext.getInstance().gainUserInfo());
                }
            });
        } else {

            setSexAndAge(uInfo);
        }
    }

    private void setSexAndAge(UserInfo uInfo) {
        if (null == uInfo) {
            return;
        }

        if (UserInfo.SEX_MALE == uInfo.getMemberBabyGender()) {

            tvSex.setText(R.string.mine_sex_boy);
            ivSex.setImageResource(R.mipmap.sex_boy);
        } else {

            tvSex.setText(R.string.mine_sex_girl);
            ivSex.setImageResource(R.mipmap.sex_girl);
        }

        String birthday = uInfo.getMemberBirthday();
        if (!StringUtil.checkEmpty(birthday)) {
//            String age = DateUtil.getYearMonth("2012-05-06");
            String age = DateUtil.getYearMonth(uInfo.getMemberBirthday());
            tvAge.setText(age);
        } else {

            AppContext.getInstance().logout();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mInputBaseInfoPop) {
            mInputBaseInfoPop.release();
        }
    }

    // ------------------ exit app --------------------
    private boolean isBacking = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isBacking) {
                hide();

                ActivityStack.getInstanse().exit();
            } else {
                isBacking = true;

                toast("再按一次退出！");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isBacking = false;
                        hide();
                    }
                }, 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

                        String[] titles = null;
                        int[] ids = null;           // for get the data
                        List<CategoryBean> categories = data.getCategoryDOs();
                        if (null != categories && !categories.isEmpty()) {

                            int size = categories.size();
                            titles = new String[size];
                            ids = new int[size];
                            for (int i = 0; i < size; i++) {
                                titles[i] = categories.get(i).getCategoryName();
                                ids[i] = categories.get(i).getId();
                            }
                        }

                        if (null == titles || titles.length <= 0) {
                            FrontdoLogger.getLogger().e(TAG, "[ " + TAG + " - unlogin] titles is empty!");
                            return;
                        }

                        // update the latest titles
                        goodTitles = titles;
                        categoryIds = ids;

                        // show the title view
                        int size = goodTitles.length;
                        for (int i = size - 1; i >= 0; i--) {

                            baseTitls.add(INDEX_GOODS, goodTitles[i]);
                            mFragments.add(INDEX_GOODS, GoodsFragment.newInstance(categoryIds[i]));
                        }
                        adapter.setDataSource(baseTitls, false);
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

    // --------- MainTitleRVAdapter.OnItemActionsListener - onItemClick ----------
    @Override
    public void onItemClick(int pos) {
        String resolution = DeviceHelper.getScreentWidth(this) + ", " + DeviceHelper.getScreentHight(this);
        toast("分辨率：" + resolution);

        BaseFragment fragment = getFragmentByPos(pos);
        switchMainFragment(fragment);
    }
}
