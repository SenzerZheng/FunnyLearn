package net.frontdo.funnylearn.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import net.frontdo.funnylearn.app.AppContext;
import net.frontdo.funnylearn.logger.FrontdoLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ProjectName: AppManager
 * Description: 其他APP管理器：安装与卸载APK，判断APP是否已安装，获取App版本号
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 10/29/2016 16:47
 */
public class AppManager {

    /**
     * 判断APP是否已安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    /**
     * 判断APP是否已安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    /**
     * @param context
     * @param fileName
     */
    public static void installApk(Context context, String fileName) {
        Uri uri = Uri.parse("file://" + fileName);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * @param context
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

    /**
     * @param context
     * @param packageName
     */
    public static void uninstallApk(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }

    /**
     * boot app by package name
     *
     * @param context
     * @param packagename
     */
    public static void doStartApplicationWithPackageName(Context context, String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

    /**
     * boot app by package name
     * <p/>
     * NOTE
     * the category must be existing CATEGORY_LAUNCHER at the target app,
     * because some apps are only existing CATEGORY_LEANBACK_LAUNCHER for tv.
     *
     * @param context
     * @param pkgName
     */
    public static void bootApp(Context context, String pkgName) {
        Intent intent = AppContext.getInstance().getPackageManager().getLaunchIntentForPackage(pkgName);
        if (intent == null) {       // 未安装app

            FrontdoLogger.getLogger().e("AppManager", "not found App!");
        } else {                    // 安装了App

            FrontdoLogger.getLogger().e("AppManager", "boot app successfully");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setAction(Intent.ACTION_MAIN);
            context.startActivity(intent);
        }
    }

    /**
     * boot app by package name and LaucherActivity
     *
     * @param context
     * @param pkgName
     */
    public static void bootAppByMainAty(Context context, String pkgName) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName(pkgName, pkgName + ".MainActivity");
        intent.setComponent(cn);
        context.startActivity(intent);
    }

    /**
     * 获取APP的versionName
     *
     * @param context
     * @param pkgName
     */
    public static String getAppVerName(Context context, String pkgName) {
        PackageManager pckMan = context.getPackageManager();
        List<PackageInfo> packageInfo = pckMan.getInstalledPackages(0);

        String versionName = "1.0";
//        ArrayList<HashMap<String, Object>> items = new ArrayList<>();
        for (PackageInfo pInfo : packageInfo) {
            if (pkgName.equals(pInfo.packageName)) {
                versionName = pInfo.versionName;

                break;
            }

//            HashMap<String, Object> item = new HashMap<>();
//
//            item.put("appimage", pInfo.applicationInfo.loadIcon(pckMan));
//            item.put("packageName", pInfo.packageName);
//            item.put("versionCode", pInfo.versionCode);
//            item.put("versionName", pInfo.versionName);
//            item.put("appName", pInfo.applicationInfo.loadLabel(pckMan).toString());
//
//            items.add(item);
        }

        return versionName;
    }
}
