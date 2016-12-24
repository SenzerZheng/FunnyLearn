package net.frontdo.funnylearn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.frontdo.funnylearn.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * ProjectName: TemplateRecycleAdapter
 * Description:
 *
 * author: JeyZheng
 * version: 4.0
 * created at: 2016/8/30 9:37
 */
public class TemplateRecycleAdapter extends BaseRVAdapter<TemplateRecycleAdapter.ItemViewHolder> {

    public TemplateRecycleAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 算父类宽度与高度
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.a_template, parent, false);
        // 不算父类宽度与高度
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null);
        ItemViewHolder viewHolder = new ItemViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Object data = mDatasource.get(position);
        if (null != data) {
            fillData(holder, data, position);
        }

        bindListener(holder);
    }

    private void bindListener(RecyclerView.ViewHolder holder) {
    }

    private void fillData(ItemViewHolder holder, Object data, int position) {
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.tv_useCondition)
//        TextView tvUseCondition;

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
         * click the button of reading at DownloadStateView
         *
         * @param pos
         */
        void onClickDlStateViewRead(int pos);
    }
}
