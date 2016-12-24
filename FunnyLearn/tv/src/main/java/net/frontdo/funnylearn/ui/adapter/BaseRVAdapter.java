package net.frontdo.funnylearn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * ProjectName: BaseRVAdapter
 * Description: RecyclerView基类Adapter
 *
 * author: JeyZheng
 * version: 1.0
 * created at: 2016/8/30 9:41
 */
@SuppressWarnings("ALL")
public class BaseRVAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    protected Context mContext;
    protected LayoutInflater layoutInflater;

    // data list
    protected List mDatasource;

    public BaseRVAdapter(Context context, List data) {
        this.mContext = context;
        this.mDatasource = data;

        layoutInflater = LayoutInflater.from(context);
    }

    /**
     * refresh data and add more data
     *
     * @param data
     * @param loadMore  true: load more data, false: refresh data
     */
    public void setDataSource(List data, boolean loadMore) {
        if (loadMore) {
            if (null != data) {
                if (null == mDatasource) {
                    mDatasource = new ArrayList<>();
                }

                mDatasource.addAll(data);
            }
        } else {
            mDatasource = data;
        }

        notifyDataSetChanged();
//        notifyItemRangeChanged(0, getItemCount());
    }

    public List getDatasource() {
        return mDatasource;
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (null == mDatasource ? 0 : mDatasource.size());
//        return AppConstants.TEST_LIST_ITEM_COUNT;
    }
}
