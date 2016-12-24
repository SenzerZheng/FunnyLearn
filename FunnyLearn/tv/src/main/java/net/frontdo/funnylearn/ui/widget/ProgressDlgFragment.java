/*
 * ProgressDlgFragment.java
 * classes : com.spider.subscriber.fragment.ProgressDlgFragment
 * @author liujun
 * V 1.0.0
 * Create at 2015-6-3 下午6:37:33
 * Copyright (c) 2015年  Spider. All Rights Reserved.
 */
package net.frontdo.funnylearn.ui.widget;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.frontdo.funnylearn.R;

/**
 * ProjectName: ProgressDlgFragment
 * Description: 对话框Fragment
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 2016/8/12 17:36
 */
@SuppressWarnings("ALL")
public class ProgressDlgFragment extends DialogFragment {
    private static final String TAG = "ProgressDlgFragment";

    private ImageView progress_ImageView;

    private TextView textView;

    private Animation anim;

    private String txt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.BaseDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loading_gif, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progress_ImageView = (ImageView) view.findViewById(R.id.iv_loading_gif);
        Glide.with(getContext()).load(R.mipmap.loading_anim).asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(progress_ImageView);

    }

    /**
     * 开始动画
     */
    public void startAnimation() {
        if (anim == null) {
            anim = AnimationUtils.loadAnimation(getActivity(), R.anim.loading_progress);
        }

        progress_ImageView.startAnimation(anim);

    }

    public void stopAnimation() {
        progress_ImageView.clearAnimation();
    }

    @Override
    public void onResume() {
        super.onResume();

//		startAnimation();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

//		if (!hidden)
//			startAnimation();
    }

    @Override
    public void onDetach() {
        super.onDetach();

//		stopAnimation();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//		stopAnimation();
    }


    public void setText(String txt) {
        this.txt = txt;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
