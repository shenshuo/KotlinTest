package zpcaifu.kotlintest.utils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;

public class HttpUtils {

    /**
     * 普通的GET请求
     */
    public final static int METHOD_GET = 0;
    /**
     * 普通的POST请求
     */
    public final static int METHOD_POST = 1;


    public static void send(int method, Object tag, String url, Map<String, String> paramsMap,
                            Callback callback,Map<String, String> headers) {
        if (method == METHOD_GET) {
            sendGet(tag, url, paramsMap, callback,headers);
            return;
        }
        sendPost(tag, url, paramsMap, callback,headers);
    }

    public static void sendGet(Object tag, String url, Map<String, String> paramsMap,
                               Callback callback,Map<String, String> headers) {
        OkHttpUtils
                .get()
                .url(url)
                .tag(tag)
                .params(paramsMap).headers(headers)

                .build()
                .execute(callback);
    }

    public static void sendPost(Object tag, String url,
                                Map<String, String> paramsMap, Callback callback,Map<String, String> headers) {
        OkHttpUtils
                .post()
                .url(url)
                .tag(tag)
                .params(paramsMap).headers(headers)
                .build()
                .execute(callback);
    }

    public static void sendPost(String url, Map<String, String> paramsMap, Callback callback,Map<String, String> headers) {
        OkHttpUtils
                .post()
                .url(url)
                .params(paramsMap).headers(headers)
                .build()
                .execute(callback);
    }

    public static void sendPost(String url, Object tag, String paramsJson, Callback callback) {
        OkHttpUtils
                .postString()
                .url(url)
                .tag(tag)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(paramsJson)
                .build()
                .execute(callback);
    }

    public static void sendPostFile(String url, File file, Callback callback) {
        OkHttpUtils
                .postFile()
                .url(url)
                .file(file)
                .build()
                .execute(callback);
    }

    public static void downloadImage(String url, Callback callback) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .connTimeOut(20000)
                .readTimeOut(20000)
                .writeTimeOut(20000)
                .execute(callback);
    }

    public static void uploadFile(String url,
                                  Map<String, String> paramsMap,
                                  File file, String fileName,
                                  String formName, Map<String, String> headerMap, Callback callback
    ) {
        OkHttpUtils.post()
                .addFile(formName, fileName, file)
                .url(url)
                .params(paramsMap)
                .headers(headerMap)
                .build()
                .execute(callback);
    }

    public static void downloadFile(String url, Callback callback) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(callback);
    }

    public static void cancel(Object tag) {
        OkHttpUtils.getInstance().cancelTag(tag);
    }

}
