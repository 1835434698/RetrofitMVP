package com.cnepay.android.swiper.core.model;

import com.cnepay.android.swiper.core.model.network.ResponseConverterFactory;
import com.cnepay.android.swiper.core.model.network.SignInterceptor;
import com.cnepay.android.swiper.core.utils.HttpsUtils;
import com.cnepay.android.swiper.model.APIService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by Administrator on 2017/4/24.
 */

public class RetrofitProvider {
    public static final int DEFAULT_MILLISECONDS = 12 * 1000;//默认超时时间
    private static String URL_BASE;

    public static APIService create() {
        return create(URL_BASE);
    }

    public static APIService create(String baseUrl) {
        //设置忽略证书
        HttpsUtils.SSLParams ssl = HttpsUtils.getSslSocketFactory(HttpsUtils.UnSafeTrustManager, null, null, null);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new SignInterceptor())
                .hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier)
                .sslSocketFactory(ssl.sSLSocketFactory, ssl.trustManager)
                .connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ResponseConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(APIService.class);
    }

    public static void init(String url_base) {
        URL_BASE = url_base;
    }
    public static String getUrlBase(){
        return URL_BASE;
    }
}
