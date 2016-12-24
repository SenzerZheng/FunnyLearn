package net.frontdo.funnylearn.common;

import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * @ClassName: TextViewUtil
 * @Description: 设置textview的工具类
 * @author LiuJun
 * @date 2015-1-13 下午2:34:50
 *
 */
public class TextViewUtil {

    private static final String TAG = "TextViewUtil";

    /**
     * 设置TextView 防止显示空字符串
     *
     * @param textView
     * @param text
     */
    public static void setTextView(TextView textView, String text) {
        if (textView == null) {
            return;
        }
        // 判断是否为空
        if (TextUtils.isEmpty(text) || text.equalsIgnoreCase("null")) {
            textView.setText("");
        } else {

            textView.setText(text);
        }

    }

    public static void setTimeTextView(TextView textView, String text) {

        if (textView == null) {
            return;
        }
        // 判断是否为空
        if (TextUtils.isEmpty(text) || text.equalsIgnoreCase("null")) {
            textView.setText("");
        } else {

            textView.setText(text.split("\\.")[0]);
        }

    }

    /**
     * 设置中划线
     *
     * @param textView
     */
    public static void setMiddleLine(TextView textView) {
        if (textView != null) {
            textView.getPaint().setFlags(
                    Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线
        }
    }
}
