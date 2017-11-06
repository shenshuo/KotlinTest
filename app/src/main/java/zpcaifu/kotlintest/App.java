package zpcaifu.kotlintest;

import android.app.Application;
import android.content.Context;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by shenshao on 2017/10/18.
 */
public class App extends Application {
    public   static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20000L, TimeUnit.MILLISECONDS).readTimeout(20000L, TimeUnit.MILLISECONDS).build();
        OkHttpUtils.initClient(okHttpClient);
    }
}
