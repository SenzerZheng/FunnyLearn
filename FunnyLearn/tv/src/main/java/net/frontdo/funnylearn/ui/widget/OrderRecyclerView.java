package net.frontdo.funnylearn.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * ProjectName: FunnyLearn
 * Description: 可以更改绘制序列的RecyclerView，主要解决Item放大遮挡问题
 * <p>
 * author: JeyZheng
 * version: 2.0
 * created at: 10/29/2016 18:47
 */
public class OrderRecyclerView extends RecyclerView {
    private int position;

    public OrderRecyclerView(Context context) {
        super(context);

        setChildrenDrawingOrderEnabled(true);
    }

    public OrderRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setChildrenDrawingOrderEnabled(true);
    }

    public OrderRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setChildrenDrawingOrderEnabled(true);
    }

    public void setPosition(int position) {
        // 刷新adapter前，在activity中调用这句传入当前选中的item在屏幕中的次序
        this.position = position;
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (i == childCount - 1) {  // 这是最后一个需要刷新的item
            return position;
        }

        if (i == position) {        // 这是原本要在最后一个刷新的item
            return childCount - 1;
        }
        return i;                   // 正常次序的item
    }
}
