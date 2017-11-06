package zpcaifu.kotlintest.utils;

import android.widget.Toast;

import zpcaifu.kotlintest.App;


public class ToastUtil {
    static Toast mToast = null;

    public static void show( String text) {
        if (mToast == null) {
            mToast = Toast.makeText(App.mContext, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);

        }
        mToast.show();
    }


}
