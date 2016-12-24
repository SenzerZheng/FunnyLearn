package net.frontdo.funnylearn.net;

import net.frontdo.funnylearn.api.HttpErr;
import net.frontdo.funnylearn.logger.FrontdoLogger;

import retrofit.Response;
import rx.Subscriber;

import static net.frontdo.funnylearn.net.FrontdoResultHelper.KEY_CODE;

/**
 * ProjectName: FrontdoSubcriber
 * Description: 封装返回结果Subscriber
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 10/22/2016 13:18
 */
public abstract class FrontdoSubcriber<T extends Response, D> extends Subscriber<T> {
    private static final String TAG = FrontdoSubcriber.class.getSimpleName();

    @Override
    public final void onCompleted() {
    }

    @Override
    public final void onError(Throwable e) {
        onFailure("", "加载网络数据失败！");
        e.printStackTrace();
        FrontdoLogger.getLogger().i(TAG, "onError--" + e);
    }

    @Override
    public final void onNext(T t) {
        FrontdoLogger.getLogger().i(TAG, "onNext--");
        if (FrontdoResultHelper.isSuccess(t)) {                 // successfully

            String code = t.headers().get(KEY_CODE);
            onSuccess(code, (D) t.body());
        } else {                                                // failure

            String code = t.headers().get(KEY_CODE);
            String msg = HttpErr.errMsgMap.get(Integer.parseInt(code));
            onFailure(code, msg);

            FrontdoLogger.getLogger().i(TAG, "onFailure");
        }
    }


    /**
     * 请求成功
     *
     * @param code 成功码
     * @param d    返回的实体类
     */
    public abstract void onSuccess(String code, D d);

    /**
     * 请求失败
     *
     * @param code    失败码
     * @param message 失败信息
     */
    public abstract void onFailure(String code, String message);
}
