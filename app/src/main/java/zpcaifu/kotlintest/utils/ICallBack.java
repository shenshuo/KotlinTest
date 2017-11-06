package zpcaifu.kotlintest.utils;

/**
 * @author leigang
 *         <p/>
 *         请求数据回调方法
 * @description
 *
 * @time 2016-12-5 17:12:13
 *
 */
public interface ICallBack<T> extends IBaseCallBack<T>{

    /**
     * 服务器出错情况
     *
     * @param code
     *      服务器返回code值
     * @param exceptionMessage
     */
    public void onError(int code, String exceptionMessage);

    /**
     * 请求结束，但是参数或者其他什么错误
     *
     * @param message
     *
     *          <p>错误信息提示<p/>
     */
    public void onRequestEnd(String message);

    /***
     *
     * 请求服务器异常
     *
     * @param e
     *      请求服务器出现的异常
     * @param message
     *      请求服务器错误信息
     *
     */
    public void onServerException(Exception e, String message);


    public void onFinish(Result<T> tResult);


}
