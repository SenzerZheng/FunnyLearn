package net.frontdo.funnylearn.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import net.frontdo.funnylearn.R;

/**
 * ProjectName: ZoomableImageView
 * Description: 获取焦点可缩放Button
 * <p>
 * author: JeyZheng
 * version: 2.0
 * created at: 10/23/2016 18:11
 */
public class ZoomableButton extends Button {

    private int zoomInAnim = R.anim.scale_zoom_in;
    private int zoomOutAnim = R.anim.scale_zoom_out;
    private Animation scaleSmallAnimation;
    private Animation scaleBigAnimation;

    public ZoomableButton(Context context) {
        super(context);

        init(context, null);
    }

    public ZoomableButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public ZoomableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ZoomableButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs);
    }

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
