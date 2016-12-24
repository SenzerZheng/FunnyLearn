package net.frontdo.funnylearn.net;

import net.frontdo.funnylearn.api.FrontdoApiService;

import retrofit.Retrofit;

/**
 * ProjectName: HttpUrls
 * Description: 接口工厂类
 * <p>
 * author: JeyZheng
 * version: 1.0
 * created at: 2016/5/19 17:55
 */
public class FrontdoApiServiceFactory {

    /**
     * 创建接口对象
     *
     * @return
     */
    public static FrontdoApiService getSpiderApiService(String url) {

        FrontdoApiService spiderApiService = null;

        Retrofit retrofit = RetrofitManager.get(url);
        spiderApiService = retrofit.create(FrontdoApiService.class);

        return spiderApiService;
    }
}