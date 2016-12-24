package net.frontdo.funnylearn.ui.adapter;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import net.frontdo.funnylearn.logger.FrontdoLogger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * ProjectName: SectionedRVAdapter
 * Description:
 * 注意：
 * 1. 此SectionedRVAdapter目前只适用于GridView样式，其他样式待开发
 * <p>
 * 逻辑：
 * 1. 主要通过实现RecyclerView.Adapter的getViewType来实现Section
 * 2. 通过控制getItemCount，实现每个Section有多少个items
 * 3. 通过isHeader与mHeaderLocationMap，将Section与items切换
 * 4. 通过重载RecyclerView.Adapter中的几个方法，然后hide这几个方法，实现自定义的功能
 * 5. TODO：还有一些功能待优化
 * <p>
 * author Aidan Follestad (afollestad)
 * upgrade: JeyZheng
 * version: 4.0
 * created at: 2016/9/28 21:15
 */
@SuppressWarnings("ALL")
public abstract class SectionedRVAdapter<VH extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<VH> {
    private static final String TAG = SectionedRVAdapter.class.getSimpleName();

    protected final static int VIEW_TYPE_HEADER_OTHER = -0x001;         // header base
    protected final static int VIEW_TYPE_HEADER = -0x002;               // header other
    protected final static int VIEW_TYPE_ITEM = -0x101;                 // item base
    protected final static int VIEW_TYPE_ITEM_OTHER = -0x102;           // item other

    protected Context mContext;
    protected Comparator mComparator;
    protected List mDatasource;

    private GridLayoutManager mLayoutManager;
    private ArrayMap<Integer, Integer> mSpanMap;                    // span(sections: 4, item: 1)
    private final ArrayMap<Integer, Integer> mHeaderLocationMap;    // header index(allCount, sectionIndex)

    // key = sectionCount, value = itemCount per section
    private ArrayMap<Integer, Integer> mSectionItemsMap;

    private boolean isShowHeadersForEmptySections;

    public SectionedRVAdapter() {
        mHeaderLocationMap = new ArrayMap<>();
        mSectionItemsMap = new ArrayMap<>();
    }

    /**
     * @param context
     * @param comparator important
     * @param datasource
     */
    public SectionedRVAdapter(Context context, Comparator comparator, List datasource) {
        this.mContext = context;
        this.mComparator = comparator;
        this.mDatasource = datasource;

        mHeaderLocationMap = new ArrayMap<>();
        mSectionItemsMap = new ArrayMap<>();
    }

    /**
     * update the comparator
     *
     * @param comparator
     */
    public void setComparator(Comparator comparator) {
        this.mComparator = comparator;
    }

    /**
     * refresh data and add more data
     *
     * @param data
     * @param loadMore true: load more data, false: refresh data
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

    /**
     * Instructs the list view adapter to whether show headers for empty sections or not.
     *
     * @param show flag indicating whether headers for empty sections ought to be shown.
     */
    public final void shouldShowHeadersForEmptySections(boolean show) {
        isShowHeadersForEmptySections = show;
    }

    public final void setLayoutManager(@Nullable GridLayoutManager lm) {
        mLayoutManager = lm;

        if (lm == null) {
            return;
        }

        // set the different span between header and item
        lm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {          // span: 跨度
                if (isHeader(position)) {                   // header: spanSize = columns = 4
                    return mLayoutManager.getSpanCount();
                }

                final int[] sectionAndPos = getSectionIndexAndRelativePosition(position);
                final int absPos = position - (sectionAndPos[0] + 1);

                return getRowSpan(
                        mLayoutManager.getSpanCount(), sectionAndPos[0], sectionAndPos[1], absPos);
            }
        });
    }

    // returns section along with offsetted position
    private int[] getSectionIndexAndRelativePosition(int itemPosition) {
        synchronized (mHeaderLocationMap) {
            Integer lastSectionIndex = -1;
            for (final Integer sectionIndex : mHeaderLocationMap.keySet()) {
                if (itemPosition > sectionIndex) {
                    lastSectionIndex = sectionIndex;
                } else {
                    break;
                }
            }

            return new int[]{mHeaderLocationMap.get(lastSectionIndex), itemPosition - lastSectionIndex - 1};
        }
    }

    @SuppressWarnings("UnusedParameters")
    protected int getRowSpan(int fullSpanSize, int section, int relativePosition, int absolutePosition) {
        return 1;
    }

    /**
     * details to see {@link #getSectionCount()}
     *
     * @return
     */
    public int getSectionSize() {
        return mSectionItemsMap.size();
    }

    /**
     * get the itemCount of per section
     *
     * @param section
     * @return
     */
    protected int getPSItemCount(int section) {
        return mSectionItemsMap.get(section);
    }

    // --------------- abstract method -----------------
    public final boolean isHeader(int position) {
        return mHeaderLocationMap.get(position) != null;
    }

    public abstract void onBindHeaderViewHolder(VH holder, int section, int position);

    public abstract void onBindViewHolder(VH holder, int section, int relativePosition, int absolutePosition);

    // --------------- hide the method at the recyclerView -----------------

    /**
     * @hide
     */
    @Override
    public final int getItemCount() {
        mHeaderLocationMap.clear();
        mSectionItemsMap.clear();

        // all count: the data and section count
        int allItemCount = 0;
        for (int section = 0; section < getSectionCount(); section++) {
            // itemCount = 4, 8, 4, 8
            int perSectionItemCount = getPSItemCount(section);
            if (isShowHeadersForEmptySections || (perSectionItemCount > 0)) {
                // mHeaderLocationMap = {(0, 0), (5, 1), (14, 2), (19, 3), (28, 4)}
                mHeaderLocationMap.put(allItemCount, section);
                allItemCount += perSectionItemCount + 1;
            }
        }

        return allItemCount;
    }

    private int getSectionCount() {                     // compute the sections and the items per section
        if (null == mDatasource || mDatasource.isEmpty() || null == mComparator) {
            FrontdoLogger.getLogger().e(TAG, "[" + TAG + " - getSectionCount] mDatasource or mComparator is null!");
            return 0;
        }

        final int COMPARATOR_EQUALS = 0;
        final int DEFAULT_ITEM_COUNT = 1;

        int sectionCount = 0;                           // section size
        int sectionIndex = 0;                           // section index
        int itemCount = DEFAULT_ITEM_COUNT;             // item size per section
        // init the first data
        mSectionItemsMap.put(sectionIndex, itemCount);

        int size = mDatasource.size();
        int index = 1;
        while (index < size) {
            Object curObj = mDatasource.get(index - 1);
            Object nextObj = mDatasource.get(index);

            if (mComparator.compare(curObj, nextObj) == COMPARATOR_EQUALS) {
                itemCount++;
            } else {
                sectionIndex++;
                itemCount = DEFAULT_ITEM_COUNT;
            }
            mSectionItemsMap.put(sectionIndex, itemCount);

            index++;
        }

        sectionCount = mSectionItemsMap.size();
        return sectionCount;
    }

    /**
     * @hide
     */
    @Override
    public final int getItemViewType(int position) {
        if (isHeader(position)) {

            return getHeaderViewType(mHeaderLocationMap.get(position));
        } else {

            final int[] sectionAndPos = getSectionIndexAndRelativePosition(position);
            return getItemViewType(sectionAndPos[0],
                    // offset section view positions
                    sectionAndPos[1],
                    position - (sectionAndPos[0] + 1));
        }
    }

    @SuppressWarnings("UnusedParameters")
    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public int getHeaderViewType(int section) {
        // noinspection ResourceType
        return VIEW_TYPE_HEADER;
    }

    /**
     * <br>
     * section 0
     * <br>
     * s0, rp0, ap0 | s0, rp1, ap1 | s0, rp2, ap2
     * <br>
     * section 1
     * <br>
     * s1, rp0, ap3 | s1, rp1, ap4 | s1, rp2, ap5
     * <br>
     * s1, rp0, ap6 | s1, rp1, ap7 | s1, rp2, ap8
     * <br>
     *
     * @param section
     * @param relativePosition the index which is related the per section
     * @param absolutePosition the index at mDatasource
     * @return
     */
    @SuppressWarnings("UnusedParameters")
    @IntRange(from = -1024, to = Integer.MAX_VALUE)
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        // noinspection ResourceType
        return VIEW_TYPE_ITEM;
    }

    /**
     * @hide
     */
    @Override
    public final void onBindViewHolder(VH holder, int position) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = null;
        if (holder.itemView.getLayoutParams() instanceof GridLayoutManager.LayoutParams) {

            layoutParams = new StaggeredGridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {

            layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        }

        if (isHeader(position)) {

            if (layoutParams != null) {
                layoutParams.setFullSpan(true);
            }

            onBindHeaderViewHolder(holder, mHeaderLocationMap.get(position), position);
        } else {

            if (layoutParams != null) {
                layoutParams.setFullSpan(false);
            }

            final int[] sectionAndPos = getSectionIndexAndRelativePosition(position);
            final int absPos = position - (sectionAndPos[0] + 1);
            onBindViewHolder(holder, sectionAndPos[0],
                    // offset section view positions
                    sectionAndPos[1], absPos);
        }

        if (layoutParams != null) {
            holder.itemView.setLayoutParams(layoutParams);
        }
    }
}
