package net.frontdo.funnylearn.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.common.DateUtil;
import net.frontdo.funnylearn.common.PhotoLoader;
import net.frontdo.funnylearn.ui.entity.Product;
import net.frontdo.funnylearn.ui.widget.ZoomableImageView;
import net.frontdo.funnylearn.ui.widget.ZoomableRelativeLay;

import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static net.frontdo.funnylearn.app.AppConstants.IVALUE_LIST_FAV;
import static net.frontdo.funnylearn.app.AppConstants.IVALUE_LIST_SEES;

/**
 * ProjectName: DateListRVAdapter
 * Description: Section格式的商品列表
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 2016/10/9 9:03
 */
@SuppressWarnings("ALL")
public class DateListRVAdapter extends SectionedRVAdapter<DateListRVAdapter.ItemViewHolder> {
    private int listType;

    public DateListRVAdapter(Context context, Comparator comparator, List datasource, int listType) {
        super(context, comparator, datasource);

        this.listType = listType;
    }

    @Override
    public void onBindHeaderViewHolder(ItemViewHolder holder, int section, int position) {
        // get the first index data per section
        Product startData = (Product) mDatasource.get(position - section);

        String showDate = "";
        if (listType == IVALUE_LIST_SEES) {                 // 最近访问

            showDate = startData.getProductUploadDate();
        } else {                                            // 最新上架 与 我的收藏

            showDate = DateUtil.formatYearMonth(startData.getProductUploadDate());
        }
        holder.tvSection.setText(showDate);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                layout = R.layout.list_item_date_list_header;
                break;

            case VIEW_TYPE_ITEM:
                layout = R.layout.list_item_date_list_base;
                break;

            default:
                layout = R.layout.list_item_date_list_extra;
                break;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int section, int relativePosition, int absolutePosition) {
        Product data = (Product) mDatasource.get(absolutePosition);
        if (null != data) {
            fillData(holder, section, data);
        }

        bindListener(holder, absolutePosition);
    }

    private void fillData(ItemViewHolder holder, int section, Product data) {
        // set tvTitle
        holder.tvName.setText(data.getName());

        // set cover
        PhotoLoader.display(mContext, holder.ivGood, data.getProductCoverVerti(), R.mipmap.place_holder);
        if (Product.P_TYPE_VIDEO == data.getType()) {          // AR

            holder.ivOverlay.setVisibility(View.VISIBLE);
        } else {                                            // APP

            holder.ivOverlay.setVisibility(View.GONE);
        }

        if (IVALUE_LIST_FAV == listType) {                  // 我的收藏，需要显示取消按钮
            holder.ivCancel.setVisibility(View.VISIBLE);
        } else {
            holder.ivCancel.setVisibility(View.GONE);
        }
    }

    private void bindListener(ItemViewHolder holder, final int absolutePosition) {
        holder.rlGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemActionsListener) {
                    itemActionsListener.onItemClick(absolutePosition);
                }
            }
        });

        holder.ivGood.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                final int TOP_ITEM_COUNT = 5;
                if (hasFocus) {
                    if (absolutePosition < TOP_ITEM_COUNT) {
                        itemActionsListener.onScrollToTop();
                    }
                }
            }
        });

        holder.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemActionsListener) {
                    itemActionsListener.onCancel(absolutePosition);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        return super.getItemViewType(section, relativePosition, absolutePosition);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        // ---------- list_item_date_list_header ------------
        @Nullable
        @Bind(R.id.tv_section)
        TextView tvSection;

        // ---------- list_item_date_list_base ------------
        @Nullable
        @Bind(R.id.iv_good)
        ImageView ivGood;
        @Nullable
        @Bind(R.id.iv_overlay)
        ImageView ivOverlay;
        @Nullable
        @Bind(R.id.rl_good)
        ZoomableRelativeLay rlGood;
        @Nullable
        @Bind(R.id.iv_cancel)
        ZoomableImageView ivCancel;
        @Nullable
        @Bind(R.id.tv_name)
        TextView tvName;
        @Nullable
        @Bind(R.id.tv_play_count)
        TextView tvPlayCount;
        @Nullable
        @Bind(R.id.rl_good_root)
        RelativeLayout rlGoodRoot;

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

        /**
         * 滚动到顶部，主要用于使得最上层的日期可以显示出来
         */
        void onScrollToTop();

        /**
         * 取消收藏
         */
        void onCancel(int pos);
    }
}