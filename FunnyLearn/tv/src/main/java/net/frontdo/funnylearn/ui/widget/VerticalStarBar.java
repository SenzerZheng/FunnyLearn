package net.frontdo.funnylearn.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import net.frontdo.funnylearn.R;

/**
 * ProjectName: VerticalStarBar
 * Description: 垂直单颗增加星星布局
 * <p>
 * review by chenpan, wangkang, wangdong 2016/11/21
 * edit by JeyZheng 2016/11/21
 * author: JeyZheng
 * version: 4.0
 * created at: 2016/11/21 17:05
 */
public class VerticalStarBar extends View {
    private int starDistance = 0;                           // 星星间距
    private int starCount = 5;                              // 星星个数
    private int starSize;                                   // 星星高度大小，星星一般正方形，宽度等于高度
    private float starMark = 0.0F;                          // 评分星星
    private Bitmap starFillBitmap;                          // 亮星星
    private Drawable starEmptyDrawable;                     // 暗星星
    private OnStarChangeListener onStarChangeListener;      // 监听星星变化接口
    private Paint paint;                                    // 绘制星星画笔

    private boolean integerMark;                            // 设置是否需要整数绘制评分

    private boolean isFirstDraw = true;                     // 判断是否为首次绘制
    private boolean isTouchable;                            // 判断是否为可以触碰（true：初始化为0；false：初始值为设置的starMark）
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isFirstDraw = false;
        }
    };

    public VerticalStarBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public VerticalStarBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化UI组件
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        setClickable(true);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        this.starDistance = (int) mTypedArray.getDimension(R.styleable.RatingBar_starDistance, 0);
        this.starSize = (int) mTypedArray.getDimension(R.styleable.RatingBar_starSize, 20);
        this.starCount = mTypedArray.getInteger(R.styleable.RatingBar_starCount, 5);

        this.starEmptyDrawable = mTypedArray.getDrawable(R.styleable.RatingBar_starEmpty);
        this.starFillBitmap = drawableToBitmap(mTypedArray.getDrawable(R.styleable.RatingBar_starFill));
        mTypedArray.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(starFillBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));

        handler.sendEmptyMessageDelayed(0, 2000);
    }

    /**
     * @param touchable false: can invoke {@link #setStarMark(float)}, true: no affect to invoke {@link #setStarMark(float)}
     */
    public void setTouchable(boolean touchable) {
        isFirstDraw = touchable;
        isTouchable = touchable;
    }

    /**
     * 设置是否需要整数评分
     *
     * @param integerMark
     */
    public void setIntegerMark(boolean integerMark) {
        this.integerMark = integerMark;
    }

    /**
     * 设置显示的星星的分数
     *
     * @param mark
     */
    public void setStarMark(float mark) {
        if (integerMark) {
            starMark = (int) Math.ceil(mark);
        } else {
            starMark = Math.round(mark * 10) * 1.0f / 10;
        }
        if (this.onStarChangeListener != null) {
            this.onStarChangeListener.onStarChange(starMark);  //调用监听接口
        }
        invalidate();
    }

    /**
     * 获取显示星星的数目
     *
     * @return starMark
     */
    public float getStarMark() {
        return starMark;
    }


    /**
     * 定义星星点击的监听接口
     */
    public interface OnStarChangeListener {
        void onStarChange(float mark);
    }

    /**
     * 设置监听
     *
     * @param onStarChangeListener
     */
    public void setOnStarChangeListener(OnStarChangeListener onStarChangeListener) {
        this.onStarChangeListener = onStarChangeListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(starSize, starSize * starCount + starDistance * (starCount - 1));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (starFillBitmap == null || starEmptyDrawable == null) {
            return;
        }

        for (int i = 0; i < starCount; i++) {
            starEmptyDrawable.setBounds((starDistance + starSize) * i, 0, (starDistance + starSize) * i + starSize, starSize);
            starEmptyDrawable.draw(canvas);
        }

        if (starMark > 1) {                                         // 多个星星选中标志处理
            if (isFirstDraw) {
                canvas.drawRect(0, 0, starSize, 0, paint);
                return;
            }

            canvas.drawRect(0, 0, starSize, starSize, paint);
            if (starMark - (int) (starMark) == 0) {
                for (int i = 1; i < starMark; i++) {
                    canvas.translate(starDistance + starSize, 0);
                    canvas.drawRect(0, 0, starSize, starSize, paint);
                }
            } else {
                for (int i = 1; i < starMark - 1; i++) {
                    canvas.translate(starDistance + starSize, 0);
                    canvas.drawRect(0, 0, starSize, starSize, paint);
                }
                canvas.translate(starDistance + starSize, 0);
                canvas.drawRect(0, 0, starSize * (Math.round((starMark - (int) (starMark)) * 10) * 1.0f / 10), starSize, paint);
            }
        } else {                                                    // 单个星星选中标志处理
            float delta = starSize * starMark;
            if (isFirstDraw) {
                canvas.drawRect(0, 0, starSize, 0, paint);
                return;
            }

            // from left to right
//            canvas.drawRect(0, 0, starSize * starMark, starSize, paint);
            // from right to left
//            canvas.drawRect(delta, 0, getMeasuredWidth(), starSize, paint);
            // from up to bottom
//            canvas.drawRect(0, 0, starSize, delta, paint);
            // from bottom to up
            canvas.drawRect(0, delta, starSize, getMeasuredHeight(), paint);
//            FrontdoLogger.getLogger().e("@TAG@", delta + "");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouchable) {
            return super.onTouchEvent(event);
        }

//        touchX(event);
        touchY(event);

        invalidate();
        return super.onTouchEvent(event);
    }

    private void touchX(MotionEvent event) {
        int x = (int) event.getX();
        if (x < 0) x = 0;
        if (x > getMeasuredWidth()) x = getMeasuredWidth();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                setStarMark(x * 1.0f / (getMeasuredWidth() * 1.0f / starCount));
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                setStarMark(x * 1.0f / (getMeasuredWidth() * 1.0f / starCount));
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
        }
    }

    private void touchY(MotionEvent event) {
        // getY according to starView
        // getRawY: the original raw Y coordinate of this event according to the screen
        int y = (int) event.getY();
        if (y < 0) y = 0;
        if (y > getMeasuredHeight()) y = getMeasuredHeight();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                setStarMark(y * 1.0f / (getMeasuredHeight() * 1.0f / starCount));
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                setStarMark(y * 1.0f / (getMeasuredHeight() * 1.0f / starCount));
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
        }
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) return null;
        Bitmap bitmap = Bitmap.createBitmap(starSize, starSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, starSize, starSize);
        drawable.draw(canvas);
        return bitmap;
    }
}
