package net.frontdo.funnylearn.common;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.widget.ImageView;

import net.frontdo.funnylearn.logger.FrontdoLogger;

/**
 * ProjectName: FastBlur
 * Description:
 *
 * author: JeyZheng
 * version: 1.0
 * created at: 10/23/2016 12:52
 */
@SuppressWarnings("ALL")
public class FastBlur {

    /**
     * 开启高斯设置
     *
     * @param bmp         需要高斯模糊的原Bitmap
     * @param iv          设置高斯Bitmap的ImageView
     * @param ratioWH     宽高比率
     * @param scaleFactor 图片缩放比例
     * @param radius      模糊程度，参数越大，约模糊
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void blur(Bitmap bmp, ImageView iv, float ratioWH, float scaleFactor, float radius) {
        if (null == bmp || null == iv) {
            return;
        }

        final int RATIO_DRAW_BMP_LEFT = 16;
        final int RATIO_DRAW_BMP_TOP = 4;
        long startMs = System.currentTimeMillis();

        // 产生一个指定大小的透明Bitmap
        Bitmap overlay = Bitmap.createBitmap(
                (int) (iv.getWidth() / ratioWH),
                (int) (iv.getHeight() / ratioWH),
                Bitmap.Config.ARGB_8888);

        // 根据透明Bitmap，创建一个画布；组成（画布上画了一个透明的bitmap）
        Canvas canvas = new Canvas(overlay);
        // 移动画布的原点，与imageView的左上角对齐
        canvas.translate(-iv.getLeft() / scaleFactor, -iv.getTop() / scaleFactor);
        // 缩放画布上的Bitmap或者图形，对画布大小不做改变（0-1：缩小；above 1：放大）
        canvas.scale(scaleFactor, scaleFactor);

        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        // 将bmp通过canvas中设置的属性（scale大小等）与起点坐标，等比例，绘画到画布上。
        canvas.drawBitmap(bmp,
                -bmp.getWidth() / RATIO_DRAW_BMP_LEFT, -bmp.getHeight() / RATIO_DRAW_BMP_TOP,
                paint);
//        canvas.drawBitmap(bmp, 0, 0, paint);

        // 添加高斯模糊效果
        overlay = doBlur(overlay, (int) radius, true);

        iv.setImageBitmap(overlay);
        // 设置图片缩放类型，fit_xy：画布与bitmap共同填充imageView的大小
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
//        iv.setBackgroundColor(Color.RED);

        FrontdoLogger.getLogger().i("doBlurTime", "[" + (System.currentTimeMillis() - startMs) + "]");
    }

    /**
     * 通过算法开始高斯模糊
     *
     * @param sentBitmap
     * @param radius
     * @param canReuseInBitmap
     * @return
     */
    public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
}
