package zpcaifu.kotlintest

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.TabHost
import android.widget.Toast

import com.mjj.PagerBottomTabStrip

import kotlinx.android.synthetic.main.activity_main2.*

import org.json.JSONObject
import zpcaifu.kotlintest.Adapter
import zpcaifu.kotlintest.R
import zpcaifu.kotlintest.fragment.Onefragment
import zpcaifu.kotlintest.fragment.Threefragment
import zpcaifu.kotlintest.fragment.Twofragment

import java.util.ArrayList
import java.util.Date


 class Main2Activity : AppCompatActivity() {

    private val iconResid = intArrayOf(R.drawable.main_one, R.drawable.main_two,

            R.drawable.main_three)

    private val iconResidClick = intArrayOf(R.drawable.main_one_down, R.drawable.main_two_down,

            R.drawable.main_three_down)

    private var mLastBackTime: Long = 0
    private val TIME_DIFF = (2 * 1000).toLong()
    private val isLogin = false
    private var adapter: Adapter? = null
    private var push = 0

    private val pushlist2 = 0
    private var webUrl: String? = ""
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        initAdapter("user")

        tab!!.builder(viewpager).DefaultMode().TabIcon(iconResid).TabClickIcon(iconResidClick).TabPadding(5).TabTextColor(Color.GRAY).TabClickTextColor(Color.parseColor("#2083E8")).build()


    }

    override fun onResume() {
        super.onResume()

    }



    internal var onPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            //      if(position==2&&getisLogin()==0){
            //		  mViewPager.setCurrentItem(0);
            //
            //
            //
            //	  }

        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }



    override fun onDestroy() {
        super.onDestroy()
    }



    private fun initAdapter(flag: String) {

        viewpager!!.setOffscreenPageLimit(3)
        viewpager!!.setOnPageChangeListener(onPageChangeListener)

        var  list: ArrayList<Fragment> = ArrayList<Fragment>()
        //		Log.e("userRole",(getuserRole().equals(Constant.USERROLE)+""));
        list?.add(Onefragment())
        list?.add(Twofragment())
        list?.add(Threefragment())





        adapter = Adapter(getSupportFragmentManager(), list)
        viewpager!!.setAdapter(adapter)
        adapter!!.notifyDataSetChanged()
    }

    fun message(v: View) {
        tab!!.setMessageNumber(viewpager!!.getCurrentItem(), 90)
    }

    fun add(v: View) {
        tab!!.addMessageNumber(viewpager!!.getCurrentItem(), 1)
    }

    fun remove(v: View) {
        tab!!.addMessageNumber(viewpager!!.getCurrentItem(), -1)
    }

    private var news: Boolean? = true
    fun news(v: View) {
        tab!!.setNews(viewpager!!.getCurrentItem(), news)
        news = if (news!!) false else true
    }




    /**
     * Created by shenshao on 15/11/26.
     */

}
