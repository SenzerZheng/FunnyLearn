package net.frontdo.funnylearn.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import net.frontdo.funnylearn.logger.FrontdoLogger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static net.frontdo.funnylearn.api.ParamsHelper.TAG;

public class VersionUtils {
    /**
     * 获取当前程序的版本号
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getVersionName(Context context) throws Exception {
        // 获取PackageManager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packInfo.versionName;
    }

    /**
     * delete file
     *
     * @param appUrl
     * @return
     */
    public static void deleteFileByAppUrl(String appUrl) {
        if (StringUtil.checkEmpty(appUrl)) {
            return;
        }

        try {
            File file = new File(FileHelper.ROOT, getAppName(appUrl));
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从服务器获取文件
     *
     * @param appUrl
     * @param pd
     * @return
     * @throws Exception
     */
    public static File getFileFromServer(String appUrl, ProgressDialog pd)
            throws Exception {
        // 如果相等的话表示当前的SDCard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//			URL url = new URL(ApiConfig.BASE + appUrl);
            URL url = new URL(appUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);

            // 获取到文件的大小
            pd.setMax(conn.getContentLength() / FileHelper.KB);

            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            File fileDir = new File(FileHelper.ROOT);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            File file = new File(FileHelper.ROOT, getAppName(appUrl));
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len = 0;
            int total = 0;

            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                // 获取当前下载量
                pd.setProgress(total / FileHelper.KB);
            }

            fos.close();
            bis.close();
            is.close();
            return file;
        } else {

            FrontdoLogger.getLogger().e(TAG, "[ " + TAG + " - getExternalStorageState] do not equal MEDIA_MOUNTED");
            return null;
        }
    }

    /**
     * 从服务器获取文件
     *
     * @param appUrl
     * @return
     * @throws Exception
     */
    public static File getFileFromServer(String appUrl) throws Exception {
        // 如果相等的话表示当前的SDCard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            URL url = new URL(ApiConfig.BASE + appUrl);
            URL url = new URL(appUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);

            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            File file = new File(FileHelper.ROOT, getAppName(appUrl));
            FileOutputStream fos = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len = 0;
            int total = 0;

            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                // 获取当前下载量
                MyUpdateNotification.update(
                        conn.getContentLength() / FileHelper.KB,    // all length
                        total / FileHelper.KB, false);                // downloaded
            }

            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    /**
     * 安装APK
     *
     * @param file
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private static String getAppName(String appUrl) {
        String[] datas = appUrl.split("/");
        return datas[datas.length - 1];
    }
}
