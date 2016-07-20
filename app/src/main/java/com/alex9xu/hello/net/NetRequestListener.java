package com.alex9xu.hello.net;

import retrofit2.Response;

/**
 * Created by Alex9Xu@hotmail.com on 2016/7/20
 */
public interface NetRequestListener {
    void onRequestSuc(int code, Response response);
    void onRequestFail(int code, String strToShow);
}
