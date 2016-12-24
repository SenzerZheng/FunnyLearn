package net.frontdo.funnylearn.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.frontdo.funnylearn.common.ActivityStack;

/**
 * ProjectName: BaseHoldBackActivity
 * Description: 带有back返回的页面（back，获取rootView）
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 2016/8/29 14:59
 */
@SuppressWarnings("ALL")
public abstract class BaseHoldBackActivity extends BaseActivity implements View.OnClickListener {

    protected abstract int onBindLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutInflater().inflate(onBindLayout(), null));

        // add aty
        ActivityStack.getInstanse().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityStack.getInstanse().removeActivity(this);
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();

//        if (R.id.rl_title_left == resId) {              // closeAty
//            finish();
//        }
    }

    /**
     * @param context
     * @param bundle
     * @param clazz   special class instead of BaseHoldBackActivity.class, beacuse it will boot BaseHoldBackActivity
     */
    public static void boot(Context context, Bundle bundle, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param bundle
     * @param reqCode
     * @param clazz   special class instead of BaseHoldBackActivity.class, beacuse it will boot BaseHoldBackActivity
     */
    public static void boot(Activity context, Bundle bundle, int reqCode, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivityForResult(intent, reqCode);
    }

    /**
     * get rootView at activity
     *
     * @return
     */
    protected View getRootView() {
        return getWindow().getDecorView().findViewById(android.R.id.content);
    }
}
