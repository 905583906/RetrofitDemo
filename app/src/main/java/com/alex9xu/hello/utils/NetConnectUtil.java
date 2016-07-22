package com.alex9xu.hello.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Alex on 2016/7/22
 */
public class NetConnectUtil {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        try{
            netInfo = cm.getActiveNetworkInfo();
        } catch (Exception e) {
            return false;
        }

        return netInfo != null && (netInfo.isConnectedOrConnecting() || netInfo.isAvailable());
    }
}
