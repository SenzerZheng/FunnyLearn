package net.frontdo.funnylearn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.frontdo.funnylearn.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ProjectName: MainTitleRVAdapter
 * Description:
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 10/23/2016 12:21
 */
public class MainTitleRVAdapter extends BaseRVAdapter<MainTitleRVAdapter.ItemViewHolder> {

    public MainTitleRVAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 算父类宽度与高度
        View itemView = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.list_item_main_title, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        String data = (String) mDatasource.get(position);
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

        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    holder.tvTitle.getPaint().setFakeBoldText(true);
                } else {

                    holder.tvTitle.getPaint().setFakeBoldText(false);
                }
                // must be added, else not effective
                holder.tvTitle.invalidate();
            }
        });
    }

    private void fillData(ItemViewHolder holder, String data) {
        holder.tvTitle.setText(data);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;

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
