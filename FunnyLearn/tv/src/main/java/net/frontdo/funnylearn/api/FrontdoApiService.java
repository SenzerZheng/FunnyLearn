package net.frontdo.funnylearn.api;

import net.frontdo.funnylearn.net.bean.ReqLogin;
import net.frontdo.funnylearn.net.bean.ReqOperFavOrApp;
import net.frontdo.funnylearn.net.bean.ReqRegister;
import net.frontdo.funnylearn.net.bean.ReqUpdateInfo;
import net.frontdo.funnylearn.ui.entity.GetFavState;
import net.frontdo.funnylearn.ui.entity.MainInfo;
import net.frontdo.funnylearn.ui.entity.MineInfo;
import net.frontdo.funnylearn.ui.entity.OperFavOrApp;
import net.frontdo.funnylearn.ui.entity.Product;
import net.frontdo.funnylearn.ui.entity.SMSCode;
import net.frontdo.funnylearn.ui.entity.UserInfo;

import java.util.List;

import retrofit.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

/**
 * ProjectName: FrontdoApiService
 * Description: retrofit理解：
 * 1. 如果在@GET, @POST...上面加入的常量中包含HOST，会优先选择该HOST作为链接主机
 * 2. 如果不存在HOST，那么就会使用在实例化时的的HOST，作为链接主机
 * <p>
 * NOTE(http://square.github.io/retrofit/)
 * POST
 * 1. @Body：请求参数为JSON字符串，可以使用pojo对象
 * 2. @Field & @FieldMap：form表单请求数据
 * 3. @Part & @PartMap：上传文件注解
 * GET
 * 1. @Query & @QueryMap：get请求，拼接参数
 * OTHER
 * 1. @Path：路径拼接，group/{id}/users
 * 2. @Header：动态添加头，也可以在OkHttpCilent中添加统一的头标准
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 2016/8/16 17:29
 */
@SuppressWarnings("ALL")
public interface FrontdoApiService {
    /**
     * 用户注册
     *
     * @param reqRegister
     * @return
     */
    @POST(HttpUrls.USER_REGISTER)
    Observable<Response<UserInfo>> userRegister(@Body ReqRegister reqRegister);

    /**
     * 常规用户登录
     *
     * @param reqLogin
     * @return
     */
    @POST(HttpUrls.USER_LOGIN)
    Observable<Response<MainInfo>> userLogin(@Body ReqLogin reqLogin);

    /**
     * 未登录时获取 主页 信息
     *
     * @param reqRegister
     * @return
     */
    @GET(HttpUrls.USER_UNLOGIN)
    Observable<Response<MainInfo>> userUnlogin();

    /**
     * Other分类列表 others = 分类的ID
     *
     * @param reqRegister
     * @return
     */
    @GET(HttpUrls.CATEGORY_LIST)
    Observable<Response<List<Product>>> getCategoryList(@Path("id") int id, @Path("aScope") int aScope);

    /**
     * Latest publishs（上架）
     *
     * @param reqRegister
     * @return
     */
    @GET(HttpUrls.LATEST_PUBLISHS)
    Observable<Response<List<Product>>> getLatestPublishs();

    /**
     * reset密码 或 编辑个人信息；status = update 重置密码 或者 edit编辑信息
     *
     * @param reqResetPwd
     * @return
     */
    @POST(HttpUrls.REST_PWD)
    Observable<Response<UserInfo>> operUserInfo(
            @Path("status") String status, @Body ReqUpdateInfo reqUpdateInfo);


    /**
     * 短信接口（注册/重置）action = register/reset；mobile = 手机号
     *
     * @param reqRegister
     * @return
     */
    @GET(HttpUrls.GET_CODE)
    Observable<Response<SMSCode>> getVerifyCode(
            @Path("action") String action, @Path("mobile") String mobile);

    /**
     * 我的信息：版本，账号 mobile = 手机号
     *
     * @param reqRegister
     * @return
     */
    @GET(HttpUrls.MY_INFO)
    Observable<Response<MineInfo>> getMyInfo(@Path("mobile") String mobile);

    /**
     * 收藏/应用（添加或取消） type = 0（收藏）或者1（应用） status = save/update
     *
     * @param reqOperFavOrApp
     * @return
     */
    @POST(HttpUrls.OPER_FAV_OR_APP)
    Observable<Response<OperFavOrApp>> operFavOrApp(@Path("type") String type, @Path("status") String status,
                                                    @Body ReqOperFavOrApp reqOperFavOrApp);

    /**
     * 收藏/应用（列表获取） type = 0（收藏）或者1（应用） mobile = 手机号
     *
     * @param reqRegister
     * @return
     */
    @GET(HttpUrls.FAV_OR_APP_LIST)
    Observable<Response<List<Product>>> getFavOrAppList(
            @Path("type") String type, @Path("mobile") String mobile);

    /**
     * 查询产品详情，通过id获取
     *
     * @param reqProductDetails
     * @return
     */
    @GET(HttpUrls.PRODUCT_DETAILS)
    Observable<Response<Product>> getDetails(@Path("id") int productId);

    /**
     * 查询产品详情，收藏状态
     *
     * @param reqProductDetails
     * @return
     */
    @GET(HttpUrls.PRODUCT_DETAILS_FAV_STATE)
    Observable<Response<GetFavState>> getDetailsFavStatus(
            @Path("mobile") String mobile, @Path("id") int productId);
}
