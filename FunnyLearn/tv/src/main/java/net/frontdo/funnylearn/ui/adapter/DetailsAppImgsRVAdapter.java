package net.frontdo.funnylearn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.common.PhotoLoader;
import net.frontdo.funnylearn.ui.widget.ZoomableImageView;

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
public class DetailsAppImgsRVAdapter extends BaseRVAdapter<DetailsAppImgsRVAdapter.ItemViewHolder> {
    private ImageView focusedIv;

    private static final String TAG = DetailsAppImgsRVAdapter.class.getSimpleName();

    public DetailsAppImgsRVAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 算父类宽度与高度
        View itemView = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.list_item_app_img, parent, false);
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
    }

    private void fillData(ItemViewHolder holder, String data) {
        PhotoLoader.display(mContext, holder.ivAppImg, data, R.mipmap.place_holder);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_app_img)
        ZoomableImageView ivAppImg;
        @Bind(R.id.rl_app_img_root)
        RelativeLayout rlAppImgRoot;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
