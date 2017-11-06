package zpcaifu.kotlintest.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import ikidou.reflect.TypeBuilder;

/**
 * @author leigang
 * @description
 * @time 2016-8-26 15:55:26
 */
public class GSONUtils {

    public static <T> Result<List<T>> fromJsonArray(String readerJson, Class<T> clazz) {
        Type type = TypeBuilder
                .newInstance(Result.class)
                .beginSubType(List.class)
                .addTypeParam(clazz)
                .endSubType()
                .build();
        return new Gson().fromJson(readerJson, type);
    }

    public static <T> Result<T> fromJsonObject(String readerJson, Class<T> clazz) {
        Type type = TypeBuilder
                .newInstance(Result.class)
                .addTypeParam(clazz)
                .build();
        return new Gson().fromJson(readerJson, type);
    }

    /**
     * 添加toJSON方法
     *
     * @param o
     * @return
     */
    public static String toJson(Object o) {
        return new Gson().toJson(o);
    }

    /**
     * 获取对应的实体类
     *
     * @param content
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String content, Class<T> clazz) {
        return new Gson().fromJson(content, clazz);
    }

    /**
     * 转成Map集合
     *
     * @param readerJson
     * @return
     */
    public static Map<String, String> toMap(String readerJson) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        return gson.fromJson(readerJson, type);
    }

}
