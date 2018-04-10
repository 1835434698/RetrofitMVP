package com.cnepay.android.swiper.core.utils;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;

/**
 * Created by Administrator on 2017/4/25.
 */

public class BodyUtils {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    public boolean isPlaintext(MediaType mediaType) {
        if (mediaType == null) return false;
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        String subtype = mediaType.subtype();
        if (subtype != null) {
            subtype = subtype.toLowerCase();
            if (subtype.contains("x-www-form-urlencoded") ||
                    subtype.contains("json") ||
                    subtype.contains("xml") ||
                    subtype.contains("html")) //
                return true;
        }
        return false;
    }

    public String bodyToString(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = copy.body().contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            return buffer.readString(charset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void logForRequest(Request request, Connection connection) throws IOException {
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;

        try {
            String requestStartMessage = "--> Request Start " + request.method() + ' ' + request.url() + ' ' + protocol;
            OkLogger.d(requestStartMessage);

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                OkLogger.d("\t" + headers.name(i) + ": " + headers.value(i));
            }

            OkLogger.d("hasRequestBody:" + hasRequestBody + "\n");
            if (hasRequestBody) {
                if (isPlaintext(requestBody.contentType())) {
                    OkLogger.d("-->"+bodyToString(request).replace("&", "\n\t"));
                } else {
                    OkLogger.d("\tbody: maybe [file part] , too large too print , ignored!");
                }
            }
        } catch (Exception e) {
            OkLogger.e(e);
        } finally {
            OkLogger.d("--> Request END ");
        }
    }

    public Response logForResponse(Response response, long tookMs) {
        Response.Builder builder = response.newBuilder();
        Response clone = builder.build();
        ResponseBody responseBody = clone.body();
        try {
            OkLogger.d("<-- BaseBean Start " + clone.code() + ' ' + clone.message() + ' ' + clone.request().url() + " (" + tookMs + "ms）");
            Headers headers = clone.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                OkLogger.d("\t" + headers.name(i) + ": " + headers.value(i));
            }
            OkLogger.d(" ");
            if (HttpHeaders.hasBody(clone)) {
                if (isPlaintext(responseBody.contentType())) {
                    String body = responseBody.string();
                    OkLogger.d("\tbody:" + body);
                    responseBody = ResponseBody.create(responseBody.contentType(), body);
                    return response.newBuilder().body(responseBody).build();
                } else {
                    OkLogger.d("\tbody: maybe [file part] , too large too print , ignored!");
                }
            }
        } catch (Exception e) {
            OkLogger.e(e);
        } finally {
            OkLogger.d("<-- END HTTP");
        }
        return response;
    }

}
