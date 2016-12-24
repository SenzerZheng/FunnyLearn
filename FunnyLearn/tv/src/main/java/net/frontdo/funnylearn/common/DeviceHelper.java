package net.frontdo.funnylearn.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.List;
import java.util.UUID;

/**
 * ProjectName: DeviceHelper
 * Description: 设备助手类
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 2016/8/18 14:25
 */
@SuppressWarnings("ALL")
public class DeviceHelper {
    private static final String PREF_UUID = "userInfo";
    private static final String UUID_KEY = "uuid";

    /**
     * 获取运营商 // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
     *
     * @param context
     * @return
     */
    public static String getProvidersName(Context context) {
        TelephonyManager telephonyManager = getTelePhoneManager(context);

        String ProvidersName = null;
        String IMSI;
        try {
            IMSI = telephonyManager.getSubscriberId();

            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {

                ProvidersName = "CM";

            } else if (IMSI.startsWith("46001")) {

                ProvidersName = "CU";

            } else if (IMSI.startsWith("46003")) {

                ProvidersName = "CT";

            } else {

                ProvidersName = "CM";
            }

            return ProvidersName;

        } catch (Exception e) {
            return "CM";
        }

    }

    /**
     * 得到电话的管理类
     *
     * @param context
     * @return
     */
    public static TelephonyManager getTelePhoneManager(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context

                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager;
    }

    /**
     * 获取手机唯一标识
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        String deviceId = getLocalMacAddress(context);

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getIMEI(context);
        }

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getUUID(context);
        }

        return deviceId;
    }

    /**
     * 得到手机的Mac地址
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddress(Context context) {

        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);

        try {
            WifiInfo info = wifi.getConnectionInfo();

            String mac[] = info.getMacAddress().split(":");

            StringBuilder macbuild = new StringBuilder();

            for (int i = 0; i < mac.length; i++) {

                macbuild.append(mac[i]);

            }

            return macbuild.toString();
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * 获取imei
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        return getTelePhoneManager(context).getDeviceId();
    }

    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreentWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取uuid
     *
     * @return
     */
    public static String getUUID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_UUID, Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString(UUID_KEY, null);
        if (TextUtils.isEmpty(uuid)) {
            // 生成随机的uuid并保存
            uuid = UUID.randomUUID().toString();
            sharedPreferences.edit().putString(UUID_KEY, uuid);
        }
        return uuid;
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreentHight(Context context) {

        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * 获取屏幕的密度
     *
     * @param context
     * @return
     */
    public static float getDensity(Context context) {

        return getDisplayMetrics(context).density;

    }

    /**
     * 获取 DisplayMetrics 此类提供了一种关于显示的通用信息，如显示大小，分辨率和字体
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();

        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(metric);

        return metric;
    }

    /**
     * 主要获取网络信息类
     *
     * @param context
     * @return
     */
    public static NetworkInfo getNetWorkInfo(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null == connectivityManager) {
            return null;
        }

        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo;
    }

    /**
     * 判断手机的网络是否可用
     *
     * @param context
     * @return true可用
     */
    public static boolean isNetAvailable(Context context) {
        NetworkInfo networkInfo = getNetWorkInfo(context);

        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断GPS是否打开
     *
     * @param context
     * @return
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));

        List<String> accessibleProviders = locationManager.getProviders(true);

        return accessibleProviders != null && accessibleProviders.size() > 0;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Activity context) {
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }
}
