package com.cnepay.android.swiper.model;

import com.cnepay.android.swiper.bean.AccountAuthBean;
import com.cnepay.android.swiper.bean.AccountDetailBean;
import com.cnepay.android.swiper.bean.BankListBean;
import com.cnepay.android.swiper.bean.BankQueryBean;
import com.cnepay.android.swiper.bean.BannerBean;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.bean.CardMangeBean;
import com.cnepay.android.swiper.bean.DownLoadBannerBean;
import com.cnepay.android.swiper.bean.LoginBean;
import com.cnepay.android.swiper.bean.MerchantAuthBean;
import com.cnepay.android.swiper.bean.MerchantQualifyBean;
import com.cnepay.android.swiper.bean.MineMessageBean;
import com.cnepay.android.swiper.bean.ModifyMessageBean;
import com.cnepay.android.swiper.bean.RealNameAuthBean;
import com.cnepay.android.swiper.bean.ResultBean;
import com.cnepay.android.swiper.bean.SettleListBean;
import com.cnepay.android.swiper.bean.SignBean;
import com.cnepay.android.swiper.bean.SignatureAuthBean;
import com.cnepay.android.swiper.bean.TransactionListBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2017/4/24.
 */

public interface APIService {
    @FormUrlEncoded
    @POST("OkHttpUtils/method")
    Observable<ResultBean> post(@Header("curTime") String curTime, @FieldMap Map<String, String> paramsMap);

    @GET("repos/square/okhttp/contributors")
    Observable<List<LoginBean>> get(@HeaderMap Map<String, String> headMap, @QueryMap Map<String, String> paramMap);

    @FormUrlEncoded
    @POST("login.action")
    Observable<Response<LoginBean>> login(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("logout.action")
    Observable<BaseBean> logout(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("settleList.action")
    Observable<SettleListBean> settleQuery(@FieldMap Map<String, String> paramMap);

    @FormUrlEncoded
    @POST("findTransList.action")
    Observable<TransactionListBean> transactionQuery(@FieldMap Map<String, String> paramMap);

    @FormUrlEncoded
    @POST("register.action")
    Observable<BaseBean> register(@HeaderMap Map<String, Object> headMap, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("sendMobileMessage.action")
    Observable<Response<BaseBean>> sendMobileMessage(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("resetPassword.action")
    Observable<BaseBean> resetPassword(@FieldMap Map<String, String> paramsMap);


    @FormUrlEncoded
    @POST("resetPasswordWithoutSession.action")
    Observable<BaseBean> resetPasswordWithoutSession(@FieldMap Map<String, String> paramsMap);


    @GET("message.action")
    Observable<MineMessageBean> queryMineMessage(@QueryMap Map<String, Object> paramsMap);

    @GET("modifyMessage.action")
    Observable<ModifyMessageBean> modifyMessage(@QueryMap Map<String, Object> paramsMap);

    @FormUrlEncoded
    @POST("tranInfo.action")
    Observable<AccountDetailBean> tranInfo(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("sendCustomerMessage.action")
    Observable<BaseBean> sendCustomerMessage(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("signin.action")
    Observable<SignBean> signin(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("bindBankCard.action")
    Observable<CardMangeBean> cardManage(@FieldMap Map<String, String> paramsMap);

    @GET("listBandCard.action")
    Observable<BankListBean> bandList(@Query("appVersion") String appVersion);

    @POST("realNameAuth.action")
    Observable<BaseBean> realNameAuth(@Body RequestBody params);

    @GET("realNameAuthStatus.action")
    Observable<RealNameAuthBean> realNameAuthStatus(@Query("appVersion") String appVersion);

    @POST("merchantAuth.action")
    Observable<BaseBean> merchantAuth(@Body RequestBody params);

    @GET("merchantAuthStatus.action")
    Observable<MerchantAuthBean> merchantAuthStatus(@Query("appVersion") String appVersion);

    @POST("accountAuth.action")
    Observable<BaseBean> accountAuth(@Body RequestBody params);

    @GET("accountAuthStatus.action")
    Observable<AccountAuthBean> accountAuthStatus(@Query("appVersion") String appVersion);

    @FormUrlEncoded
    @POST("signatureAuth.action")
    Observable<BaseBean> signatureAuth(@FieldMap Map<String, Object> paramsMap);

    @GET("signatureAuthStatus.action")
    Observable<SignatureAuthBean> signatureAuthStatus(@Query("appVersion") String appVersion);

    @FormUrlEncoded
    @POST("merchantQualify.action")
    Observable<MerchantQualifyBean> merchantQualify(@Field("appVersion") String appVersion);

    @GET("bankQuery.action")
    Observable<BankQueryBean> bankQuery(@Query("keyWord") String keyWord, @Query("reqNo") String reqNo, @Query("max") String max, @Query("p") String p, @Query("reqTime") String reqTime, @Query("appVersion") String appVersion);

    @GET("banner.action.action")
    Observable<BannerBean> getBannerList(@QueryMap Map<String, Object> paramsMap);


    @FormUrlEncoded
    @POST("checkMobileMessage.action")
    Observable<BaseBean> checkMobileMessage(@HeaderMap Map<String, Object> headMap,@FieldMap Map<String, String> paramsMap);


    @FormUrlEncoded
    @POST("validatePassword.action")
    Observable<BaseBean> validatePassword(@FieldMap Map<String, String> paramsMap);

    @GET ("downloadImg.action")
    Observable<DownLoadBannerBean> downloadBanner(@QueryMap Map<String, String> paramsMap);

}

