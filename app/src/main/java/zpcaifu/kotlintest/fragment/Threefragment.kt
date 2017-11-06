package zpcaifu.kotlintest.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import zpcaifu.kotlintest.R

/**
 * Created by shenshao on 2017/11/6.
 */
class Threefragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(activity, R.layout.activity_main, null)
        return view
    }
}