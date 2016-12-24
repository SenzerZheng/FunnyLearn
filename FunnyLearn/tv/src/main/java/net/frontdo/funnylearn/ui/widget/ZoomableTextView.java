package net.frontdo.funnylearn.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import net.frontdo.funnylearn.R;

/**
 * ProjectName: ZoomableTextView
 * Description: 获取焦点可缩放TextView
 *
 * author: JeyZheng
 * version: 1.0
 * created at: 10/31/2016 23:07
 */
public class ZoomableTextView extends TextView {

    private int zoomInAnim = R.anim.scale_zoom_in;
    private int zoomOutAnim = R.anim.scale_zoom_out;
    private Animation scaleSmallAnimation;
    private Animation scaleBigAnimation;

    public ZoomableTextView(Context context) {
        super(context);

        init(context, null);
    }

    public ZoomableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public ZoomableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ZoomableTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void init(Context context, AttributeSet attrs) {

        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZoomableImageView);

            int zoomIn = typedArray.getResourceId(R.styleable.ZoomableImageView_zoomInIv, 0);
            int zoomOut = typedArray.getResourceId(R.styleable.ZoomableImageView_zoomOutIv, 0);

            if (zoomIn > 0) {
                zoomInAnim = zoomIn;
            }

            if (zoomOut > 0) {
                zoomOutAnim = zoomOut;
            }
        }

//        setClipToOutline(false);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

        if (gainFocus) {        // zoom in

            zoomIn();
        } else {                // zoom out

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
