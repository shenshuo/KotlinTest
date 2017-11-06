package zpcaifu.kotlintest.utils;


import org.json.JSONObject;

import java.util.List;

/**
 * @author leigang
 * @description ICallBack 的默认实现类
 * @time 2016-12-29 20:16:01
 */
public class DefaultCallBack<T> implements ICallBack<T> {
    @Override
    public void onSuccess(List<T> list) {

    }

    @Override
    public void onSuccess(T bean) {

    }

    @Override
    public void onSuccess(JSONObject response) {

    }

    @Override
    public void onError(int code, String exceptionMessage) {

    }

    @Override
    public void onRequestEnd(String message) {

    }

    @Override
    public void onServerException(Exception e, String message) {

    }

//    @Override
//    public void onRequestEnd(String message) {
//        ToastUtil.showCenterToast(message);
//    }
//
//    @Override
//    public void onServerException(Exception e, String message) {
//        ToastUtils.show(getException(e));
//    }

    @Override
    public void onFinish(Result<T> tResult) {

    }

//    public String getException(Exception e) {
//        String exception;
//        exception = ResourcesUtils.getResById(R.string.unknown_server_exception);
//        if (e instanceof SocketTimeoutException) {
//            exception = ResourcesUtils.getResById(R.string.server_connection_timeout);
//        }
//        return exception;
//    }
}
