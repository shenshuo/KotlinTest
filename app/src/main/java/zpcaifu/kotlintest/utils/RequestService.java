package zpcaifu.kotlintest.utils;

import android.text.TextUtils;
import android.util.Log;

import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * @author leigang
 *
 * @description 请求网络service
 *
 * @time 2016-11-3 22:03:24
 */
public class RequestService implements IBaseService {

    private Object TAG;

    private static RequestService sRequestService;

    public RequestService() {
        TAG = this.getClass().getSimpleName();
    }

    public static RequestService getInstance(){
        if(sRequestService == null){
            sRequestService = new RequestService();
        }
        return sRequestService;
    }

    public <T> void sendPost(String url,
                             Map<String, String> paramsMap,
                             Class<T> clazz, ICallBack<T> callBack,Map<String, String> headers) {

        sendRequest(HttpUtils.METHOD_POST, null,url, paramsMap, clazz, callBack,headers);
    }

    public <T> void sendPost(String url,Object Tag,
                             Map<String, String> paramsMap,
                             Class<T> clazz, ICallBack<T> callBack,Map<String, String> headers) {

        sendRequest(HttpUtils.METHOD_POST, Tag,url, paramsMap, clazz, callBack,headers);
    }

    public <T> void sendPost(String url,
                             String paramsJson,
                             Class<T> clazz, ICallBack<T> callBack,Map<String, String> headers) {

        sendRequest(HttpUtils.METHOD_POST,null, url, paramsJson, clazz, callBack, headers);
    }

    public <T> void sendPost(String url,Object Tag,
                             String paramsJson,
                             Class<T> clazz, ICallBack<T> callBack,Map<String, String> headers) {
        sendRequest(HttpUtils.METHOD_POST, Tag,url, paramsJson, clazz, callBack,headers);
    }

    public <T> void sendGet(String url, Map<String, String> paramsMap, Class<T> clazz, ICallBack<T> callBack,Map<String, String> headers) {
        sendRequest(HttpUtils.METHOD_GET,null, url, paramsMap, clazz, callBack,headers);
    }

    public <T> void sendGet(String url, Object tag ,Map<String, String> paramsMap, Class<T> clazz, ICallBack<T> callBack,Map<String, String> headers) {
        sendRequest(HttpUtils.METHOD_GET, tag ,url, paramsMap, clazz, callBack,headers);
    }

    private <T> void sendRequest(int method,Object tag,String url, Object params,
                                 Class<T> clazz, ICallBack<T> callBack,Map<String, String> headers) {
        if(tag != null){
            TAG = tag;
        }
        Map<String, String> paramsMap = new HashMap<>();
        String paramsStr = null;
        if (params instanceof Map) {
            paramsMap = (Map<String, String>) params;
            HttpUtils.send(method, TAG, url, paramsMap, new CustomStringCallback(callBack, clazz),headers);
        } else if (params instanceof String) {
            paramsStr = (String) params;
            HttpUtils.sendPost(url, TAG,paramsStr, new CustomStringCallback(callBack, clazz));
        }

    }

    class CustomStringCallback<T> extends StringCallback {

        private ICallBack<T> mCallBack;
        private Class<T> mClazz;

        public CustomStringCallback(ICallBack<T> callBack, Class<T> clazz) {
            mCallBack = callBack;
            mClazz = clazz;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            if(call.isCanceled()){
               return;
            }
            Log.e("TAG", "onResponse:" + TAG + e + e.getMessage());
            mCallBack.onServerException(e, e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e("TAG", "onResponse:" + TAG + response);
            //FileUtil.writeToFile(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/pcitc/responce.txt",response,"utf-8",false);
            try {
                mCallBack.onSuccess(new JSONObject(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(response)) {
                mCallBack.onError(id, "请求成功,服务器返回数据为空");
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("success")) {
                    String errorContent = jsonObject.getString("message");
                    mCallBack.onRequestEnd(errorContent);
                    Result res = GSONUtils.fromJson(response, Result.class);
                    res.setSourceData(response);
                    mCallBack.onFinish(res);
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mCallBack.onError(0, "JSONException 数据异常");
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(response);
                Object data = jsonObject.get("data");
                if (data instanceof JSONArray) {
                    Result<List<T>> listResult = GSONUtils.fromJsonArray(response, mClazz);
                    mCallBack.onSuccess(listResult.data);
                } else if (data instanceof JSONObject) {
                    Result<T> tResult = GSONUtils.fromJsonObject(response, mClazz);
                    mCallBack.onSuccess(jsonObject);
                } else {
                    mCallBack.onError(id, "服务器返回JSON格式错误");
                }
            } catch (JSONException e) {
                mCallBack.onError(id, "JSONException 数据异常");
            }
        }
    }


    public void cancel(Object tag) {
        HttpUtils.cancel(tag);
        TAG=this.getClass().getSimpleName();
    }

    public void cancel() {
        HttpUtils.cancel(TAG);
    }


}
