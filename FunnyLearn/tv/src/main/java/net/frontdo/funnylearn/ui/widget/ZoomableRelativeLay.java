package net.frontdo.funnylearn.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import net.frontdo.funnylearn.R;

/**
 * ProjectName: ZoomableRelativeLay
 * Description: 获取焦点可缩放ImageView
 * <p>
 * author: JeyZheng
 * version: 2.0
 * created at: 10/23/2016 18:11
 */
public class ZoomableRelativeLay extends RelativeLayout {

    private int zoomInAnim = R.anim.scale_zoom_in;
    private int zoomOutAnim = R.anim.scale_zoom_out;
    private Animation scaleSmallAnimation;
    private Animation scaleBigAnimation;

    public ZoomableRelativeLay(Context context) {
        super(context);

        init(context, null);
    }

    public ZoomableRelativeLay(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public ZoomableRelativeLay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ZoomableRelativeLay(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZoomableRelativeLay);

            int zoomIn = typedArray.getResourceId(R.styleable.ZoomableRelativeLay_zoomIn, 0);
            int zoomOut = typedArray.getResourceId(R.styleable.ZoomableRelativeLay_zoomOut, 0);

            if (zoomIn > 0) {
                zoomInAnim = zoomIn;
            }

            if (zoomOut > 0) {
                zoomOutAnim = zoomOut;
            }
        }

        setClipChildren(false);
        setClipToPadding(false);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

        if (gainFocus) {
            View rlGood = this.findViewById(R.id.rl_good);
            if (null != rlGood) {
                rlGood.setBackgroundResource(R.drawable.shape_list_bg);
            }

            zoomIn();
        } else {

            View rlGood = this.findViewById(R.id.rl_good);
            if (null != rlGood) {
                rlGood.setBackgroundResource(0);
            }
            zoomOut();
        }

        if (null != mOnZoomableFocusedChangeListener) {
            mOnZoomableFocusedChangeListener.onFocusChanged(gainFocus, direction);
        }
    }

    private void zoomIn() {
        if (scaleSmallAnimation == null) {
            scaleSmallAnimation = AnimationUtils.loadAnimation(getContext(), zoomInAnim);
        }

        startAnimation(scaleSmallAnimation);
    }

    private void zoomOut() {
        if (scaleBigAnimation == null) {
            scaleBigAnimation = AnimationUtils.loadAnimation(getContext(), zoomOutAnim);
        }

        startAnimation(scaleBigAnimation);
    }

    private OnZoomableFocusedChangeListener mOnZoomableFocusedChangeListener;

    public void setOnZoomableFocusedChangeListener(OnZoomableFocusedChangeListener listener) {
        this.mOnZoomableFocusedChangeListener = listener;
    }


    public interface OnZoomableFocusedChangeListener {
        void onFocusChanged(boolean gainFocus, int direction);
    }
}
