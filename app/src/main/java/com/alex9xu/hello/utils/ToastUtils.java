package com.alex9xu.hello.utils;

import android.widget.Toast;

import com.alex9xu.hello.base.DemoApp;

/**
 * Created by Alex on 2016/7/22
 */
public class ToastUtils {

    public static void toastShort(CharSequence content) {
        if(null!=content && content.length()>0) {
            Toast.makeText(DemoApp.mAppContext, content, Toast.LENGTH_SHORT).show();
        }
    }

    public static void toastShort(int resId) {
        Toast.makeText(DemoApp.mAppContext, resId, Toast.LENGTH_SHORT).show();
    }

}
