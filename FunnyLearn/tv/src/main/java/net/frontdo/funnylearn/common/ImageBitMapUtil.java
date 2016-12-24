/**  

 * @文件名 :ImageBitMapUtil.java 

 * @包名   :com.spider.subscriber.util 

 * @日期   :2014-12-2 上午9:42:07 

 * @版本   :V1.0.0 
  
 * @版权声明:Copyright (c) 2014年 spider. All rights reserved. 

 */
package net.frontdo.funnylearn.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @ClassName: ImageBitMapUtil
 * @Description: 操作图片的工具类
 * @author tufengchao
 * @date 2014-12-2 上午9:42:07
 * 
 */
public class ImageBitMapUtil {

	/**
	 * 
	 * @param context
	 *            上下文环境
	 * @param resId
	 *            资源文件Id
	 * @return
	 */
	public static Bitmap getBitmapFromResources(Context context, int resId) {
		Resources res = context.getResources();

		return BitmapFactory.decodeResource(res, resId);
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 *            图片的角度
	 * @param bitmap
	 *            操作的Bitmap
	 * @return
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);

		return resizedBitmap;
	}

	/**
	 * 获取图片旋转的角度
	 * 
	 * @param path
	 *            图片的路径
	 * @return
	 */
	public static int getPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;

			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;

				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			Log.e("error", e.toString());
		}
		return degree;
	}

	/**
	 * 加载缩略图
	 * 
	 * @param filePath
	 *            图片路径
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @return
	 */
	public static Bitmap loadImgThumbnail(String filePath, int w, int h) {

		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {

			File file = new File(filePath);
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (Exception e) {
			}
		}
		return zoomBitmap(bitmap, w, h);
	}


	public final static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
			                     .openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 等比例缩放图片
	 * 
	 * @param bitmap
	 *            操作的图片
	 * @param width
	 *            目标图片的宽度
	 * @param height
	 *            目标图片的高度
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		if (bitmap != null) {
			bitmap.recycle();
		}
		return newbmp;
	}

	/**
	 * 把drawable转bitmap
	 * 
	 * @param drawable
	 *            操作的 图片
	 * @return
	 */
	public static Bitmap drawabletoBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicWidth();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
				: Config.RGB_565);

		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 对bitmap进行圆角处理
	 * 
	 * @param bitmap
	 *            操作的图片
	 * @param roundPX
	 *            目标图片的圆角度数
	 * @return
	 */
	public static Bitmap getRoundRectBitmap(Bitmap bitmap, float roundPX) {
		Bitmap newbmp = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);

		Canvas canvas = new Canvas(newbmp);
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(0xff424242);
		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return newbmp;
	}

	public static Bitmap loadScaleBitmapResource(Context context,
												 int resourceId, float scale) {

		Options option = new Options();

		option.inSampleSize = (int) (1 / scale);

		return BitmapFactory.decodeResource(context.getResources(), resourceId,
				option);

	}

	/**
	 * 获取图片宽度
	 * 
	 * @param context
	 * @param resourceId
	 * @return
	 */
	public static int getBitmapWidth(Context context, int resourceId) {

		Options option = new Options();

		option.inJustDecodeBounds = true;

		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				resourceId);

		return bitmap.getWidth();

	}

	public static int getBitmapHeight(Context context, int resourceId) {

		Options option = new Options();

		option.inJustDecodeBounds = true;

		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				resourceId);

		return bitmap.getHeight();

	}
}
