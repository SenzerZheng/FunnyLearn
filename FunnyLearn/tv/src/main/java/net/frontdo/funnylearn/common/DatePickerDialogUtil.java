package net.frontdo.funnylearn.common;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.frontdo.funnylearn.R;

import java.util.Calendar;

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

        DatePickerDialog datePickerDlg = new DatePickerDialog(context, R.style.style_date_picker_dialog, listener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDlg.setTitle(context.getString(R.string.date_picker_title));
        datePickerDlg.show();
    }
}
