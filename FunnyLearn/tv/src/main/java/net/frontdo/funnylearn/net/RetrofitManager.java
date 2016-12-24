package net.frontdo.funnylearn.net;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import net.frontdo.funnylearn.BuildConfig;
import net.frontdo.funnylearn.api.HttpUrls;
import net.frontdo.funnylearn.logger.FrontdoLogger;

import java.io.IOException;
import java.util.WeakHashMap;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * ProjectName: HttpUrls
 * Description: Retrofit管理类
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 2016/5/19 17:55
 */
public class RetrofitManager {
    private static String TAG = "RetrofitManager";
    private static RetrofitManager manager;

    private WeakHashMap<String, Retrofit> retrofitMap = new WeakHashMap<>();
    private OkHttpClient okHttpClient;

    public static RetrofitManager getManager() {
        if (manager == null) {
            synchronized (RetrofitManager.class) {
                if (manager == null) {
                    manager = new RetrofitManager();
                }
            }
        }
        return manager;
    }

    private RetrofitManager() {
        okHttpClient = new OkHttpClient();

        // 添加拦截器，打印网络请求信息(正式打包去掉，否会敏感数据会泄漏)
        if (!HttpUrls.BT_RELEASE.equalsIgnoreCase(BuildConfig.BUILD_TYPE)) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient.interceptors().add(interceptor);
        }

        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json; charset=UTF-8")
                        .addHeader("Accept-Encoding", "gzip, deflate")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("Accept", "*/*")
//                        .addHeader("Cookie", "add cookies here")
                        .build();
                return chain.proceed(request);
            }
        });

        okHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                long t1 = System.nanoTime();
                Request request = chain.request();
                FrontdoLogger.getLogger().d(TAG, String.format("Sending request %s on %s%n%s",
                        request.url(), chain.connection(), request.headers()));

                Response response = chain.proceed(request);

                long t2 = System.nanoTime();
                FrontdoLogger.getLogger().d(TAG, String.format("Received response for %s in %.1fms%n%s",
                        request.url(), (t2 - t1) / 1e6d, response.headers()));
                return response;
            }
        });
    }

    private Retrofit getRetrofit(String baseUrl) {
        Retrofit retrofit = retrofitMap.get(baseUrl);
        Log.i(TAG, baseUrl);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .build();

            retrofitMap.put(baseUrl, retrofit);
        }
        return retrofit;
    }


    /**
     * 创建retrofit实例
     *
     * @param baseUrl 服务器地址
     * @return
     */
    public static Retrofit get(String baseUrl) {
        return getManager().getRetrofit(baseUrl);
    }


}