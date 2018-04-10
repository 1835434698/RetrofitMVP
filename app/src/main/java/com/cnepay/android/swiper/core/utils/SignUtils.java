package com.cnepay.android.swiper.core.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/4/24.
 */

public class SignUtils {
    private BodyUtils bodyUtils;

    private Map<String, String> params = new HashMap<>();

    public SignUtils() {
        this.bodyUtils = new BodyUtils();
    }

    public Request signParams(Request oldRequest) {
        boolean hasRequestBody = oldRequest.body() != null;
        Headers headers = oldRequest.headers();
        params.put("reqTime", headers.get("reqTime"));
        //TODO 签名将Head和body进行排序RSA加密
        Request.Builder newRequestBuilder = oldRequest.newBuilder();
        switch (oldRequest.method()) {
            case "GET":
                String paramGet = oldRequest.url().uri().getQuery();
                if (!TextUtils.isEmpty(paramGet)) {
                    for (String str : paramGet.split("&")) {
                        String[] p = str.split("=");
                        params.put(p[0], p[1]);
                    }
                }
                newRequestBuilder.url(oldRequest.url().newBuilder().addQueryParameter("sign", getSignContent(params)).build());
                break;
            case "POST":
                if (hasRequestBody) {
                    if (bodyUtils.isPlaintext(oldRequest.body().contentType())) {
                        FormBody.Builder bodyBuilder = new FormBody.Builder();
                        String[] body = bodyUtils.bodyToString(oldRequest).split("&");
                        for (String param : body) {
                            String[] p = param.split("=");
                            params.put(p[0], p[1]);
                            bodyBuilder.add(p[0], p[1]);
                        }
                        newRequestBuilder.post(bodyBuilder.add("sign", getSignContent(params)).build());
                    }
                }
                break;
        }
        return newRequestBuilder.build();
    }

    //生成验签密文
    private String getSignContent(Map<String, String> sortedParams) {
        if (sortedParams == null || sortedParams.size() == 0) return "";
        StringBuffer content = new StringBuffer();
        ArrayList keys = new ArrayList(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;

        for (int i = 0; i < keys.size(); ++i) {
            String key = (String) keys.get(i);
            String value = sortedParams.get(key);
            if (areNotEmpty(new String[]{key, value})) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                ++index;
            }
        }

        return content.toString();
    }

    private boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values != null && values.length != 0) {
            String[] arr$ = values;
            int len$ = values.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                String value = arr$[i$];
                result &= !isEmpty(value);
            }
        } else {
            result = false;
        }

        return result;
    }

    private boolean isEmpty(String value) {
        int strLen;
        if (value != null && (strLen = value.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(value.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }
}
