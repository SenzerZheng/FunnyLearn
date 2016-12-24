package net.frontdo.funnylearn.common;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.logger.FrontdoLogger;

import java.io.File;

public class MyUpdateNotification {
	private static NotificationManager manager = null;
	private static NotificationCompat.Builder builder = null;
	private static Context context = null;

	/**
	 * step one : initialize
	 * 
	 * @param context
	 * @param title
	 * @param contentText
	 */
	public static void initNotification(Context context, String title,
			String contentText) {
		MyUpdateNotification.context = context;
		
		manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		builder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(title)
				.setContentText(contentText);
		
		// 设置这个标志当用户单击面板就可以让通知自动取消
		builder.setAutoCancel(true);
		// time stamp
		builder.setWhen(System.currentTimeMillis());
	}
	
	protected static void setContentText(String contentText) {
		builder.setContentText(contentText);
	}
	
	protected static void setProgress(int max, int progress, boolean indeterminate) {
		builder.setProgress(max, progress, false);
	}
	
	protected static void notify(int id) {
		manager.notify(0, builder.build());
	}
	
	/**
	 * step two : updating
	 * 
	 * @param max
	 *            progressBar的总长度
	 * @param progress
	 *            progressBar的进度（即已下载的长度）
	 * @param indeterminate
	 *            表示进度是否不确定（true为不确定，false为确定）
	 */
	public static void update(int max, int progress, boolean indeterminate) {
		double percent = ((double) progress / (double) max) * 100;
		
		builder.setProgress(max, progress, indeterminate);
//		builder.setContentInfo(String.format("%.2f", percent) + "%");
		builder.setContentInfo(String.format("%.2f%%", percent));
		FrontdoLogger.getLogger().e("TAG", String.format("%.2f%%", percent));
		manager.notify(0, builder.build());
	}
	
	/**
	 * step three : click and installing
	 * 
	 * @param file
	 */
	public static void setContentIntent(File file) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		builder.setContentIntent(pendingIntent);
	}
	
	/**
	 * step four : finish
	 * 
	 * @param finishDesc
	 */
	public static void complete(String finishDesc) {
		builder.setContentText(finishDesc);
		// remove the notification
//		builder.setProgress(0, 0, false);
		manager.notify(0, builder.build());
	}
}
