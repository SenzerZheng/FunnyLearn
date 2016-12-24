package net.frontdo.funnylearn.ui.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.common.PhotoLoader;
import net.frontdo.funnylearn.logger.FrontdoLogger;
import net.frontdo.funnylearn.ui.entity.Product;
import net.frontdo.funnylearn.ui.widget.ZoomableImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ProjectName: AppsRVAdapter
 * Description: 我的应用
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 10/23/2016 12:21
 */
public class AppsRVAdapter extends BaseRVAdapter<AppsRVAdapter.ItemViewHolder> {

    private ImageView focusedIv;

    private static final String TAG = AppsRVAdapter.class.getSimpleName();

    public AppsRVAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 算父类宽度与高度
        View itemView = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.list_item_app, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Product data = (Product) mDatasource.get(position);
        if (null != data) {
            fillData(holder, data);
        }

        bindListener(holder, position);
    }

    private void bindListener(final ItemViewHolder holder, final int position) {
        holder.ivApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (null != itemActionsListener) {

                    itemActionsListener.onItemClick(position);
                }
            }
        });

        holder.ivApp.setOnZoomableFocusedChangeListener(
                new ZoomableImageView.OnZoomableFocusedChangeListener() {
                    @Override
                    public void onFocusChanged(boolean gainFocus, int direction) {
                        FrontdoLogger.getLogger().d(TAG, "ivApp gainFocus, " + gainFocus + ", " + position);
                        if (gainFocus) {
                            if (null != focusedIv) {
                                focusedIv.clearAnimation();
                                focusedIv.setVisibility(View.GONE);
                            }
                            focusedIv = holder.ivDel;

                            holder.ivDel.clearAnimation();
                            holder.ivDel.setVisibility(View.VISIBLE);
                        } else {

                            holder.ivDel.setVisibility(View.GONE);
                        }
                    }
                });

        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemActionsListener) {

                    itemActionsListener.onDelClick(position);
                }
            }
        });
    }

    private void fillData(ItemViewHolder holder, Product data) {
        PhotoLoader.display(mContext, holder.ivApp, data.getProductCoverHori(), R.mipmap.ic_launcher);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_app)
        ZoomableImageView ivApp;
        @Bind(R.id.iv_del)
        ZoomableImageView ivDel;
        @Bind(R.id.rl_app_root)
        RelativeLayout rlAppRoot;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // -------------- PropertyAnimator --------------
    private static final float SCALE_START = 1.0f;
    private static final float SCALE_END = 1.16f;

    private void zoomIn(ImageView iv) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", SCALE_START, SCALE_END);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", SCALE_START, SCALE_END);

        Animator animator = ObjectAnimator.ofPropertyValuesHolder(iv, pvhX, pvhY);
        animator.setDuration(200);
        animator.start();
    }

    private void zoomOut(ImageView iv) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", SCALE_END, SCALE_START);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", SCALE_END, SCALE_START);

        Animator animator = ObjectAnimator.ofPropertyValuesHolder(iv, pvhX, pvhY);
        animator.setDuration(200);
        animator.start();
    }

    private OnItemActionsListener itemActionsListener;

    /**
     * 监听赋值
     *
     * @param l
     */
    public void setItemActionsListener(OnItemActionsListener l) {
        itemActionsListener = l;
    }

    /**
     * 处理Item中, 各个点击事件
     */
    public static interface OnItemActionsListener {
        /**
         * item的点击事件，用于进入详情页面
         *
         * @param pos
         */
        void onItemClick(int pos);

        /**
         * 点击卸载APK事件
         *
         * @param pos
         */
        void onDelClick(int pos);
    }
}
