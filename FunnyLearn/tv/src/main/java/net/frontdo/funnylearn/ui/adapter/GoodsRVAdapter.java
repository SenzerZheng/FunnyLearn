package net.frontdo.funnylearn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.common.glide.PhotoLoader;
import net.frontdo.funnylearn.ui.entity.Product;
import net.frontdo.funnylearn.ui.widget.ZoomableRelativeLay;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ProjectName: GoodsRVAdapter
 * Description: 视频或APP商品列表
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 10/23/2016 12:21
 */
public class GoodsRVAdapter extends BaseRVAdapter<GoodsRVAdapter.ItemViewHolder> {

    public GoodsRVAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 算父类宽度与高度
        View itemView = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.list_item_good, parent, false);
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemActionsListener.onItemClick(position);
            }
        });
    }

    private void fillData(ItemViewHolder holder, Product data) {
        holder.tvName.setText(data.getName());
        PhotoLoader.display(mContext, holder.ivGood, data.getProductCoverVerti(), R.mipmap.place_holder);

        if (Product.P_TYPE_VIDEO == data.getType()) {          // AR

            holder.ivOverlay.setVisibility(View.VISIBLE);
        } else {                                            // APP

            holder.ivOverlay.setVisibility(View.GONE);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_good)
        ImageView ivGood;
        @Bind(R.id.iv_overlay)
        ImageView ivOverlay;
        @Bind(R.id.rl_good)
        RelativeLayout  rlGood;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_play_count)
        TextView tvPlayCount;
        @Bind(R.id.rl_good_root)
        ZoomableRelativeLay rlGoodRoot;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
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
    }
}
