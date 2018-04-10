package com.cnepay.android.swiper.core.model.network;

import com.cnepay.android.swiper.bean.BaseBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;


/**
 * created by millerJK on time : 2017/5/15
 * description :
 */

class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private Type type;


    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            //ResultResponse 只解析result字段
            BaseBean resultResponse = gson.fromJson(response, BaseBean.class);
            if (resultResponse.isSuccess()) {
                //success表示成功返回，继续用本来的Model类解析
                return gson.fromJson(response, type);
            } else {
                //ErrResponse 将msg解析为异常消息文本
                throw new ResultException(resultResponse.isSuccess(), resultResponse.getRespMsg()
                        , resultResponse.getRespTime(), resultResponse.getRespCode());
            }
        } finally {
        }
    }
}


