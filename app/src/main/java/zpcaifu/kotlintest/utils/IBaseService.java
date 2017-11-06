package zpcaifu.kotlintest.utils;

import java.util.Map;


public interface IBaseService {

    public <T> void sendPost(String url, Map<String, String> paramsMap, Class<T> clazz, ICallBack<T> callBack, Map<String, String> headers);

    public <T> void sendGet(String url, Map<String, String> paramsMap, Class<T> clazz, ICallBack<T> callBack, Map<String, String> headers);
}
