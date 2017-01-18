package net.frontdo.funnylearn.common;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.ui.widget.datepicker.CustomDatePicker;

import java.util.Calendar;

import static net.frontdo.funnylearn.ui.widget.datepicker.CustomDatePicker.DEFAULT_START_DATE;
import static net.frontdo.funnylearn.ui.widget.datepicker.CustomDatePicker.SPLIT_DATE_TIME;

/**
 * ProjectName: DatePickerDialogUtil
 * Description: 日期选择对话框工具类
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 12/17/2016 21:14
 */
public class DatePickerDialogUtil {

    /**
     * show the datePickerDialog
     *
     * @param context
     * @param listener
     * @param year
     * @param month
     * @param dayOfMonth
     */
    public static void show(@NonNull Context context, @Nullable OnDateSetListener listener,
                            int year, int month, int dayOfMonth) {

        DatePickerDialog datePickerDlg = new DatePickerDialog(context, listener, year, month, dayOfMonth);
        datePickerDlg.show();
    }

    /**
     * show the current date
     *
     * @param context
     * @param listener
     */
    public static void showCur(@NonNull Context context, @Nullable OnDateSetListener listener) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int style = 0;
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            style = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;   // <==> THEME_HOLO_LIGHT
//        } else {
//            style = R.style.style_date_picker_dialog;         // use the theme, at sys version: 2.3
//        }
        style = AlertDialog.THEME_HOLO_LIGHT;

        DatePickerDialog datePickerDlg = new DatePickerDialog(context, style, listener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDlg.setTitle(context.getString(R.string.date_picker_title));
        datePickerDlg.show();
    }

    /**
     * show the custom date picker
     *
     * @param context
     * @param tv
     */
    public static void showCustomDPV(@NonNull Context context, @Nullable final TextView tv) {
        String now = DateUtil.getENDate4();
        String text = tv.getText().toString().trim();
        if (StringUtil.checkEmpty(text)) {
            text = now.split(SPLIT_DATE_TIME)[0];
        }

        CustomDatePicker datePicker = new CustomDatePicker(context, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tv.setText(time.split(SPLIT_DATE_TIME)[0]);
            }
        }, DEFAULT_START_DATE, now);                                // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        datePicker.showSpecificTime(false);                         // 不显示时和分
        datePicker.setIsLoop(false);                                // 不允许循环滚动

        datePicker.show(text);
    }
}
