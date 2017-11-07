package zpcaifu.kotlintest.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zhy.http.okhttp.OkHttpUtils
import kotlinx.android.synthetic.main.listfragment.*
import okhttp3.OkHttpClient
import org.json.JSONObject
import zpcaifu.kotlintest.KotlinAdapter
import zpcaifu.kotlintest.ProductZT
import zpcaifu.kotlintest.R
import zpcaifu.kotlintest.V6BbtAdapter
import zpcaifu.kotlintest.utils.DefaultCallBack
import zpcaifu.kotlintest.utils.GSONUtils
import zpcaifu.kotlintest.utils.RequestService
import zpcaifu.kotlintest.utils.Response
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by shenshao on 2017/11/6.
 */
class Onefragment : Fragment() {
    internal var hashMap: MutableMap<String, String> = HashMap()
    internal var hashMap2: MutableMap<String, String> = HashMap()
    internal var list: ArrayList<ProductZT> = ArrayList<ProductZT>()
    var gson :Gson?= Gson()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(activity, R.layout.listfragment, null)
        init()
        return view
    }
    fun init(){
        hashMap.put("pageNo", "10")
        hashMap.put("pageSize",   "5")
        hashMap.put("requestType", "C_D_B")

        RequestService.getInstance().sendGet("https://www.rmbbox.com/api/v5/list/bbtList", "login", hashMap, Response::class.java, object : DefaultCallBack<Response>() {
            override fun onServerException(e: Exception, message: String) {
                super.onServerException(e, message)
            }

            override fun onSuccess(response: JSONObject) {
                super.onSuccess(response)
                if (response.optString("data") != "null") {
                    list = gson!!.fromJson<ArrayList<ProductZT>>(response.optString("data"), object : TypeToken<ArrayList<ProductZT>>() {
                    }.type)
                    lv.adapter= KotlinAdapter(activity,list,"")
                }
            }
        }, hashMap2)
    }
}


