package net.frontdo.funnylearn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.ui.entity.Product;
import net.frontdo.funnylearn.ui.widget.ZoomableTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ProjectName: AppsRVAdapter
 * Description: 详情APP图片适配器
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 10/23/2016 12:21
 */
public class DetailsVideoRVAdapter extends BaseRVAdapter<DetailsVideoRVAdapter.ItemViewHolder> {
    private static final String TAG = DetailsVideoRVAdapter.class.getSimpleName();

    public DetailsVideoRVAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 算父类宽度与高度
        View itemView = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.list_item_video_img, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Product.VideoDOsBean data = (Product.VideoDOsBean) mDatasource.get(position);
        if (null != data) {
            fillData(holder, data);
        }

        bindListener(holder, position);
    }

    private void bindListener(final ItemViewHolder holder, final int position) {
        holder.tvVideoPiece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (null != itemActionsListener) {
                    itemActionsListener.onItemClick(position);
                }
            }
        });
    }

    private void fillData(ItemViewHolder holder, Product.VideoDOsBean data) {

        holder.tvVideoPiece.setText(data.getVideoName());
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_video_piece)
        ZoomableTextView tvVideoPiece;
        @Bind(R.id.rl_video_piece)
        RelativeLayout rlVideoPiece;

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
    public interface OnItemActionsListener {
        /**
         * item的点击事件，用于进入详情页面
         *
         * @param pos
         */
        void onItemClick(int pos);
    }
}
