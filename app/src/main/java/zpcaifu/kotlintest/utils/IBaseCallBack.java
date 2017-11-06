package zpcaifu.kotlintest.utils;

import org.json.JSONObject;

import java.util.List;

/**
 * @author leigang
 * @description
 * <P>访问接口的基类</P>
 * @time 2017年3月24日16:09:02
 */
public interface IBaseCallBack<T> {
    /**
     * 服务器返回JSON_ARRAY
     * @param list
     *  集合的列表
     *
     */
    public void onSuccess(List<T> list);
    /**
     *
     * 服务器返回JSON_OBJECT
     * @param bean
     *
     *      对应的实体类
     */
    public void onSuccess(T bean);

    /**
     *
     * 数据已字符串形式返回
     *
     * @param response
     */
    public void onSuccess(JSONObject response);

}
