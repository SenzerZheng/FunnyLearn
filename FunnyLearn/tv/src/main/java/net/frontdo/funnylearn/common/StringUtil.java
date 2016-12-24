package net.frontdo.funnylearn.common;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;

import net.frontdo.funnylearn.logger.FrontdoLogger;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ProjectName: StringUtil
 * Description:
 *
 * author: JeyZheng
 * version: 1.0
 * created at: 10/23/2016 12:55
 */
@SuppressWarnings("ALL")
public class StringUtil {

    public static final String TAG = "StringUtil";
    public static final int PHONE_LEN = 11;
    public static final String MONEY_MARK = "¥";
    public static DecimalFormat decimalformat = new DecimalFormat("0.00");

    /**
     * 获取价格字符串
     *
     * @param price
     * @return
     */
    public static String getFormatPrice(double price) {
        String formatPrice = decimalformat.format(price);
        return formatPrice;
    }

    public static String getFormatPrice(String priceString) {

        float price = getFloat(priceString);

        return getFormatPrice(price);

    }

    /**
     * 添加标识
     *
     * @param money
     * @return
     */
    public static String appendMoneyMark(String money) {
        if (TextUtils.isEmpty(money))
            return "¥0.00";
        return "¥" + money;
    }

    /**
     * 获取浮点数
     *
     * @param string
     * @return
     */
    public static float getFloat(String string) {
        float result = 0;
        try {
            result = Float.valueOf(string);
        } catch (Exception e) {
            FrontdoLogger.getLogger().e("getFloat", e.getMessage());
        }
        return result;
    }

    /**
     * 获取String
     *
     * @param d
     * @return
     */
    public static String getString(double d) {
        String result = "0";
        try {
            result = String.valueOf(d);
        } catch (Exception e) {
            FrontdoLogger.getLogger().e("getString", e.getMessage());
        }
        return result;
    }


    /**
     * 获取最短的价格字符串
     *
     * @param priceString
     * @return
     */
    public static String getShortestPriceString(String priceString) {
        StringBuilder decimalStringBuilder = new StringBuilder(priceString);
        int pos = decimalStringBuilder.indexOf(".");
        if (pos != -1) {
            int stringLength = decimalStringBuilder.length();
            while ('0' == decimalStringBuilder.charAt(stringLength - 1)
                    && stringLength >= pos + 1) {
                decimalStringBuilder.deleteCharAt(stringLength - 1);
                stringLength = decimalStringBuilder.length();
            }
            if (pos == decimalStringBuilder.length() - 1) {
                decimalStringBuilder.deleteCharAt(pos);
            }
        }
        return decimalStringBuilder.toString();
    }

    /**
     * 验证手机号 - 简单
     *
     * @param mobile
     * @return
     */
    public static boolean isMobileSimple(String mobile) {
        boolean valid = false;
        if (!TextUtils.isEmpty(mobile)) {
            if (mobile.length() == PHONE_LEN) {

                return true;
            }
        }

        return valid;
    }

    /**
     * 验证手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobile(String mobiles) {
        boolean valid = false;
        if (!TextUtils.isEmpty(mobiles)) {
            Pattern p = Pattern
                    .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

            Matcher m = p.matcher(mobiles);
            valid = m.matches();
        }

        return valid;

    }

    /**
     * 验证邮政编码
     *
     * @param post
     * @return
     */
    public static boolean checkPost(String post) {
        if (!TextUtils.isEmpty(post) && post.matches("[1-9]\\d{5}(?!\\d)")) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 将字符串转换为整形
     *
     * @param string
     * @param defaultValue 当出错时，返回的默认值
     * @return
     */
    public static int intValueOf(String string, int defaultValue) {

        int value = defaultValue;

        try {
            value = Integer.valueOf(string);
        } catch (Exception e) {
            FrontdoLogger.getLogger().d(TAG, e.getMessage() + "");
        }

        return value;
    }


    /**
     * 获取整数
     *
     * @param intString
     * @return
     */
    public static int getInt(String intString) {
        int result = 0;
        try {
            result = Integer.valueOf(intString);
        } catch (Exception e) {
            FrontdoLogger.getLogger().e("getInt", e.getMessage());
        }
        return result;
    }


    /**
     * 获取格式化的价格
     *
     * @param price
     * @return
     */
    public static SpannableStringBuilder getFormatPriceSpannable(String price) {
        String priceStr = MONEY_MARK + " " + getFormatPrice(price);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(priceStr);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(12, true), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int decimalIndex = priceStr.indexOf(".");
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(18, true), 2, decimalIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(12, true), decimalIndex, priceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }


    /**
     * 判断是否是url
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        return !TextUtils.isEmpty(url) && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("www"));
    }

    /**
     * @param url
     * @return
     */
    public static String getHttpUrl(String url) {
        if (isUrl(url) || TextUtils.isEmpty(url)) {
            return url;
        } else {
            return "http://" + url;
        }
    }

    /**
     * 检查String是否为空
     *
     * @param s
     * @return
     */
    public static boolean checkEmpty(String s) {
        boolean isEmpty = true;

        if (null != s && !"".equals(s)) {
            isEmpty = false;
        }
        return isEmpty;
    }


    /**
     * 检查是否是有效的密码
     *
     * @param pswd 密码 6-16不含空格
     * @return
     */
    public static boolean checkPswdChar(String pswd) {
        boolean valid = true;
        if (TextUtils.isEmpty(pswd))
            valid = false;
        else if (pswd.contains(" ")) {
            valid = false;
        } else if (pswd.length() < 6 || pswd.length() > 16) {
            valid = false;
        }
        return valid;
    }

    public static boolean checkPswd(String pwd) {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        boolean flag = pwd.matches(regex);
        if (flag) {

            return true;
        } else {

//            ToastHelper.toast(context, "你的密码安全级别不够高，建议修改密码", 3000);
            return false;
        }
    }


    /**
     * 检查字符串是否是有效的手机号码
     *
     * @param string
     * @return
     */
    public static boolean checkIsMoblie(String string) {
        boolean result = false;
        if (!TextUtils.isEmpty(string)) {
            if (string.length() == 11) {
                result = true;
            }
        }
        return result;
    }


    /**
     * 获取加密的手机
     *
     * @param mobile
     * @return
     */
    public static String getEncryMobile(String mobile) {

        String result;

        if (checkIsMoblie(mobile)) {
            StringBuilder stringBuilder = new StringBuilder(mobile.substring(0, 3));
            stringBuilder.append("****");
            stringBuilder.append(mobile.substring(7));
            result = stringBuilder.toString();
        } else {
            result = "";
        }

        return result;
    }


    /**
     * 检查字符串是否有效
     *
     * @param string
     * @param min        最小长度
     * @param max        最大长度
     * @param charString 不包含字符
     * @return 是否有效
     */
    public static boolean checkString(String string, int min, int max,
                                      String charString) {
        boolean valid = false;
        if (!TextUtils.isEmpty(string)) {
            if (string.length() >= min && string.length() <= max
                    && !string.contains(charString)) {
                valid = true;
            }
        }
        return valid;
    }


    /**
     * 保留一位小数
     *
     * @param m
     * @return
     */
    public static String keepADecimal(double m) {

        DecimalFormat df = new DecimalFormat("0.0");

        return df.format(m);
    }


    /**
     * String类型转数组
     *
     * @param inputString 要转的String
     * @param splitChar   分割符
     * @return
     */
    public static String[] stringToArray(String inputString, String splitChar) {
        if (TextUtils.isEmpty(splitChar) || TextUtils.isEmpty(inputString)) {
            return null;
        } else {
            return inputString.split(splitChar);
        }
    }


    /**
     * 格式化null字符串
     *
     * @param string
     * @return
     */
    public static String formatString(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        } else {
            return string;
        }
    }


    /**
     * 是否是蜘蛛网自营
     *
     * @param shop
     * @return
     */
    public static boolean isSpider(String shop) {

        boolean is = false;

        if (checkEmpty(shop)) {
            is = true;
        }

        if ("1".equals(shop)) {
            is = true;
        }

        return is;
    }


    /**
     * 保留一位小数
     *
     * @param dou
     * @return
     */
    public static double changeDouble(Double dou) {
        NumberFormat nf = new DecimalFormat("0.0 ");
        dou = Double.parseDouble(nf.format(dou));
        return dou;
    }

    /**
     * 判断是否是订阅商品
     *
     * @param type 订阅商品类型
     * @return
     */
    public static boolean isSubscrib(String type) {

        boolean is = true;

        if ("ts".equals(type))
            is = false;

        return is;
    }


    /**
     * 配送方式
     *
     * @param dType k-快递，j-就近配送，z-自提
     * @return
     */
    public static boolean isExpress(String dType) {

        boolean is = true;

        if (!"k".equals(dType))
            is = false;

        return is;
    }

    /**
     * 是否是就近配送方式
     *
     * @param dType
     * @return
     */
    public static boolean isNearest(String dType) {
        boolean is = true;

        if (!"j".equals(dType))
            is = false;

        return is;
    }

    /**
     * 是否是蜘蛛网快递
     *
     * @param delivery
     * @return
     */
    public static boolean isSpiderExpress(String delivery) {
        boolean is = true;

        if (!delivery.contains("快递") && !delivery.contains("挂号"))
            is = false;


        return is;
    }


    /**
     * 配送方式
     *
     * @param dType k-快递，j-就近配送，z-自提
     * @return
     */
    public static boolean isArayacak(String dType) {
        boolean is = false;

        if ("z".equals(dType))
            is = true;

        return is;
    }


    /**
     * 配送方式
     *
     * @param dType 1-自提，2-就近配送，3-快递
     * @return
     */
    public static boolean isExpressTwo(String dType) {

        boolean is = true;

        if (!"3".equals(dType))
            is = false;

        return is;
    }


    /**
     * 去除null字符串
     *
     * @param string
     * @return
     */
    public static String removeNullStr(String string) {
        String result = string;
        if (!TextUtils.isEmpty(string)) {
            result = result.replace("null", "");
        }
        return result;
    }

    /**
     * 返回非空字符串
     *
     * @param string
     * @return
     */
    public static String getNotNullString(String string) {
        if (string == null) {
            string = "";
        }
        return string;
    }

    /**
     * 去除html标签
     *
     * @param content
     * @return
     */
    public static String removeHtml(String content) {

        if (!TextUtils.isEmpty(content)) {

            // 过滤文章内容中的html
            content = content.replaceAll("</?[^<]+>", "");
            // 去除字符串中的空格 回车 换行符 制表符 等
            content = content.replaceAll("\\s*|\t|\r|\n", "");
            // 去除空格
            content = content.replaceAll("&nbsp;", "");
            // 去掉其他一些字符
            content = content.replaceAll("\\\\", "");

            //去除特殊字符串
            content = content.replaceAll("&apos;", "\'");

        }
        return content;
    }

    /**
     * 是否只有一家供应商（包括蜘蛛网）
     *
     * @param supplier
     * @return
     */
    public static boolean isOneSupplier(String supplier) {
        boolean is = true;

        if (!"1".equals(supplier))
            is = false;

        return is;
    }

    /**
     * 验证验证码的有效性
     *
     * @param verifyCode
     * @return
     */
    public static boolean isValidVerifyCode(String verifyCode) {

        boolean valid = false;

        if (checkEmpty(verifyCode))
            return valid;

        if (verifyCode.length() == 6) {
            valid = true;
            return valid;
        }
        return valid;
    }
}
